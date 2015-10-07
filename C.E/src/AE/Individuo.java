package AE;

import java.util.ArrayList;


public class Individuo {
    int fitness;
    boolean[] codigo;
    
    int[][] transiciones = new int[10][3]; // AFD
    
    public Individuo(){
    }
    
    public Individuo(boolean[] codigo){
        this.codigo = codigo;
        transiciones = configurarTransiciones();
        fitness = fitness();
    }
    
    
    // También a modificar según el caso
    public double mejorFitness(){ //AFD
        return 1000;
    }
    
    public static boolean[] generarCodAleatorio(int tamano){
        boolean[] cod = new boolean[tamano];
        for (int i = 0; i < tamano; i++) {
            if(Math.random()<0.5)cod[i] = true;
            else cod[i] = false;
        }
        return cod;
    }
    
    public static Individuo[] cruzar(String tipoCruce, Individuo uno, Individuo dos){
        switch (tipoCruce) {
            case "1punto": 
                int corte = (int) (Math.random()*uno.codigo.length);
                boolean[] tres = new boolean[uno.codigo.length];  
                boolean[] cuatro = new boolean[uno.codigo.length];  
                for(int i=0;i<uno.codigo.length;i++){
                    if(i<=corte){
                        tres[i] = uno.codigo[i];
                        cuatro[i] = dos.codigo[i];
                    }else {
                        tres[i] = dos.codigo[i];
                        cuatro[i]  = uno.codigo[i];;
                    }
                }
                Individuo[] Zwei = {new Individuo(tres), new Individuo(cuatro)};
                return Zwei;
                
            case "2puntos":
                int corte1 = (int) (Math.random()*uno.codigo.length);
                int corte2 = (int) (Math.random()*uno.codigo.length);
                if(corte1>corte2){
                    int temp = corte1;
                    corte1=corte2;
                    corte2=temp;
                }
                boolean[] drei = new boolean[uno.codigo.length];  
                boolean[] vier = new boolean[uno.codigo.length];  
                for(int i=0;i<uno.codigo.length;i++){
                    if(i<=corte1 || i>corte2){
                        drei[i] = uno.codigo[i];
                        vier[i] = dos.codigo[i];
                    }else if(i>corte1 && i<=corte2){
                        vier[i] = uno.codigo[i];
                        drei[i]  = dos.codigo[i];;
                    }
                }
                Individuo[] zwei = {new Individuo(drei), new Individuo(vier)};
                return zwei;
                
            case "uniforme":
                //implementando
        }
        return null;
    }
    
    public void mutar(){
        int largo = codigo.length;
        double probM = 1.0/largo;
        for (int i=0; i<largo;i++) {
            if(Math.random()< probM){
                codigo[i] = !codigo[i];
            }
        }
    }

    private int[][]  configurarTransiciones(){ //AFD
        int[][] transicion = new int[10][3];
        for (int i = 0; i < 10; i++) { // Cada estado son 9bits, bit0=aceptación,bit1:5=estado al que pasa si recibe 0, bit5:9=estado al que pasa si recibe 1.
                transicion[i][0] = codigo[i*9] ?1:0;
                transicion[i][1] = ((codigo[i*9+1]?1:0)*8 + (codigo[i*9+2]?1:0)*4 + (codigo[i*9+3]?1:0)*2 + (codigo[i*9+4]?1:0)*1 + (int)(Math.random()*5))%10; // Conversión a entero
                transicion[i][2] = ((codigo[i*9+5]?1:0)*8 + (codigo[i*9+6]?1:0)*4 + (codigo[i*9+7]?1:0)*2 + (codigo[i*9+8]?1:0)*1 + (int)(Math.random()*5))%10; // Conversión a entero
        }
        return transicion;
    }
    
    // Función a modificar
    private int fitness(){ // AFD
        ArrayList<boolean[]> training = AE.training;
        int fitness=0;
        for (boolean[] training1 : training) {
            int estado=0;
            for (int i = 1; i < training1.length; i++) {
                estado = sigEstado(estado,training1[i]);
            }
            fitness += transiciones[estado][0]==(training1[0]?1:0)?1:0; // Suma al fitness si el estado de aceptación del autómata coincide con el valor de la cadena probada.
        }
        return fitness;
    }
    
    private int sigEstado(int estado, boolean training1) {
        return transiciones[estado][training1?2:1];
    }
    
    
    
}