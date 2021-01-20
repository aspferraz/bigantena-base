/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.service;

import com.bigantena.dao.BasicDAO;
import com.bigantena.model.Usuario;
import com.bigantena.model.UsuarioRegra;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author aspferraz
 */
@Service("usuarioRegraService")
public class UsuarioRegraService {
    
    @Autowired
    private BasicDAO basicDAO;
    
    private static final Logger logger = Logger.getLogger(UsuarioRegraService.class);
    
    public List<String> obterRegrasPorUsuario(Usuario usuario) {
        //Trata valores de parâmetros nulos
        if (usuario == null || StringUtils.isBlank(usuario.getLogin())) {
            logger.error("param 'usuario' is null or invalid", new IllegalArgumentException());
            return null;
        }
        String hql = "SELECT ur.mRegra FROM " + UsuarioRegra.class.getName() + " ur "
                + "inner join ur.mUsuario as usuario "
                + "WHERE usuario.mLogin = :login "
                + "ORDER BY ur.mRegra asc";
        
        Map<String, Object> params = new HashMap<>();
        params.put("login", usuario.getLogin());
        List<String> result = (List<String>)basicDAO.executeHQLQuery(hql, params);
        return result;
    }
    
}
