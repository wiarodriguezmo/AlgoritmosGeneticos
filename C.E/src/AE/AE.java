package AE;


public class AE {

    public static void main(String[] args) {
        Poblacion poblacion = new Poblacion(100,10,0.6); // #individuos, cromosoma, cruce.
        Poblacion superPoblacion = evolucionar(poblacion,100);
    }
    
    // Este es el ciclo While. 
    public static Poblacion evolucionar(Poblacion poblacion, int generaciones){
        boolean fin = false;
        while(!fin){
            Poblacion padres = poblacion.seleccion("torneo"); // Existe también: Ranking, Ruleta, Elitista y Elitista.
            Poblacion hijos = padres.generarHijos();
            
            poblacion.individuos.addAll(hijos.individuos);
            poblacion = poblacion.seleccion("torneo");
            
            //falta
            generaciones--;
            if(generaciones <=0)fin=true; 
// la idea es comprobar también otras condiciones, más que un número de iteraciones, e.g si los últimos 5 estados son los mismos, o muy cercanos
        }
    }

    
    
}
