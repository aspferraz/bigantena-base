/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.model;

import com.bigantena.bean.AbstractBean;
import com.bigantena.web.util.View;
import com.fasterxml.jackson.annotation.JsonView;


import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author aspferraz
 */

@Entity
@Table(name = "cidade")
public class Cidade extends AbstractBean implements Serializable {
    
    @JsonView(View.Minima.class)
    @Column(name = "nome", length = 58)
    private String mNome;
    
    @JsonView(View.Minima.class)
    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado mEstado;
    
    @JsonView(View.Minima.class)
    @ManyToOne
    @JoinColumn(name = "id_pais")
    private Pais mPais;
    
    @JsonView(View.BasicaComRadios.class)
    @OneToMany(mappedBy = "mCidade")
    private Set<Radio> mRadios;
    
    @JsonView(View.Minima.class)
    @Column(name = "qtd_radios")
    private Integer mQtdRadios;
    
    @Column(name = "url", length = 78)
    private String mUrl; //url para importação dos dados

    public Cidade() {
    }
    
    public Cidade(String nome) {
        mNome = nome;
    }
    
    public Cidade(Integer id) {
        super.mId = id;
    }

    public Cidade(Integer id, String nome, String url, Estado estado, Pais pais) {
        super.mId = id;
        mNome = nome;
        mUrl = url;
        mEstado = estado;
        mPais = pais;
    }

    public String getNome() {
        return mNome;
    }

    public void setNome(String nome) {
        mNome = nome;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Integer getQtdRadios() {
        return mQtdRadios;
    }

    public void setQtdRadios(Integer qtdRadios) {
        mQtdRadios = qtdRadios;
    }
    
    public Estado getEstado() {
        return mEstado;
    }

    public void setEstado(Estado estado) {
        mEstado = estado;
    }

    public Pais getPais() {
        return mPais;
    }

    public void setPais(Pais pais) {
        mPais = pais;
    }
    
    public Set<Radio> getRadios() {
        return mRadios;
    }

    public void setRadios(Set<Radio> radios) {
        mRadios = radios;
    }
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "mRadios");
    }
}
