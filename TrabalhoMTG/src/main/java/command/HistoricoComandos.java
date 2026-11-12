package command;

import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;

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

    public Comando desfazer() throws SQLException {
        Comando ultimo = executados.peek();
        if (ultimo == null) {
            return null;
        }

        ultimo.desfazer();
        executados.pop();
        return ultimo;
    }

}
