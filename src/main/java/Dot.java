
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
            if(position.x>900||position.x<100||position.y>900||position.y<100){
                alive=false;
            }else if((int)position.x==Simulation.goal.x&&(int)position.y==Simulation.goal.y){
                reachedGoal=true;
                alive=false;
            }
        }

    }

    public void move(){
        if(dotStep<brain.directions.length&&!reachedGoal) {
            acc.add(brain.directions[dotStep]);
            vel.add(acc);
            if(vel.magnitude()>5){
                vel.normalise();
                vel.multiply(5);
            }
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
        //System.out.println(fitness);
        if(this.reachedGoal){
            this.fitness = 1.0/16.0 + 1000.0/(dotStep*dotStep);
        }
    }

    Dot makeBby(){
        Dot baby = new Dot(new Vector2(500,500));
        baby.brain = brain.clone();
        baby.dotStep=0;
        return baby;
    }

}
