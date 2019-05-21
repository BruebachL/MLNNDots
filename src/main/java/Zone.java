import mikera.vectorz.Vector2;

public class Zone {
    int xPosition;
    int yPosition;
    int width;
    int height;

    public Zone(int x, int y, int w, int h){
        this.xPosition=x;
        this.yPosition=y;
        this.width=w;
        this.height=h;
    }

    boolean isWithinZone(Vector2 point){
        if(point.x>=xPosition&&point.x<=xPosition+width&&point.y>=yPosition&&point.y<=yPosition+height){
            return true;
        }
        return false;
    }

}
