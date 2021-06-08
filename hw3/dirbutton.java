/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce325.hw3;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

/**
 *
 * @author vassilina
 */
public class dirbutton extends JButton{
    private File file;

    public dirbutton(String dirname,File file) {
        super(dirname);
        this.file=file;
    }
    public File getFile(){
      return(file);
    }
    
    
}
