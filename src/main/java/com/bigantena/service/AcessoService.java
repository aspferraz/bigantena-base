/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.service;

import com.bigantena.bean.AbstractBean;
import com.bigantena.dao.BasicDAO;
import com.bigantena.model.Acesso;
import com.bigantena.model.Radio;
import java.util.Date;
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

@Service("acessoService")
public class AcessoService {
    
    private static final Logger logger = Logger.getLogger(AcessoService.class);
    
    @Autowired
    private BasicDAO basicDAO;
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void salvar(Acesso acesso) {
//        acesso.setId(obterProximoValorDaSequencia());
        String sql = "insert into acesso(id, id_radio, dt_acesso) values ((select nextval('acesso_id_seq')), :idRadio, :data)";
       
        Map<String, Object> params = new HashMap<>();
        params.put("idRadio", acesso.getRadio().getId());
        params.put("data", acesso.getDataAcesso());
        
        basicDAO.executeSQLUpdate(sql, params);
    }
    
    public Acesso buscarPorId(Integer id) {
        Acesso a = (Acesso)basicDAO.getOne(Acesso.class, id);
        return a;
    } 
    
    public List<Acesso> buscarPorIds(List<Integer> ids) {
        String hql = "SELECT a FROM " + Acesso.class.getName() + " a "
                + "WHERE a.mId in (:ids) ";
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params);
        if (!result.isEmpty()){  
            return (List<Acesso>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Acesso> obterAcessosPorRadio(Radio r) {
        String hql = "SELECT a FROM " + Acesso.class.getName() + " a " 
                + "INNER JOIN a.mRadio as radio "
                + "WHERE radio.id = :idRadio "
                + "ORDER BY a.mDataAcesso desc";
        Map<String, Object> params = new HashMap<>();
        params.put("idRadio", r.getId());
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params);
        
        if (!result.isEmpty()){  
            return (List<Acesso>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Acesso> buscarAcessosPorRadioComDataMaiorQue(Radio r, Date dt) {
        //Trata valores de parâmetros nulos
        if (r == null) {
            logger.error("param 'r' is null", new IllegalArgumentException());
            return null;
        }
        if (dt == null) {
            logger.error("param 'dt' is null", new IllegalArgumentException());
            return null;
        }
        
        String hql = "SELECT a FROM " + Acesso.class.getName() + " a "
                + "INNER JOIN a.mRadio as radio "
                + "WHERE radio.id = :idRadio "
                + "AND a.mDataAcesso > :dataAcesso "
                + "ORDER BY a.mDataAcesso desc";
        
        Map<String, Object> params = new HashMap<>();
        params.put("idRadio", r.getId());
        params.put("dataAcesso", dt);
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params);
        if (!result.isEmpty()){  
            return (List<Acesso>)result;
        }
        else {
            return null;
        }
    }
    
//    public Integer obterProximoValorDaSequencia() {
//        return basicDAO.getNextSequenceValue("acesso_id_seq");
//    }
    
}
