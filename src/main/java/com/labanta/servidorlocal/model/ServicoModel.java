package com.labanta.servidorlocal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ServicoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String titulo;
    private String descricao;
    private Double preco;
    private Boolean estaAtivo;
    private Double precoComDesconto;
    private String imagemCapa;

    public ServicoModel() {
    }

    public ServicoModel(
            String titulo,
            String descricao,
            double preco,
            Boolean estaAtivo,
            Double precoComDesconto,
            String imagemCapa) {

        this.titulo = titulo;
        this.descricao = descricao;
        this.preco = preco;
        this.estaAtivo = estaAtivo;
        this.precoComDesconto = precoComDesconto;
        this.imagemCapa = imagemCapa;
    }

    public Long getId() {
        return id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getPreco() {
        return preco;
    }

    public void setEstaAtivo(boolean estaAtivo) {
        this.estaAtivo = estaAtivo;
    }

    public boolean getEstaAtivo() {
        return estaAtivo;
    }

    public void setPrecoComDesconto(Double precoComDesconto) {  this.precoComDesconto = precoComDesconto;
    }

    public Double getPrecoComDesconto() {
        return precoComDesconto;
    }

    public void setImagemCapa(String imagemCapa) {
        this.imagemCapa = imagemCapa;
    }

    public String getImagemCapa() {
        return imagemCapa;
    }
}
