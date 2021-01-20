/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.dao;

import com.bigantena.bean.AbstractBean;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static java.util.Map.*;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author aspferraz
 */

@Repository(value = "basicDAO")
public class BasicDAOImpl implements BasicDAO, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext(type = javax.persistence.PersistenceContextType.TRANSACTION)
    protected EntityManager entityManager;
    
    private static final Logger logger = Logger.getLogger(BasicDAOImpl.class);
    
    /**
     * OPÇÃO 001: findByNamedQuery com parâmetros
     *
     * @param namedQuery
     * @param namedParams
     * @return 
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
    public List<? extends AbstractBean> findByNamedQuery(String namedQuery,
            Map<String, Object> namedParams) {
        try {
            logger.info("Buscando com a namedQuery "
                    + namedQuery + " com "
                    + namedParams != null ? namedParams.size() : "0" + " parametros");
            Query query = entityManager.createNamedQuery(namedQuery);
            if (namedParams != null) {
                Entry<String, Object> mapEntry;
                for (Iterator it = namedParams.entrySet().iterator(); 
                        it.hasNext(); 
                        query.setParameter((String) mapEntry.getKey(), mapEntry.getValue())) {
                    mapEntry = (Entry<String, Object>) it.next();
                    logger.info("Parâmetro: " + mapEntry.getKey() + ", Valor: "
                            + mapEntry.getValue());
                }

            }
            List<? extends AbstractBean> returnList
                    = (List<? extends AbstractBean>) query.getResultList();
            logger.info("Objetos encontrados: " + returnList.size());

            return returnList;
            
        } catch (Exception e) {
            logger.error("Ocorreu um erro ao executar o findByNamedQuery com parâmetros. MSG ORIGINAL: "
                    + e.getMessage(), e);
            throw new DAOException("Ocorreu um erro ao executar o findByNamedQuery com parâmetros");
        }
    }

    /**
     * OPÇÃO 002: findByNamedQuery com parâmetros e limitando a quantidade
     * máxima de resultados que deve ser retornado
     *
     * @param namedQuery
     * @param namedParams
     * @param maxResults
     * @return 
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
    public List<? extends AbstractBean> findByNamedQuery(String namedQuery,
            Map<String, Object> namedParams, int maxResults) {
        try {
            logger.info("Buscando com a namedQuery "
                    + namedQuery + " com "
                    + namedParams != null ? namedParams.size() : "0" + " parametros");
            Query query = entityManager.createNamedQuery(namedQuery);
            query.setMaxResults(maxResults);
            if (namedParams != null) {
                Entry<String, Object> mapEntry;
                for (Iterator it = namedParams.entrySet().iterator(); 
                        it.hasNext(); 
                        query.setParameter((String) mapEntry.getKey(), mapEntry.getValue())) {

                    mapEntry = (Entry<String, Object>) it.next();
                    logger.info("Parâmetro: " + mapEntry.getKey() + ", Valor: "
                            + mapEntry.getValue());
                }
            }
            List<? extends AbstractBean> returnList
                    = (List<? extends AbstractBean>) query.getResultList();
            logger.info("Objetos encontrados: " + returnList.size());

            return returnList;
            
        } catch (Exception e) {
            logger.error("Ocorreu um erro ao executar o findByNamedQuery com parâmetros. MSG ORIGINAL: "
                    + e.getMessage(), e);
            throw new DAOException("Ocorreu um erro ao executar o findByNamedQuery com parâmetros");
        }
    }

    /**
     * OPÇÃO 003: findByNamedQuery sem parâmetros e nem limitação de quantidade,
     * o mais simples de todos
     *
     * @param namedQuery
     * @return 
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
    public List<? extends AbstractBean> findByNamedQuery(String namedQuery) {
        try {
            logger.info((new StringBuilder("Buscando com a namedQuery "))
                    .append(namedQuery)
                    .append(" sem nenhum parametro")
                    .toString());
            Query query = entityManager.createNamedQuery(namedQuery);
            List<? extends AbstractBean> returnList
                    = (List<? extends AbstractBean>) query.getResultList();

            logger.info("Objetos encontrados: " + returnList.size());
            return returnList;
        } catch (Exception e) {
            logger.error("Ocorreu um erro ao executar o findByNamedQuery sem parâmetros. MSG ORIGINAL: "
                    + e.getMessage(), e);
            throw new DAOException("Ocorreu um erro ao executar o findByNamedQuery sem parâmetros");
        }
    }

    /**
     * OPÇÃO 004: findByNamedQuery sem parâmetros, mas com limitação de
     * quantidade de resultados
     *
     * @param namedQuery
     * @param maxResults
     * @return 
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
    public List<? extends AbstractBean> findByNamedQuery(String namedQuery, int maxResults) {
        try {
            logger.info((new StringBuilder("Buscando com a namedQuery "))
                    .append(namedQuery).append(" sem nenhum parametro")
                    .toString());
            Query query = entityManager.createNamedQuery(namedQuery);
            query.setMaxResults(maxResults);
            List<? extends AbstractBean> returnList
                    = (List<? extends AbstractBean>) query.getResultList();
            logger.info("Objetos encontrados: " + returnList.size());
            
            return returnList;
            
        } catch (Exception e) {
            logger.error("Ocorreu um erro ao executar o findByNamedQuery sem parâmetros. MSG ORIGINAL: "
                    + e.getMessage(), e);
            throw new DAOException("Ocorreu um erro ao executar o findByNamedQuery sem parâmetros");
        }
    }
    
     /**
     * OPÇÃO 005: find objeto pela classe
     *
     * @param clazz
     * @param id
     * @return 
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
    public AbstractBean getOne(Class clazz, Integer id) {
        try {
            logger.info("Buscando bean " + clazz.getName() + " com id " + id);
            Query query = entityManager
                    .createQuery("SELECT c FROM " + clazz.getName() + " c WHERE c.mId = :id");
            query.setParameter("id", id);
            List<? extends AbstractBean> returnList 
                    = (List<? extends AbstractBean>) query.getResultList();
            if (!returnList.isEmpty() && returnList.size() == 1) {
                logger.info("Objetos encontrados: 1");
                return returnList.get(0);
            } else {
                logger.info("Objetos encontrados: 0");
                return null;
            }
            
        } catch (Exception e) {
            logger.error("Ocorreu um erro ao executar o findById. MSG ORIGINAL: "
                    + e.getMessage(), e);
            throw new DAOException("Ocorreu um erro ao executar o findById");
        }
    }
    
    
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
    public List<? extends AbstractBean> getAll(Class clazz) {
        try {
            logger.info("Buscando todos de " + clazz.getName());
            Query query = entityManager
                    .createQuery("SELECT c FROM " + clazz.getName() + " c ORDER BY c.mId ASC ");
           
            List<? extends AbstractBean> returnList 
                    = (List<? extends AbstractBean>) query.getResultList();
            
            logger.info("Objetos encontrados: " + returnList.size());

            return returnList;
            
        } catch (Exception e) {
            logger.error("Ocorreu um erro ao executar o getAll. MSG ORIGINAL: "
                    + e.getMessage(), e);
            throw new DAOException("Ocorreu um erro ao executar o getAll");
        }
    }
    
    
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
    public List<? extends AbstractBean> findByQuery(String hql, Map namedParams) {
        try {
            logger.info((new StringBuilder("Buscando com a query: "))
                    .append(hql).toString());
            Query query = entityManager.createQuery(hql);
            if (namedParams != null) {
                Entry mapEntry;
                for (Iterator it = namedParams.entrySet().iterator(); 
                        it.hasNext(); 
                        query.setParameter((String) mapEntry.getKey(), mapEntry.getValue())) {
                    mapEntry = (Entry) it.next();
                    logger.info("Parâmetro: " + mapEntry.getKey() + ", Valor: "
                            + mapEntry.getValue());
                }
            }
            List<? extends AbstractBean> returnList
                    = (List<? extends AbstractBean>) query.getResultList();
            logger.info("Objetos encontrados: " + returnList.size());

            return returnList;
            
        } catch (Exception e) {
            logger.error(
            "Ocorreu um erro ao executar o findByQuery. MSG ORIGINAL: "+ e.getMessage(), e);
            throw new DAOException("Ocorreu um erro ao executar o findByQuery");
        }
    }
   
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
    public List<? extends AbstractBean> findByQuery(String hql, Map namedParams, int maxResults) {
        try {
            logger.info((new StringBuilder("Buscando com a query: "))
                    .append(hql).toString());
            Query query = entityManager.createQuery(hql);
            query.setMaxResults(maxResults);
            if (namedParams != null) {
                Entry mapEntry;
                for (Iterator it = namedParams.entrySet().iterator(); 
                        it.hasNext(); 
                        query.setParameter((String) mapEntry.getKey(), mapEntry.getValue())) {
                    mapEntry = (Entry) it.next();
                    logger.info("Parâmetro: " + mapEntry.getKey() + ", Valor: "
                            + mapEntry.getValue());
                }
            }
            List<? extends AbstractBean> returnList
                    = (List<? extends AbstractBean>) query.getResultList();
            logger.info("Objetos Encontrados: " + returnList.size());

            return returnList;
            
        } catch (Exception e) {
            logger.error(
            "Ocorreu um erro ao executar o findByQuery. MSG ORIGINAL: "
                      + e.getMessage(), e);
            throw new DAOException("Ocorreu um erro ao executar o findByQuery");
        }
    }
    
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
    public List<? extends AbstractBean> findByQuery(String hql, Map namedParams, int page, int limitPerPage) {
        try {
            logger.info((new StringBuilder("Buscando com a query: "))
                    .append(hql).toString());
            Query query = entityManager.createQuery(hql)
                .setFirstResult(calculateOffset(page, limitPerPage))
                .setMaxResults(limitPerPage);
            if (namedParams != null) {
                Entry mapEntry;
                for (Iterator it = namedParams.entrySet().iterator(); 
                        it.hasNext(); 
                        query.setParameter((String) mapEntry.getKey(), mapEntry.getValue())) {
                    mapEntry = (Entry) it.next();
                    logger.info("Parâmetro: " + mapEntry.getKey() + ", Valor: "
                            + mapEntry.getValue());
                }
            }
            List<? extends AbstractBean> returnList
                    = (List<? extends AbstractBean>) query.getResultList();
            logger.info("Objetos Encontrados: " + returnList.size());

            return returnList;
            
        } catch (Exception e) {
            logger.error(
            "Ocorreu um erro ao executar o findByQuery. MSG ORIGINAL: "
                      + e.getMessage(), e);
            throw new DAOException("Ocorreu um erro ao executar o findByQuery");
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
    public List<?> executeHQLQuery(String hql, Map namedParams) {
        try {
            logger.info((new StringBuilder("Buscando com a query: "))
                    .append(hql).toString());
            Query query = entityManager.createQuery(hql);
            if (namedParams != null) {
                Entry mapEntry;
                for (Iterator it = namedParams.entrySet().iterator(); 
                        it.hasNext(); 
                        query.setParameter((String) mapEntry.getKey(), mapEntry.getValue())) {
                    mapEntry = (Entry) it.next();
                    logger.info("Parâmetro: " + mapEntry.getKey() + ", Valor: "
                            + mapEntry.getValue());
                }
            }
            List<?> returnList
                    = (List<?>) query.getResultList();
            logger.info("Objetos Encontrados: " + returnList.size());

            return returnList;
            
        } catch (Exception e) {
            logger.error(
            "Ocorreu um erro ao executar o executeHQLQuery. MSG ORIGINAL: "
                      + e.getMessage(), e);
            throw new DAOException("Ocorreu um erro ao executar o executeHQLQuery");
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
    public List<?> executeSQLQuery(String sqlQuery, Map namedParams) {
        try {
            Query query = entityManager.createNativeQuery(sqlQuery);
            if (namedParams != null) {
                Entry mapEntry;
                for (Iterator it = namedParams.entrySet().iterator();
                        it.hasNext();
                        query.setParameter((String) mapEntry.getKey(), mapEntry.getValue())) {
                    mapEntry = (Entry) it.next();
                    logger.info("Parâmetro: " + mapEntry.getKey() + ", Valor: "
                            + mapEntry.getValue());
                }
            }
            List<?> returnList
                    = (List<?>) query.getResultList();
            logger.info("Objetos Encontrados: " + returnList.size());

            return returnList;
        } catch (Exception e) {
            logger.error(
                    "Ocorreu um erro ao executar o executeSQLQuery. MSG ORIGINAL: "
                    + e.getMessage(), e);
            throw new DAOException("Ocorreu um erro ao executar o executeSQLQuery");
        }
    }
    
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
    public void executeSQLUpdate(String sqlQuery, Map namedParams) {
        try {
            Query query = entityManager.createNativeQuery(sqlQuery);
            if (namedParams != null) {
                Entry mapEntry;
                for (Iterator it = namedParams.entrySet().iterator();
                        it.hasNext();
                        query.setParameter((String) mapEntry.getKey(), mapEntry.getValue())) {
                    mapEntry = (Entry) it.next();
                    logger.info("Parâmetro: " + mapEntry.getKey() + ", Valor: "
                            + mapEntry.getValue());
                }
            }
            int entitiesAffected = query.executeUpdate();
            logger.info("Atualização efetuada com sucesso. ("+entitiesAffected+")");
            
        } catch (Exception e) {
            logger.error(
                    "Ocorreu um erro ao executar o executeSQLUpdate. MSG ORIGINAL: "
                    + e.getMessage(), e);
            throw new DAOException("Ocorreu um erro ao executar o executeSQLUpdate");
        }
    }
    
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
    public Criteria getCriteria(Class clazz) {
        try {
            Session session = entityManager.unwrap(Session.class);
            return session.createCriteria(clazz);
        } catch (Exception e) {
            logger.error(
                    "Ocorreu um erro ao executar o getCriteria. MSG ORIGINAL: "
                    + e.getMessage(), e);
            throw new DAOException("Ocorreu um erro ao executar o getCriteria");
        }
    }
    
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
    public Integer getMaxId(Class clazz) {
        try {
            logger.info("Obtendo ID máximo do Bean " + clazz.getName());
            Query query = entityManager
                    .createQuery("SELECT max(c.mId) FROM " + clazz.getName() + " c");
            
            Integer id = (Integer) query.getSingleResult();
            if (id != null) {
                logger.info("Objeto encontrado: "+id);
                return id;
            } else {
                logger.info("Nenhum objeto encontrado");
                return null;
            }
        } catch (Exception e) {
            logger.error("Ocorreu um erro ao executar o getMaxId. MSG ORIGINAL: "
                    + e.getMessage(), e);
            throw new DAOException("Ocorreu um erro ao executar o getMaxId");
        }
    }
    
//    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
//    @Override
//    public Integer getNextSequenceValue(String sequenceName) {
//        try {
//            logger.info("Obtendo próximo valor da SEQUENCE '" + sequenceName + "'");
//            Query query = entityManager.createNativeQuery("select nextval('" + sequenceName + "')");
//            try {
//                Integer value = ((BigInteger) query.getSingleResult()).intValue();
//                logger.info("Objeto encontrado: " + value);
//                return value;
//            } catch (NoResultException ex) {
//                logger.error("Nenhum objeto encontrado", ex);
//                return null;
//            }
//        } catch (Exception e) {
//            logger.error("Ocorreu um erro ao executar o getNextSequenceValue. MSG ORIGINAL: "
//                    + e.getMessage(), e);
//            throw new DAOException("Ocorreu um erro ao executar o getNextSequenceValue");
//        }
//    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public AbstractBean save(AbstractBean bean) {
        try {
            logger.info("Salvando Bean " + bean.getClass().getName());
            entityManager.persist(bean);
            return bean;
        } catch (Exception e) {
            logger.error("Ocorreu um erro ao tentar salvar. MSG ORIGINAL: "
                    + e.getMessage(), e);
            throw new DAOException("Ocorreu um erro ao tentar salvar");
        }
    }
   
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public AbstractBean saveAndFlush(AbstractBean bean) {
        try {
            logger.info("Salvando Bean " + bean.getClass().getName());
            entityManager.persist(bean);
            entityManager.flush();
            entityManager.clear();
            return bean;
        } catch (Exception e) {
            logger.error("Ocorreu um erro ao tentar salvar. MSG ORIGINAL: "
                    + e.getMessage(), e);
            throw new DAOException("Ocorreu um erro ao tentar salvar");
        }
    }
   
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public AbstractBean update(AbstractBean bean) {
        try {
            logger.info("Alterando Bean " + bean.getClass().getName());
            return entityManager.merge(bean);
        } catch (Exception e) {
            logger.error("Ocorreu um erro ao tentar atualizar. MSG ORIGINAL: "
                    + e.getMessage(), e);
            throw new DAOException("Ocorreu um erro ao tentar atualizar");
        }
    }
   
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void delete(AbstractBean bean) {
        try {
            logger.info("Deletando Bean " + bean.getClass().getName());
            entityManager.remove(bean);
        } catch (Exception e) {
            logger.error("Ocorreu um erro ao tentar deletar. MSG ORIGINAL: "
                    + e.getMessage(), e);
            throw new DAOException("Ocorreu um erro ao tentar deletar");
        }
    }

    @Override
    public void clear() {
        entityManager.clear();
    }

    @Override
    public void flush() {
        entityManager.flush();
    }
    
    private int calculateOffset(int page, int limit) {
        return ((page - 1) * limit);
    }
}
