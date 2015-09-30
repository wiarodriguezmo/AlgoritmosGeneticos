package AE;

import java.util.ArrayList;
import java.util.List;

public class Poblacion {
    double cruce;
    ArrayList<Individuo> individuos = new ArrayList<>();
    
    public Poblacion(int poblacionInicial, int largoCromosoma, double cruce){
        for (int i = 0; i < poblacionInicial; i++){
            Individuo individuo = new Individuo(Individuo.generarCodAleatorio(largoCromosoma));
            individuos.add(individuo);
        }
        this.cruce = cruce;
    }
    
    public Poblacion(ArrayList<Individuo> individuos, double cruce){
        this.individuos = individuos;
        this.cruce = cruce;
    }
    

    
    public Poblacion seleccion(String metodo, int tamano){
        switch (metodo) {
            case "torneo": 
                return torneo(tamano);
            case "ranking":
                System.out.println("Ranking");
                return null;
            case "elistista":
                System.out.println("Elitista");
                return null;
            case "ruleta":
                System.out.println("Ruleta");
                return null;
            case "id":
                System.out.println("ID (implementando)");
                return null;
            default: System.out.println("Error...");
                return null;
        }
                
        // El objetoi Población que se devuelve debe ser completo, contener todos los parámetros de la población trabajada.
    }
    

    public void ruleta(){
    }
    
    // Asume que la 
    private Poblacion torneo(int tamano){
        ArrayList<Individuo> seleccionados = new ArrayList<>();
        for (int i = 0; i < tamano; i++) {
            //según n individuos de la población se hace n torneos para seleccionar n padres
            ArrayList<Individuo> muestreo = selAleatoria(16); // 16 es el número de muestreo para cada los n torneos, 16individuos participan cada torneo
            seleccionados.add(torneoRecursivo(muestreo.subList(0, muestreo.size())));
        }
        return new Poblacion(seleccionados, cruce);
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
        double prob1 = (double)uno.fitness/(double)(uno.fitness+dos.fitness);
        if(Math.random()< prob1)return uno;
        else return dos;
    }
    
    public ArrayList<Individuo> selAleatoria(int tamano){
        ArrayList<Individuo> muestra = new ArrayList<>();
        for(int i=0; i<tamano; i++){
            muestra.add(individuos.get((int) (Math.random()*individuos.size()))); //selecciona aleatoriamente un individuo de la población
        }
        return muestra;
    }
    
    public Poblacion generarHijos(String tipoCruce){
        ArrayList<Individuo> hijos = new ArrayList();
        for (int i = 0; i < individuos.size(); i+=2) {
            Individuo uno = individuos.get(i);
            Individuo dos = individuos.get(i+1);
            if(Math.random()<cruce){
                Individuo[] resulCruce = Individuo.cruzar(tipoCruce, uno,dos);
                uno = resulCruce[0];
                dos = resulCruce[1];
            }
            uno.mutar();
            hijos.add(uno);
            dos.mutar();
            hijos.add(dos);
        }
        return new Poblacion(hijos, cruce);
    }

}