/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.model;

import com.bigantena.bean.AbstractBean;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author aspferraz
 */
@Entity
@Table(name = "usuario")
public class Usuario extends AbstractBean {
    
    @Column(name = "nome", length = 255)
    private String mNome;
    
    @NotNull
    @Column(name = "login", length = 45)
    private String mLogin;
    
    @Column(name = "email", length = 255)
    private String mEmail;
    
    @NotNull
    @Column(name = "senha", length = 60)
    private String mSenha;
    
    @NotNull
    @Column(name = "habilitado")
    private Boolean mHabilitado;
    
    @Transient
    @Size(min = 6, max = 15)
    @Pattern(regexp = "\\S+", message = "Spaces are not allowed")
    private String mSenhaDescriptografada;
    
    @Transient
    private Set<Radio> mRadiosFavoritas;
    
    @Transient
    private Date mDataRegistro;
    
    public Usuario() {
    }
    
    public Usuario(String nome) {
        mNome = nome;
    }
    
    public Usuario(Usuario u) {
        super.mId = u.getId();
        mNome = u.getNome();
        mEmail = u.getEmail();
        mSenha = u.getSenha();
        mHabilitado = u.isHabilitado();
    }

    public String getNome() {
        return mNome;
    }

    public void setNome(String nome) {
        mNome = nome;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        mLogin = login;
    }
    
    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getSenha() {
        return mSenha;
    }

    public void setSenha(String senha) {
        mSenha = senha;
    }

    public Boolean isHabilitado() {
        return mHabilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        mHabilitado = habilitado;
    }
}
