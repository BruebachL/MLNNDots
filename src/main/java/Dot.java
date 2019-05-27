
import mikera.vectorz.*;

public class Dot {
    Brain brain;
    Vector2 position;
    double fitness = 0;
    volatile double lastFitness;
    boolean alive = true;
    boolean reachedGoal = false;
    boolean isChampion=false;
    boolean ranOutOfSteps = false;
    Vector2 vel = new Vector2(0,0);
    Vector2 acc = new Vector2(0,0);

    int dotStep = 0;

    public Dot(Vector2 p){
        brain = new Brain(4000);
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
            acc.normalise();
            vel.add(acc);
            vel.normalise();
            vel.multiply(3);
            position.add(vel);
            dotStep++;
        }else{
            ranOutOfSteps=true;
            alive=false;
        }
    }

    double calculateFitness(){
        fitness=0;

        if(goalVisible()) {
            double distanceToGoal = Math.hypot(Math.abs(position.x-Simulation.goal.xPosition), Math.abs(position.y-Simulation.goal.yPosition));
            this.fitness = 1.0/(1.0/(distanceToGoal*distanceToGoal));
        }else{
            this.fitness = 1.0/(this.position.y/1000.0);
            if(!this.ranOutOfSteps){
                this.fitness /= 10;
            }
        }
        if(this.isChampion){
            this.fitness *=2;
        }
        this.fitness *= 1+zonesPassed()*3;
        if(this.reachedGoal){
            this.fitness = 1.0/16.0 + 100.0/(dotStep*dotStep);
        }
        return fitness;
    }

    Dot makeBby(){
        Dot baby = new Dot(new Vector2(1920.0/2.0,800));
        baby.brain = brain.clone();
        baby.dotStep=0;
        baby.lastFitness=fitness;
        return baby;
    }

    boolean goalVisible(){
        for(Zone killZone : Simulation.killZones){
            if(linesIntersect(position.x,position.y,Simulation.goal.xPosition,Simulation.goal.yPosition,killZone.xPosition,killZone.yPosition,killZone.xPosition+killZone.width, killZone.yPosition+killZone.height)){
                return false;
            }
        }
        return true;
    }

    boolean linesIntersect(double p0_x, double p0_y, double p1_x, double p1_y, double p2_x, double p2_y, double p3_x, double p3_y){
        {
            double s1_x, s1_y, s2_x, s2_y;
            s1_x = p1_x - p0_x;     s1_y = p1_y - p0_y;
            s2_x = p3_x - p2_x;     s2_y = p3_y - p2_y;

            double s, t;
            s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y)) / (-s2_x * s1_y + s1_x * s2_y);
            t = ( s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x)) / (-s2_x * s1_y + s1_x * s2_y);

            if (s >= 0 && s <= 1 && t >= 0 && t <= 1)
            {
                // Collision detected
                return true;
            }

            return false; // No collision
        }

    }

    int zonesPassed(){
        int zonesPassed = 0;
        for(Zone killZone : Simulation.killZones){
            if(this.position.y<killZone.yPosition){
                zonesPassed++;
            }
        }
        return zonesPassed/2;
    }

}
