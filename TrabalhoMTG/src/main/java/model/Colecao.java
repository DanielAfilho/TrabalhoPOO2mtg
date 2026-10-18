package model;

import java.util.List;

public class Colecao {
    private List<Carta> cartasUsuario;
    private List<Carta> cartasTotais;

    public Colecao(List<Carta> cartasUsuario, List<Carta> cartasTotais) {
        this.cartasUsuario = cartasUsuario;
        this.cartasTotais = cartasTotais;
    }

    public List<Carta> getCartasUsuario() {
        return cartasUsuario;
    }

    public void setCartasUsuario(List<Carta> cartasUsuario) {
        this.cartasUsuario = cartasUsuario;
    }

    public List<Carta> getCartasTotais() {
        return cartasTotais;
    }

    public void setCartasTotais(List<Carta> cartasTotais) {
        this.cartasTotais = cartasTotais;
    }

}
