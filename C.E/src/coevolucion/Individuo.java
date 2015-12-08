package coevolucion;

public class Individuo implements Cloneable{
    double fitness;
    double codigo[];
    int amigos[]=new int[2];
    
    public Individuo(){
    }
    
    public Individuo(double[] codigo, int clase){
        this.codigo = codigo;
        fitness = AE.fitness(this.codigo,clase);
        amigos[0] = (int)Math.floor(Math.random()*20);
        amigos[1] = (int)Math.floor(Math.random()*20);
    }
    
    public Individuo(double[] codigo, double f, int amigos[]){
        this.codigo = codigo;
        fitness = f;
        this.amigos = amigos;
    }
    
    public static double[] generarCodAleatorio(int dim, double frontera, int clase){
        double[][] pob = {{2.4910092347734514, 1.6053763784788853, 1.1739165276980135, 1.9010951749083835, -0.9214058618997152, -3.7950272053946055, 
            -6.4631789943809395, -2.9646001965165603, -0.229380389336856, -2.2809842188576988, 2.886653659974411, -7.068707667584363, -2.0742138152853826, 
            4.333471409474601, 1.584155825427095, 3.543812965662882, -1.6206892494590548, 1.86895852593112, 1.1568549583496464, 2.221217169440024, 
            2.2001687117976783, -0.2707667924846584, 0.6847918484097066, 0.6790369372220981, 3.9868118660884027, 0.67181756589821, -1.9147342919838062, 
            -0.49490577338719577, -1.9463929316941786, 0.06309413258445908, 3.7936779465266977, 2.1103553906698416, -4.864490188982441, 
            -1.3435784124639638, 0.6986366459254514, 3.05339093516952, 3.899337954661947, -2.9092294693907608, -0.6373881765042975},
            {4.183648459509488, 0.07114888062782043, -4.05991399503478, 0.692324791368347, -2.9125226207386827, -2.2280694930608993, 3.92620807854434, 
            -3.367861137744882, 3.3278394001720777, -1.4741949447885951, -2.8637747357917203, -2.5751406410633892, 4.145833471695971, 
            -0.44366952730810194, -0.6102504332629177, -0.1436749584379493, -0.4665103649638147, -2.925347306087703, -3.656153452038163, 
            -1.4530946942668634, -2.68527416457555, 1.1387014869155294, 4.00750687584336, 2.7960823241923882, 2.536703248985395, -0.926743517832926, 
            0.6599876890623185, 0.085992944740744, 2.629646342157166, 7.0343619552206995, -3.029250830340099, 3.4730800363356957, 2.8603750036743296, 
            -0.07495681604776472, -1.1482103790677516, 3.1302389838157567, 2.3321163259312083, -2.1244051233830223, -1.7918634172967378},
            {2.0282801805851456, 2.44118563419611, 0.26544049046424956, -3.764963394897041, -4.1777531176802425, 2.619367764544762, 4.004054093835425, 
            -4.135565520678976, -3.054675289904761, 1.3090702458246763, 1.2721173325142452, -2.067716696702237, 0.44599068819875587, 0.6005189613235574, 
            -0.9630025864542935, 2.4692882367489144, 2.9904932299968348, 0.10594941358403265, 0.32837121366184086, -2.9303651774728503, 
            -2.7936861723416326, 2.5392303101924014, 2.9743150928962985, 4.251533373206799, -3.5970676501624554, -2.1526528482354723, 1.4911994505065147, 
            0.7896505213522635, -2.4922510409423784, 3.1692240315220035, 2.361772644163192, 4.339177976236284, 1.1729650882692377, 3.142996363419143, 
            -0.8795937733093613, 0.1008148281066541, 2.1534614841474493, 0.5467959293176303, -2.7207108430246585}};
        double[] cod = new double[dim];
        for (int i = 0; i < dim; i++)
            cod[i] = Math.random()*frontera*2 - frontera; // uniforme entre 0 y 2veces la frontera, con la resta queda centrada en 0 la dist de la var.
           
        return pob[clase];
    }
    
