package AE;

public class Individuo {
    double fitness;
    double codigo[];
    
    public Individuo(){
    }
    
    public Individuo(double[] codigo){
        this.codigo = codigo;
        fitness = AE.fitness(this.codigo);
    }
    
    public Individuo(double[] codigo, double f){
        this.codigo = codigo;
        fitness = f;
    }
    
    public static double[] generarCodAleatorio(int dim, double frontera){
        double[] cod = new double[dim];
        for (int i = 0; i < dim; i++)
            cod[i] = Math.random()*frontera*2 - frontera; // uniforme entre 0 y 2veces la frontera, con la resta queda centrada en 0 la dist de la var.
           
        return cod;
    }
    
    // se crean individuos con fitness 0, porque en la mutación se hará la evaluación de este.
    public static Individuo[] cruzar(String tipoCruce, Individuo uno, Individuo dos){
        
        switch (tipoCruce) {
            case "1punto": 
                int corte = (int) (Math.random()*uno.codigo.length);
                double[] tres = new double[uno.codigo.length];  
                double[] cuatro = new double[uno.codigo.length];  
                for(int i=0;i<corte;i++){
                    tres[i] = uno.codigo[i];
                    cuatro[i] = dos.codigo[i];
                }
                for(int i=corte;i<uno.codigo.length;i++){
                    tres[i] = dos.codigo[i];
                    cuatro[i] = uno.codigo[i];
                }
                Individuo[] Zwei = {new Individuo(tres,0), new Individuo(cuatro,0)};
                return Zwei;
                
            case "2puntos":
                int corte1 = (int) (Math.random()*uno.codigo.length);
                int corte2 = (int) (Math.random()*uno.codigo.length);
                if(corte1>corte2){
                    int temp = corte1;
                    corte1=corte2;
                    corte2=temp;
                }
                double[] drei = new double[uno.codigo.length];  
                double[] vier = new double[uno.codigo.length];  
                for(int i=0;i<corte1;i++){
                        drei[i] = uno.codigo[i];
                        vier[i] = dos.codigo[i];
                }for(int i=corte1;i<corte2;i++){
                        drei[i] = dos.codigo[i];
                        vier[i] = uno.codigo[i];
                }for(int i=corte2;i<uno.codigo.length;i++){
                        drei[i] = uno.codigo[i];
                        vier[i] = dos.codigo[i];
                }
                Individuo[] zwei = {new Individuo(drei,0), new Individuo(vier,0)};
                return zwei;
                
            case "diferencia": 
                tres = uno.codigo;
                cuatro = dos.codigo;
                for(int i=0;i<tres.length;i++){
                    double dif = Math.abs(tres[i] - cuatro[i]);
                    if(Math.random() < 0.25){cuatro[i] += dif; tres[i] += dif;}
                    else if(Math.random() < 0.5){cuatro[i] -= dif; tres[i] += dif;}
                    else if(Math.random() < 0.75){cuatro[i] += dif; tres[i] -= dif;}
                    else cuatro[i] -= dif; tres[i] -= dif;
                }
                Individuo[] Zwei1 = {new Individuo(tres,0), new Individuo(cuatro,0)};
                return Zwei1;
                
            case "promedio": 
                tres = uno.codigo;
                cuatro = dos.codigo;
                for(int i=0;i<tres.length;i++){
                    double promedio = (tres[i] + cuatro[i]) / 2.0;
                    cuatro[i] = tres[i] = promedio;
                }
                
                Individuo[] Zwei2 = {new Individuo(tres,0), new Individuo(cuatro,0)};
                return Zwei2;
            case "intercambio":
                //implementando
        }
        return null;
    }
    
    public void mutar(double probMutacion){
        for (int i = 0; i < codigo.length; i++) {
             if(Math.random() < probMutacion){
                if(Math.random() < 0.5)codigo[i] += Math.random()*0.1*codigo[i];
                else codigo[i] -= Math.random()*0.1*codigo[i];
            }
        }
        fitness = AE.fitness(this.codigo);
    }
}