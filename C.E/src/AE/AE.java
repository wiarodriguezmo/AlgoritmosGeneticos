package AE;

import java.io.IOException;

public class AE {
    
    public static void main(String[] args) throws IOException{
        int numIndividuos = 100;
        int D=2;
        double frontera = 2.048;
        double probMutacion = 0.2;
        Poblacion poblacion = new Poblacion(numIndividuos,D,0.6,frontera,probMutacion); // #individuos, Dimensión, probCruce, frontera.
        
        //Probando
            double comienzo = 0.0;
            for (int i = 0; i < numIndividuos; i++) {
                comienzo +=  poblacion.individuos.get(i).fitness;
            }System.out.println(" Población inicial : " + comienzo/(double)numIndividuos);
        //Probando 
        
        Poblacion superPoblacion = evolucionar(poblacion,600); // población y generaciones
        
         //Probando
            double finalizado=0;
            for (int i = 0; i < numIndividuos; i++) {
                finalizado+= superPoblacion.individuos.get(i).fitness;
            }System.out.println(" Población final : " + finalizado/(double)numIndividuos);
            System.out.println("Mejor individuo: " + superPoblacion.mejor.fitness);
        //Probando */
    }
    
    // Este es el ciclo While. 
    public static Poblacion evolucionar(Poblacion poblacion, int generaciones){
        boolean fin = false;
        while(!fin){
            Poblacion padres = poblacion;//.seleccion("ss",poblacion.individuos.size()); // Existe también: Ranking, Ruleta, steadyState, Elitista y Torneo. (Trabajando en Stochastic universal sampling) 
            Poblacion hijos = padres.generarHijos("promedio"); // Para cruce existe: promedio, intercambio, suma/resta aleatoria de la mitad de la diferencia (suYre)
            
            poblacion.individuos.addAll(hijos.individuos);
            poblacion = poblacion.seleccion("ranking",hijos.individuos.size());
            
            generaciones--;
            if(generaciones <=0)fin=true; 
            // la idea es comprobar también otras condiciones, más que un número de iteraciones, e.g si los últimos 5 estados son los mismos, o muy cercanos
        }
        return poblacion;
    }
    
    /* La siguiente función está próxima a ser cambiada por lambda expresión de Java8 */
    
    // Función a modificar
    public static double fitness(double codigo[]){ 
        Funciones func = new Funciones();
        return -func.Rosenbrock(codigo);
    }   

}
