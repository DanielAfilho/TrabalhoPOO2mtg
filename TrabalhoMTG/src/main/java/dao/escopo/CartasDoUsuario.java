package dao.escopo;

/** PADRÃO GoF: STRATEGY (estratégia concreta) — apenas cartas que o usuário JÁ possui. */
public class CartasDoUsuario implements EscopoColecao {

    @Override
    public String condicaoSql() {
        return "EXISTS (SELECT 1 FROM TemCarta tc WHERE tc.cartaId = c.cartaId AND tc.usuarioId = ?)";
    }

}
