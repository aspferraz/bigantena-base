/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.service;

import com.bigantena.bean.AbstractBean;
import com.bigantena.dao.BasicDAO;
import com.bigantena.model.Cidade;
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

@Service("cidadeService")
public class CidadeService {
    
    private static final Logger logger = Logger.getLogger(CidadeService.class);
    
    @Autowired
    private BasicDAO basicDAO;
    
    @Autowired
    private EstadoService estadoService;
    
    @Autowired
    private PaisService paisService;
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    synchronized public Cidade salvar(Cidade cidade) {
        return (Cidade)basicDAO.saveAndFlush(cidade);
    }
    
    synchronized public Cidade obterCidadePorPaginaHTML(String dominio, String url) {
        Cidade cidade = null;
        try {
            String[] strArr = url.split("/");
            Integer idCidade = Integer.valueOf(strArr[strArr.length - 1]);
            
            //testa se cidade já existe no banco
            if (buscarPorId(idCidade) != null) {
                return null;
            }
            
            cidade = new Cidade(idCidade);
            cidade.setUrl(StringUtils.substringAfter(url, dominio));
            
            Document doc = Jsoup.connect(url).timeout(30*1000).get();
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
            String nomeCidade = elNome.text();
            if (nomeCidade.startsWith("Rádios online ")) {
                nomeCidade = StringUtils.substringAfter(nomeCidade, "Rádios online ");
            } else {
                throw new ParseException("String que precede nome da Cidade não localizada", 0);
            }
            cidade.setNome(nomeCidade);
            Element elEstado = elements.get(3);
            Element elPais = null;
            if (elEstado.getElementsByTag("a").isEmpty()) {
                elPais = elements.get(2);
            }
            
            Elements aElements;
            if (elPais != null) {
                aElements = elPais.getElementsByTag("a");
                if (aElements.isEmpty()) {
                    throw new ParseException("Elementos <a> não localizados", 0);
                }
                String nomePais = aElements.get(0).text();

                Pais pais = paisService.obterPorNome(nomePais);
                if (pais == null) {
                    throw new ParseException("Pais com o nome '" + nomePais + "' não encontrado", 0);
                }
                cidade.setPais(pais);
            } else {
                aElements = elEstado.getElementsByTag("a");
                if (aElements.isEmpty()) {
                    throw new ParseException("Elementos <a> não localizados", 0);
                }
                String nomeEstado = aElements.get(0).text();
                if (nomeEstado.contains("(")) {
                    nomeEstado = StringUtils.substringBefore(nomeEstado, " (");
                }

                Estado estado = estadoService.obterPorNome(nomeEstado);
                if (estado == null) {
                    elPais = elements.get(2);
                    String nomePais = elPais.getElementsByTag("a").get(0).text();
                    Pais pais = paisService.obterPorNome(nomePais);
                    if (pais == null) {
                        throw new ParseException("Pais com o nome '" + nomePais + "' não encontrado", 0);
                    }
                    cidade.setPais(pais);
                } else {
                    cidade.setEstado(estado);
                }
            }
        } catch(IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
        } catch(ParseException pe) {
            logger.error(pe.getMessage(), pe);
        }
        return cidade;
    }
    
    public Cidade buscarPorId(Integer id) {
        Cidade c = (Cidade)basicDAO.getOne(Cidade.class, id);
        return c;
    }
    
