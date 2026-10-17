package model.carta;

public class Tipo {
    private int id;
    private String nomeTipo;

    public Tipo(int id, String nomeTipo) {
        this.id = id;
        this.nomeTipo = nomeTipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeTipo() {
        return nomeTipo;
    }

    public void setNomeTipo(String nomeTipo) {
        this.nomeTipo = nomeTipo;
    }

}
