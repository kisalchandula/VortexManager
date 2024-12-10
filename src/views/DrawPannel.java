package views;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//Drawing Graphic Object by Mouse Press and Drag
public class DrawPannel extends JPanel{
	public int x, y, x2, y2;
	
	public DrawPannel(){
		 x = y = x2 = y2 = 0; // 
         MyMouseListener listener = new MyMouseListener();
         addMouseListener(listener);
         addMouseMotionListener(listener);	
	}
	

	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		JFrame f = new JFrame("Draw Box Mouse");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setContentPane(new DrawPannel());
        f.setSize(500, 600);
        f.setVisible(true);

	}
	
	public void drawPerfectRect(Graphics g, int x, int y, int x2, int y2) {
        int px = Math.min(x,x2);
        int py = Math.min(y,y2);
        int pw=Math.abs(x-x2);
        int ph=Math.abs(y-y2);
        g.drawRect(px, py, pw, ph);
    }
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        drawPerfectRect(g, x, y, x2, y2);
    }

	
	class MyMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
        	x = e.getX();
    		y= e.getY();
        }

        public void mouseDragged(MouseEvent e) {
        	x2 = e.getX();
    		y2= e.getY();
    		repaint();
        }

        public void mouseReleased(MouseEvent e) {
        	x2 = e.getX();
    		y2= e.getY();
    		repaint();
        }
    }

}
