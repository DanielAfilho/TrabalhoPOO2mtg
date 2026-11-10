package command;

import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * PADRÃO GoF: COMMAND (invoker).
 *
 * Executa os comandos e mantém uma pilha com os já executados, permitindo desfazer
 * as ações da coleção na ordem inversa.
 */
public class HistoricoComandos {
    private static final int LIMITE = 20;

    private final Deque<Comando> executados = new ArrayDeque<>();

    public void executar(Comando comando) throws SQLException {
        comando.executar();
        executados.push(comando);

        if (executados.size() > LIMITE) {
            executados.removeLast();
        }
    }

    public boolean podeDesfazer() {
        return !executados.isEmpty();
    }

    /** Desfaz o último comando executado e o devolve (para a camada acima informar o que foi desfeito). */
    public Comando desfazer() throws SQLException {
        Comando ultimo = executados.peek();
        if (ultimo == null) {
            return null;
        }

        ultimo.desfazer();   // só sai da pilha se o desfazer funcionou
        executados.pop();
        return ultimo;
    }

}
