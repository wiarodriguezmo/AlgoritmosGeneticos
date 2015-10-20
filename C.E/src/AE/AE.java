package AE;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AE {
    static ArrayList<boolean[]> training = new ArrayList<>();
    
    public static void main(String[] args) throws IOException{
        int comienzo = 0, finalizado=0;
        generarConjEntrenam();
        
        Poblacion poblacion = new Poblacion(100,90,0.6); // #individuos, largoCromosoma, probCruce.
        for (int i = 0; i < 100; i++) {
            comienzo +=  poblacion.individuos.get(i).fitness;
        }System.out.println(" Población inicial : " + comienzo/100);
        
        Poblacion superPoblacion = evolucionar(poblacion,600); // población y generaciones
        Individuo mejor = new Individuo();
        mejor.fitness = 0;
        for (int i = 0; i < 100; i++) {
            if(mejor.fitness<superPoblacion.individuos.get(i).fitness)mejor=superPoblacion.individuos.get(i);
            finalizado+= superPoblacion.individuos.get(i).fitness;
        }System.out.println(" Población final : " + finalizado/100);
        System.out.println("Mejor individuo: " + mejor.fitness);
    }
    
    // Este es el ciclo While. 
    public static Poblacion evolucionar(Poblacion poblacion, int generaciones){
        boolean fin = false;
        while(!fin){
            Poblacion padres = poblacion;//.seleccion("ss",poblacion.individuos.size()); // Existe también: Ranking, Ruleta, steadyState, Elitista y Torneo.
            Poblacion hijos = padres.generarHijos("1punto"); // Para cruce existe: 1punto, 2puntos, uniforme. (Trabajando en "Cut and splice" y "Genes Dominantes"). 
            
            poblacion.individuos.addAll(hijos.individuos);
            poblacion = poblacion.seleccion("ranking",hijos.individuos.size());
            
            //falta
            generaciones--;
            if(generaciones <=0)fin=true; 
// la idea es comprobar también otras condiciones, más que un número de iteraciones, e.g si los últimos 5 estados son los mismos, o muy cercanos
        }
        return poblacion;
    }
    
    private static void generarConjEntrenam() throws FileNotFoundException, IOException{
        
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("/home/developer/CE/ComputacionEvolutiva/files/gecco-test-10.txt"));
            String line = br.readLine();
            line = br.readLine();
                   
            while (line != null) {
                String[] cromo = line.split("\\s+");
                boolean[] temp;
                temp = new boolean[Integer.parseInt(cromo[1])+1];
                temp[0] = cromo[0].equals("1");
                for (int i = 1; i < temp.length; i++) {
                    temp[i] =  cromo[i+1].equals("1"); // interesante condición
                }
                line = br.readLine();
                training.add(temp);
            }
        }finally {
            br.close();
        }
    }

   
}
