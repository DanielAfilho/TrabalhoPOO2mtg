package dao.escopo;

public class CartasFaltantes implements EscopoColecao {

    @Override
    public String condicaoSql() {
        return "NOT EXISTS (SELECT 1 FROM TemCarta tc WHERE tc.cartaId = c.cartaId AND tc.usuarioId = ?)";
    }

}
