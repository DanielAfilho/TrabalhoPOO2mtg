package util;

import dao.AtributoDAO;
import dao.CartaDAO;
import model.Carta;
import model.atributos.Cor;
import model.atributos.Subtipo;
import model.atributos.Supertipo;
import model.atributos.Tipo;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Programa de uso único para popular o banco com cartas.
 *
 * Uso: rode a classe passando como argumento a PASTA das imagens.
 *   - Cada imagem (png/jpg/jpeg) vira uma carta; o nome do arquivo (sem extensão) é o nome da carta.
 *   - Opcional: um arquivo "cartas.csv" na mesma pasta, separado por ponto e vírgula, com as colunas:
 *       nome;tipo;supertipo;subtipo;cores;dano;resistencia
 *     (vários valores na mesma coluna separados por vírgula, ex.: "Verde,Branco").
 *     Sem o CSV, a carta entra sem tipo e com dano e resistência 0.
 *
 * Rode os INSERT de tipos/supertipos/subtipos/cores ANTES.
 */
public class CarregarCartas {
    private static final int LARGURA_MAXIMA = 300;

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Informe a pasta das imagens como argumento.");
            return;
        }

        Path pasta = Paths.get(args[0]);
        if (!Files.isDirectory(pasta)) {
            System.out.println("Pasta não encontrada: " + pasta);
            return;
        }

        AtributoDAO atributoDAO = new AtributoDAO();
        List<Tipo> tipos = atributoDAO.listarTipos();
        List<Supertipo> supertipos = atributoDAO.listarSupertipos();
        List<Subtipo> subtipos = atributoDAO.listarSubtipos();
        List<Cor> cores = atributoDAO.listarCores();

        Map<String, String[]> csv = lerCsv(pasta.resolve("cartas.csv"));
        CartaDAO cartaDAO = new CartaDAO();

        List<Path> imagens = new ArrayList<>();
        try (Stream<Path> arquivos = Files.list(pasta)) {
            arquivos.filter(CarregarCartas::ehImagem).sorted().forEach(imagens::add);
        }

        int inseridas = 0;
        int repetidas = 0;
        int falhas = 0;

        for (Path arquivo : imagens) {
            String nome = semExtensao(arquivo.getFileName().toString());

            try {
                String[] linha = csv.get(nome.toLowerCase());
                List<String> avisos = new ArrayList<>();

                Carta carta = new Carta(0, reduzir(arquivo), nome, 0, 0);
                carta.setTipos(buscar(coluna(linha, 1), tipos, avisos));
                carta.setSupertipos(buscar(coluna(linha, 2), supertipos, avisos));
                carta.setSubtipos(buscar(coluna(linha, 3), subtipos, avisos));
                carta.setCores(buscar(coluna(linha, 4), cores, avisos));
                carta.setDano(numero(coluna(linha, 5)));
                carta.setResistencia(numero(coluna(linha, 6)));

                cartaDAO.cadastrarCarta(carta);
                inseridas++;
                System.out.println("OK: " + nome);

                for (String aviso : avisos) {
                    System.out.println("   aviso (" + nome + "): atributo não existe no banco -> " + aviso);
                }
            } catch (SQLException e) {
                if ("23505".equals(e.getSQLState())) {
                    repetidas++;
                    System.out.println("JÁ EXISTE: " + nome);
                } else {
                    falhas++;
                    System.out.println("ERRO em " + nome + ": " + e.getMessage());
                }
            } catch (IOException e) {
                falhas++;
                System.out.println("ERRO lendo a imagem de " + nome + ": " + e.getMessage());
            }
        }

        System.out.println();
        System.out.println("Inseridas: " + inseridas + " | já existiam: " + repetidas + " | falhas: " + falhas);
    }

    private static boolean ehImagem(Path caminho) {
        String nome = caminho.getFileName().toString().toLowerCase();
        return nome.endsWith(".png") || nome.endsWith(".jpg") || nome.endsWith(".jpeg");
    }

    private static String semExtensao(String nome) {
        int ponto = nome.lastIndexOf('.');
        return ponto < 0 ? nome : nome.substring(0, ponto);
    }

    /** Reduz a imagem para no máximo LARGURA_MAXIMA de largura e devolve como JPEG. */
    private static byte[] reduzir(Path arquivo) throws IOException {
        BufferedImage original = ImageIO.read(arquivo.toFile());
        if (original == null) {
            throw new IOException("formato de imagem não reconhecido");
        }

        int largura = Math.min(original.getWidth(), LARGURA_MAXIMA);
        int altura = Math.max(1, original.getHeight() * largura / original.getWidth());

        // JPEG não tem transparência: desenha sobre fundo branco
        BufferedImage reduzida = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = reduzida.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, largura, altura, java.awt.Color.WHITE, null);
        g.dispose();

        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        ImageIO.write(reduzida, "jpg", saida);
        return saida.toByteArray();
    }

    private static Map<String, String[]> lerCsv(Path csv) throws IOException {
        Map<String, String[]> mapa = new HashMap<>();
        if (!Files.exists(csv)) {
            return mapa;
        }

        for (String linha : Files.readAllLines(csv, StandardCharsets.UTF_8)) {
            if (linha.isBlank()) {
                continue;
            }
            String[] partes = linha.split(";", -1);
            String chave = partes[0].trim().toLowerCase();
            if (chave.equals("nome")) {
                continue; // cabeçalho
            }
            mapa.put(chave, partes);
        }

        return mapa;
    }

    private static String coluna(String[] linha, int indice) {
        if (linha == null || indice >= linha.length) {
            return "";
        }
        return linha[indice].trim();
    }

    private static int numero(String texto) {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /** Procura cada nome do texto (separados por vírgula) no catálogo, comparando com o toString(). */
    private static <T> List<T> buscar(String texto, List<T> catalogo, List<String> avisos) {
        List<T> encontrados = new ArrayList<>();

        for (String nome : texto.split(",")) {
            nome = nome.trim();
            if (nome.isEmpty()) {
                continue;
            }

            T achado = null;
            for (T item : catalogo) {
                if (item.toString().trim().equalsIgnoreCase(nome)) {
                    achado = item;
                    break;
                }
            }

            if (achado == null) {
                avisos.add(nome);
            } else {
                encontrados.add(achado);
            }
        }

        return encontrados;
    }
}