    public Cidade obterPorNome(String nome) {
        //Trata valores de parâmetros nulos e retira acentos
        nome = Strings.unaccent(StringUtils.defaultString(nome));
        
        String hql = "SELECT c FROM " + Cidade.class.getName() + " c "
                + "WHERE function('unaccent', lower(trim(c.mNome))) like lower(:nome) ";
        Map<String, Object> params = new HashMap<>();
        params.put("nome", nome.trim());
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 1);
        if (!result.isEmpty()){  
            return (Cidade)result.get(0);
        }
        else {
            return null;
        }
    }
    
    public List<Cidade> buscarPorEstado(Estado estado) {
        String hql = "SELECT c FROM " + Cidade.class.getName() + " c "
                + "WHERE c.mEstado is not null and c.mEstado.mId = :idEstado "
                + "ORDER BY c.mNome ASC "; 
        
        Map<String, Object> params = new HashMap<>();
        params.put("idEstado", estado.getId());
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 1000);
        if (!result.isEmpty()){  
            return (List<Cidade>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Cidade> buscarPorEstadoEGeneros(Estado estado, List<Integer> idsGeneros) {
        String sql =
         "select c.id, c.nome, count(r.id) from radio r"
         +"     inner join radio_genero rg on rg.id_radio = r.id"
         +"     inner join cidade c on c.id = r.id_cidade"
	 +"	inner join estado e on e.id = c.id_estado"
	 +" where" 
	 +" 	r.desativada is false and rg.id_genero in (:idsGeneros)"
         +"     and e.id = :idEstado"
         +"	group by c.id"
	 +"	order by c.id";
        
        Map<String, Object> params = new HashMap<>();
        params.put("idsGeneros", idsGeneros);
        params.put("idEstado", estado.getId());
        
        List<?> sqlResult = basicDAO.executeSQLQuery(sql, params);
        
        if (!sqlResult.isEmpty()) {
            List<Cidade> result = buscarPorEstado(estado);
            basicDAO.clear();
            if (result != null) {
                for (Iterator<Cidade> iter = result.iterator(); iter.hasNext();) {
                    Cidade c = iter.next();
                    boolean contains = false;
                    for (Object[] row : (List<Object[]>)sqlResult) {
                        if (c.getId() == ((Number)row[0]).intValue()) {
                            contains = true;
                            c.setQtdRadios(((Number)row[2]).intValue());
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
    
    public List<Cidade> buscarPorPais(Pais pais) {
        String hql = "SELECT c FROM " + Cidade.class.getName() + " c "
                + "WHERE c.mPais is not null and c.mPais.mId = :idPais "
                + "ORDER BY c.mNome ASC "; 
        
        Map<String, Object> params = new HashMap<>();
        params.put("idPais", pais.getId());
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params);
        if (!result.isEmpty()){  
            return (List<Cidade>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Cidade> buscarPorPaisEGeneros(Pais pais, List<Integer> idsGeneros) {
        String sql =
         "select c.id, c.nome, count(r.id) from radio r"
	 +"	inner join radio_genero rg on rg.id_radio = r.id"
	 +"	inner join cidade c on c.id = r.id_cidade"
	 +"	inner join pais p on p.id = c.id_pais"
         +" where" 
	 +"	r.desativada is false and rg.id_genero in (:idsGeneros)"
	 +"	and p.id = :idPais"
	 +"	group by c.id"
	 +"	order by c.id";
        
        Map<String, Object> params = new HashMap<>();
        params.put("idsGeneros", idsGeneros);
        params.put("idPais", pais.getId());
        
        List<?> sqlResult = basicDAO.executeSQLQuery(sql, params);
        
        if (!sqlResult.isEmpty()) {
            List<Cidade> result = buscarPorPais(pais);
            basicDAO.clear();
            if (result != null) {
                for (Iterator<Cidade> iter = result.iterator(); iter.hasNext();) {
                    Cidade c = iter.next();
                    boolean contains = false;
                    for (Object[] row : (List<Object[]>)sqlResult) {
                        if (c.getId() == ((Number)row[0]).intValue()) {
                            contains = true;
                            c.setQtdRadios(((Number)row[2]).intValue());
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
    
    public Cidade obterPorNomeEstadoOuNomePais(String nome, String nomeEstado, String nomePais) {
        String hql = "SELECT c FROM " + Cidade.class.getName() + " c "
                + "left join c.mEstado as estado "
                + "left join c.mPais as pais "
                + "WHERE c.mNome = :nome and "
                + "((estado.mNome like :nomeEstado) or "
                + "(pais.mNome like :nomePais))";
        Map<String, Object> params = new HashMap<>();
        params.put("nome", nome);
        params.put("nomeEstado", nomeEstado);
        params.put("nomePais", nomePais);
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 1);
        if (!result.isEmpty()){  
            return (Cidade)result.get(0);
        }
        else {
            return null;
        }
    }
    
    public Integer obterIdMaximo() {
        return basicDAO.getMaxId(Cidade.class);
    }
}
