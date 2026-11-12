package dao;

import dao.escopo.EscopoColecao;
import model.Carta;
import model.FiltroCartas;
import model.Usuario;
import model.atributos.Atributo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO implements IConsulta {

    @Override
    public List<Carta> buscar(Usuario usuario, EscopoColecao escopo, FiltroCartas filtro) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT c.cartaId, c.imagem FROM Carta c WHERE ")
                .append(escopo.condicaoSql());
        List<Object> parametros = new ArrayList<>();
        parametros.add(usuario.getId());

        if (filtro.getNome() != null) {
            sql.append(" AND c.nomeCarta ~* ?");
            parametros.add(filtro.getNome());
        }

        // a carta precisa ter TODOS os atributos selecionados (mesma semântica do INTERSECT original)
        exigirAtributos(sql, parametros, "TemTipo", "tipoId", filtro.getTipos());
        exigirAtributos(sql, parametros, "TemSupertipo", "supertipoId", filtro.getSupertipos());
        exigirAtributos(sql, parametros, "TemSubtipo", "subtipoId", filtro.getSubtipos());
        exigirAtributos(sql, parametros, "TemCor", "corId", filtro.getCores());

        if (filtro.getResistencia() != null) {
            sql.append(" AND c.resistencia = ?");
            parametros.add(filtro.getResistencia());
        }

        if (filtro.getDano() != null) {
            sql.append(" AND c.dano = ?");
            parametros.add(filtro.getDano());
        }

        sql.append(" ORDER BY c.nomeCarta");

        List<Carta> lista = new ArrayList<>();

        try (Connection conexao = Conexao.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Carta(rs.getInt("cartaId"), rs.getBytes("imagem")));
                }
            }
        }

        return lista;
    }

    // tabela e coluna vêm sempre de constantes do código, nunca da entrada do usuário
    private void exigirAtributos(StringBuilder sql, List<Object> parametros, String tabela, String coluna,
                                 List<? extends Atributo> atributos) {
        for (Atributo a : atributos) {
            sql.append(" AND EXISTS (SELECT 1 FROM ").append(tabela)
                    .append(" x WHERE x.cartaId = c.cartaId AND x.").append(coluna).append(" = ?)");
            parametros.add(a.getId());
        }
    }

}
