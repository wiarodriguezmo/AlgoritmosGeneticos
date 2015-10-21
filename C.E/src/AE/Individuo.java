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
    
    public static double[] generarCodAleatorio(int dim, double frontera){
        double[] cod = new double[dim];
        for (int i = 0; i < dim; i++)
            cod[i] = Math.random()*frontera;
           
        return cod;
    }
    
    public static Individuo[] cruzar(String tipoCruce, Individuo uno, Individuo dos){
        double[] tres = uno.codigo;
        double[] cuatro = dos.codigo;
        switch (tipoCruce) {
            case "promedio": 
                for(int i=0;i<tres.length;i++){
                    double promedio = (tres[i] + cuatro[i]) / 2.0;
                    cuatro[i] = tres[i] = promedio;
                }
                
                Individuo[] Zwei = {new Individuo(tres), new Individuo(cuatro)};
                return Zwei;
             
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
    }
}