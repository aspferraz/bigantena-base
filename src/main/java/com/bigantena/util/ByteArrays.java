/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.apache.log4j.Logger;

/**
 *
 * @author aspferraz
 */
public final class ByteArrays {
    
    private static final Logger logger = Logger.getLogger(ByteArrays.class);
    
    private ByteArrays() {
    }

    public static byte[] compress(byte[] byteA) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setInput(byteA);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(byteA.length);
        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer); // returns the generated code... index  
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();
        logger.info("Original: " + byteA.length / 1024 + " Kb");
        logger.info("Compressed: " + output.length / 1024 + " Kb");
        return output;
    }

    public static byte[] decompress(byte[] byteA) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(byteA);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(byteA.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();
        logger.info("Original: " + byteA.length);
        logger.info("Compressed: " + output.length);
        return output;
    }

}
