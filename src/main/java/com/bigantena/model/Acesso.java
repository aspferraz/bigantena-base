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
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author aspferraz
 */

@Entity
@Table(name = "acesso")
public class Acesso extends AbstractBean {
    
    @JsonView(View.Minima.class)
    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_radio")
    private Radio mRadio;
    
    @JsonView(View.Basica.class)
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario mUsuario;
    
    @JsonView(View.Minima.class)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JsonProperty("dataAcesso")
    @Column(name = "dt_acesso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mDataAcesso;
    
    public Acesso() {
    }
    
    public Acesso(Integer id) {
        mId = id;
    }
    
    public Acesso(Integer id, Radio radio, Date dataAcesso) {
        this(id, null, radio, dataAcesso);
    }
    
    public Acesso(Integer id, Usuario usuario, Radio radio, Date dataAcesso) {
        super.mId = id;
        mUsuario = usuario;
        mRadio = radio;
        mDataAcesso = dataAcesso;
    }
    
    public Usuario getUsuario() {
        return mUsuario;
    }

    public void setUsuario(Usuario usuario) {
        mUsuario = usuario;
    }

    public Radio getRadio() {
        return mRadio;
    }

    public void setRadio(Radio radio) {
        mRadio = radio;
    }
    
    public Date getDataAcesso() {
        return mDataAcesso;
    }

    public void setDataAcesso(Date dataAcesso) {
        mDataAcesso = dataAcesso;
    }

    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.DEFAULT_STYLE);
    }
    
}
