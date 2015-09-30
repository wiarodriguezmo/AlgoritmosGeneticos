package AE;

import java.util.ArrayList;
import java.util.List;

public class Poblacion {
    double mutacion, cruce;
    ArrayList<Individuo> individuos = new ArrayList<>();
    
    public Poblacion(int poblacionInicial, int largoCromosoma, double cruce){
        for (int i = 0; i < poblacionInicial; i++){
            Individuo individuo = new Individuo(Individuo.generarCodAleatorio(largoCromosoma));
            individuos.add(individuo);
        }
        this.cruce = cruce;
        mutacion = 1/largoCromosoma;
    }
    
    public Poblacion(ArrayList<Individuo> individuos, double mutacion, double cruce){
        this.individuos = individuos;
        this.mutacion = mutacion;
        this.cruce = cruce;
    }
    

    
    public Poblacion seleccion(String metodo){
        switch (metodo) {
            case "torneo": 
                System.out.println("Torneo");
                return torneo();
            case "ranking":
                System.out.println("Ranking");
                break;
            case "elistista":
                System.out.println("Elitista");
                break;
            case "ruleta":
                System.out.println("Ruleta");
                break;
            case "id":
                System.out.println("ID (implementando)");
                break;
            default: System.out.println("Error...");
                break;
        }
                
        // El objetoi Población que se devuelve debe ser completo, contener todos los parámetros de la población trabajada.
    }
    

    public void ruleta(){
    }
    
    // Asume que la 
    private Poblacion torneo(){
        ArrayList<Individuo> seleccionados = new ArrayList<>();
        for (int i = 0; i < individuos.size(); i++) { //según n individuos de la población se hace n torneos para seleccionar n padres
            ArrayList<Individuo> muestreo = selAleatoria(16); // 16 es el número de muestreo para cada los n torneos, 16individuos participan cada torneo
            seleccionados.add(torneoRecursivo(muestreo.subList(0, muestreo.size()-1)));
        }
        return new Poblacion(seleccionados, mutacion, cruce);
    }
    
    private Individuo torneoRecursivo(List<Individuo> muestra){
        Individuo uno, dos;
        if(muestra.size()>2){
            uno = torneoRecursivo(muestra.subList(0, muestra.size()/2));
            dos = torneoRecursivo(muestra.subList(muestra.size()/2, muestra.size()));
        }else {
            uno = muestra.get(0);
            dos = muestra.get(1);
        }
        int diff = Math.abs(uno.fitness  - dos.fitness);
        if(Math.random()< (uno.fitness/(uno.fitness+dos.fitness)))return uno;
        else return dos;
    }
    
    public ArrayList<Individuo> selAleatoria(int tamano){
        ArrayList<Individuo> muestra = new ArrayList<>();
        for(int i=0; i<tamano; i++){
            muestra.add(individuos.get((int) (Math.random()*individuos.size()))); //selecciona aleatoriamente un individuo de la población
        }
        return muestra;
    }
    
    public Poblacion generarHijos(){
        for (int i = 0; i < individuos.size(); i+=2) {
            Individuo uno = individuos.get(i);
            Individuo dos = individuos.get(i+1);
            if(Math.random()<cruce){
                Individuo[] resulCruce = cruzar(uno,dos);
                uno = resulCruce[0];
                dos = resulCruce[1];
            }
            uno = mutar(uno);
            dos = mutar(dos);
            
        }
    }
}