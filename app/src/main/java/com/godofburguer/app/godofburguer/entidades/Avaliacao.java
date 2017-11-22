package com.godofburguer.app.godofburguer.entidades;

public class Avaliacao {

    private Integer id, agilidade, satisfacao, qualidade;
    private String titulo, descricao;

    public Avaliacao(){
    }

    public Avaliacao(Integer id, Integer agilidade, Integer satisfacao, Integer qualidade) {
        this.id = id;
        this.agilidade = agilidade;
        this.satisfacao = satisfacao;
        this.qualidade = qualidade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public void setAgilidade(Integer agilidade) {
        this.agilidade = agilidade;
    }

    public Integer getAgilidade() {
        return agilidade;
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