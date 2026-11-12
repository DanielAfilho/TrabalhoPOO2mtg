package service;

import model.atributos.Cor;
import model.atributos.Subtipo;
import model.atributos.Supertipo;
import model.atributos.Tipo;

import java.util.List;

public record CatalogoAtributos(List<Tipo> tipos,
                                List<Supertipo> supertipos,
                                List<Subtipo> subtipos,
                                List<Cor> cores) {
}
