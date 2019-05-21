import mikera.vectorz.*;
import java.util.Random;

public class Brain {
    Vector2[] directions;
    int size;

    public Brain(int size){
        directions = new Vector2[size];
        randomize();
    }

    void randomize(){
        for(int i = 0; i<directions.length;i++){
            Random dir = new Random();
            directions[i] = new Vector2(Math.cos(dir.nextGaussian()*360),Math.sin(dir.nextGaussian()*360));
        }
    }

    protected Brain clone(){
        Brain clone = new Brain(directions.length);
        for(int i = 0; i<directions.length;i++){
            clone.directions[i] = (Vector2)directions[i].copy();
        }
        return clone;
    }

    void mutate(){
        double mutationRate = 0.01 - 1.0/Simulation.testPop.minStep;
        for(int i = 0; i<directions.length;i++){
            double random = Math.random();
            if(random<mutationRate){
                Random dir = new Random();
                directions[i] = new Vector2(Math.cos(dir.nextGaussian()*360),Math.sin(dir.nextGaussian()*360));
            }
        }
    }
}
