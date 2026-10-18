package model;

public class Usuario {
    private int id = -1;
    private String nomeUsuario;
    private String senha;

    public Usuario () {
    }

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

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario (String nome) {
        nomeUsuario = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return nomeUsuario;
    }

}
