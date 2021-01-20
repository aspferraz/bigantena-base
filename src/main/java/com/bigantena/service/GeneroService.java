/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.service;

import com.bigantena.bean.AbstractBean;
import com.bigantena.dao.BasicDAO;
import com.bigantena.model.Genero;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
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


@Service("generoService")
public class GeneroService {
    
    private static final Logger logger = Logger.getLogger(GeneroService.class);
    
    @Autowired
    private BasicDAO basicDAO;
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    synchronized public Genero salvar(Genero genero) {
        return (Genero)basicDAO.saveAndFlush(genero);
    }
    
    synchronized public Genero obterGeneroPorPaginaHTML(String dominio, String url) {
        Genero genero = null;
        try {
            String[] strArr = url.split("/");
            Integer idGenero = Integer.valueOf(strArr[strArr.length - 1]);
            genero = new Genero(idGenero);
            genero.setUrl(StringUtils.substringAfter(url, dominio));
            
            Document doc = Jsoup.connect(url).timeout(10*1000).get();
            Elements elements = doc.getElementsByAttributeValue("class", "page-header");
            if (elements.isEmpty()) {
                throw new ParseException("Elemento <h2 class='page-header'> não localizado>", 0);
            }
            Element e = elements.get(0);
            String nome = e.getElementsByTag("small").text();
            genero.setNome(nome);
            
            
        } catch(IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
        } catch(ParseException pe) {
            logger.error(pe.getMessage(), pe);
        }
        return genero;
    }
    
    public Genero buscarPorId(Integer id) {
        Genero g = (Genero)basicDAO.getOne(Genero.class, id);
        return g;
    } 
    
    public List<Genero> buscarTodos() {
        List<? extends AbstractBean> result = basicDAO.getAll(Genero.class);
        if (!result.isEmpty()){  
            return (List<Genero>)result;
        }
        else {
            return null;
        }
    }
    
    public Genero obterPorNome(String nome) {
        String hql = "SELECT g FROM " + Genero.class.getName() + " g WHERE g.mNome = :nome";
        Map<String, Object> params = new HashMap<>();
        params.put("nome", nome);
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 1);
        if (!result.isEmpty()){  
            return (Genero)result.get(0);
        }
        else {
            return null;
        }
    }
    
    public Integer obterIdMaximo() {
        return basicDAO.getMaxId(Genero.class);
    }
}
