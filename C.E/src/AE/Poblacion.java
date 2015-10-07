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
                return ranking(tamano);
            case "elitista":
                return elitista(tamano);
            case "ss":
                return steadyState(tamano);
            case "ruleta":
                return ruleta(tamano);
            default: System.out.println("Error...");
                return null;
        }
    }
    
    
    // Ordena los elementos y luego selecciona uno aleatorio, si pasa X~N(0,1) < C.X^(-r), 
    // que es lo mismo que Math.random()< 1.0 / Math.pow(2, sel)  | siendo C=1, X=2 y r=sel. (sel es el ranking).
    private Poblacion ranking(int tamano){ // http://www.opttek.com/documentation/engine/WebHelp/SimulationOptimization_Ranking_and_selection_algorithm.htm
        ArrayList<Individuo> seleccionados1 = individuos;
        
        Collections.sort(seleccionados1, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    Individuo i1 = (Individuo) o1;
                    Individuo i2 = (Individuo) o2;
                    return new Double(i2.fitness).compareTo(new Double(i1.fitness));
                }
            });
        
        ArrayList<Individuo> seleccionados = new ArrayList<Individuo>();
        while(tamano>0) {  //según n (tamaño) padres a seleccionar de la población se hace n lanzamientos de la ruleta.
            int sel = (int) (Math.random()*individuos.size());
            Individuo individuo = individuos.get(sel);
            if(Math.random()< 1.0 / (double)(double)Math.pow(2, sel)){
                seleccionados.add(individuo);
                tamano--;
            }
        }
        return new Poblacion(seleccionados, cruce);
    }
    
    private Poblacion elitista(int tamano){
        ArrayList<Individuo> seleccionados1 = steadyState((int)(tamano/3.0)).individuos;
        ArrayList<Individuo> seleccionados = ruleta(tamano-(int)(tamano/3.0)).individuos;
        seleccionados.addAll(seleccionados1);
        return new Poblacion(seleccionados, cruce);
    }
    
    private Poblacion steadyState(int tamano){
        ArrayList<Individuo> seleccionados1 = individuos;
        
        Collections.sort(seleccionados1, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    Individuo i1 = (Individuo) o1;
                    Individuo i2 = (Individuo) o2;
                    return new Double(i2.fitness).compareTo(new Double(i1.fitness));
                }
            });
        ArrayList<Individuo> seleccionados = new ArrayList<Individuo>();
        for (Iterator<Individuo> it = seleccionados1.iterator(); it.hasNext() && tamano>0; tamano--) {
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
            if(Math.random()< ((double)individuo.fitness) / mejorFitness()){
                seleccionados.add(individuo);
                tamano--;
            }
        }
        return new Poblacion(seleccionados, cruce);
    }
    
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
    
    // Eavluar cual es el mejor fitness de acuerdo a la poblacion
    public double mejorFitness(){
        double mejorFit = -1;
        for(Individuo each : individuos){
            if(mejorFit < (double)each.fitness)
                mejorFit = (double) each.fitness;
        }
        return mejorFit;
    }
}