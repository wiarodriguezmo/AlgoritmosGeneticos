package AE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Iterator;

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
            case "elitista":
                return elitista(tamano);
            case "ruleta":
                return ruleta(tamano);
            case "id":
                System.out.println("ID (implementando)");
                return null;
            default: System.out.println("Error...");
                return null;
        }
                
        // El objetoi Población que se devuelve debe ser completo, contener todos los parámetros de la población trabajada.
    }
    
    private Poblacion elitista(int tamano){
        ArrayList<Individuo> seleccionados1 = individuos;
        
        Collections.sort(seleccionados1, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    Individuo i1 = (Individuo) o1;
                    Individuo i2 = (Individuo) o2;
                    return new Integer(i2.fitness).compareTo(new Integer(i1.fitness));
                }
            });
        ArrayList<Individuo> seleccionados = new ArrayList<Individuo>();
        for (Iterator<Individuo> it = individuos.iterator(); it.hasNext() && tamano>0; tamano--) {
            Individuo next = it.next();
            seleccionados.add(next);
        }
        return new Poblacion(seleccionados, cruce);
    }
    
    // este método emplea el algoritmo O(1) de estocástica aceptancia [1]
    // [1] https://en.wikipedia.org/wiki/Fitness_proportionate_selection#Java_-_stochastic_acceptance_O.281.29_version & http://arxiv.org/abs/1109.3627
    private Poblacion ruleta(int tamano){
        ArrayList<Individuo> seleccionados = new ArrayList<>();
        while(tamano>0) {  //según n (tamaño) padres a seleccionar de la población se hace n lanzamientos de la ruleta.
            Individuo individuo = individuos.get((int) (Math.random()*individuos.size()));
            if(Math.random()< ((double)individuo.fitness) / (double)individuo.mejorFitness()){
                seleccionados.add(individuo);
                tamano--;
            }
        }
        return new Poblacion(seleccionados, cruce);
    }
    
    // Asume que la 
    private Poblacion torneo(int tamano){
        ArrayList<Individuo> seleccionados = new ArrayList<>();
        for (int i = 0; i < tamano; i++) {
            //según n(tamaño) individuos de la población se hace n torneos para seleccionar n padres
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