package com.godofburguer.app.godofburguer.entidades;

public class Avaliacao {

    private Integer satisfacao, qualidade, agilidade, pontos;
    private String titulo, descricao;

    public Avaliacao(){
    }

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }

    public Integer getSatisfacao() {
        return satisfacao;
    }

    public void setSatisfacao(Integer satisfacao) {
        this.satisfacao = satisfacao;
    }

    public Integer getQualidade() {
        return qualidade;
    }

    public void setQualidade(Integer qualidade) {
        this.qualidade = qualidade;
    }

    public Integer getAgilidade() {
        return agilidade;
    }

    public void setAgilidade(Integer agilidade) {
        this.agilidade = agilidade;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}