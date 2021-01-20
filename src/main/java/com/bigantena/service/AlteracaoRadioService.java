/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.service;

import com.bigantena.bean.AbstractBean;
import com.bigantena.dao.BasicDAO;
import com.bigantena.model.AlteracaoRadio;
import com.bigantena.model.Radio;
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

@Service("alteracaoRadioService")
public class AlteracaoRadioService {
    
    private static final Logger logger = Logger.getLogger(AlteracaoRadioService.class);
    
    @Autowired
    private BasicDAO basicDAO;
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void salvar(AlteracaoRadio alteracaoRadio) {
        String sql
                = "insert into alteracao_radio (id, id_radio, nome_radio, logo_radio, site_radio, url_servidor_radio, dt_alteracao_radio, nome_arquivo_logo_radio)"
                + " values((select nextval('alteracao_radio_id_seq')), :idRadio, null, null, null, :url, :dataAlteracao, null)";

        Map<String, Object> params = new HashMap<>();
        params.put("idRadio", alteracaoRadio.getRadio().getId());
        params.put("url", alteracaoRadio.getUrlServidorRadio());
        params.put("dataAlteracao", alteracaoRadio.getDataAlteracaoRadio());

        basicDAO.executeSQLUpdate(sql, params);
    }
    
    public AlteracaoRadio buscarPorId(Integer id) {
        AlteracaoRadio ar = (AlteracaoRadio)basicDAO.getOne(AlteracaoRadio.class, id);
        return ar;
    }
    
    public List<AlteracaoRadio> buscarPorIds(List<Integer> ids) {
        String hql = "SELECT ar FROM " + AlteracaoRadio.class.getName() + " ar "
                + "WHERE ar.mId in (:ids) ";
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params);
        if (!result.isEmpty()){  
            return (List<AlteracaoRadio>)result;
        }
        else {
            return null;
        }
    }
    
    public List<AlteracaoRadio> obterAlteracoesPorRadio(Radio r) {
        String hql = "SELECT ar FROM " + AlteracaoRadio.class.getName() + " ar " 
                + "INNER JOIN ar.mRadio as radio "
                + "WHERE radio.id = :idRadio "
                + "ORDER BY ar.mDataAlteracaoRadio desc";
        Map<String, Object> params = new HashMap<>();
        params.put("idRadio", r.getId());
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params);
        
        if (!result.isEmpty()){  
            return (List<AlteracaoRadio>)result;
        }
        else {
            return null;
        }
    }
    
//    public Integer obterProximoValorDaSequencia() {
//        return basicDAO.getNextSequenceValue("alteracao_radio_id_seq");
//    }
    
}
