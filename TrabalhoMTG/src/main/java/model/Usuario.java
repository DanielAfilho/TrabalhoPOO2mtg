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

    public void setId(int id1) {
        id = id1;
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

    public void setSenha(String senha1) {
        senha = senha1;
    }


}
