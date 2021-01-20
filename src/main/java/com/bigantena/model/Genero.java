/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.model;

import com.bigantena.bean.AbstractBean;
import com.bigantena.web.util.View;
import com.fasterxml.jackson.annotation.JsonView;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author aspferraz
 */

@Entity
@Table(name = "genero")
public class Genero extends AbstractBean {
    
    @JsonView(View.Minima.class)
    @Column(name = "nome", length = 44)
    private String mNome;
    
    @JsonView(View.Minima.class)
    @Column(name = "qtd_radios")
    private Integer mQtdRadios;
    
    @Column(name = "url", length = 64)
    private String mUrl;

    public Genero() {
    }

    public Genero(String nome) {
        mNome = nome;
    }
    
    public Genero(Integer id) {
        super.mId = id;
    }
    
    public Genero(Integer id, String nome, String url) {
        super.mId = id;
        mNome = nome;
        mUrl = url;
    }

    public String getNome() {
        return mNome;
    }

    public void setNome(String nome) {
        this.mNome = nome;
    }

    public Integer getQtdRadios() {
        return mQtdRadios;
    }

    public void setQtdRadios(Integer qtdRadios) {
        mQtdRadios = qtdRadios;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
