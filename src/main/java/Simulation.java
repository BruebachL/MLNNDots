import mikera.vectorz.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Simulation extends JComponent implements KeyListener {
    static volatile boolean startSim = false;
    static JFrame frame = new JFrame();
    static JLabel listener = new JLabel();
    static Zone goal = new Zone(1920/2,100,15,15);

    static Zone killZone = new Zone(0,600,750,10);
    static Zone killZone2 = new Zone(850,600,1800,10);
    static Zone killZone3 = new Zone(0,500,450,10);
    static Zone killZone4 = new Zone(550,500,1800,10);
    static Zone[] killZones = new Zone[]{killZone,killZone2,killZone3,killZone4};
    static Population testPop = new Population(100);

    static int timeStep = 20;
    static double mutationRateTracker;

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args){
        Simulation test = new Simulation();
        test.run();
    }

    static private class startSim extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if(startSim){
                startSim=false;
            }else{
                startSim=true;
            }
        }
    }

    static private class slowDown extends AbstractAction{
        public void actionPerformed(ActionEvent e){
            if(!(timeStep--<0)) {
                timeStep--;
            }
        }
    }

    static private class speedUp extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            timeStep++;
        }
    }

    public void run(){
        long start = System.currentTimeMillis();
        frame.setSize(1920, 1000);
        frame.getContentPane().add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.add(listener);
        listener.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F"), "startSim");
        listener.getActionMap().put("startSim", new startSim());
        listener.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("E"), "slowDown");
        listener.getActionMap().put("slowDown", new slowDown());
        listener.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("Q"), "speedUp");
        listener.getActionMap().put("speedUp", new speedUp());
        while(true) {
            if(startSim) {
                if (testPop.allDotsDead()) {
                    testPop.calculateFitness();
                    testPop.naturalSelection();
                    testPop.mutateBbys();
                } else {
                    frame.repaint();
                    testPop.update();
                    try {
                        TimeUnit.MILLISECONDS.sleep(timeStep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(goal.xPosition,goal.yPosition,goal.width,goal.height);
        for(Zone killZone : killZones) {
            g.setColor(Color.ORANGE);
            g.fillRect(killZone.xPosition, killZone.yPosition, killZone.width, killZone.height);
        }
        int dotTracker = 0;

        for(int i = 0; i<testPop.dots.length;i++) {
            //g.setColor(Color.RED);
            if(testPop.dots[i].isChampion){
                g.setColor(Color.GREEN);
            }
            Color fitnessShade = new Color(255,0,(int) sigmoid(testPop.dots[i].calculateFitness())*255);
            g.setColor(fitnessShade);
            g.fillOval((int) testPop.dots[i].position.x, (int) testPop.dots[i].position.y, 10, 10);
            StringBuilder distanceToGoal = new StringBuilder();
            distanceToGoal.append(Math.hypot(Math.abs(testPop.dots[i].position.x-Simulation.goal.xPosition), Math.abs(testPop.dots[i].position.y-Simulation.goal.yPosition)));
            String dist = distanceToGoal.toString();
            g.drawString(dist, (int)testPop.dots[i].position.x + 10, (int)testPop.dots[i].position.y);

            StringBuilder display = new StringBuilder();
            display.append(dotTracker);
            display.append(" Fitness: ");
            display.append(testPop.dots[i].lastFitness);
            String displayString = display.toString();
            g.drawString(displayString, 50, 50 * dotTracker + 50);
            dotTracker++;
        }
        g.drawString( "Generation: " + testPop.generation,1700,100);
        g.drawString( "Global Minstep: " + testPop.minStep,1700,150);
        g.drawString( "BestDot Step: " + testPop.dots[testPop.bestDot].dotStep,1700,200);
        g.drawString( "Pop size: " + testPop.dots.length,1700,250);
        g.drawString("Global Fitness: " + testPop.fitnessSum, 1700, 300);
        g.drawString("BestDot Fitness: " + testPop.championFitness,1700,350);
        g.drawString("BestDot Fitness Percentage: " + testPop.championFitness/(testPop.fitnessSum/100.0),1650,400);
        g.drawString(testPop.bestDot + " ", 50,25);
        StringBuilder letsUpdate = new StringBuilder();
        letsUpdate.append(mutationRateTracker);
        String mR = letsUpdate.toString();
        g.drawString(mR, 50,10);
        //g.setColor(Color.BLUE);
        //g.fillRect((int) testPop.dots[testPop.bestDot].position.x, (int) testPop.dots[testPop.bestDot].position.y, 10, 10);
    }

    public static double sigmoid(double x) {
        return (1/( 1 + Math.pow(Math.E,(-1*x))));
    }
}
