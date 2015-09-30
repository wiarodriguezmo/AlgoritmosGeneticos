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
    
    
    public static Individuo[] cruzar(String tipoCruce, Individuo uno, Individuo dos){
        switch (tipoCruce) {
            case "1punto": 
                int corte = (int) (Math.random()*10);
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
            //case "2puntos":
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
    
    
}