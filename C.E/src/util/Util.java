package util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author awake
 */
public class Util {
    
    public static double[] normalizar(Object[] o){
        double promedio = promedio((Double[]) (o));
        double desv = desviacion((Double[]) (o), promedio);
        double[] n = new double[o.length];
        for (int i = 0; i < o.length; i++)
            n[i] = ((double) o[i] - promedio) / desv;
        return n;
    }
 
    
    public static double promedio(Double[] o){
        Double sum = 0.0;
        for (int i = 0; i < o.length; i++) {
                sum = o[i] + sum;
        }
        return sum/o.length;
    }
    
    public static double desviacion(Double[] o, double promedio){
        return Math.sqrt(varianza(o, promedio));
    }

    public static double varianza(Double[] o, double promedio){
        Double desv = 0.0;
        for (int i = 0; i < o.length; i++)              
                desv += (o[i]-promedio)*(o[i]-promedio);
        return desv / o.length;
    }
}
