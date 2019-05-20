
import mikera.vectorz.*;

public class Dot {
    Brain brain;
    Vector2 position;
    double fitness = 0;
    boolean alive = true;
    boolean reachedGoal = false;
    boolean isChampion=false;
    Vector2 vel = new Vector2(0,0);
    Vector2 acc = new Vector2(0,0);

    int dotStep = 0;

    public Dot(Vector2 p){
        brain = new Brain(400);
        this.position=p;
    }

    public void update(){
        if(alive&&!reachedGoal){
            move();
            if(position.x>950||position.x<50||position.y>950||position.y<50){
                alive=false;
            }else if((int)position.x<=Simulation.goal.x+10&&(int)position.x>=Simulation.goal.x-10&&(int)position.y<=Simulation.goal.y+10&&(int)position.y>=Simulation.goal.y-10){
                reachedGoal=true;
                alive=false;
            }
        }

    }

    public void move(){
        if(dotStep<brain.directions.length&&!reachedGoal) {
            acc.add(brain.directions[dotStep]);
            vel.add(acc);
                vel.normalise();
                vel.multiply(2);
            position.add(vel);
            dotStep++;
        }else{
            alive=false;
        }
    }

    void calculateFitness(){
        fitness=0;
        double distanceToGoal = Math.hypot(Math.abs(position.y-Simulation.goal.y),Math.abs(position.x-Simulation.goal.x));
        this.fitness = 1.0/(distanceToGoal*distanceToGoal);
        this.fitness /= 5;
        //System.out.println(fitness);
        if(this.reachedGoal){
            this.fitness = 1.0/16.0 + 100.0/(dotStep*dotStep);
        }
    }

    Dot makeBby(){
        Dot baby = new Dot(new Vector2(500,400));
        baby.brain = brain.clone();
        baby.dotStep=0;
        return baby;
    }

}
