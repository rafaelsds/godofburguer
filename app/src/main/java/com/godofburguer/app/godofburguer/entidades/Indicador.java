package com.godofburguer.app.godofburguer.entidades;



public class Indicador {

    private Integer satisfacao, qualidade, agilidade;

    public  Indicador(Integer satisfacao, Integer qualidade, Integer agilidade){
        this.satisfacao = satisfacao;
        this.qualidade = qualidade;
        this.agilidade = agilidade;
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

}
