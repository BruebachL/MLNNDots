
import mikera.vectorz.*;

public class Dot {
    Brain brain;
    Vector2 position;
    double fitness = 0;
    boolean alive = true;
    boolean reachedGoal = false;
    boolean isChampion=false;
    boolean ranOutOfSteps = false;
    Vector2 vel = new Vector2(0,0);
    Vector2 acc = new Vector2(0,0);

    int dotStep = 0;

    public Dot(Vector2 p){
        brain = new Brain(2000);
        this.position=p;
    }

    public void update(){
        if(alive&&!reachedGoal){
            move();
            if(position.x>1900||position.x<50||position.y>950||position.y<50||isInKillZones()){
                alive=false;
            }else if(Simulation.goal.isWithinZone(position)){
                reachedGoal=true;
                alive=false;
            }
        }

    }

    public boolean isInKillZones(){
        for(Zone killZone : Simulation.killZones){
            if(killZone.isWithinZone(position)){
                return true;
            }
        }
        return false;
    }

    public void move(){
        if(dotStep<brain.directions.length&&!reachedGoal) {
            acc.add(brain.directions[dotStep]);
            vel.add(acc);
                vel.normalise();
                vel.multiply(1);
            position.add(vel);
            dotStep++;
        }else{
            alive=false;
        }
    }

    void calculateFitness(){
        fitness=0;
        double distanceToGoal = Math.hypot(Math.abs(position.y-Simulation.goal.yPosition),Math.abs(position.x-Simulation.goal.xPosition));
        this.fitness = 1.0/(distanceToGoal*distanceToGoal);
        this.fitness /= 2;
        if(!this.alive&&!this.ranOutOfSteps){
            this.fitness /= 5;
        }
        //System.out.println(fitness);
        if(this.reachedGoal){
            this.fitness = 1.0/16.0 + 100.0/(dotStep*dotStep);
        }
    }

    Dot makeBby(){
        Dot baby = new Dot(new Vector2(1920.0/2.0,800));
        baby.brain = brain.clone();
        baby.dotStep=0;
        return baby;
    }

}
