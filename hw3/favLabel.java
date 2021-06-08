/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce325.hw3;

import java.io.File;
import javax.swing.JLabel;

/**
 *
 * @author vassilina
 */
public class favLabel extends JLabel{
    private File file;

    public favLabel(File file,String name) {
        super(name);
        this.file=file;
    }
    public File getFile(){
        return(file);
    }
}
