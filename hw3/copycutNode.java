/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce325.hw3;
import java.awt.Color;
import java.io.*;
import javax.swing.JButton;
/**
 *
 * @author vassilina
 */
public class copycutNode {
    private String name;
    private File file;
    
    private mybutton button;
    boolean grey;

    public copycutNode(File file,mybutton button) {
        this.file=file;
        this.name=file.getName();
        this.button=button;
        this.grey=true;
        
    }
     public void defaultBackground(){
        button.setBackground(new JButton().getBackground());
    }
   public String getName(){
       return(name);
   }
    public File getFile(){
      return(file);
    }
}
