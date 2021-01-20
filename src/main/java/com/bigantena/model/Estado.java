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
@Table(name = "estado")
public class Estado extends AbstractBean implements Serializable {
    
    @JsonView(View.Minima.class)
    @Column(name = "nome", length = 64)
    private String mNome;
    
    @JsonView(View.Minima.class)
    @Column(name = "sigla", length = 5)
    private String mSigla;
    
    @JsonView(View.Minima.class)
    @Column(name = "qtd_radios")
    private Integer mQtdRadios;
    
    @JsonView(View.BasicaComCidades.class)
    @OneToMany(mappedBy = "mEstado")
    private Set<Cidade> mCidades;
    
    @JsonView(View.Basica.class)
    @ManyToOne
    @JoinColumn(name = "id_pais")
    private Pais mPais;

    @Column(name = "url", length = 78)
    private String mUrl;
    
    public Estado() {
    }
    
    public Estado(String nome) {
        mNome = nome;
    }
    
    public Estado(Integer id) {
        super.mId = id;
    }
    
    public Estado(Integer id, String nome, String sigla, String url, Set<Cidade> cidades, Pais pais) {
        super.mId = id;
        mNome = nome;
        mSigla = sigla;
        mUrl = url;
        mCidades = cidades;
        mPais = pais;
    }


    public String getNome() {
        return mNome;
    }

    public void setNome(String nome) {
        mNome = nome;
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
    
    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
    
    public Set<Cidade> getCidades() {
        return mCidades;
    }

    public void setCidades(Set<Cidade> cidades) {
        mCidades = cidades;
    }
    
    public Pais getPais() {
        return mPais;
    }

    public void setPais(Pais pais) {
        mPais = pais;
    }
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "mCidades");
    }
    
}
