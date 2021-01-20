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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author aspferraz
 */

@Entity
@Table(name = "continente")
public class Continente extends AbstractBean implements Serializable {
    
    @JsonView(View.Minima.class)
    @Column(name = "nome", length = 51)
    private String mNome;
    
    @JsonView(View.Minima.class)
    @Column(name = "nome_en", length = 51)
    private String mNomeEn;
    
    @JsonView(View.Minima.class)
    @Column(name = "sigla", length = 5)
    private String mSigla;
    
    @JsonView(View.Minima.class)
    @Column(name = "qtd_radios")
    private Integer mQtdRadios;
    
    @JsonView(View.BasicaComPaises.class)
    @OneToMany(mappedBy = "mContinente")
    private Set<Pais> mPaises;
    
    public Continente() {
    }
    
    public Continente(String nome) {
        mNome = nome;
    }
    
    public Continente(Integer id) {
        super.mId = id;
    }
            
    public Continente(Integer id, String nome, String nomeEn, String sigla) {
        super.mId = id;
        mNome = nome;
        mNomeEn = nomeEn;
        mSigla = sigla;
    }

    public String getNome() {
        return mNome;
    }

    public void setNome(String nome) {
        mNome = nome;
    }

    public String getNomeEn() {
        return mNomeEn;
    }

    public void setNomeEn(String nomeEn) {
        mNomeEn = nomeEn;
    }

    public String getSigla() {
        return mSigla;
    }

    public void setSigla(String sigla) {
        mSigla = sigla;
    }

    public Integer getQtdRadios() {
        return mQtdRadios;
    }

    public void setQtdRadios(Integer qtdRadios) {
        mQtdRadios = qtdRadios;
    }
    
    public Set<Pais> getPaises() {
        return mPaises;
    }

    public void setPaises(Set<Pais> paises) {
        mPaises = paises;
    }
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "mPaises");
    }
}
