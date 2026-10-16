package model;

public class Usuario {
    private int id;
    private String nomeUsuario;
    private String senha;

    public Usuario (int id, String nomeUsuario, String senha) {
        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nomeUsuario;
    }

    public void setNome (String nome) {
        nomeUsuario = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


}
