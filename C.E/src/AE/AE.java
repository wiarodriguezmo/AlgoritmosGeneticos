package AE;

import java.io.IOException;

public class AE {
    
    public static void main(String[] args) throws IOException{
        int numIndividuos = 100;
        
        Poblacion poblacion = new Poblacion(numIndividuos,90,0.6); // #individuos, largoCromosoma, probCruce.
        
        //Probando
            int comienzo = 0;
            for (int i = 0; i < numIndividuos; i++) {
                comienzo +=  poblacion.individuos.get(i).fitness;
            }System.out.println(" Población inicial : " + comienzo/numIndividuos);
        //Probando
        
        Poblacion superPoblacion = evolucionar(poblacion,600); // población y generaciones
        
        //Probando
            int finalizado=0;
            Individuo mejor = new Individuo();
            mejor.fitness = 0;
            for (int i = 0; i < numIndividuos; i++) {
                if(mejor.fitness<superPoblacion.individuos.get(i).fitness)mejor=superPoblacion.individuos.get(i);
                finalizado+= superPoblacion.individuos.get(i).fitness;
            }System.out.println(" Población final : " + finalizado/numIndividuos);
            System.out.println("Mejor individuo: " + mejor.fitness);
        //Probando
    }
    
    // Este es el ciclo While. 
    public static Poblacion evolucionar(Poblacion poblacion, int generaciones){
        boolean fin = false;
        while(!fin){
            Poblacion padres = poblacion;//.seleccion("ss",poblacion.individuos.size()); // Existe también: Ranking, Ruleta, steadyState, Elitista y Torneo. (Trabajando en Stochastic universal sampling) 
            Poblacion hijos = padres.generarHijos("1punto"); // Para cruce existe: 1punto, 2puntos, uniforme. (Trabajando en "Cut and splice" y "Genes Dominantes"). 
            
            poblacion.individuos.addAll(hijos.individuos);
            poblacion = poblacion.seleccion("ranking",hijos.individuos.size());
            
            generaciones--;
            if(generaciones <=0)fin=true; 
            // la idea es comprobar también otras condiciones, más que un número de iteraciones, e.g si los últimos 5 estados son los mismos, o muy cercanos
        }
        return poblacion;
    }
    
    /* Las siguientes funciones están próximas a ser cambiadas por lambda expresión de Java8 */
    
    // Función a modificar
    public static int fitness(){ 
        return 0;
    }   
    
    // También a modificar según el caso
    public static double mejorFitness(){ //AFD
        return 1000;
    }
}
