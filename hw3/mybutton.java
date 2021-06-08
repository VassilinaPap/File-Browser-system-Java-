/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce325.hw3;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.*;
import java.io.*;
/**
 *
 * @author vassilina
 */
public class mybutton extends JButton{
    private File file;
    
    private boolean directory;
   

    public mybutton(String filename,Icon icon,File file) {
        super();
        this.setText(filename);
        this.setPreferredSize(new Dimension(100,100));
        this.setIcon(icon);
        this.setVerticalTextPosition(SwingConstants.BOTTOM);
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.file=file;
        
        //System.out.println(file.getAbsolutePath());
        if(file.isDirectory()){
            directory=true;

        }
        else{
            directory=false;
        }
        

    }
    public void greyBackground(){
        this.setBackground(Color.LIGHT_GRAY);
        //grey=true;
    }
    public File getFile(){
      return(file);
    }
    public boolean isDir(){
      return(directory);
    }
//    public void whiteBackground(){
//        this.setBackground(Color.white);
//        grey=false;
//    }
    
    
}
