import mikera.vectorz.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.TimeUnit;

public class Simulation extends JComponent implements KeyListener {
    static JFrame frame = new JFrame();
    static JLabel listener = new JLabel();
    static Vector2 goal = new Vector2(500,100);
    Dot alpha = new Dot(new Vector2(500,500));
    Population testPop = new Population(200);

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

    public void run(){
        long start = System.currentTimeMillis();
        frame.setSize(1000, 1000);
        frame.getContentPane().add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.add(listener);
        //listener.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, InputEvent.SHIFT_DOWN_MASK), "up");
        //listener.getActionMap().put("up", new moveUp());
        while(true) {
            if(testPop.allDotsDead()){
             testPop.calculateFitness();
             testPop.naturalSelection();
             testPop.mutateBbys();
            }else {
                frame.repaint();
                testPop.update();
                try {
                    TimeUnit.MILLISECONDS.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(500,100,15,15);
        for(int i = 0; i<testPop.dots.length;i++) {
            g.setColor(Color.RED);
            if(testPop.dots[i].isChampion){
                g.setColor(Color.GREEN);
            }
            g.fillRect((int) testPop.dots[i].position.x, (int) testPop.dots[i].position.y, 10, 10);
        }
        g.drawString( "Generation: " + testPop.generation,800,100);
        g.drawString( "Global Minstep: " + testPop.minStep,800,150);
        g.drawString( "BestDot Step: " + testPop.dots[testPop.bestDot].dotStep,800,200);
        g.drawString( "Pop size: " + testPop.dots.length,800,250);
        g.drawString("Global Fitness: " + testPop.fitnessSum, 800, 300);
        g.drawString("BestDot Fitness: " + testPop.championFitness,800,350);
        g.setColor(Color.BLUE);
        g.fillRect((int) testPop.dots[testPop.bestDot].position.x, (int) testPop.dots[testPop.bestDot].position.y, 10, 10);
    }
}
