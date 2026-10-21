[README.md](https://github.com/user-attachments/files/33749552/README.md)
# Magic Card Tracker

Buscador e coleção de cartas de **Magic: The Gathering**, feito em **JavaFX** com banco de dados **PostgreSQL** para o 1º trabalho de Programação Orientada a Objetos II.

O usuário faz login, vê todas as cartas cadastradas, filtra por nome, tipo, supertipo, subtipo, cor, dano e resistência, e monta a sua coleção clicando nas cartas.

## Funcionalidades

- **Login e cadastro de usuário** (tela de login).
- **CRUD de cartas** (tela *Cadastro de cartas*): criar, buscar pelo nome, atualizar e excluir, com tipos, supertipos, subtipos, cores, dano, resistência e imagem.
- **CRUD de usuários** (tela *Usuários*): lista em tabela (`TableView`), criar, atualizar e excluir.
- **Coleção** do usuário: duas áreas, *Minhas cartas* e *Disponíveis*. Um clique na imagem adquire ou remove a carta. Cada usuário tem no máximo uma unidade de cada carta.
- **Filtros** na Coleção: nome, tipo, supertipo, subtipo, cor, dano e resistência.

## Arquitetura

O projeto segue **MVC** com o padrão **DAO**:

| Camada | Pacote | Responsabilidade |
|---|---|---|
| Model | `model`, `model.atributos` | Classes que representam os dados (`Carta`, `Usuario`, `Tipo`, `Supertipo`, `Subtipo`, `Cor`) |
| View | `resources/view` | Telas em FXML (`Login`, `Colecao`, `CadastroCarta`, `EditarUsuario`) |
| Controller | `controller` | Reage aos eventos da tela e conversa com os DAOs |
| DAO | `dao` | Todo o acesso ao banco, com `PreparedStatement`. Cada DAO implementa uma interface (`ICarta`, `IUsuario`, `IAtributo`, `IColecao`, `IConsulta`) |
| Apoio | `main` | `Main` (inicia o programa) e `GerenciadorTelas` (troca de telas e usuário logado) |

Os diagramas UML estão em `docs/`.

## Como executar

### 1. Banco de dados

1. Instale o PostgreSQL (senha do usuário `postgres`: `postgres`).
2. Crie o banco e rode o script:
   ```sql
   CREATE DATABASE "MagicCardTracker";
   ```
   ```
   psql -U postgres -d MagicCardTracker -f sql/banco.sql
   ```
   O script cria as tabelas e os dados iniciais (tipos, supertipos, subtipos e cores).

A conexão está em `src/main/java/dao/IConst.java`. Se o seu banco usa outra porta, usuário ou senha, ajuste esse arquivo.

### 2. Programa

Abra o projeto no IntelliJ, configure o SDK do JavaFX e rode `main.Main` com os **VM options**:

```
--module-path "<caminho>\javafx-sdk-27\lib" --add-modules javafx.controls,javafx.fxml
```

### 3. Cartas de exemplo (opcional)

O banco começa sem cartas. Duas formas de popular:

- Pela tela **Cadastro de cartas**.
- Com as ferramentas de `ferramentas/`, que carregam um deck inteiro de uma vez:
  1. `python baixar_deck.py decklist.txt deck_saida` baixa as imagens e gera o `cartas.csv` e o `atributos.sql` (usa a API pública do [Scryfall](https://scryfall.com/docs/api)).
  2. Rode o `atributos.sql` no banco.
  3. Rode a classe `util.CarregarCartas`, passando a pasta `deck_saida` como argumento.

## Estrutura do repositório

```
src/main/java/
  controller/   LoginController, ColecaoController, CadastroCartaController, EditarUsuarioController
  dao/          interfaces I*, implementações *DAO, Conexao, IConst
  model/        Carta, Usuario, Colecao e atributos/ (Tipo, Supertipo, Subtipo, Cor)
  main/         Main, GerenciadorTelas
  util/         CarregarCartas (ferramenta de carga)
src/main/resources/view/   telas FXML
sql/           banco.sql
docs/          diagramas UML
ferramentas/   baixar_deck.py, decklist.txt, CarregarCartas.java
```
