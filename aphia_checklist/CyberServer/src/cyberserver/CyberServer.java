/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cyberserver;

import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Geofrey Nyabuto
 */
public class CyberServer{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
   trialData TD = new trialData();
    }
        
  }  

class trialData  extends JFrame{
    public trialData()
  {
//    super("Fullscreen");
    getContentPane().setPreferredSize( Toolkit.getDefaultToolkit().getScreenSize());
    pack();
    setResizable(false);
    show();


    SwingUtilities.invokeLater(new Runnable() {
      public void run()
      {
        Point p = new Point(0, 0);
        SwingUtilities.convertPointToScreen(p, getContentPane());
        Point l = getLocation();
        l.x -= p.x;
        l.y -= p.y;
        setLocation(l);
      }
    });
  }
}