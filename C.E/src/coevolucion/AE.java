package coevolucion;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class AE {
    private static int numF = 0;
    static int numIndividuos = 20;    
    static ArrayList<double[]> training = new ArrayList<>();
    
    public static void main(String[] args) throws IOException, CloneNotSupportedException{
        double frontera = generarConjEntrenam();
        int D=39;
        double probMutacion = 10.0/(double)D;
        Poblacion[] poblacion = new Poblacion[3];
        
        poblacion[0] = new Poblacion(numIndividuos,D,0.6,frontera,probMutacion,1); // #individuos, Dimensión, probCruce, frontera, Mutacion, clase a identificar.
        poblacion[1] = new Poblacion(numIndividuos,D,0.6,frontera,probMutacion,2);
        poblacion[2] = new Poblacion(numIndividuos,D,0.6,frontera,probMutacion,3);
        
        //Probando
            double comienzo=0.0, comienzo2=0.0, comienzo3=0.0;
            for (int i = 0; i < numIndividuos; i++){
                comienzo +=  poblacion[0].individuos.get(i).fitness; 
                comienzo2 +=  poblacion[1].individuos.get(i).fitness; 
                comienzo3 +=  poblacion[2].individuos.get(i).fitness; 
            }
        //Probando 
        
        Poblacion[] superPoblacion = evolucionar(poblacion,30000); // población y generaciones
        
         //Probando
            double finalizado=0.0, finalizado2=0.0, finalizado3=0.0;
            for (int i = 0; i < numIndividuos; i++){
                finalizado +=  superPoblacion[0].individuos.get(i).fitness; 
                finalizado2 +=  superPoblacion[1].individuos.get(i).fitness; 
                finalizado3 +=  superPoblacion[2].individuos.get(i).fitness; 
            }
            System.out.println(" Población inicial 1: " + comienzo/(double)numIndividuos + ", 2: " + comienzo2/(double)numIndividuos  + " y 3: " + comienzo3/(double)numIndividuos );
            System.out.println("Mejor individuo: " + poblacion[0].mejor.fitness + " 2:" + poblacion[1].mejor.fitness + " 3:" + poblacion[2].mejor.fitness);
        
            System.out.println(" Población final 1: " + finalizado/(double)numIndividuos + ", 2: " + finalizado2/(double)numIndividuos  + " y 3: " + finalizado3/(double)numIndividuos );
            System.out.println("Mejor individuo: " + superPoblacion[0].mejor.fitness + " 2:" + superPoblacion[1].mejor.fitness + " 3:" + superPoblacion[2].mejor.fitness);
        //Probando 

    }
    
    // Este es el ciclo While. 
    public static Poblacion[] evolucionar(Poblacion[] poblacion, int generaciones) throws CloneNotSupportedException{
        boolean fin = false;
        Poblacion[] padres = new Poblacion[3];
        Poblacion[] hijos = new Poblacion[3];
        while(!fin){
            //padres[0] = poblacion[0].seleccion("ss",poblacion[0].individuos.size()); // Existe también: Ranking, Ruleta, steadyState, Elitista y Torneo. (Trabajando en Stochastic universal sampling) 
            hijos[0] = poblacion[0].generarHijos("2puntos",poblacion[1],poblacion[2]);
            poblacion[0].seleccionSS(hijos[0]);
                    
            hijos[1] = poblacion[1].generarHijos("2puntos",poblacion[0],poblacion[2]);
            poblacion[1].individuos.addAll(hijos[1].individuos);
            poblacion[1] = poblacion[1].seleccion("elitista",numIndividuos);
            
            System.out.println("Individuo uno, Fitness: " + poblacion[0].individuos.get(0).fitness + " Cod: " + Arrays.toString(poblacion[0].individuos.get(0).codigo));
//            
//            hijos[2] = poblacion[2].generarHijos("2puntos");
//            poblacion[2].individuos.addAll(hijos[0].individuos);
//            poblacion[2] = poblacion[2].seleccion("elitista",numIndividuos);
            
            generaciones--;
            if(generaciones <=0)fin=true; 
            // la idea es comprobar también otras condiciones, más que un número de iteraciones, e.g si los últimos 5 estados son los mismos, o muy cercanos
        }
        return poblacion;
    }
    
    /* La siguiente función está próxima a ser cambiada por lambda expresión de Java8 */
    
    // Función a modificar
    public static int fitness(double codigo[], int clase){
        int fitness = 0;
        for(Iterator<double[]> iterator = training.iterator(); iterator.hasNext();) {
            double[] next = iterator.next();
            
            boolean clasifica = true;
            for (int i = 0; i < next.length-1; i++) {
                if(codigo[i*3]<0.0)
                    if(!((codigo[i*3+1]<=next[i] && codigo[i*3+2]>next[i]) || (codigo[i*3+1]>next[i] && codigo[i*3+2]<=next[i]))){
                        clasifica = false;
                        break;
                    }
            }
            if(clasifica)if((int)next[13]==clase)fitness--;
            else if((int)next[13]!=clase)fitness--;
        }
            
        if(numF%1000==0)System.out.println("Eval del Fitnes #: " + numF + " : " + fitness);
        numF++;
        return fitness;
    }   
    
    public static int fitnessConjunto(double codigo[], int clase, double[] codigoB, double[] codigoC){
        int fitness = 0;
        for(Iterator<double[]> iterator = training.iterator(); iterator.hasNext();) {
            double[] next = iterator.next();
            
            switch (clase){
                case 1:
                    if((int)next[13]==1){
                        if(uno(codigo,next)&&!(dos(codigoB,next)||tres(codigoC,next)))fitness--;
                    }else if((int)next[13]==2){
                        if(dos(codigoB,next)&&!(uno(codigo,next)||tres(codigoC,next)))fitness--;
                    }else if(tres(codigoC,next)&&!(uno(codigo,next)||dos(codigoB,next)))fitness--;
                    break;
                case 2:
                    if((int)next[13]==1){
                        if(uno(codigoB,next)&&!(dos(codigo,next)||tres(codigoC,next)))fitness--;
                    }else if((int)next[13]==2){
                        if(dos(codigo,next)&&!(uno(codigoB,next)||tres(codigoC,next)))fitness--;
                    }else if(tres(codigoC,next)&&!(uno(codigoB,next)||dos(codigo,next)))fitness--;
                    break;
                default: 
                    if((int)next[13]==1){
                        if(uno(codigoB,next)&&!(dos(codigoC,next)||tres(codigo,next)))fitness--;
                    }else if((int)next[13]==2){
                        if(dos(codigoC,next)&&!(uno(codigoB,next)||tres(codigo,next)))fitness--;
                    }else if(tres(codigo,next)&&!(uno(codigoB,next)||dos(codigoC,next)))fitness--;
                    break;
            }
        }
            
        if(numF%1000==0)System.out.println("mejor Fitnes #: " + numF + " : " + fitness);
        numF++;
        return fitness;
    }   
    
    private static boolean uno(double[] codigo, double[] next) {
        for (int i = 0; i < next.length-1; i++) {
            if(codigo[i*3]<0.0)
                if(!((codigo[i*3+1]<=next[i] && codigo[i*3+2]>next[i]) || (codigo[i*3+1]>next[i] && codigo[i*3+2]<=next[i])))return false;
        }return true;
    }

    private static boolean dos(double[] codigo, double[] next) {
        for (int i = 0; i < next.length-1; i++) {
            if(codigo[i*3]<0.0)
                if(!((codigo[i*3+1]<=next[i] && codigo[i*3+2]>next[i]) || (codigo[i*3+1]>next[i] && codigo[i*3+2]<=next[i])))return false;
        }return true;
    }

    private static boolean tres(double[] codigo, double[] next) {
        for (int i = 0; i < next.length-1; i++) {
            if(codigo[i*3]<0.0)
                if(!((codigo[i*3+1]<=next[i] && codigo[i*3+2]>next[i]) || (codigo[i*3+1]>next[i] && codigo[i*3+2]<=next[i])))return false;
        }return true;
    }
    
    private static double generarConjEntrenam() throws FileNotFoundException, IOException{
        double frontera=0.0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("/home/developer/CE/ComputacionEvolutiva/files/wine.data"));
            String line = br.readLine();
                   
            while (line != null) {
                String[] cromo = line.split(",");
                double[] temp;
                temp = new double[14];
                for (int i = 0; i < temp.length-1; i++)
                    temp[i] =  Double.valueOf(cromo[i+1]); 
                temp[13] = Double.valueOf(cromo[0]); 
                line = br.readLine();
                training.add(temp);
            }
        }finally {
            br.close();
        }
        double promedio[] = new double[13];
        double desvE[] = new double[13];
        double suma[] = new double[13];
        for (Iterator<double[]> iterator = training.iterator(); iterator.hasNext();) {
            double[] next = iterator.next();
            for (int i = 0; i < promedio.length; i++){
                suma[i] += next[i]*next[i];
                promedio[i] += next[i];
            }
        }
        for(int i = 0; i < promedio.length; i++){
            promedio[i] = promedio[i]/(double)training.size();
            desvE[i] = Math.sqrt(suma[i]/(double)training.size() - promedio[i]*promedio[i]);
        }
        for(Iterator<double[]> iterator = training.iterator(); iterator.hasNext();) {
            double[] next = iterator.next();
            for(int i = 0; i < promedio.length; i++){
                next[i] = (next[i] - promedio[i])/desvE[i];
                if(frontera<Math.abs(next[i]))frontera=Math.abs(next[i]);
            }
        }
        return frontera;
    }


}
