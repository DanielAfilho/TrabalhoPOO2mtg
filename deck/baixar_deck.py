"""
Baixa as imagens e gera o cartas.csv (e o atributos.sql) a partir da lista de um deck.

Uso:
    python baixar_deck.py decklist.txt pasta_saida

decklist.txt: uma carta por linha, no formato "1 Sol Ring" (nomes em inglês).
Gera na pasta de saída:
    - <Nome da carta>.jpg   (imagem, com o nome da carta)
    - cartas.csv            (para o CarregarCartas.java)
    - atributos.sql         (INSERT dos tipos/subtipos/cores usados; rode ANTES do CarregarCartas)
"""
import json
import os
import re
import sys
import time
import urllib.error
import urllib.parse
import urllib.request

API = "https://api.scryfall.com/cards/named?exact="
CABECALHOS = {"User-Agent": "TrabalhoPOO2-MTG/1.0", "Accept": "application/json;q=0.9,*/*;q=0.8"}

TIPOS = {
    "Artifact": "Artefato", "Creature": "Criatura", "Enchantment": "Encantamento",
    "Sorcery": "Feitiço", "Instant": "Mágica Instantânea", "Planeswalker": "Planeswalker",
    "Land": "Terreno", "Battle": "Batalha", "Kindred": "Tribal", "Tribal": "Tribal",
}
SUPERTIPOS = {"Basic": "Básico", "Legendary": "Lendário", "Snow": "Neve", "World": "Mundo"}
CORES = {"W": "Branco", "U": "Azul", "B": "Preto", "R": "Vermelho", "G": "Verde"}
SUBTIPOS_COM_ESPACO = ["Time Lord"]  # subtipos que têm duas palavras


def ler_lista(caminho):
    """Devolve os nomes das cartas, sem repetir (terrenos básicos entram uma vez só)."""
    nomes = []
    with open(caminho, encoding="utf-8") as f:
        for linha in f:
            linha = linha.strip()
            if not linha:
                continue
            m = re.match(r"^\d+x?\s+(.+)$", linha)
            nome = m.group(1).strip() if m else linha
            if nome not in nomes:
                nomes.append(nome)
    return nomes


def separar_tipos(type_line):
    """'Legendary Creature — Frog Wizard' -> (['Lendário'], ['Criatura'], ['Frog', 'Wizard'])"""
    # cartas de duas faces: usa só a primeira face
    type_line = type_line.split("//")[0].strip()
    esquerda, _, direita = type_line.partition("—")

    supertipos, tipos = [], []
    for palavra in esquerda.split():
        if palavra in SUPERTIPOS:
            supertipos.append(SUPERTIPOS[palavra])
        elif palavra in TIPOS:
            tipos.append(TIPOS[palavra])

    direita = direita.strip()
    subtipos = []
    for composto in SUBTIPOS_COM_ESPACO:
        if composto in direita:
            subtipos.append(composto)
            direita = direita.replace(composto, " ")
    subtipos += direita.split()
    return supertipos, tipos, subtipos


def numero(texto):
    try:
        return int(texto)
    except (TypeError, ValueError):
        return 0  # "*", "1+*" ou ausente


def montar_linha(dados):
    """Converte a resposta do Scryfall numa linha do CSV (e devolve os atributos para o SQL)."""
    face = dados["card_faces"][0] if "card_faces" in dados and "type_line" not in dados else dados
    type_line = dados.get("type_line") or face.get("type_line", "")
    supertipos, tipos, subtipos = separar_tipos(type_line)

    cores = [CORES[c] for c in (dados.get("colors") or face.get("colors") or []) if c in CORES]
    if not cores:
        cores = ["Incolor"]

    poder = dados.get("power", face.get("power"))
    resistencia = dados.get("toughness", face.get("toughness"))

    linha = [
        dados["name"], ",".join(tipos), ",".join(supertipos), ",".join(subtipos),
        ",".join(cores), str(numero(poder)), str(numero(resistencia)),
    ]
    return linha, tipos, supertipos, subtipos, cores


def url_imagem(dados):
    imagens = dados.get("image_uris") or dados["card_faces"][0]["image_uris"]
    return imagens["normal"]


def nome_de_arquivo(nome):
    return re.sub(r'[<>:"/\\|?*]', "", nome)


def baixar(url, destino):
    req = urllib.request.Request(url, headers=CABECALHOS)
    with urllib.request.urlopen(req, timeout=30) as r, open(destino, "wb") as f:
        f.write(r.read())


def consultar(nome):
    req = urllib.request.Request(API + urllib.parse.quote(nome), headers=CABECALHOS)
    with urllib.request.urlopen(req, timeout=30) as r:
        return json.loads(r.read().decode("utf-8"))


def sql_inserts(tabela, coluna, valores):
    if not valores:
        return ""
    itens = ",\n ".join("('" + v.replace("'", "''") + "')" for v in sorted(valores))
    return f"INSERT INTO {tabela} ({coluna}) VALUES\n {itens}\nON CONFLICT DO NOTHING;\n\n"


def main():
    if len(sys.argv) < 3:
        print("Uso: python baixar_deck.py decklist.txt pasta_saida")
        return

    nomes = ler_lista(sys.argv[1])
    saida = sys.argv[2]
    os.makedirs(saida, exist_ok=True)

    linhas = ["nome;tipo;supertipo;subtipo;cores;dano;resistencia"]
    todos = {"tipo": set(), "super": set(), "sub": set(), "cor": set()}
    nao_achadas = []

    for i, nome in enumerate(nomes, 1):
        try:
            dados = consultar(nome)
            linha, tipos, supers, subs, cores = montar_linha(dados)
            arquivo = os.path.join(saida, nome_de_arquivo(dados["name"]) + ".jpg")
            if not os.path.exists(arquivo):
                baixar(url_imagem(dados), arquivo)
            linhas.append(";".join(linha))
            todos["tipo"].update(tipos)
            todos["super"].update(supers)
            todos["sub"].update(subs)
            todos["cor"].update(cores)
            print(f"[{i}/{len(nomes)}] OK: {dados['name']}")
        except urllib.error.HTTPError as e:
            nao_achadas.append(f"{nome} (HTTP {e.code})")
            print(f"[{i}/{len(nomes)}] NÃO ACHEI: {nome} (HTTP {e.code})")
        except Exception as e:  # rede, JSON inesperado etc.
            nao_achadas.append(f"{nome} ({e})")
            print(f"[{i}/{len(nomes)}] ERRO: {nome}: {e}")
        time.sleep(0.12)  # o Scryfall pede no máximo ~10 requisições por segundo

    with open(os.path.join(saida, "cartas.csv"), "w", encoding="utf-8", newline="") as f:
        f.write("\n".join(linhas) + "\n")

    with open(os.path.join(saida, "atributos.sql"), "w", encoding="utf-8") as f:
        f.write(sql_inserts("Tipo", "nomeTipo", todos["tipo"]))
        f.write(sql_inserts("Supertipo", "nomeSupertipo", todos["super"]))
        f.write(sql_inserts("Subtipo", "nomeSubtipo", todos["sub"]))
        f.write(sql_inserts("Cor", "nomeCor", todos["cor"]))

    print()
    print(f"Prontas: {len(linhas) - 1} de {len(nomes)}. Pasta: {saida}")
    if nao_achadas:
        print("Não encontradas (confira o nome em inglês):")
        for n in nao_achadas:
            print("  -", n)


if __name__ == "__main__":
    main()
