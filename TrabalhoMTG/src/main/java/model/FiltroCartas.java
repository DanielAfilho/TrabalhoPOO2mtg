package model;

import model.atributos.Cor;
import model.atributos.Subtipo;
import model.atributos.Supertipo;
import model.atributos.Tipo;

import java.util.List;

/**
 * PADRÃO GoF: BUILDER (produto).
 *
 * Representa, de forma imutável, o conjunto de filtros que o usuário aplica na tela de Coleção.
 * Só pode ser criado através de {@link Builder}, que permite montar apenas os filtros desejados
 * (todos são opcionais) sem construtores com muitos parâmetros nem chamadas em ordem obrigatória.
 */
public final class FiltroCartas {
    private final String nome;
    private final List<Tipo> tipos;
    private final List<Supertipo> supertipos;
    private final List<Subtipo> subtipos;
    private final List<Cor> cores;
    private final Integer dano;
    private final Integer resistencia;

    private FiltroCartas(Builder b) {
        this.nome = b.nome;
        this.tipos = b.tipos;
        this.supertipos = b.supertipos;
        this.subtipos = b.subtipos;
        this.cores = b.cores;
        this.dano = b.dano;
        this.resistencia = b.resistencia;
    }

    /** Filtro vazio (sem nenhuma restrição). */
    public static FiltroCartas vazio() {
        return new Builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    /** @return texto (regex, sem diferenciar maiúsculas) ou null quando não há filtro por nome */
    public String getNome() {
        return nome;
    }

    public List<Tipo> getTipos() {
        return tipos;
    }

    public List<Supertipo> getSupertipos() {
        return supertipos;
    }

    public List<Subtipo> getSubtipos() {
        return subtipos;
    }

    public List<Cor> getCores() {
        return cores;
    }

    /** @return valor de dano ou null quando não há filtro por dano */
    public Integer getDano() {
        return dano;
    }

    /** @return valor de resistência ou null quando não há filtro por resistência */
    public Integer getResistencia() {
        return resistencia;
    }

    /** PADRÃO GoF: BUILDER (construtor passo a passo). */
    public static final class Builder {
        private String nome;
        private List<Tipo> tipos = List.of();
        private List<Supertipo> supertipos = List.of();
        private List<Subtipo> subtipos = List.of();
        private List<Cor> cores = List.of();
        private Integer dano;
        private Integer resistencia;

        private Builder() {
        }

        public Builder nome(String nome) {
            this.nome = (nome == null || nome.isBlank()) ? null : nome.trim();
            return this;
        }

        public Builder tipos(List<Tipo> tipos) {
            this.tipos = tipos == null ? List.of() : List.copyOf(tipos);
            return this;
        }

        public Builder supertipos(List<Supertipo> supertipos) {
            this.supertipos = supertipos == null ? List.of() : List.copyOf(supertipos);
            return this;
        }

        public Builder subtipos(List<Subtipo> subtipos) {
            this.subtipos = subtipos == null ? List.of() : List.copyOf(subtipos);
            return this;
        }

        public Builder cores(List<Cor> cores) {
            this.cores = cores == null ? List.of() : List.copyOf(cores);
            return this;
        }

        public Builder dano(Integer dano) {
            this.dano = dano;
            return this;
        }

        public Builder resistencia(Integer resistencia) {
            this.resistencia = resistencia;
            return this;
        }

        public FiltroCartas build() {
            return new FiltroCartas(this);
        }
    }
}
