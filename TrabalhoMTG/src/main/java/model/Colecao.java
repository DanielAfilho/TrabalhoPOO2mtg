package model;

import java.util.List;

public class Colecao {
    private List<Carta> cartasUsuario;
    private List<Carta> cartasDisponiveis;

    public Colecao(List<Carta> cartasUsuario, List<Carta> cartasDisponiveis) {
        this.cartasUsuario = cartasUsuario;
        this.cartasDisponiveis = cartasDisponiveis;
    }

    public List<Carta> getCartasUsuario() {
        return cartasUsuario;
    }

    public void setCartasUsuario(List<Carta> cartasUsuario) {
        this.cartasUsuario = cartasUsuario;
    }

    public List<Carta> getCartasDisponiveis() {
        return cartasDisponiveis;
    }

    public void setCartasDisponiveis(List<Carta> cartasDisponiveis) {
        this.cartasDisponiveis = cartasDisponiveis;
    }

}
