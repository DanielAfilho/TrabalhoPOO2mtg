package model.carta;

public class Custo {
    private Cor cor;
    private int quantidade;

    public Custo(Cor cor, int quantidade) {
        this.cor = cor;
        this.quantidade = quantidade;
    }

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

}
