package dao.escopo;

/**
 * PADRÃO GoF: STRATEGY (interface da estratégia).
 *
 * Define COMO restringir a consulta de cartas em relação à coleção de um usuário.
 * Cada estratégia devolve uma condição SQL (a cláusula WHERE da consulta) que contém
 * exatamente um parâmetro "?" — o id do usuário.
 *
 * O contexto é {@link dao.ConsultaDAO}, que monta a consulta sem saber qual escopo está usando.
 */
public interface EscopoColecao {

    String condicaoSql();

}
