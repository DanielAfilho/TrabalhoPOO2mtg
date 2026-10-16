package model.carta;

public class Supertipo {
    private int id;
    private String nomeSuperipo;

    public Supertipo (int id, String nomeSupertipo) {
        this.id = id;
        this.nomeSuperipo = nomeSupertipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeSuperipo() {
        return nomeSuperipo;
    }

    public void setNomeSuperipo(String nomeSuperipo) {
        this.nomeSuperipo = nomeSuperipo;
    }

}
