/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.service;

import com.bigantena.bean.AbstractBean;
import com.bigantena.dao.BasicDAO;
import com.bigantena.model.Estado;
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

@Service("estadoService")
public class EstadoService {
    
    private static final Logger logger = Logger.getLogger(EstadoService.class);
    
    @Autowired
    private BasicDAO basicDAO;
    
    @Autowired
    private PaisService paisService;
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    synchronized public Estado salvar(Estado estado) {
        return (Estado)basicDAO.saveAndFlush(estado);
    }
    
    public Estado buscarPorId(Integer id) {
        Estado e = (Estado)basicDAO.getOne(Estado.class, id);
        return e;
    } 
    
    synchronized public Estado obterEstadoPorPaginaHTML(String dominio, String url) {
        Estado estado = null;
        try {
            String[] strArr = url.split("/");
            Integer idEstado = Integer.valueOf(strArr[strArr.length - 1]);
            estado = new Estado(idEstado);
            estado.setUrl(StringUtils.substringAfter(url, dominio));
            
            Document doc = Jsoup.connect(url).timeout(10*1000).get();
            Elements elements = doc.getElementsByAttributeValue("id", "breadcrumbs");
            if (elements.isEmpty()) {
                throw new ParseException("Elemento <ol id='breadcrumbs'> não localizado", 0);
            }
            Element e = elements.get(0);
            elements = e.getElementsByTag("li");
            if (elements.isEmpty()) {
                throw new ParseException("Elementos <li> não localizados", 0);
            }
            Element elNome = elements.get(elements.size() - 1);
            String nomeEstado = elNome.text();
            if (nomeEstado.startsWith("Rádios online ")) {
                nomeEstado = StringUtils.substringAfter(nomeEstado, "Rádios online ");
                if (nomeEstado.contains("(")) {
                    estado.setSigla(StringUtils.substringBetween(nomeEstado, "(", ")"));
                    nomeEstado = StringUtils.substringBefore(nomeEstado, " (");
                }
            } else {
                throw new ParseException("String que precede nome do Estado não localizada", 0);
            }
            estado.setNome(nomeEstado);
            Element elPais = elements.get(elements.size() - 2);
            elements = elPais.getElementsByTag("a");
            if (elements.isEmpty()) {
                throw new ParseException("Elementos <a> não localizados", 0);
            }
            String nomePais = elements.get(0).text();
            Pais pais = paisService.obterPorNome(nomePais);
            if (pais == null) {
                throw new ParseException("Pais com o nome '"+nomePais+"' não encontrado", 0);
            } 
            estado.setPais(pais);
            
        } catch(IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
        } catch(ParseException pe) {
            logger.error(pe.getMessage(), pe);
        }
        return estado;
    }
    
    public Estado obterPorNome(String nome) {
        //Trata valores de parâmetros nulos e retira acentos
        nome = Strings.unaccent(StringUtils.defaultString(nome));
        
        String hql = "SELECT e FROM " + Estado.class.getName() + " e "
                + "WHERE function('unaccent', lower(trim(e.mNome))) like lower(:nome) ";
        Map<String, Object> params = new HashMap<>();
        params.put("nome", nome.trim());
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 1);
        if (!result.isEmpty()){  
            return (Estado)result.get(0);
        }
        else {
            return null;
        }
    }
    
    public List<Estado> buscarPorPais(Pais pais) {
        String hql = "SELECT e FROM " + Estado.class.getName() + " e "
                + "WHERE e.mPais.mId = :idPais "
                + "ORDER BY e.mNome ASC "; 
        
        Map<String, Object> params = new HashMap<>();
        params.put("idPais", pais.getId());
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 500);
        if (!result.isEmpty()){  
            return (List<Estado>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Estado> buscarPorPaisEGeneros(Pais pais, List<Integer> idsGeneros) {
        String sql =
        "select e.id, e.nome, count(r.id) from radio r" 
         +"	inner join radio_genero rg on rg.id_radio = r.id"
         +"	inner join cidade c on c.id = r.id_cidade"
         +"	inner join estado e on e.id = c.id_estado"
         +"  where"
         +"	r.desativada is false and rg.id_genero in (:idsGeneros)"
         +"	and e.id_pais = :idPais"
         +"  group by e.id"
         +"  order by e.id";
        
        Map<String, Object> params = new HashMap<>();
        params.put("idsGeneros", idsGeneros);
        params.put("idPais", pais.getId());
        List<?> sqlResult = basicDAO.executeSQLQuery(sql, params);
        
        if (!sqlResult.isEmpty()) {
            List<Estado> result = buscarPorPais(pais);
            basicDAO.clear();
            if (result != null) {
                for (Iterator<Estado> iter = result.iterator(); iter.hasNext();) {
                    Estado e = iter.next();
                    boolean contains = false;
                    for (Object[] row : (List<Object[]>)sqlResult) {
                        if (e.getId() == ((Number)row[0]).intValue()) {
                            contains = true;
                            e.setQtdRadios(((Number)row[2]).intValue());
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
    
    public Integer obterIdMaximo() {
        return basicDAO.getMaxId(Estado.class);
    }
    
}
