/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.model;

import com.bigantena.bean.AbstractBean;
import com.bigantena.util.ByteArrays;
import com.bigantena.web.util.View;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.DataFormatException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;


/**
 *
 * @author aspferraz
 */
@Entity
@Table(name = "radio")
public class Radio extends AbstractBean implements Serializable {
    
    @JsonView(View.MinimaComLogo.class)
    @Column(name = "logo")
    private byte[] mLogo;
    
    @JsonView(View.MinimaComLogo.class)
    @Column(name = "nome_arquivo_logo")
    private String mNomeArquivoLogo;
    
    @JsonView(View.Minima.class)
    @Column(name = "slogan")
    private String mSlogan;
    
    @JsonView(View.Minima.class)
    @NotEmpty
    @Column(name = "nome")
    private String mNome;
    
    @JsonView(View.Minima.class)
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cidade")
    private Cidade mCidade;
    
    @JsonView(View.Minima.class)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pais")
    private Pais mPais;
    
    @JsonView(View.Minima.class)
    @ManyToMany(fetch = FetchType.EAGER)
      @JoinTable(name="radio_genero", joinColumns=
      {@JoinColumn(name="id_radio")}, inverseJoinColumns=
        {@JoinColumn(name="id_genero")})
    private Set<Genero> mGeneros;
    
    @JsonView(View.Completa.class)
    @OneToMany(mappedBy = "mRadio", fetch = FetchType.LAZY)
    private Set<Acesso> mAcessos;
    
    @JsonView(View.Minima.class)
    @Column(name = "site")
    private String mSite;
    
    @Transient
//    @ManyToOne
//    @JoinColumn(name = "id_programacao")
    private Programacao mProgramacao;
    
    @JsonView(View.Minima.class)
    @Column(name = "url_servidor_1")
    private String mUrlServidor1;
    
    @JsonView(View.Minima.class)
    @Column(name = "url_servidor_2")
    private String mUrlServidor2;
    
    @JsonView(View.Minima.class)
    @Column(name = "url_servidor_3")
    private String mUrlServidor3; //informado pelo usuário
    
    @JsonView(View.Minima.class)
    @Column(name = "desativada")
    private Boolean mDesativada;
    
    @JsonView(View.Basica.class)
    @Column(name = "qtd_problemas_reportados")
    private Integer mQuantidadeProblemasReportados;
    
