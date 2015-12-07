package coevolucion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Iterator;

public class Poblacion implements Cloneable{
    double cruce;
    ArrayList<Individuo> individuos = new ArrayList<>();
    Individuo mejor;
    double probMutacion;
    int clase;
    
    public Poblacion(int poblacionInicial, int dim, double cruce, double frontera, double probMutacion, int clase) throws CloneNotSupportedException{
        for (int i = 0; i < poblacionInicial; i++){
            Individuo individuo = new Individuo(Individuo.generarCodAleatorio(dim,frontera),clase);
            individuos.add(individuo);
            this.clase = clase;
        }
        this.cruce = cruce;
        this.probMutacion = probMutacion;
        mejor = mejorFitness();
    }
    
    public Poblacion(ArrayList<Individuo> individuos, double cruce, double probMutacion, int clase) throws CloneNotSupportedException{
        this.individuos = individuos;
        this.cruce = cruce;
        this.clase = clase;
        this.probMutacion = probMutacion;
        mejor = mejorFitness();
    }
    
    private Individuo mejorFitness() throws CloneNotSupportedException{
        Individuo temp = new Individuo();
        temp.fitness= Double.POSITIVE_INFINITY; // esto es relativo y sujeto a cambios
        for (Individuo individuo : individuos)
                if(individuo.fitness<temp.fitness)temp=(Individuo) individuo.clone(); // esto es relativo y sujeto a cambios 
            
        return temp;
    }
    
