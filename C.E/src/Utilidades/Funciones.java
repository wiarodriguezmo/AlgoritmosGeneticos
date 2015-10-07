/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilidades;

/**
 *
 * @author JhhToshiba
 */
public class Funciones {
    
    public Funciones(){}
    //Son cada una de las funciones del paper del profe, los parametros values[] son los valores a ser evaluados
    //en la funcion, el valor de retorno es el fitness en ese punto
    public double Rosenbrock(double[] values){
        MathEval mathEval = new MathEval();
        if(values[0]<-2048 || values[0]>2048 || values[1]<-2048 || values[1]>2048) return -1;
        mathEval.setVariable("x1", values[0]);
        mathEval.setVariable("x2", values[1]);
        double f = mathEval.evaluate("100*(x1^2 - x2)^2 + (1 - x1)^2");
        return f;
    }
    public double Schwefel(double[] values){
        MathEval mathEval = new MathEval();
        double f=0;
        int n = values.length;
        mathEval.setVariable("n", n);
        for(int i = 0; i<n; i++){
            if(values[i]<-512 || values[i]>512) return -1;
            mathEval.setVariable("x"+(i+1), values[i]);
            f = f + mathEval.evaluate("-x"+(i+1)+ " * sin(sqrt(abs(x"+(i+1)+")))");
        }
        f = mathEval.evaluate("418.9829 * n") + f;
        return f;
    }
    public double Rastrigin(double values[]){
        MathEval mathEval = new MathEval();
        double f=0;
        int n = values.length;
        int A = 10;
        mathEval.setVariable("n", n);
        mathEval.setVariable("A", A);
        for(int i = 0; i<n; i++){
            if(values[i]<-5.12 || values[i]>5.12) return -1;
            mathEval.setVariable("x"+(i+1), values[i]);
            f = f + mathEval.evaluate("(x"+(i+1)+"^2 - (A * cos(2*pi*x"+(i+1)+")))");
        }
        f = mathEval.evaluate("A * n") + f;
        return f;
    }
    public double Griewangk(double values[]){
        MathEval mathEval = new MathEval();
        double f=0;
        double g=1;
        int n = values.length;
        mathEval.setVariable("n", n);
        for(int i = 0; i<n; i++){
            if(values[i]<-600 || values[i]>600) return -1;
            mathEval.setVariable("x"+(i+1), values[i]);
            f = f + mathEval.evaluate("((x"+(i+1)+"^2 )/4000)");
            g = g * mathEval.evaluate("cos((x"+(i+1)+" )/sqrt(" + (i+1) + "))");
        }
        f = 1 + f - g;
        return f;
    }
    public static void main(String args[]){
        Funciones func = new Funciones();
        double[] values = new double[120];
        double f;
        double j=0;
        /*double f = func.Rosenbrock(values);*/
        values[0] = 0;
        for(int i=0; j<60; i++){
            j=j+0.1;
            values[0] = j; 
            f = func.Griewangk(values);
            System.out.println(j+",");
        }
    }
}
