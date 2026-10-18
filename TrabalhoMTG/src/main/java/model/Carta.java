package model;

import model.atributos.Cor;
import model.atributos.Subtipo;
import model.atributos.Supertipo;
import model.atributos.Tipo;

import java.util.List;

public class Carta {
    private int id;
    private String nomeCarta;
    private int resistencia;
    private int dano;
    private byte[] imagem;
    private List<Cor> cores;
    private List<Tipo> tipos;
    private List<Supertipo> supertipos;
    private List<Subtipo> subtipos;

    public Carta (int id, byte[] imagem) {
        this.id = id;
        this.imagem = imagem;
    }

    public Carta(int id, byte[] imagem, String nomeCarta, int resistencia, int dano) {
        this.id = id;
        this.imagem = imagem;
        this.nomeCarta = nomeCarta;
        this.resistencia = resistencia;
        this.dano = dano;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCarta() {
        return nomeCarta;
    }

    public void setNomeCarta(String nomeCarta) {
        this.nomeCarta = nomeCarta;
    }

    public int getResistencia() {
        return resistencia;
    }

    public void setResistencia(int resistencia) {
        this.resistencia = resistencia;
    }

    public int getDano() {
        return dano;
    }

    public void setDano(int dano) {
        this.dano = dano;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public List<Cor> getCores() {
        return cores;
    }

    public void setCores(List<Cor> cores) {
        this.cores = cores;
    }

    public List<Tipo> getTipos() {
        return tipos;
    }

    public void setTipos(List<Tipo> tipos) {
        this.tipos = tipos;
    }

    public List<Supertipo> getSupertipos() {
        return supertipos;
    }

    public void setSupertipos(List<Supertipo> supertipos) {
        this.supertipos = supertipos;
    }

    public List<Subtipo> getSubtipos() {
        return subtipos;
    }

    public void setSubtipos(List<Subtipo> subtipos) {
        this.subtipos = subtipos;
    }

}
