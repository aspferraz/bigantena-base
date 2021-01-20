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
public class RadioXMLParser extends XMLParser {

    @Override
    protected File[] getFiles() {
        return new File[]{ 
            new File(baseUri + "\\sitemap_radio1.xml")
//                ,new File(baseUri + "\\sitemap_radio2.xml")
//                ,new File(baseUri + "\\sitemap_radio3.xml")
//                ,new File(baseUri + "\\sitemap_radio4.xml")
//                ,new File(baseUri + "\\sitemap_radio5.xml") 
        };    
    }
    
}
