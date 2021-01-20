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
import com.bigantena.model.Genero;
import com.bigantena.model.Pais;
import com.bigantena.model.Radio;
import com.bigantena.util.Strings;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
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
@Service("radioService")
public class RadioService {
    
    private static final Logger logger = Logger.getLogger(RadioService.class);
    
    @Autowired
    private BasicDAO basicDAO;
    @Autowired
    private GeneroService generoService;
    @Autowired
    private CidadeService cidadeService;
    @Autowired
    private EstadoService estadoService;
    @Autowired
    private PaisService paisService;
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    synchronized public Radio salvar(Radio radio) {
        return (Radio)basicDAO.saveAndFlush(radio);
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    synchronized public Radio atualizar(Radio radio) {
        Radio r;
        r  = (Radio) basicDAO.update(radio);
        basicDAO.flush();
        basicDAO.clear();
        return r;
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void persistChanges() {
        basicDAO.flush();
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void clearPersistenceContext() {
        basicDAO.clear();
    }
    
    //:)
    private Document connect(String url) throws IOException {
        Document doc = null;
        int count = 0;
        int maxTries = 3;
        boolean continueHttpCall = true;
        while (continueHttpCall) {
            try {
                String userAgent = "Mozilla/5.0 (X11; U; Linux i586; en-US; rv:1.7.3) Gecko/20040924 Epiphany/1.4.4 (Ubuntu)";
//                String userAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)";
                doc = Jsoup.connect(url).userAgent(userAgent).timeout(30 * 1000).get();
                return doc;
            } catch (SocketException se) {
                if (++count == maxTries) {
                    continueHttpCall = false;
                    logger.error(se.getMessage(), se);
                }
            }
        }
        return doc;
    } 
    
//    List<Integer> idsRadiosDesativadas = null;
    synchronized public Radio obterRadioPorPaginaHTML(String dominio, String url) {
        Radio radio = null;
        try {
            logger.info("> "+url);
            String[] strArr = url.split("/");
            Integer idRadio = Integer.valueOf(strArr[strArr.length - 1]);
            
//            tenta atualizar radios desativadas
   
//            if (idsRadiosDesativadas == null) {
//                idsRadiosDesativadas = obterIdsRadiosDesativadas();
//            }
//            if (idsRadiosDesativadas.contains(idRadio)) {
//                radio = buscarPorId(idRadio);
//            } 
//            else {
//                return null;
//            }

//          testa se radio já existe no banco
            
//            if (buscarPorId(idRadio) != null) {
//                return null;
//            }
            
            radio = new Radio(idRadio);
            
            radio.setUrl(StringUtils.substringAfter(url, dominio));
            
            Document doc = connect(url);
            logger.info("> "+"connected!");
            
            Elements elements;
            
            elements = doc.getElementsByAttributeValue("class", "col-12 header-radio");
            if (elements.isEmpty()) {
                throw new ParseException("elemento <div class='col-12 header-radio' não localizado", 0);
            }
            Element e = elements.get(0);
            String nomeRadio = e.getElementsByTag("h1").text();
            radio.setNome(nomeRadio);
            
            elements = e.select("img[src~=(?i)\\.(png|jpe?g|bmp)]");
            if (elements.isEmpty()) {
                throw new ParseException("elemento <img> não localizado", 0);
            }
            e = elements.get(0);
            String urlLogo = e.attr("src");
            
            String nomeArquivoLogo = urlLogo.split("/")[urlLogo.split("/").length - 1];
            radio.setNomeArquivoLogo(nomeArquivoLogo);
            
//            elements = doc.getElementsByAttributeValue("class","col-12 info-radio");
//            if (elements.isEmpty()) {
//                throw new ParseException("elemento <div class='col-12 info-radio'> não localizado "
//                        + "para a url '"+radio.getUrl()+"'", 0);
//            }
//            
//            e = elements.get(0);
//            Elements b = e.select("b:contains(Segmentos)");
//            
//            String strNomeGeneros = b.isEmpty() ? null : b.get(0).parent().ownText().trim();
//            
//            if (strNomeGeneros != null) {
//                String[] arrNomeGeneros = strNomeGeneros.split(",");
//                for (String nomeGenero : arrNomeGeneros) {
//                    nomeGenero = nomeGenero.trim();
//                    Genero genero = generoService.obterPorNome(nomeGenero);
//                    if (genero == null) {
//                        Integer nId = generoService.obterIdMaximo() + 1;
//                        genero = new Genero(nId);
//                        genero.setNome(nomeGenero);
//                        genero = generoService.salvar(genero);
//                    }
//                    radio.addGenero(genero);
//                }
//            }
//            
//            b = e.select("b:contains(Cidade)");
//            String nomeCidade = b.get(0).parent().ownText().trim();
//            b = e.select("b:contains(Estado)");
//            String nomeEstado = b.get(0).parent().ownText().trim();
//            b = e.select("b:contains(País)");
//            String nomePais = b.get(0).parent().ownText().trim();
//            Cidade cidade = cidadeService.obterPorNomeEstadoOuNomePais(nomeCidade, nomeEstado, nomePais);
//            if (cidade == null) {
//                logger.info("A cidade '"+nomeCidade+" não foi encontrada no banco. (Tentando salvar nova cidade)");
//                Estado estado = estadoService.obterPorNome(nomeEstado);
//                if (estado != null) {
//                    Integer nId = cidadeService.obterIdMaximo() + 1;
//                    Cidade novaCidade = new Cidade(nId);
//                    novaCidade.setNome(nomeCidade);
//                    novaCidade.setEstado(estado);
//                    novaCidade = cidadeService.salvar(novaCidade);
//                    cidade = novaCidade;
//                } 
//                else {
//                    logger.info("O estado '"+nomeEstado+" não foi encontrado no banco. (Tentando salvar novo estado)");
//                    Pais pais = paisService.obterPorNome(nomeEstado);//verifica se existe país com o nome desse estado
//                    if (pais == null) {
//                        logger.info("Criando novo Estado");
//                        Integer nId = estadoService.obterIdMaximo() + 1;
//                        Estado novoEstado = new Estado(nId);
//                        novoEstado.setNome(nomeEstado);
//                        pais = paisService.obterPorNome(nomePais);
//                        if (pais == null) {
//                            throw new ParseException("Pais '"+nomePais+"' não foi encontrado no banco", 0);
//                        }
//                        novoEstado.setPais(pais);
//                        novoEstado = estadoService.salvar(novoEstado);
//                        
//                        logger.info("Criando nova Cidade");
//                        nId = cidadeService.obterIdMaximo() + 1;
//                        Cidade novaCidade = new Cidade(nId);
//                        novaCidade.setNome(nomeCidade);
//                        novaCidade.setEstado(novoEstado);
//                        novaCidade = cidadeService.salvar(novaCidade);
//                        cidade = novaCidade;
//                    } 
//                    else {
//                        logger.info("Criando nova Cidade sem Estado, apenas País");
//                        Integer nId = cidadeService.obterIdMaximo() + 1;
//                        Cidade novaCidade = new Cidade(nId);
//                        novaCidade.setNome(nomeCidade);
//                        novaCidade.setPais(pais);
//                        novaCidade = cidadeService.salvar(novaCidade);
//                        cidade = novaCidade;
//                    }
//                }
//            }
//            radio.setCidade(cidade);
//            
//
//            b = e.select("b:contains(Site)");
//            String site = b.isEmpty() ? null : b.get(0).parent().ownText().trim();
//            radio.setSite(site);
            
            elements = doc.getElementsByTag("script");

            for (Element script : elements) {
                if (script.attr("src").isEmpty() && script.attr("type").isEmpty()) {
                    if(script.data().trim().contains("document.getElementById('player-loading')")) {
                        String scriptCode = script.data();
                        
                        String[] urlsSrc = StringUtils.substringsBetween(scriptCode, "src:  \"", "\" }"); //mp4
                        String[] urlsNetConnection; //rtmp
                        String[] urlsIpad; // aac, m3u8
                        String[] urls; //aac, mp4    
                        
                        if (urlsSrc == null) {                            
                            urlsNetConnection = StringUtils.substringsBetween(scriptCode, "netConnectionUrl: \"", "\",");
                            if (urlsNetConnection != null) { //RTMP
                                //logger.info(scriptCode);
                                String [] clipCodeArr = StringUtils.substringsBetween(scriptCode, "clip: {", "}");
                                String clipCode = clipCodeArr[0];
                                String[] urlComplement = StringUtils.substringsBetween(clipCode, "url: \"", "\",");
                                radio.setUrlServidor1(urlsNetConnection[0] +"/"+ urlComplement[0]);
                                urlsIpad = StringUtils.substringsBetween(clipCode, "ipadUrl: \"", "\"");
                                radio.setUrlServidor2(urlsIpad[0]);
                            }
                            if (urlsNetConnection == null) {
                                urls = StringUtils.substringsBetween(scriptCode, "'url':'", "',");
                                if (urls != null) {
                                    radio.setUrlServidor1(urls[0]);
                                }
                                else {
                                    String [] clipCodeArr = StringUtils.substringsBetween(scriptCode, "clip: {", "}");
                                    if (clipCodeArr != null) {
                                        String clipCode = clipCodeArr[0];
                                        urls = StringUtils.substringsBetween(clipCode, "url: \"", "\",");
                                        if (urls != null) {
                                            radio.setUrlServidor1(urls[0]);
                                        }
                                    }
                                }
                            }
                        } else {
                            radio.setUrlServidor1(urlsSrc[0]);
                            if (urlsSrc.length > 1)
                                radio.setUrlServidor2(urlsSrc[1]); 
                        }
                        break;
                    }
                }
            }
            
            if (radio.getUrlServidor1() == null) {
                radio.setDesativada(true);
            } else {
                byte[] imgBytes = lerImagem(urlLogo);
                radio.setLogo(imgBytes);
                
                radio.setDesativada(false);
            }
            radio.setDataUltimaAtualizacao(new Date());
            
        } catch (IOException | ParseException ex) {
            radio = null;
            logger.error(ex.getMessage(), ex);
        }
        return radio;
    }
    
    private byte[] lerImagem(String imgUrl) throws IOException {
        
        URL url = new URL(imgUrl);
        
        ByteArrayOutputStream bos;
        
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        uc.setReadTimeout(10000);
        uc.setConnectTimeout(15000);
        
        uc.addRequestProperty("User-Agent", 
            "Mozilla/5.0 (X11; U; Linux i586; en-US; rv:1.7.3) Gecko/20040924 Epiphany/1.4.4 (Ubuntu)");
        
        try (InputStream is = uc.getInputStream()) {
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int length;
            while ((length = is.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
            }
        }
        
        byte[] imgBytes = bos.toByteArray();
        bos.close();
        
        return imgBytes;
    }
    
    public Radio buscarPorId(Integer id) {
        Radio r = (Radio)basicDAO.getOne(Radio.class, id);
        return r;
    }
    
    public List<Radio> buscarPorIds(List<Integer> ids) {
        String strIds = ids.stream().map(Object::toString).collect(Collectors.joining(","));
        
        String hql = "SELECT r FROM " + Radio.class.getName() + " r "
                + "WHERE r.mId in (:ids) "
                + "ORDER BY function('find_in_set', cast(r.id as text), :strIds) ";
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        params.put("strIds", strIds);
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params);
        if (!result.isEmpty()){  
            return (List<Radio>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Radio> buscarPorIds(List<Integer> ids, int page, int limit) {
        String strIds = ids.stream().map(Object::toString).collect(Collectors.joining(","));
        
        String hql = "SELECT r FROM " + Radio.class.getName() + " r "
                + "WHERE r.mId in (:ids) "
                + "ORDER BY function('find_in_set', cast(r.id as text), :strIds) ";
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        params.put("strIds", strIds);
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, page, limit);
        if (!result.isEmpty()){  
            return (List<Radio>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Radio> buscarPorNome(String nome) {
        //Trata valores de parâmetros nulos e retira acentos
        nome = Strings.unaccent(StringUtils.defaultString(nome));
        
        String hql = "SELECT r FROM " + Radio.class.getName() + " r "
                + "WHERE function('unaccent', lower(trim(r.mNome))) like lower(:nome) ";
        Map<String, Object> params = new HashMap<>();
        params.put("nome", "%" + nome.trim() + "%");
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 250);
        if (!result.isEmpty()){  
            return (List<Radio>)result;
        }
        else {
            return null;
        }
    }
    
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Radio> buscarPorExemplo(Radio radio) {
        Criteria baseCriteria = basicDAO.getCriteria(Radio.class);
        addExample(baseCriteria, radio, "mNome");
        addNomeRestriction(baseCriteria, radio.getNome());
        
//      Restrição para rádios ativas
        baseCriteria.add(Restrictions.and(
            Restrictions.eq("mDesativada", false),
            Restrictions.sqlRestriction("length({alias}.url_servidor_1) > ?", 2, IntegerType.INSTANCE))
        );
        
        baseCriteria.addOrder(Order.asc("mNome"));
            
        Cidade cidade = radio.getCidade(); 
        if (cidade != null) {
            Criteria cidadeCriteria = baseCriteria.createCriteria("mCidade", "cidade", JoinType.INNER_JOIN);
            addExample(cidadeCriteria, cidade, "cidade.mNome");
            addIdRestriction(cidadeCriteria, cidade.getId(), "cidade");
            addNomeRestriction(cidadeCriteria, cidade.getNome());
            cidadeCriteria.addOrder(Order.asc("cidade.mNome"));
            
            Estado estado = cidade.getEstado();
            if (estado != null) {
                Criteria estadoCriteria = cidadeCriteria.createCriteria("cidade.mEstado", "estado", JoinType.INNER_JOIN);
                addExample(estadoCriteria, estado, "estado.mNome");
                addIdRestriction(estadoCriteria, estado.getId(), "estado");
                addNomeRestriction(estadoCriteria, estado.getNome());
                estadoCriteria.addOrder(Order.asc("estado.mNome"));    
                
                if (estado.getPais() != null) {
                    Criteria paisCriteria = estadoCriteria.createCriteria("mPais", "pais_1", JoinType.INNER_JOIN);
                    addExample(paisCriteria, estado.getPais(), "pais_1.mNome");
                    addIdRestriction(paisCriteria, estado.getPais().getId(), "pais_1");
                    addNomeRestriction(paisCriteria, estado.getPais().getNome());
                    paisCriteria.addOrder(Order.asc("pais_1.mNome"));    
                }
            }
            if (cidade.getPais() != null) {
                Criteria paisCriteria = cidadeCriteria.createCriteria("mPais", "pais_2", JoinType.INNER_JOIN);
                addExample(paisCriteria, cidade.getPais(), "pais_2.mNome");
                addIdRestriction(paisCriteria, cidade.getPais().getId(), "pais_2");
                addNomeRestriction(paisCriteria, cidade.getPais().getNome());
                paisCriteria.addOrder(Order.asc("pais_2mNome"));    
            }
        }
        if (radio.getGeneros() != null && !radio.getGeneros().isEmpty()) {
            Criteria generosCriteria = baseCriteria.createCriteria("mGeneros", "genero", JoinType.INNER_JOIN);
            Genero genero = radio.getGeneros().iterator().next();
            addExample(generosCriteria, genero, "genero.mNome");
            addIdRestriction(generosCriteria, genero.getId(), "genero");
            addNomeRestriction(generosCriteria, genero.getNome());
            generosCriteria.addOrder(Order.asc("genero.mNome"));
        }
        
        List<? extends AbstractBean> result = baseCriteria.list();
        if (!result.isEmpty()) {  
            return (List<Radio>)result;
        }
        else {
            return null;
        }
    }
    
    private void addIdRestriction(Criteria c, Number id, String alias) {
        if (id != null) {
            c.add(Restrictions.eq(alias+".mId", id));
        }
    }
    
    private void addNomeRestriction(Criteria c, String nome) {
        if (StringUtils.isNoneBlank(nome)) {
            c.add(Restrictions.sqlRestriction("unaccent_string(lower({alias}.nome)) like lower(?)",  
                Strings.unaccent("%" + StringUtils.defaultString(nome).trim() + "%"), 
                StringType.INSTANCE));
        }
    }
    
    private void addExample(Criteria c, Object entity, String... propertiesExcluded) {
        if (entity != null) {
            Example ex = Example.create(entity).excludeZeroes().enableLike().ignoreCase();
            if (propertiesExcluded != null) {
                for (String property : propertiesExcluded) {
                    ex.excludeProperty(property);
                }
            }
            c.add(ex); 
        }
    }
    
    public List<Radio> buscarPorPalavrasChave(String strQ, int page, int limit) {
        //Trata valores de parâmetros nulos e retira acentos
        strQ = Strings.unaccent(StringUtils.defaultString(strQ));
        String[] palavras = StringUtils.split(strQ);
        
        Map<String, Object> params = new HashMap<>();
        String hql = "SELECT r FROM " + Radio.class.getName() + " r "
                + "inner join r.mCidade as cidade "
                + "left join cidade.mEstado as estado "
                + "left join cidade.mPais as pais_1 "
                + "left join estado.mPais as pais_2 "
                + "WHERE ";
        
                for (int i = 0; i < palavras.length; i++) {
                    hql +=
                    "function('unaccent', lower("
                   +   "concat(r.mNome, ' ', "
                   +       "cidade.mNome, ' ', "
                   +       "function('COALESCE', estado.mSigla, ''), ' ', "
                   +       "function('COALESCE', estado.mNome, ''), ' ', "
                   +       "function('COALESCE', pais_1.mNome, ''), ' ', "
                   +       "function('COALESCE', pais_2.mNome, '') )"
                   + ") ) like lower(:palavraChave"+i+") ";
                   if (i < palavras.length - 1) {
                        hql += "and "   ;
                   }
                   
                   params.put("palavraChave"+i, "%"+palavras[i]+"%");
                   
                }   
                
                hql += "and r.mDesativada is false "
                + "and length(r.mUrlServidor1) > 2 "
                + "order by pais_1.mNome, "
                        + "pais_2.mNome, "
                        + "estado.mSigla, "
                        + "estado.mNome, "
                        + "cidade.mNome, "
                        + "r.mNome, "
                        + "r.mId asc";
            
            
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, page, limit);
        if (!result.isEmpty()){  
            return (List<Radio>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Radio> buscarPorCidade(Cidade cidade) {
        //Trata valores de parâmetros nulos e retira acentos
        cidade.setNome(Strings.unaccent(StringUtils.defaultString(cidade.getNome())));
        cidade.setId(NumberUtils.toInt(String.valueOf(cidade.getId()), -1));
        Map<String, Object> params = new HashMap<>();
        
        String hql = "SELECT r FROM " + Radio.class.getName() + " r " +
                     "inner join r.mCidade as cidade " +
            "WHERE (1 = 1) ";
        if (cidade.getId() != -1) {
            hql += "AND cidade.mId = :idCidade ";
            params.put("idCidade", cidade.getId());
        }
        else if (StringUtils.isNotBlank(cidade.getNome())) {
            hql += "AND function('unaccent', lower(trim(cidade.mNome))) like lower(:nomeCidade) ";
            params.put("nomeCidade", "%" + cidade.getNome().trim() + "%");
        }
        hql += "AND r.mDesativada is false "
                + "AND length(r.mUrlServidor1) > 2 "
                + "ORDER BY cidade.mNome, r.mNome asc";
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 500);
        if (!result.isEmpty()){  
            return (List<Radio>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Radio> buscarPorCidadeEGeneros(Cidade cidade, List<Integer> idsGeneros) {
        //Trata valores de parâmetros nulos e retira acentos
        cidade.setNome(Strings.unaccent(StringUtils.defaultString(cidade.getNome())));
        cidade.setId(NumberUtils.toInt(String.valueOf(cidade.getId()), -1));
        Map<String, Object> params = new HashMap<>();
        
        String hql = "SELECT r FROM " + Radio.class.getName() + " r " +
                     "inner join r.mCidade as cidade " +
                     "inner join r.mGeneros as genero " +
            "WHERE (1 = 1) ";
        if (cidade.getId() != -1) {
            hql += "AND cidade.mId = :idCidade ";
            params.put("idCidade", cidade.getId());
        }
        else if (StringUtils.isNotBlank(cidade.getNome())) {
            hql += "AND function('unaccent', lower(trim(cidade.mNome))) like lower(:nomeCidade) ";
            params.put("nomeCidade", "%" + cidade.getNome().trim() + "%");
        }
        if (idsGeneros != null && !idsGeneros.isEmpty()) {
            hql += "AND genero.mId in (:idsGeneros) ";
            params.put("idsGeneros", idsGeneros);
        }
        
        hql += "AND r.mDesativada is false "
                + "AND length(r.mUrlServidor1) > 2 "
                + "ORDER BY cidade.mNome, r.mNome asc";
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 500);
        if (!result.isEmpty()){  
            return (List<Radio>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Radio> buscarPorEstado(Estado estado) {
        //Trata valores de parâmetros nulos e retira acentos
        estado.setNome(Strings.unaccent(StringUtils.defaultString(estado.getNome())));
        estado.setId(NumberUtils.toInt(String.valueOf(estado.getId()), -1));
        Map<String, Object> params = new HashMap<>();
        
        String hql = "SELECT r FROM " + Radio.class.getName() + " r " +
                     "inner join r.mCidade.mEstado as estado " +
            "WHERE (1 = 1) ";
        if (estado.getId() != -1) {
            hql += "AND estado.mId = :idEstado ";
            params.put("idEstado", estado.getId());
        }
        else if(StringUtils.isNotBlank(estado.getNome())) {
            hql += "AND function('unaccent', lower(trim(estado.mNome))) like lower(:nomeEstado) "; 
            params.put("nomeEstado", "%" + estado.getNome().trim() + "%");
        }
        hql += "AND r.mDesativada is false "
                + "AND length(r.mUrlServidor1) > 2 "
                + "ORDER BY estado.mNome, r.mCidade.mNome, r.mNome asc";
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 500);
        if (!result.isEmpty()) {  
            return (List<Radio>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Radio> buscarPorPais(Pais pais) {
        //Trata valores de parâmetros nulos e retira acentos
        pais.setNome(Strings.unaccent(StringUtils.defaultString(pais.getNome())));
        pais.setId(NumberUtils.toInt(String.valueOf(pais.getId()), -1));
        Map<String, Object> params = new HashMap<>();
        
        String hql = "SELECT r FROM " + Radio.class.getName() + " r " +
                     "left join r.mCidade.mEstado as estado " +
                     "inner join estado.mPais as pais_1 " +
                     "left join r.mCidade.mPais as pais_2 " +
            "WHERE (1 = 1) ";
        
        if (pais.getId() != -1) {    
            hql += "AND (pais_1.mId = :idPais OR pais_2.mId = :idPais) ";
            params.put("idPais", pais.getId());
        }
        else if (StringUtils.isNotBlank(pais.getNome())) {
            hql += "AND (function('unaccent', lower(trim(pais_1.mNome))) like lower(:nomePais) "
                    + "OR function('unaccent', lower(trim(pais_2.mNome))) like lower(:nomePais)) ";
            params.put("nomePais", "%" + pais.getNome().trim() + "%");
        }

        hql += "AND r.mDesativada is false "
                + "AND length(r.mUrlServidor1) > 2 "
                + "ORDER BY pais_1.mNome, "
                + "pais_2.mNome, "
                + "estado.mNome, "
                + "r.mCidade.mNome, "
                + "r.mNome asc";
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 500);
        if (!result.isEmpty()){  
            return (List<Radio>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Radio> buscarPorGenero(Genero genero) {
        //Trata valores de parâmetros nulos e retira acentos
        genero.setNome(Strings.unaccent(StringUtils.defaultString(genero.getNome())));
        genero.setId(NumberUtils.toInt(String.valueOf(genero.getId()), -1));
        Map<String, Object> params = new HashMap<>();
        
        String hql = "SELECT r FROM " + Radio.class.getName() + " r " +
                     "inner join r.mGeneros as genero " +
            "WHERE (1 = 1) ";
        
        if (genero.getId() != -1) {
            hql += "AND :idGenero = genero.mId ";
            params.put("idGenero", genero.getId());
        }
        if (StringUtils.isNotBlank(genero.getNome())) {
            hql += "AND function('unaccent', lower(trim(genero.mNome))) like lower(:nomeGenero) "; 
            params.put("nomeGenero", "%" + genero.getNome().trim() + "%");
        }
        
        hql += "AND r.mDesativada is false "
                + "AND length(r.mUrlServidor1) > 2 "
                + "ORDER BY r.mNome asc";
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 500);
        if (!result.isEmpty()){  
            return (List<Radio>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Radio> buscarMaisAcessadas(Genero genero, Date dtInicial, Date dtFinal, Integer limite) {
         //Trata valores de parâmetros nulos e retira acentos
         if (dtInicial == null) {
            logger.error("param 'dtInicial' is null", new IllegalArgumentException());
            return null;
        }
        if (dtFinal == null) {
            logger.error("param 'dtFinal' is null", new IllegalArgumentException());
            return null;
        }
        genero.setNome(Strings.unaccent(StringUtils.defaultString(genero.getNome())));
        genero.setId(NumberUtils.toInt(String.valueOf(genero.getId()), -1));
        limite = NumberUtils.toInt(String.valueOf(limite), 100);
        
        Map<String, Object> params = new HashMap<>();
        
        String hql = "SELECT r FROM " + Radio.class.getName() + " r " +
                     "inner join r.mAcessos as acesso " +
                     "inner join r.mGeneros as genero " +
            "WHERE acesso.mDataAcesso BETWEEN :dtInicial AND :dtFinal ";
        
        params.put("dtInicial", dtInicial);
        params.put("dtFinal", dtFinal);
        
        if (genero.getId() != -1) {
            hql += "AND :idGenero = genero.mId ";
            params.put("idGenero", genero.getId());
        }
        if (StringUtils.isNotBlank(genero.getNome())) {
            hql += "AND function('unaccent', lower(trim(genero.mNome))) like lower(:nomeGenero) "; 
            params.put("nomeGenero", "%" + genero.getNome().trim() + "%");
        }
        
        hql += "AND r.mDesativada is false "
                + "GROUP BY r.mId ";
        hql += "ORDER BY size(r.mAcessos) desc";
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, limite);
        if (!result.isEmpty()){  
            return (List<Radio>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Radio> buscarMaisAcessadas(Date dtInicial, Date dtFinal, Integer limite) {
        //Trata valores de parâmetros nulos
        if (dtInicial == null) {
            logger.error("param 'dtInicial' is null", new IllegalArgumentException());
            return null;
        }
        if (dtFinal == null) {
            logger.error("param 'dtFinal' is null", new IllegalArgumentException());
            return null;
        }
        limite = NumberUtils.toInt(String.valueOf(limite), 100);
        
        String hql = "SELECT r FROM " + Radio.class.getName() + " r " 
                     + "inner join r.mAcessos as acesso " 
            + "WHERE acesso.mDataAcesso BETWEEN :dtInicial AND :dtFinal " 
            + "AND r.mDesativada is false "
                + "GROUP BY r.mId "
            + "ORDER BY size(r.mAcessos) desc";
        
        Map<String, Object> params = new HashMap<>();
        params.put("dtInicial", dtInicial);
        params.put("dtFinal", dtFinal);
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, limite);
        if (!result.isEmpty()){  
            return (List<Radio>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Radio> buscarPorQtdProblemasReportadosMaiorQue(Integer qtd) {
        //Trata valores de parâmetros nulos
        qtd = NumberUtils.toInt(String.valueOf(qtd), 0);
        
        String hql = "SELECT r FROM " + Radio.class.getName() + " r "
                + "WHERE r.mQuantidadeProblemasReportados > :quantidade "
                + "ORDER BY r.mNome asc";
        Map<String, Object> params = new HashMap<>();
        params.put("quantidade", qtd);
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 100);
        if (!result.isEmpty()){  
            return (List<Radio>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Radio> buscarPorDataUltimoProblemaReportadoMaiorQue(Date dt) {
        //Trata valores de parâmetros nulos
        if (dt == null) {
            logger.error("param 'dt' is null", new IllegalArgumentException());
            return null;
        }
        
        String hql = "SELECT r FROM " + Radio.class.getName() + " r "
                + "WHERE r.mDataUltimoProblemaReportado > :data ORDER BY r.mNome asc"
                + "ORDER BY r.mNome asc";
        Map<String, Object> params = new HashMap<>();
        params.put("data", dt);
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 100);
        if (!result.isEmpty()){  
            return (List<Radio>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Radio> buscarPorDataUltimaAtualizacaoMaiorQue(Date dt) {
        //Trata valores de parâmetros nulos
        if (dt == null) {
            logger.error("param 'dt' is null", new IllegalArgumentException());
            return null;
        }
        
        String hql = "SELECT r FROM " + Radio.class.getName() + " r "
                + "WHERE r.mDataUltimaAtualizacao > :data "
                + "ORDER BY r.mNome asc";
        Map<String, Object> params = new HashMap<>();
        params.put("data", dt);
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params, 100);
        if (!result.isEmpty()){  
            return (List<Radio>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Radio> buscarPorNomeArquivoLogo(String nomeArquivo) {
        //Trata valores de parâmetros nulos
        if (nomeArquivo == null) {
            logger.error("param 'nomeArquivo' is null", new IllegalArgumentException());
            return null;
        }
        
        String hql = "SELECT r FROM " + Radio.class.getName() + " r "
                + "WHERE r.mNomeArquivoLogo like :nomeArquivo "
                + "ORDER BY r.mNome asc";
        Map<String, Object> params = new HashMap<>();
        params.put("nomeArquivo", nomeArquivo);
        
        List<? extends AbstractBean> result = basicDAO.findByQuery(hql, params);
        if (!result.isEmpty()){  
            return (List<Radio>)result;
        }
        else {
            return null;
        }
    }
    
    public List<Integer> obterIdsRadiosDesativadas() {
        String hql = "SELECT r.mId FROM " + Radio.class.getName() + " r "
                + "WHERE r.mDesativada is true ";
        
        List<Integer> result = (List<Integer>)basicDAO.executeHQLQuery(hql, null);
        if (!result.isEmpty()){  
            return result;
        }
        else {
            return null;
        }
    }
    
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Integer obterIdMaximo() {
        return basicDAO.getMaxId(Radio.class);
    }
    
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Integer obterProximoId() {
        return obterIdMaximo() + 1;
    }
}
