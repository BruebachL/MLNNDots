import mikera.vectorz.*;
import java.util.Random;

public class Brain {
    Vector2[] directions;
    double mutationRate;
    int size;

    public Brain(int size){
        directions = new Vector2[size];
        randomize();
    }

    void randomize(){
        Random dir = new Random();
        for(int i = 0; i<directions.length;i++){
            if(i==0) {
                directions[i] = new Vector2(Math.cos(dir.nextGaussian() * 360), Math.sin(dir.nextGaussian() * 360));
            }else {
                directions[i] = new Vector2(directions[i-1].x,directions[i-1].y);
                directions[i].x += Math.cos(dir.nextGaussian() * 90.0 - 180.0);
                directions[i].y += Math.sin(dir.nextGaussian() * 90.0 - 180.0);
            }
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
        mutationRate = 0.01;
        Simulation.mutationRateTracker = mutationRate;
        for(int i = 0; i<directions.length;i++){
            double random = Math.random();
            if(random<mutationRate){
                Random dir = new Random();
                //directions[i] = new Vector2(Math.cos(dir.nextGaussian()*360),Math.sin(dir.nextGaussian()*360));
                directions[i].x += Math.cos(dir.nextGaussian()*20-10);
                directions[i].y += Math.sin(dir.nextGaussian()*20-10);
            }
        }
    }
}
