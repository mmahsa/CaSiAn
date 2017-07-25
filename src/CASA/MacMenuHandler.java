/*
 * Copyright (C) 2016 mahsa.moein
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package CASA;
import javax.swing.JOptionPane;
import com.apple.mrj.MRJAboutHandler;
import com.apple.mrj.MRJPrefsHandler;
import com.apple.mrj.MRJQuitHandler;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author mahsa.moein
 */
public class MacMenuHandler implements MRJAboutHandler, MRJQuitHandler, MRJPrefsHandler
{
  public void handleAbout()
  {
//    JOptionPane.showMessageDialog(null, 
//                                  "Hello world", 
//                                  "about", 
//                                  JOptionPane.INFORMATION_MESSAGE);
      
      JLabel casaIcon = new JLabel(new ImageIcon(this.getClass().getClassLoader().getResource("resource/CASA_About.png")));
        
      casaIcon.setSize(231, 223);
      //MRJAboutHandler.class.getClass().
        //this.add(casaIcon);    
  }

  public void handlePrefs() throws IllegalStateException
  {
    JOptionPane.showMessageDialog(null, 
                                  "prefs", 
                                  "prefs", 
                                  JOptionPane.INFORMATION_MESSAGE);
  }

  public void handleQuit() throws IllegalStateException
  {
    JOptionPane.showMessageDialog(null, 
                                  "quit", 
                                  "quit", 
                                  JOptionPane.INFORMATION_MESSAGE);
    // handle exit here
    // System.exit(0);
  }

}
    

