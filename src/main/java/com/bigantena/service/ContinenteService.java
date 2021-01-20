/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.service;

import com.bigantena.bean.AbstractBean;
import com.bigantena.dao.BasicDAO;
import com.bigantena.model.Continente;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author aspferraz
 */

@Service("continenteService")
public class ContinenteService {

    private static final Logger logger = Logger.getLogger(ContinenteService.class);

    @Autowired
    private BasicDAO basicDAO;
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Continente salvar(Continente continente) {
        return (Continente)basicDAO.saveAndFlush(continente);
    }
    
    public Continente buscarPorId(Integer id) {
        Continente c = (Continente)basicDAO.getOne(Continente.class, id);
        return c;
    }
    
    public List<Continente> buscarTodos() {
        List<? extends AbstractBean> result = basicDAO.getAll(Continente.class);
        if (!result.isEmpty()){  
            return (List<Continente>)result;
        }
        else {
            return null;
        }
    }
    
    public Continente obterPorNome(String nome) {
        String hql = "SELECT c FROM " + Continente.class.getName() + " c WHERE c.mNome = :nome";
        Map<String, Object> params = new HashMap<>();
        params.put("nome", nome);
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 1);
        if (!result.isEmpty()){  
            return (Continente)result.get(0);
        }
        else {
            return null;
        }
    }
    
    public Continente obterPorNomeEn(String nomeEn) {
        String hql = "SELECT c FROM " + Continente.class.getName() + " c WHERE c.mNomeEn = :nomeEn";
        Map<String, Object> params = new HashMap<>();
        params.put("nomeEn", nomeEn);
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 1);
        if (!result.isEmpty()){  
            return (Continente)result.get(0);
        }
        else {
            return null;
        }
    }
    
    public Continente obterPorSigla(String sigla) {
        String hql = "SELECT c FROM " + Continente.class.getName() + " c WHERE c.mSigla = :sigla";
        Map<String, Object> params = new HashMap<>();
        params.put("sigla", sigla);
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 1);
        if (!result.isEmpty()){  
            return (Continente)result.get(0);
        }
        else {
            return null;
        }
    }

}
