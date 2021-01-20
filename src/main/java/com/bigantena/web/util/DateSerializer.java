/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.web.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aspferraz on 09/11/2018.
 *
 * @deprecated use {@link com.fasterxml.jackson.annotation.JsonFormat} instead.
 */
@Deprecated
public class DateSerializer extends JsonSerializer<Date>{
 
    /* (non-Javadoc)  
    * @see org.codehaus.jackson.map.JsonSerializer#serialize(java.lang.Object, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)  
    */  
    @Override
    public void serialize(Date dtValue, JsonGenerator generator, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = formatter.format(dtValue);
        generator.writeString(formattedDate);
    }  

}
