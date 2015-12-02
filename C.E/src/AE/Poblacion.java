package AE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Iterator;

public class Poblacion {
    double cruce;
    ArrayList<Individuo> individuos = new ArrayList<>();
    Individuo mejor;
    double probMutacion;
    
    public Poblacion(int poblacionInicial, int dim, double cruce, double frontera, double probMutacion){
        for (int i = 0; i < poblacionInicial; i++){
            Individuo individuo = new Individuo(Individuo.generarCodAleatorio(dim,frontera));
            individuos.add(individuo);
        }
        this.cruce = cruce;
        this.probMutacion = probMutacion;
        mejor = mejorFitness();
    }
    
    public Poblacion(ArrayList<Individuo> individuos, double cruce, double probMutacion){
        this.individuos = individuos;
        this.cruce = cruce;
        this.probMutacion = probMutacion;
        mejor = mejorFitness();
    }
    
    private Individuo mejorFitness(){
        Individuo temp = new Individuo();
        temp.fitness= Double.POSITIVE_INFINITY; // esto es relativo y sujeto a cambios
        for (Individuo individuo : individuos)
                if(individuo.fitness<temp.fitness)temp=individuo; // esto es relativo y sujeto a cambios 
            
        return temp;
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
    private Poblacion ranking(int tamano){ // http://www.geatbx.com/docu/algindex-02.html
        ArrayList<Individuo> seleccionados1;
        
        ArrayList<Individuo> seleccionados = new ArrayList<Individuo>();
        while(tamano>0) {  //según n (tamaño) padres a seleccionar de la población se hace n lanzamientos de la ruleta.
            seleccionados1 = selAleatoria(10);
        
            Collections.sort(seleccionados1, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        Individuo i1 = (Individuo) o1;
                        Individuo i2 = (Individuo) o2;
                        return new Double(i1.fitness).compareTo(new Double(i2.fitness));
                    }
                });
            
            int sel = (int) (Math.random()*seleccionados1.size());
            if(Math.random()< 1.0 / (double)Math.pow(2, sel)){
                seleccionados.add(seleccionados1.get(sel));
                tamano--;
            }
        }
        return new Poblacion(seleccionados, cruce,probMutacion);
    }
    
    private Poblacion elitista(int tamano){
        ArrayList<Individuo> seleccionados1 = steadyState((int)(tamano/3.0)).individuos;
        ArrayList<Individuo> seleccionados = ruleta(tamano-(int)(tamano/3.0)).individuos;
        seleccionados.addAll(seleccionados1);
        return new Poblacion(seleccionados, cruce,probMutacion);
    }
    
    private Poblacion steadyState(int tamano){
        ArrayList<Individuo> seleccionados1 = individuos;
        
        Collections.sort(seleccionados1, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    Individuo i1 = (Individuo) o1;
                    Individuo i2 = (Individuo) o2;
                    return new Double(i1.fitness).compareTo(new Double(i2.fitness));
                }
            });
        ArrayList<Individuo> seleccionados = new ArrayList<Individuo>();
        for (Iterator<Individuo> it = seleccionados1.iterator(); it.hasNext() && tamano>0; tamano--) {
            Individuo next = it.next();
            seleccionados.add(next);
        }
        return new Poblacion(seleccionados, cruce,probMutacion);
    }
    
    // este método emplea el algoritmo O(1) de estocástica aceptancia [1] de Adam Lipowski, Dorota Lipowska
    // [1] https://en.wikipedia.org/wiki/Fitness_proportionate_selection#Java_-_stochastic_acceptance_O.281.29_version & http://arxiv.org/abs/1109.3627
    private Poblacion ruleta(int tamano){
        ArrayList<Individuo> seleccionados = new ArrayList<>();
        while(tamano>0) {  //según n (tamaño) padres a seleccionar de la población se hace n lanzamientos de la ruleta.
            Individuo individuo = individuos.get((int) (Math.random()*individuos.size()));
            if(Math.random()< (individuo.fitness) / mejor.fitness){
                seleccionados.add(individuo);
                tamano--;
            }
        }
        return new Poblacion(seleccionados, cruce,probMutacion);
    }
    
    private Poblacion torneo(int tamano){
        ArrayList<Individuo> seleccionados = new ArrayList<>();
        for (int i = 0; i < tamano; i++) {
            //según n(tamaño) individuos de la población se hace n torneos para seleccionar n padres
            ArrayList<Individuo> muestreo = selAleatoria(8); // 16 es el número de muestreo para cada los n torneos, 16individuos participan cada torneo
            seleccionados.add(torneoRecursivo(muestreo.subList(0, muestreo.size())));
        }
        return new Poblacion(seleccionados, cruce,probMutacion);
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
        double prob1 = uno.fitness/(uno.fitness+dos.fitness);
        if(Math.random()< prob1)return dos;
        else return uno;
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
            uno.mutar(probMutacion);
            hijos.add(uno);
            dos.mutar(probMutacion);
            hijos.add(dos);
        }
        return new Poblacion(hijos, cruce,probMutacion);
    }

}