/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.service;


import com.bigantena.bean.AbstractBean;
import com.bigantena.dao.BasicDAO;
import com.bigantena.model.Continente;
import com.bigantena.model.Pais;
import com.bigantena.util.Strings;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author aspferraz
 */
@Service("paisService")
public class PaisService {
    
    private static final Logger logger = Logger.getLogger(PaisService.class);
    
    @Autowired
    private BasicDAO basicDAO;
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    synchronized public Pais salvar(Pais pais) {
        return (Pais)basicDAO.saveAndFlush(pais);
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Pais atualizar(Pais p) {
        p = (Pais) basicDAO.update(p);
        basicDAO.flush();
        basicDAO.clear();
        return p;
    }
    
    synchronized public Pais obterPaisPorPaginaHTML(String dominio, String url) {
        Pais pais = null;
        try {
            String[] strArr = url.split("/");
            Integer idPais = Integer.valueOf(strArr[strArr.length - 1]);
            pais = new Pais(idPais);
            pais.setUrl(StringUtils.substringAfter(url, dominio));
            
            Document doc = Jsoup.connect(url).timeout(10*1000).get();
            Elements elements = doc.getElementsByAttributeValue("class", "page-header");
            if (elements.isEmpty()) {
                throw new ParseException("Elemento <h2 class='page-header'> não localizado>", 0);
            }
            Element e = elements.get(0);
            String nome = e.getElementsByTag("small").text();
            pais.setNome(nome);
            
            
        } catch(IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
        } catch(ParseException pe) {
            logger.error(pe.getMessage(), pe);
        }
        return pais;
    }
    
    public Pais buscarPorId(Integer id) {
        Pais p = (Pais)basicDAO.getOne(Pais.class, id);
        return p;
    } 
    
    public List<Pais> buscarTodos() {
        String hql = "SELECT p FROM " + Pais.class.getName() + " p "
                + "ORDER BY p.mNome ASC ";
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, null);
        if (!result.isEmpty()){  
            return (List<Pais>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Pais> buscarPorGeneros(List<Integer> idsGeneros) {
       String sql = 
       "select * from (" 
	+"      select p.id, p.nome, count(r.id) from radio r"
	+"          inner join radio_genero rg on rg.id_radio = r.id"
	+"          inner join cidade c on c.id = r.id_cidade"
	+"          left outer join estado e on e.id = c.id_estado"
	+"          left outer join pais p on p.id = e.id_pais"
	+"      where" 
	+"          r.desativada is false and rg.id_genero in (:idsQ1)"
	+"          and (p.id in (select id from pais))"
	+"	group by p.id"
	+"	order by p.id) as q1"

    +" union "

        +"select * from ("
        +"      select p.id, p.nome, count(r.id) from radio r"
        +"          inner join radio_genero rg on rg.id_radio = r.id"
        +"          inner join cidade c on c.id = r.id_cidade"
        +"          left outer join estado e on e.id = c.id_estado"
        +"          left outer join pais p on p.id = c.id_pais"
        +"      where" 
        +"          r.desativada is false and rg.id_genero in (:idsQ2)"
        +"          and (p.id in (select id from pais))"
        +"      group by p.id"
        +"      order by p.id) as q2"
        +"      order by 1, 3 desc";
        
       Map<String, Object> params = new HashMap<>();
       params.put("idsQ1", idsGeneros);
       params.put("idsQ2", idsGeneros);
       
       List<?> sqlResult = basicDAO.executeSQLQuery(sql, params);
        if (!sqlResult.isEmpty()) {
            List<Pais> result = buscarTodos();
            basicDAO.clear();
            if (result != null) {
                for (Iterator<Pais> iter = result.iterator(); iter.hasNext();) {
                    Pais p = iter.next();
                    boolean contains = false;
                    for (Object[] row : (List<Object[]>)sqlResult) {
                        if (p.getId() == ((Number)row[0]).intValue()) {
                            contains = true;
                            p.setQtdRadios(((Number)row[2]).intValue());
                            break;
                        }
                    }
                    if (!contains) {
                        iter.remove();
                    }
                }
                if (!result.isEmpty())
                    return result;
            }
            return null;
        }
        else {
            return null;
        }
    }
    
    public List<Pais> buscarPorContinente(Continente continente) {
        String hql = "SELECT p FROM " + Pais.class.getName() + " p "
                + "WHERE p.mContinente.mId = :idContinente "
                + "ORDER BY p.mNome ASC ";
        Map<String, Object> params = new HashMap<>();
        params.put("idContinente", continente.getId());
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 500);
        if (!result.isEmpty()){  
            return (List<Pais>)result;
        }
        else {
            return null;
        }
    }
    
    public Pais obterPorNome(String nome) {
        //Trata valores de parâmetros nulos e retira acentos
        nome = Strings.unaccent(StringUtils.defaultString(nome));
        
        String hql = "SELECT p FROM " + Pais.class.getName() + " p "
                + "WHERE function('unaccent', lower(trim(p.mNome))) like lower(:nome) ";
        Map<String, Object> params = new HashMap<>();
        params.put("nome", nome.trim());
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 1);
        if (!result.isEmpty()){  
            return (Pais)result.get(0);
        }
        else {
            return null;
        }
    }
    
    public Pais obterPorSiglaOuNomeEn(String sigla, String nomeEn) {
        String hql = "SELECT p FROM " + Pais.class.getName() + " p "
                + "WHERE lower(trim(p.mSigla)) like lower(:sigla) OR lower(trim(p.mNomeEn)) like lower(:nomeEn) ";
        Map<String, Object> params = new HashMap<>();
        params.put("sigla", sigla.trim());
        params.put("nomeEn", StringUtils.defaultString(nomeEn, "").trim());
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 1);
        if (!result.isEmpty()){  
            return (Pais)result.get(0);
        }
        else {
            return null;
        }
    }
    
}
