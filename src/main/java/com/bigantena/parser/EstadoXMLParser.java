/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.parser;

import java.io.File;

/**
 *
 * @author aspferraz
 */
public class EstadoXMLParser extends XMLParser {

    @Override
    protected File[] getFiles() {
        return new File[]{ new File(baseUri + "\\sitemap_estados.xml") };        
    }
    
}