    @JsonView(View.Basica.class)  
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JsonProperty("dataUltimoProblemaReportado")
    @Column(name = "dt_ultimo_problema_reportado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mDataUltimoProblemaReportado;
    
    @JsonView(View.Basica.class)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JsonProperty("dataUltimaAtualizacao")
    @Column(name = "dt_ultima_atualizacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mDataUltimaAtualizacao;
    
    @JsonView(View.Minima.class)
    @Column(name = "avaliacao")
    private Float mAvaliacao; 
    
    @JsonView(View.Minima.class)
    @Column(name = "qtd_avaliacoes")
    private Integer mQtdAvaliacoes; 
    
    @Column(name = "url")
    private String mUrl; //url para importação dos dados
    
    private static final Logger logger = Logger.getLogger(Radio.class);

    public Radio() {
    }
    
    public Radio(Integer id) {
        super.mId = id;
    }

    public Radio(Integer id, byte[] logo, String nomeArquivoLogo, String slogan, String nome, Cidade cidade, 
            Pais pais, Set<Genero> generos, String site, Programacao programacao, String urlServidor1, 
            String urlServidor2, Boolean desativada, Date dataUltimaAtualizacao, Float avaliacao, 
            Integer qtdAvaliacoes, String url) {
        
        super.mId = id;
        mLogo = logo;
        mNomeArquivoLogo = nomeArquivoLogo;
        mSlogan = slogan;
        mNome = nome;
        mCidade = cidade;
        mPais = pais;
        mGeneros = generos;
        mSite = site;
        mProgramacao = programacao;
        mUrlServidor1 = urlServidor1;
        mUrlServidor2 = urlServidor2;
        mDesativada = desativada;
        mDataUltimaAtualizacao = dataUltimaAtualizacao;
        mAvaliacao = avaliacao;
        mQtdAvaliacoes = qtdAvaliacoes;
        mUrl = url;
    }

    /**
     * @return the mLogo
     */
    public byte[] getLogo() {
        return mLogo;
    }

    /**
     * @param logo the mLogo to set
     */
    public void setLogo(byte[] logo) {
        mLogo = logo;
    }
    
    public String getNomeArquivoLogo() {
        return mNomeArquivoLogo;
    }

    public void setNomeArquivoLogo(String nomeArquivoLogo) {
        this.mNomeArquivoLogo = nomeArquivoLogo;
    }

    /**
     * @return the mSlogan
     */
    public String getSlogan() {
        return mSlogan;
    }

    /**
     * @param slogan the mSlogan to set
     */
    public void setSlogan(String slogan) {
        mSlogan = slogan;
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

    /**
     * @return the mLocal
     */
    public Cidade getCidade() {
        return mCidade;
    }

    /**
     * @param cidade the mLocal to set
     */
    public void setCidade(Cidade cidade) {
        mCidade = cidade;
    }

    public Pais getPais() {
        return mPais;
    }

    public void setPais(Pais pais) {
        this.mPais = pais;
    }

    /**
     * @return the mGeneros
     */
    public Set<Genero> getGeneros() {
        return mGeneros;
    }

    /**
     * @param generos the mGeneros to set
     */
    public void setGeneros(Set<Genero> generos) {
        mGeneros = generos;
    }

    public void addGenero(Genero genero) {
        if (mGeneros == null) {
            mGeneros = new HashSet<>();
        }
        mGeneros.add(genero);
    }
    
    public Set<Acesso> getAcessos() {
        return mAcessos;
    }

    public void setAcessos(Set<Acesso> mAcessos) {
        this.mAcessos = mAcessos;
    }
    
    public void addAcesso(Acesso acesso) {
        if (mAcessos == null) {
            mAcessos = new HashSet<>();
        }
        mAcessos.add(acesso);
    }
    
    /**
     * @return the mSite
     */
    public String getSite() {
        return mSite;
    }

    /**
     * @param site the mSite to set
     */
    public void setSite(String site) {
        mSite = site;
    }

    public Programacao getProgramacao() {
        return mProgramacao;
    }

    public void setProgramacao(Programacao programacao) {
        mProgramacao = programacao;
    }

    public String getUrlServidor1() {
        return mUrlServidor1;
    }

    public void setUrlServidor1(String urlServidor1) {
        mUrlServidor1 = urlServidor1;
    }

    public String getUrlServidor2() {
        return mUrlServidor2;
    }

    public void setUrlServidor2(String urlServidor2) {
        mUrlServidor2 = urlServidor2;
    }

    public String getUrlServidor3() {
        return mUrlServidor3;
    }

    public void setUrlServidor3(String urlServidor3) {
        mUrlServidor3 = urlServidor3;
    }

    public Boolean isDesativada() {
        return mDesativada;
    }

    public void setDesativada(Boolean desativada) {
        mDesativada = desativada;
    }

    public Integer getQuantidadeProblemasReportados() {
        return mQuantidadeProblemasReportados;
    }

    public void setQuantidadeProblemasReportados(Integer quantidadeProblemasReportados) {
        mQuantidadeProblemasReportados = quantidadeProblemasReportados;
    }

    public Date getDataUltimoProblemaReportado() {
        return mDataUltimoProblemaReportado;
    }

    public void setDataUltimoProblemaReportado(Date dataUltimoProblemaReportado) {
        mDataUltimoProblemaReportado = dataUltimoProblemaReportado;
    }
    
    public Date getDataUltimaAtualizacao() {
        return mDataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(Date dataUltimaAtualizacao) {
        mDataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    public Float getAvaliacao() {
        return mAvaliacao;
    }

    public void setAvaliacao(Float avaliacao) {
        mAvaliacao = avaliacao;
    }
    
    public Integer getQtdAvaliacoes() {
        return mQtdAvaliacoes;
    }

    public void setQtdAvaliacoes(Integer qtdAvaliacoes) {
        mQtdAvaliacoes = qtdAvaliacoes;
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
    
    public void reportarProblema() {
        mQuantidadeProblemasReportados += 1;
        mDataUltimoProblemaReportado = new Date();
    }

    public void avaliar(Float notaAvaliacao) {
        if (mQtdAvaliacoes >= 0) {
            mAvaliacao = ((mAvaliacao * mQtdAvaliacoes) + notaAvaliacao) / mQtdAvaliacoes + 1;
            mQtdAvaliacoes += 1;
        }
        else
            throw new IllegalStateException();
    }
    
    public Integer positivar() {
        return ++mQtdAvaliacoes;
    }

    public Integer negativar() {
        return mQtdAvaliacoes > 0 ? --mQtdAvaliacoes : 0;
    }
    
    /**
     * This is the default implementation of readObject. Customize as necessary.
     */
    private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException {
        // perform the default de-serialization
        aInputStream.defaultReadObject();
        
        try {
            byte[] buf = new byte[]{};
            aInputStream.readFully(buf);
            mLogo = ByteArrays.decompress(buf);
        } catch (DataFormatException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
 
    /**
     * This is the default implementation of writeObject. Customize as necessary.
     */
    private void writeObject(ObjectOutputStream aOutputStream) throws IOException {
        // perform the default serialization for all non-transient, non-static fields
        aOutputStream.defaultWriteObject();
        
        aOutputStream.write(ByteArrays.compress(mLogo));        
    }
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "mLogo", "mGeneros", "mAcessos");
    }
}
