package reales;

import util.Funciones;
import java.io.IOException;
import test.BenchmarkCEC2013_LSGO;

public class AE {
    private static int numF = 0;
    static int numIndividuos = 40;
    
    
    public static void main(String[] args) throws IOException{
        
        int D=1000;
        double frontera = 5;
        double probMutacion = 10.0/(double)D;
        Poblacion poblacion = new Poblacion(numIndividuos,D,0.6,frontera,probMutacion); // #individuos, Dimensión, probCruce, frontera.
        
        //Probando
            double comienzo = 0.0;
            for (int i = 0; i < numIndividuos; i++) {
                comienzo +=  poblacion.individuos.get(i).fitness; 
            }System.out.println(" Población inicial : " + comienzo/(double)numIndividuos);
            System.out.println("Mejor individuo: " + poblacion.mejor.fitness);
        //Probando 
        
        Poblacion superPoblacion = evolucionar(poblacion,10000); // población y generaciones
        
         //Probando
            double finalizado=0;
            
            for (int i = 0; i < numIndividuos; i++) 
                finalizado+= superPoblacion.individuos.get(i).fitness;
            System.out.println(" Población final : " + finalizado/(double)numIndividuos);
            System.out.println("Mejor individuo: " + superPoblacion.mejor.fitness);
        //Probando */
    }
    
    // Este es el ciclo While. 
    public static Poblacion evolucionar(Poblacion poblacion, int generaciones){
        boolean fin = false;
        while(!fin){
            Poblacion padres = poblacion.seleccion("ss",poblacion.individuos.size()/4); // Existe también: Ranking, Ruleta, steadyState, Elitista y Torneo. (Trabajando en Stochastic universal sampling) 
            Poblacion hijos = padres.generarHijos("2puntos"); 
            Poblacion hijos2 = padres.generarHijos("1punto");
            Poblacion hijos3 = padres.generarHijos("promedio");
            Poblacion hijos4 = padres.generarHijos("2puntos");
            
            poblacion.individuos.addAll(hijos.individuos);
            poblacion.individuos.addAll(hijos2.individuos);
            poblacion.individuos.addAll(hijos3.individuos);
            poblacion.individuos.addAll(hijos4.individuos);
            
            poblacion = poblacion.seleccion("elitista",numIndividuos);
            
            generaciones--;
            if(generaciones <=0)fin=true; 
            // la idea es comprobar también otras condiciones, más que un número de iteraciones, e.g si los últimos 5 estados son los mismos, o muy cercanos
        }
        return poblacion;
    }
    
    /* La siguiente función está próxima a ser cambiada por lambda expresión de Java8 */
    
    // Función a modificar
    public static double fitness(double codigo[]){
        double temp = BenchmarkCEC2013_LSGO.f2(codigo);
        if(numF%100==0)System.out.println("Eval del Fitnes #: " + numF + " : " + temp);
        numF++;
        return temp;
    }   

}
