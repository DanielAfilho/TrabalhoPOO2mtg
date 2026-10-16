package model.carta;

public class Subtipo {
    private int id;
    private String nomeSubtipo;

    public Subtipo(int id, String nomeSubtipo) {
        this.id = id;
        this.nomeSubtipo = nomeSubtipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeSubtipo() {
        return nomeSubtipo;
    }

    public void setNomeSubtipo(String nomeSubtipo) {
        this.nomeSubtipo = nomeSubtipo;
    }

}
