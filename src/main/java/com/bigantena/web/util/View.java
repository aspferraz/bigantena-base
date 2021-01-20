/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.web.util;

/**
 *
 * @author aspferraz
 */
public class View {

    public interface Minima {
    }
    
    public interface MinimaComLogo extends Minima {
    }
    
    public interface MinimaComBandeira extends Minima {
    }
    
    public interface Basica extends MinimaComLogo, MinimaComBandeira {
    }
    
    public interface BasicaComPaises extends Basica {
    }
    
    public interface BasicaComEstados extends Basica {
    }
    
    public interface BasicaComCidades extends Basica {
    }
    
    public interface BasicaComLocais extends BasicaComPaises, BasicaComEstados, BasicaComCidades {
    }
    
    public interface BasicaComRadios extends Basica {
    }
    
    public interface Completa extends BasicaComLocais, BasicaComRadios {
    }

}
