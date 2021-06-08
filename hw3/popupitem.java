/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce325.hw3;

import java.io.File;
import javax.swing.JMenuItem;

/**
 *
 * @author vassilina
 */
public class popupitem extends JMenuItem{
    private File file;
    
    public popupitem(File file) {
            super(file.getAbsolutePath());
            this.file=file;
    }
    public File popupgetfile(){
        return(this.file);
    }
    
}
