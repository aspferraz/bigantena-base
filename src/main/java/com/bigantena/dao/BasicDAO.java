/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.dao;

import com.bigantena.bean.AbstractBean;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;

/**
 *
 * @author aspferraz
 */
public interface BasicDAO {

    public abstract List<? extends AbstractBean>
            findByNamedQuery(String queryName, Map<String, Object> map);

    public abstract List<? extends AbstractBean>
            findByNamedQuery(String queryName);

    public abstract List<? extends AbstractBean>
            findByNamedQuery(String queryName, Map<String, Object> params, int maxResults);

    public abstract List<? extends AbstractBean>
            findByNamedQuery(String queryName, int maxResults);
            
    public abstract AbstractBean getOne(Class clazz, Integer id);     
    
    public abstract List<? extends AbstractBean> getAll(Class clazz);
    
    public abstract Integer getMaxId(Class clazz);
    
//    public abstract Integer getNextSequenceValue(String sequenceName);

    public abstract  List<? extends AbstractBean>
            findByQuery(String hqlQuery, Map<String, Object> params);

    public abstract List<? extends AbstractBean>
            findByQuery(String hqlQuery, Map<String, Object> params, int maxResults);
            
            public abstract List<? extends AbstractBean>
            findByQuery(String hqlQuery, Map<String, Object> params, int page, int limitPerPage);
            
    public abstract List<?> executeHQLQuery (String hqlQuery, Map<String, Object> params);
    
    public abstract List<?> executeSQLQuery (String sqlQuery, Map<String, Object> params);
    
    public abstract void executeSQLUpdate (String sqlQuery, Map<String, Object> params);
    
    public abstract Criteria getCriteria(Class clazz);
        
    public abstract AbstractBean save(AbstractBean abstractbean);

    public abstract AbstractBean saveAndFlush(AbstractBean abstractbean);

    public abstract AbstractBean update(AbstractBean abstractbean);

    public abstract void delete(AbstractBean abstractbean);

    public abstract void clear();

    public abstract void flush();

}