    public Poblacion seleccion(String metodo, int tamano) throws CloneNotSupportedException{
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
    private Poblacion ranking(int tamano) throws CloneNotSupportedException{ // http://www.geatbx.com/docu/algindex-02.html
        ArrayList<Individuo> muestraAleatoria;
        
        ArrayList<Individuo> seleccionados = new ArrayList<>();
        while(tamano>0) {  //según n (tamaño) padres a seleccionar de la población se hace n lanzamientos de la ruleta.
            muestraAleatoria = selAleatoria(10);
        
            Collections.sort(muestraAleatoria, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        Individuo i1 = (Individuo) o1;
                        Individuo i2 = (Individuo) o2;
                        return new Double(i1.fitness).compareTo(i2.fitness);
                    }
                });
            
            int sel = (int) (Math.random()*muestraAleatoria.size());
            if(Math.random()< 1.0 / (double)Math.pow(2, sel)){
                seleccionados.add(muestraAleatoria.get(sel));
                tamano--;
            }
        }
        return new Poblacion(seleccionados, cruce,probMutacion,clase);
    }
    
    private Poblacion elitista(int tamano) throws CloneNotSupportedException{
        ArrayList<Individuo> seleccionados1 = steadyState((int)(tamano/3.0)).individuos;
        ArrayList<Individuo> seleccionados = selAleatoria(tamano-(int)(tamano/3.0));
        seleccionados.addAll(seleccionados1);
        return new Poblacion(seleccionados, cruce,probMutacion,clase);
    }
    
    private Poblacion steadyState(int tamano) throws CloneNotSupportedException{
        ArrayList<Individuo> seleccionados1 = cloneList(individuos);
        
        Collections.sort(seleccionados1, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    Individuo i1 = (Individuo) o1;
                    Individuo i2 = (Individuo) o2;
                    return new Double(i1.fitness).compareTo(i2.fitness);
                }
            });
        ArrayList<Individuo> seleccionados = new ArrayList<>();
        for (Iterator<Individuo> it = seleccionados1.iterator(); it.hasNext() && tamano>0; tamano--) {
            Individuo next = it.next();
            seleccionados.add(next);
        }
        return new Poblacion(seleccionados, cruce,probMutacion,clase);
    }
    
    // este método emplea el algoritmo O(1) de estocástica aceptancia [1] de Adam Lipowski, Dorota Lipowska
    // [1] https://en.wikipedia.org/wiki/Fitness_proportionate_selection#Java_-_stochastic_acceptance_O.281.29_version & http://arxiv.org/abs/1109.3627
    private Poblacion ruleta(int tamano) throws CloneNotSupportedException{
        ArrayList<Individuo> seleccionados = new ArrayList<>();
        while(tamano>0) {  //según n (tamaño) padres a seleccionar de la población se hace n lanzamientos de la ruleta.
            Individuo individuo = (Individuo) individuos.get((int) (Math.random()*individuos.size())).clone();
            if(Math.random()< (individuo.fitness) / mejor.fitness){
                seleccionados.add(individuo);
                tamano--;
            }
        }
        return new Poblacion(seleccionados, cruce,probMutacion,clase);
    }
    
    private Poblacion torneo(int tamano) throws CloneNotSupportedException{
        ArrayList<Individuo> seleccionados = new ArrayList<>();
        for (int i = 0; i < tamano; i++) {
            //según n(tamaño) individuos de la población se hace n torneos para seleccionar n padres
            ArrayList<Individuo> muestreo = selAleatoria(8); // 16 es el número de muestreo para cada los n torneos, 16individuos participan cada torneo
            seleccionados.add(torneoRecursivo(muestreo.subList(0, muestreo.size())));
        }
        return new Poblacion(seleccionados, cruce,probMutacion,clase);
    }
    
    private Individuo torneoRecursivo(List<Individuo> muestra) throws CloneNotSupportedException{
        Individuo uno, dos;
        if(muestra.size()>2){
            uno = torneoRecursivo(muestra.subList(0, muestra.size()/2));
            dos = torneoRecursivo(muestra.subList(muestra.size()/2, muestra.size()));
        }else {
            uno = (Individuo) muestra.get(0).clone();
            dos = (Individuo) muestra.get(1).clone();
        }
        double prob1 = uno.fitness/(uno.fitness+dos.fitness);
        if(Math.random()< prob1)return dos;
        else return uno;
    }
    
    public ArrayList<Individuo> selAleatoria(int tamano) throws CloneNotSupportedException{
        ArrayList<Individuo> muestra = new ArrayList<>();
        for(int i=0; i<tamano; i++){
            muestra.add((Individuo) individuos.get((int) (Math.random()*individuos.size())).clone()); //selecciona aleatoriamente un individuo de la población
        }
        return muestra;
    }
    
    public Poblacion generarHijos(String tipoCruce,Poblacion poblacionB,Poblacion poblacionC) throws CloneNotSupportedException{
        ArrayList<Individuo> hijos = new ArrayList();
        for (int i = 0; i < individuos.size(); i+=2) {
            Individuo uno = (Individuo) individuos.get(i).clone();
            Individuo dos = (Individuo) individuos.get(i+1).clone();
            if(Math.random()<cruce){
                Individuo[] resulCruce = Individuo.cruzar(tipoCruce, uno,dos);
                uno = resulCruce[0];
                dos = resulCruce[1];
            }
            uno.mutar(probMutacion,clase);
            dos.mutar(probMutacion,clase);
            
            uno.fitness = AE.fitnessConjunto(uno.codigo, clase, poblacionB.individuos.get(uno.amigos[0]).codigo, poblacionC.individuos.get(uno.amigos[1]).codigo);
            dos.fitness = AE.fitnessConjunto(dos.codigo, clase, individuos.get(dos.amigos[0]).codigo, individuos.get(dos.amigos[1]).codigo);
            hijos.add(uno);
            hijos.add(dos);
        }
        return new Poblacion(hijos, cruce,probMutacion,clase);
    }

    public void seleccionSS(Poblacion hijos) throws CloneNotSupportedException{
        for (int i = 0; i < individuos.size(); i+=2) {
            if(individuos.get(i).distancia(hijos.individuos.get(i))<individuos.get(i+1).distancia(hijos.individuos.get(i))){
                if(individuos.get(i).fitness>=hijos.individuos.get(i).fitness)individuos.set(i, hijos.individuos.get(i));
                if(individuos.get(i+1).fitness>=hijos.individuos.get(i+1).fitness)individuos.set(i+1, hijos.individuos.get(i+1));
            }
            else {
                if(individuos.get(i).fitness>=hijos.individuos.get(i+1).fitness)individuos.set(i, hijos.individuos.get(i+1));
                if(individuos.get(i+1).fitness>=hijos.individuos.get(i).fitness)individuos.set(i+1, hijos.individuos.get(i));                
            }
        }
        mejor = mejorFitness();
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Poblacion cloned = (Poblacion)super.clone();
        cloned.individuos = cloneList(individuos);
        cloned.mejor = (Individuo) mejor.clone();
        return cloned;
    }
    
    public static ArrayList<Individuo> cloneList(ArrayList<Individuo> indList) throws CloneNotSupportedException {
    ArrayList<Individuo> listaInd = new ArrayList<>();
    for (Individuo ind : indList) {
        listaInd.add((Individuo)ind.clone());
    }
    return listaInd;
}

}