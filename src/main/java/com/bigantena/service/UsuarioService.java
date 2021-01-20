/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.service;

import com.bigantena.bean.AbstractBean;
import com.bigantena.dao.BasicDAO;
import com.bigantena.model.Usuario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author aspferraz
 */

@Service("usuarioService")
public class UsuarioService {
    
    @Autowired
    private BasicDAO basicDAO;
    
    public Usuario obterPorLogin(String login) {
        String hql = "SELECT u FROM " + Usuario.class.getName() + " u WHERE u.mLogin = :login";
        Map<String, Object> params = new HashMap<>();
        params.put("login", login);
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 1);
        if (!result.isEmpty()){  
            return (Usuario)result.get(0);
        }
        else {
            return null;
        }
    }
}
