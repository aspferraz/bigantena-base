/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.model;

import com.bigantena.bean.AbstractBean;
import com.bigantena.web.util.View;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author aspferraz
 */

@Entity
@Table(name = "alteracao_radio")
public class AlteracaoRadio extends AbstractBean {
    
    @JsonView(View.Minima.class)
    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_radio")
    private Radio mRadio;
    
    @JsonView(View.Minima.class)
    @Column(name = "nome_radio")
    private String mNomeRadio;
    
    @JsonView(View.Minima.class)
    @Column(name = "logo_radio")
    private byte[] mLogoRadio;
    
    @JsonView(View.Minima.class)
    @Column(name = "nome_arquivo_logo_radio")
    private String mNomeArquivoLogoRadio;
    
    @JsonView(View.Minima.class)
    @Column(name = "url_servidor_radio")
    private String mUrlServidorRadio;
    
    @JsonView(View.Minima.class)
    @Column(name = "site_radio")
    private String mSiteRadio;
    
    @JsonView(View.Minima.class)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JsonProperty("dataAlteracaoRadio")
    @Column(name = "dt_alteracao_radio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mDataAlteracaoRadio;

    public AlteracaoRadio() {
    }
    
    public AlteracaoRadio(Integer id) {
        super.mId = id;
    }
    
    public AlteracaoRadio(Integer id, Radio radio, String urlServidorRadio, Date dataAlteracaoRadio) {
        super.mId = id;
        mRadio = radio;
        mUrlServidorRadio = urlServidorRadio;
        mDataAlteracaoRadio = dataAlteracaoRadio;   
    }

    public AlteracaoRadio(Integer id, Radio radio, String nomeRadio, byte[] logoRadio, String nomeArquivoLogoRadio, String urlServidorRadio, String siteRadio, Date dataAlteracaoRadio) {
        super.mId = id;
        mRadio = radio;
        mNomeRadio = nomeRadio;
        mLogoRadio = logoRadio;
        mNomeArquivoLogoRadio = nomeArquivoLogoRadio;
        mUrlServidorRadio = urlServidorRadio;
        mSiteRadio = siteRadio;
        mDataAlteracaoRadio = dataAlteracaoRadio;
    }

    public Radio getRadio() {
        return mRadio;
    }

    public void setRadio(Radio radio) {
        mRadio = radio;
    }

    public String getNomeRadio() {
        return mNomeRadio;
    }

    public void setNomeRadio(String nomeRadio) {
        mNomeRadio = nomeRadio;
    }

    public byte[] getmLogoRadio() {
        return mLogoRadio;
    }

    public void setLogoRadio(byte[] logoRadio) {
        mLogoRadio = logoRadio;
    }

    public String getNomeArquivoLogoRadio() {
        return mNomeArquivoLogoRadio;
    }

    public void setNomeArquivoLogoRadio(String nomeArquivoLogoRadio) {
        this.mNomeArquivoLogoRadio = nomeArquivoLogoRadio;
    }

    public String getUrlServidorRadio() {
        return mUrlServidorRadio;
    }

    public void setUrlServidorRadio(String urlServidorRadio) {
        mUrlServidorRadio = urlServidorRadio;
    }

    public String getSiteRadio() {
        return mSiteRadio;
    }

    public void setSiteRadio(String siteRadio) {
        mSiteRadio = siteRadio;
    }

    public Date getDataAlteracaoRadio() {
        return mDataAlteracaoRadio;
    }

    public void setDataAlteracaoRadio(Date dataAlteracaoRadio) {
        mDataAlteracaoRadio = dataAlteracaoRadio;
    }
}
