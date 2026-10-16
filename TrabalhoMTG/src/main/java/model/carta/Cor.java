package model.carta;

public class Cor {
    private int id;
    private String nomeCor;

    public Cor(int id, String nomeCor) {
        this.id = id;
        this.nomeCor = nomeCor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCor() {
        return nomeCor;
    }

    public void setNomeCor(String nomeCor) {
        this.nomeCor = nomeCor;
    }

}
