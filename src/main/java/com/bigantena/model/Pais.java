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
import javax.persistence.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author aspferraz
 */

@Entity
@Table(name = "pais")
public class Pais extends AbstractBean implements Serializable {
    
    @JsonView(View.Minima.class)
    @Column(name = "nome", length = 51)
    private String mNome;
    
    @JsonView(View.Minima.class)
    @Column(name = "nome_en", length = 51)
    private String mNomeEn;
    
    @JsonView(View.Minima.class)
    @Column(name = "sigla", length = 5)
    private String mSigla;
    
    @JsonView(View.MinimaComBandeira.class)
    @Column(name = "bandeira")
    private byte[] mBandeira;
    
    @JsonView(View.MinimaComBandeira.class)
    @Column(name = "nome_arquivo_bandeira")
    private String mNomeArquivoBandeira;
    
    @JsonView(View.Minima.class)
    @Column(name = "qtd_radios")
    private Integer mQtdRadios;
    
    @JsonView(View.Basica.class)
    @ManyToOne
    @JoinColumn(name = "id_continente")
    private Continente mContinente;
    
    @JsonView(View.BasicaComEstados.class)
    @OneToMany(mappedBy = "mPais")
    private Set<Estado> mEstados;
    
    @JsonView(View.BasicaComCidades.class)
    @OneToMany(mappedBy = "mPais")
    private Set<Cidade> mCidades;
    
    @Column(name = "url", length = 67)
    private String mUrl; //url para importação dos dados
    
    public Pais() {
    }
    
    public Pais(String nome) {
        mNome = nome;
    }
    
    public Pais(Integer id) {
        super.mId = id;
    }

    public Pais(Integer id, String nome, String nomeEn, String sigla, Continente continente, byte[] bandeira, String nomeArquivoBandeira, String url) {
        super.mId = id;
        mNome = nome;
        mNomeEn = nomeEn;
        mSigla = sigla;
        mContinente = continente;
        mBandeira = bandeira;
        mNomeArquivoBandeira = nomeArquivoBandeira;
        mUrl = url;
    }

    /**
     * @return the mNome
     */
    public String getNome() {
        return mNome;
    }

    /**
     * @param nome the mNome to set
     */
    public void setNome(String nome) {
        mNome = nome;
    }

    public String getNomeEn() {
        return mNomeEn;
    }

    public void setNomeEn(String nomeEn) {
        mNomeEn = nomeEn;
    }
    
    /**
     * @return the mSigla
     */
    public String getSigla() {
        return mSigla;
    }

    /**
     * @param sigla the mSigla to set
     */
    public void setSigla(String sigla) {
        mSigla = sigla;
    }

    public byte[] getBandeira() {
        return mBandeira;
    }

    public void setBandeira(byte[] bandeira) {
        mBandeira = bandeira;
    }

    public String getNomeArquivoBandeira() {
        return mNomeArquivoBandeira;
    }

    public void setNomeArquivoBandeira(String nomeArquivoBandeira) {
        mNomeArquivoBandeira = nomeArquivoBandeira;
    }

    public Integer getQtdRadios() {
        return mQtdRadios;
    }

    public void setQtdRadios(Integer qtdRadios) {
        mQtdRadios = qtdRadios;
    }
    
    public Continente getContinente() {
        return mContinente;
    }

    public void setContinente(Continente continente) {
        mContinente = continente;
    }
    
    public Set<Estado> getEstados() {
        return mEstados;
    }
    
    public Set<Cidade> getCidades() {
        return mCidades;
    }
    
    /**
     * @return the mUrl
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * @param url the mUrl to set
     */
    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "mEstados", "mCidades", "mBandeira");
    }
    
}