    // se crean individuos con fitness 0, porque en la mutación se hará la evaluación de este.
    public static Individuo[] cruzar(String tipoCruce, Individuo uno, Individuo dos){
        
        switch (tipoCruce) {
            case "1punto": 
                int corte = (int) (Math.random()*uno.codigo.length);
                double[] tres = new double[uno.codigo.length];  
                double[] cuatro = new double[uno.codigo.length];  
                for(int i=0;i<corte;i++){
                    tres[i] = uno.codigo[i];
                    cuatro[i] = dos.codigo[i];
                }
                for(int i=corte;i<uno.codigo.length;i++){
                    tres[i] = dos.codigo[i];
                    cuatro[i] = uno.codigo[i];
                }
                Individuo[] Zwei = {new Individuo(tres,0.0,uno.amigos), new Individuo(cuatro,0.0,dos.amigos)};
                return Zwei;
                
            case "2puntos":
                int corte1 = (int) (Math.random()*uno.codigo.length);
                int corte2 = (int) (Math.random()*uno.codigo.length);
                if(corte1>corte2){
                    int temp = corte1;
                    corte1=corte2;
                    corte2=temp;
                }
                double[] drei = new double[uno.codigo.length];  
                double[] vier = new double[uno.codigo.length];  
                for(int i=0;i<corte1;i++){
                        drei[i] = uno.codigo[i];
                        vier[i] = dos.codigo[i];
                }for(int i=corte1;i<corte2;i++){
                        drei[i] = dos.codigo[i];
                        vier[i] = uno.codigo[i];
                }for(int i=corte2;i<uno.codigo.length;i++){
                        drei[i] = uno.codigo[i];
                        vier[i] = dos.codigo[i];
                }
                Individuo[] zwei = {new Individuo(drei,0.0,uno.amigos), new Individuo(vier,0.0,dos.amigos)};
                return zwei;
                
            case "diferencia": 
                tres = uno.codigo;
                cuatro = dos.codigo;
                for(int i=0;i<tres.length;i++){
                    double dif = Math.abs(tres[i] - cuatro[i]);
                    if(Math.random() < 0.25){cuatro[i] += dif; tres[i] += dif;}
                    else if(Math.random() < 0.5){cuatro[i] -= dif; tres[i] += dif;}
                    else if(Math.random() < 0.75){cuatro[i] += dif; tres[i] -= dif;}
                    else cuatro[i] -= dif; tres[i] -= dif;
                }
                Individuo[] Zwei1 = {new Individuo(tres,0.0,uno.amigos), new Individuo(cuatro,0.0,dos.amigos)};
                return Zwei1;
                
            case "promedio": 
                tres = uno.codigo;
                cuatro = dos.codigo;
                for(int i=0;i<tres.length;i++){
                    double promedio = (tres[i] + cuatro[i]) / 2.0;
                    cuatro[i] = tres[i] = promedio;
                }
                
                Individuo[] Zwei2 = {new Individuo(tres,0.0,uno.amigos), new Individuo(cuatro,0.0,dos.amigos)};
                return Zwei2;
            case "intercambio":
                //implementando
        }
        return null;
    }
    
    public void mutar(double probMutacion, int clase, double mejor){
        double factorMuta = 1.0 - fitness/mejor;
        for (int i = 0; i < codigo.length; i++) {
             if(Math.random() < probMutacion){
                if(Math.random() < 0.5)codigo[i] += Math.random()*factorMuta*codigo[i];
                else codigo[i] -= Math.random()*0.2*codigo[i];
            }
        }
        
    }
    
    //Distancia Euclidiana
    public double distancia(Individuo otro){
        double distancia=0.0;
        for (int i = 0; i < codigo.length; i++) {
            distancia += (codigo[i]-otro.codigo[i])*(codigo[i]-otro.codigo[i]);
        }
        return Math.sqrt(distancia);
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();        
    }
}