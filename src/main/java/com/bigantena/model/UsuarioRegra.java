/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.model;

import com.bigantena.bean.AbstractBean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author aspferraz
 */

@Entity
@Table(name="usuario_regra")
public class UsuarioRegra extends AbstractBean {
    
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario mUsuario;
    
    @Column(name = "regra", length = 45)
    private String mRegra;

    
    public UsuarioRegra() {
    }
    
    public Usuario getUsuario() {
        return mUsuario;
    }

    public void setUsuario(Usuario usuario) {
        mUsuario = usuario;
    }

    public String getRegra() {
        return mRegra;
    }

    public void setRegra(String regra) {
        mRegra = regra;
    }
}
