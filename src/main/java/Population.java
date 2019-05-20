import mikera.util.Random;
import mikera.vectorz.Vector2;

public class Population {
    Dot[] dots;
    double fitnessSum;
    int generation=0;
    int bestDot=0;
    int minStep=400;
    double championFitness;

    public Population(int size){
        dots = new Dot[size];
        for(int i = 0; i<dots.length;i++){
            dots[i] = new Dot(new Vector2(500,400));
        }
    }

    void update(){
        for (Dot dot : dots) {
            if(dot.dotStep>minStep){
                dot.alive=false;
            }else {
                dot.update();
            }
        }
    }

    void calculateFitness(){
        for(Dot dot : dots){
            dot.calculateFitness();
        }
    }

    boolean allDotsDead(){
        for(Dot dot : dots){
            if(dot.alive){
                return false;
            }
        }
        return true;
    }

    void naturalSelection(){
    Dot[] newDots = new Dot[dots.length];
    setBestDot();
    calculateFitnessSum();

    Dot champion = dots[bestDot];
    newDots[0] = champion.makeBby();
    newDots[0].isChampion=true;
    for(int i = 1; i<newDots.length;i++){
        Dot parent = selectParent();
        newDots[i] = parent.makeBby();
    }

    dots = newDots.clone();
    generation++;
    //fitnessSum=0;
    }

    void mutateBbys(){
        for(int i = 1; i<dots.length;i++){
            dots[i].brain.mutate();
        }
    }

    void calculateFitnessSum(){
        fitnessSum=0;
        for(Dot dot : dots){
            fitnessSum += dot.fitness;
        }
        //fitnessSum *= 10000;
    }

    Dot selectParent(){
        Random rand = new Random();
        //System.out.println("Fitness Sun: " + fitnessSum);
        double randNumber = Math.random()*fitnessSum;
        //System.out.println("RNG: " + randNumber);
        double runningSum = 0;
        for(int i = 0; i<dots.length;i++){
            runningSum += dots[i].fitness;
            //System.out.println("Running sum: " + runningSum);
            if(runningSum>randNumber){
                return dots[i];
            }
        }
        return null;
    }

    void setBestDot(){
        double max = 0;
        int maxIndex = 0;
        //System.out.println("Evaluating best dot ---------------------");
        for(int i=0; i<dots.length;i++){
            if(dots[i].fitness>max){
                //System.out.println(dots[i].reachedGoal +" " + dots[i].fitness);
                max=dots[i].fitness;
                maxIndex=i;
            }
        }
        //System.out.println("Finished evaluation ---------------------");
        bestDot=maxIndex;
        System.out.println(dots[bestDot].reachedGoal);
        if(dots[bestDot].reachedGoal||dots[bestDot].alive){
            minStep=dots[bestDot].dotStep;
        }
        championFitness=dots[bestDot].fitness;
    }

}
