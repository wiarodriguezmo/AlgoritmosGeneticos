package AE;


public class Individuo {
    int fitness;
    boolean[] codigo;
    
    public Individuo(){
    }
    
    public Individuo(boolean[] codigo){
        this.codigo = codigo;
        fitness = fitness();
    }
    
    // Función a modificar
    private int fitness(){
        int temp=0;
        for(int i=0;i<codigo.length;i++)
            if(codigo[i])temp++;
        
        return temp;
    }
    
    public static boolean[] generarCodAleatorio(int tamano){
        boolean[] cod = new boolean[tamano];
        for (int i = 0; i < tamano; i++) {
            if(Math.random()<0.5)cod[i] = true;
            else cod[i] = false;
        }
        return cod;
    }
    
    public Individuo[] cruzar(Individuo uno,Individuo dos){
        
        
    }
    
    public Individuo mutar(Individuo x){
        
    }
}