package AE;


public class AE {

    public static void main(String[] args) { 
        int comienzo = 0, finalizado=0;
        
        Poblacion poblacion = new Poblacion(100,10,0.6); // #individuos, largoCromosoma, probCruce.
        
        for (int i = 0; i < 100; i++) {
            comienzo +=  poblacion.individuos.get(i).fitness;
        }System.out.println(" Población inicial : " + comienzo);
        
        Poblacion superPoblacion = evolucionar(poblacion,100);
        
        for (int i = 0; i < 100; i++) {
            finalizado+= superPoblacion.individuos.get(i).fitness;
        }System.out.println(" Población final : " + finalizado);
    }
    
    // Este es el ciclo While. 
    public static Poblacion evolucionar(Poblacion poblacion, int generaciones){
        boolean fin = false;
        while(!fin){
            Poblacion padres = poblacion.seleccion("elitista",poblacion.individuos.size()); // Existe también: Ranking, Ruleta, Elitista y Elitista.
            Poblacion hijos = padres.generarHijos("1punto"); // Para cruce existe: 1punto, 2puntos, uniforme. (Trabajando en "Cut and splice" y "Genes Dominantes"). 
            
            poblacion.individuos.addAll(hijos.individuos);
            poblacion = poblacion.seleccion("elitista",padres.individuos.size());
            
            //falta
            generaciones--;
            if(generaciones <=0)fin=true; 
// la idea es comprobar también otras condiciones, más que un número de iteraciones, e.g si los últimos 5 estados son los mismos, o muy cercanos
        }
        return poblacion;
    }

   
}
