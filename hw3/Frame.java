/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce325.hw3;
import javax.swing.*;
import java.awt.*;
import java.io.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.awt.event.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.swing.text.Document;
//import javax.swing.text.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public final class Frame {
    
    private final String iconspath;
    private String homepath;
    private String OS;
    private  String favpath;
    private final int WIDTH=600;   
    private final int HEIGHT=500;  
    private JFrame frame;
    private JPanel favouritesPanel;
    private JPanel Panel;
    private JPanel currentContent;
    private JTextField textField;
    private JButton searchButton;
    private JPanel directoryPanel;
    private JMenuItem NewWindowItem;
    private JMenuItem ExitItem;
    private JMenuItem CutItem ;
    private JMenuItem CopyItem ;
    private JMenuItem PasteItem ;
    private JMenuItem RenameItem ;
    private JMenuItem DeleteItem ;
    private JMenuItem AddToFavouritesItem ;
    private JMenuItem PropertiesItem ;
    private JMenuItem SearchItem ;
    private JScrollPane scrolled;
    private JPopupMenu searchpopup;
    private copycutNode filenode;
    private boolean STH;
    private File source;
    private File dest;
    private JScrollPane scolledArea;
    private boolean CUT;
    private boolean searchstate;
    private  boolean override;
    private  boolean DELETE;
    private String dirstate;

    public Frame(){
        
        DELETE=false;
        homepath=System.getProperty("user.home");
        OS = System.getProperty("os.name").toLowerCase();
         
        File fileroot=new File(homepath);
        iconspath="hw4-icons"+fileroot.separator+"icons"+fileroot.separator;
        frame=new JFrame();
        frame.setSize(700,500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  
        CUT=false;
        STH=false;
        
               
        favouritesPanel=new JPanel();
        favouritesPanel.setLayout(new BoxLayout(favouritesPanel, BoxLayout.Y_AXIS));
        JPanel Panel=new JPanel();
        Panel.setLayout(new BorderLayout());
        JPanel subPanel = new JPanel(new FlowLayout());
        subPanel.setPreferredSize(new Dimension(subPanel.getPreferredSize().width,40));
        subPanel.setLayout(new FlowLayout((FlowLayout.LEFT)));
        textField = new JTextField(41);
        searchButton=new JButton("Search");
        searchButton.setVisible(false);
        textField.setVisible(false);
        subPanel.add(textField);
        subPanel.add(searchButton);
        searchstate=false;
        ActionListener sl=new searchlistener();
        searchButton.addActionListener(sl);
        directoryPanel=new JPanel();
        directoryPanel.setLayout(new ModifiedFlowLayout((FlowLayout.LEFT)));
        //directoryPanel.setPreferredSize(new Dimension(directoryPanel.getPreferredSize().width,40));
        scolledArea = new JScrollPane(directoryPanel);
        scolledArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        JPanel subPanel2 = new JPanel();
        subPanel2.setLayout(new BoxLayout(subPanel2, BoxLayout.Y_AXIS));
        subPanel2.add(subPanel);
        subPanel2.add(scolledArea);
        
       
        Panel.add(subPanel2,BorderLayout.PAGE_START);
        
        currentContent=new JPanel();
        //currentContent.setPreferredSize(new Dimension(currentContent.getPreferredSize().width, 900));
        currentContent.setLayout(new ModifiedFlowLayout((FlowLayout.LEFT)));
       
        
        
        
        scrolled = new JScrollPane(currentContent);
        scrolled.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        Panel.add(scrolled,BorderLayout.CENTER);     
        JSplitPane split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setLeftComponent(favouritesPanel);
        split.setRightComponent(Panel);
        split.setDividerLocation(130);

        frame.add(split);
      
        opendir(fileroot);
        openallbreadcrumb(fileroot);

        favpath=homepath+fileroot.separator+".java-file-browser"+fileroot.separator+"properties.xml";
        File fav=new File(favpath);
        if(fav.exists()){
            showfav();
            
        }
        else{
             //System.out.println("NOT");
            File homefile=new File(homepath);
            favLabel label=new favLabel(homefile,fileroot.getName());
           // System.out.println(label.getFile().getAbsolutePath());
            favouritesPanel.add(label);

            MouseListener l=new favbuttonlistener();
            label.addMouseListener(l);
            
        }
       
        JMenu fileMenu = new JMenu("File");
        JMenuBar bar = new JMenuBar();
       
        bar.add(fileMenu);
        frame.setJMenuBar(bar); 
        NewWindowItem = new JMenuItem("New Window");
        fileMenu.add(NewWindowItem );
       
        NewWindowItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                      
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        Frame w=new Frame();
                        
                    }
                });
            } 
        });
        
        ExitItem = new JMenuItem("Exit");
        fileMenu.add(ExitItem );
        ExitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            } 
        });
        
        JMenu EditMenu = new JMenu("Edit");
        bar.add(EditMenu);
        CutItem = new JMenuItem("Cut");
        CopyItem = new JMenuItem("Copy");
        PasteItem = new JMenuItem("Paste");
        RenameItem = new JMenuItem("Rename");
        DeleteItem = new JMenuItem("Delete");
        AddToFavouritesItem = new JMenuItem("Add to Favourites");
        PropertiesItem = new JMenuItem("Properties");
        EditMenu.add(CutItem);
        CutItem.setEnabled(false);
        ActionListener cl=new cutlistener();
        CutItem.addActionListener(cl);
        EditMenu.add(CopyItem);
        ActionListener ml=new copylistener();
        CopyItem.addActionListener(ml);
        CopyItem.setEnabled(false);
        EditMenu.add(PasteItem);
        ActionListener al=new pastelistener();
        PasteItem.addActionListener(al);
        PasteItem.setEnabled(false);
        EditMenu.add(RenameItem);
        ActionListener rl=new renamelistener();
        RenameItem.addActionListener(rl);
        RenameItem.setEnabled(false);
        EditMenu.add(DeleteItem);
        ActionListener dl=new deletelistener();
        DeleteItem.addActionListener(dl);
        DeleteItem.setEnabled(false);
        EditMenu.add(AddToFavouritesItem);
        AddToFavouritesItem.setEnabled(false);
        ActionListener fl=new addfavlistener();
        AddToFavouritesItem.addActionListener(fl);
        EditMenu.add(PropertiesItem);
        PropertiesItem.setEnabled(false);
        ActionListener pl=new propertieslistener();
        PropertiesItem.addActionListener(pl);
        JMenu SearchMenu = new JMenu("Search");
        bar.add(SearchMenu);
        SearchItem = new JMenuItem("Search");
        SearchMenu.add(SearchItem);
        SearchItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(searchstate){
                    searchButton.setVisible(false);
                    textField.setVisible(false);
                    searchstate=false;
                }
                else{
                    searchButton.setVisible(true);
                    textField.setVisible(true);
                    searchstate=true;
                }
            } 
        });
        frame.setVisible(true);
    }
 
    public void createFav(String name){
        
        File xmlFile = new File(favpath);
        File favdir=new File(homepath+ xmlFile.separator+".java-file-browser");
        if(!favdir.isDirectory()){
            favdir.mkdir();
        }
        
        boolean res;
        try{
            res=xmlFile.createNewFile();
            //System.out.println(res);
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();
            Element root = document.createElement("favourites");
            document.appendChild(root);
 
            
            Element directory = document.createElement("directory");
 
            root.appendChild(directory);
            Attr attr = document.createAttribute("idname");
            attr.setValue(name);
            directory.setAttributeNode(attr);
            
            Element PathName = document.createElement("pathname");
            PathName.appendChild(document.createTextNode(name));
            directory.appendChild(PathName);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(xmlFile);
            transformer.transform(domSource, streamResult);
        } catch ( ParserConfigurationException | TransformerException e1) {
            e1.printStackTrace();
        }
        catch (IOException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        currentContent.removeAll();
        currentContent.revalidate();
        currentContent.repaint();
        opendir(filenode.getFile().getParentFile());
    }
  
     public class favbuttonlistener implements MouseListener{
         
        @Override
        public void mouseClicked(MouseEvent e) {
       
            if( SwingUtilities.isLeftMouseButton(e) ){
                if(filenode!=null){
                    filenode.defaultBackground();
                }
                Object ob=e.getSource();
                favLabel lab=(favLabel)ob;
                CutItem.setEnabled(false);
                CopyItem.setEnabled(false);
                PasteItem.setEnabled(false);                   
                RenameItem.setEnabled(false);
                DeleteItem.setEnabled(false);
                AddToFavouritesItem.setEnabled(false);
                PropertiesItem.setEnabled(false);
                
                if(lab.getFile().isDirectory()){
                    currentContent.removeAll();
                    currentContent.revalidate();
                    currentContent.repaint();
                    directoryPanel.removeAll();
                    directoryPanel.revalidate();
                    directoryPanel.repaint();
                    openallbreadcrumb(lab.getFile());
                    opendir(lab.getFile());


                }
            }
            else if(SwingUtilities.isRightMouseButton(e)){
                if(filenode!=null){
                    filenode.defaultBackground();
                }
                Object ob=e.getSource();
                favLabel lab=(favLabel)ob;
                
                CutItem.setEnabled(false);
                CopyItem.setEnabled(false);
                PasteItem.setEnabled(false);                   
                RenameItem.setEnabled(false);
                DeleteItem.setEnabled(false);
                AddToFavouritesItem.setEnabled(false);
                PropertiesItem.setEnabled(false);

                JPopupMenu popup = new JPopupMenu();
             
                JMenuItem Delete = new JMenuItem("Delete");
             
               
               
                popup.add(Delete);
                //ActionListener dl=new deletelistener();
                Delete.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        File deletefile=lab.getFile();
                        String deletepath=deletefile.getAbsolutePath();
                        try{
                            File fXmlFile = new File(favpath);
                            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                            Document doc = dBuilder.parse(fXmlFile);

                            doc.getDocumentElement().normalize();

                            NodeList nList = doc.getElementsByTagName("directory");

                            for (int temp = 0; temp < nList.getLength(); temp++) {

                                Node nNode = nList.item(temp);

                                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                                    Element eElement = (Element) nNode;
                                    String namepath=eElement.getElementsByTagName("pathname").item(0).getTextContent();
                                    boolean res=namepath.equals(deletepath);
                                    if(res){
                                        eElement.getParentNode().removeChild(eElement);
                                    }
                                }
                            }
                            DOMSource source = new DOMSource(doc);

                            TransformerFactory transformerFactory = TransformerFactory.newInstance();
                            Transformer transformer = transformerFactory.newTransformer();
                            StreamResult result = new StreamResult(favpath);
                           
                            transformer.transform(source, result);
                          
                            showfav();
                        } catch (TransformerException|IOException | ParserConfigurationException | DOMException | SAXException ae) {
                            ae.printStackTrace();
                        }
                    }
                    

                     
                    
                });
                
                popup.show(e.getComponent(),e.getX(),e.getY());
            }
        }
        @Override
        public void mousePressed(MouseEvent me) {
            
        }
        @Override
        public void mouseReleased(MouseEvent me) {
            
        }
        @Override
        public void mouseEntered(MouseEvent me) {
        }

        @Override
        public void mouseExited(MouseEvent me) {
            
        }

    }
    public class addfavlistener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
           
            File fav=new File(favpath);
            String path=filenode.getFile().getAbsolutePath();
            
            if(fav.exists()){
                try{
                    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                    Document doc = docBuilder.parse(favpath);
                    Element root = doc.getDocumentElement();
                    
                    Element directory = doc.createElement("directory");
 
                    root.appendChild(directory);
                    Attr attr = doc.createAttribute("idname");
                    attr.setValue(path);
                    directory.setAttributeNode(attr);

                    Element PathName = doc.createElement("pathname");
                    PathName.appendChild(doc.createTextNode(path));
                    directory.appendChild(PathName);
                    
                    DOMSource source = new DOMSource(doc);

                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    StreamResult result = new StreamResult(favpath);
                    try {
                        transformer.transform(source, result);
                    } catch (TransformerException ex) {
                        Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                catch(ParserConfigurationException pce){
                    
                }
                catch(SAXException sae){
                    
                }
                catch(IOException ioe){
                    
                
                } catch (TransformerConfigurationException ex) {
                        Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
                }
              
            
            }
            else{
                //System.out.println("CREATE");
                createFav(path);
            }
            showfav();
            
            
        }
    }
    public void showfav(){
         try {
             
       
            favouritesPanel.removeAll();
            favouritesPanel.revalidate();
            favouritesPanel.repaint(); 
            File homefile=new File(homepath);
            favLabel label=new favLabel(homefile,homefile.getName());
            favouritesPanel.add(label);
            MouseListener l=new favbuttonlistener();
            label.addMouseListener(l);
            
            File fXmlFile = new File(favpath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("directory");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                    Node nNode = nList.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                            Element eElement = (Element) nNode;
                            //System.out.println("directory id : " + eElement.getAttribute("id"));
                            String namepath=eElement.getElementsByTagName("pathname").item(0).getTextContent();
                            //System.out.println("pathname : " + namepath);
                            File fil=new File(namepath);
                            label=new favLabel(fil,fil.getName());
                            
                           // System.out.println(label.getFile().getAbsolutePath());
                            favouritesPanel.add(label);
                            MouseListener ml=new favbuttonlistener();
                            label.addMouseListener(ml);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    public class copylistener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            source=filenode.getFile();
            CUT=false;
            STH=true;
            //System.out.println(source.getAbsolutePath());
            
        }
        
    }
    public class cutlistener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
          
            source=filenode.getFile();
            CUT=true;
            STH=true;
            //System.out.println(source.getAbsolutePath());
            
        }
        
    }
      public class poplistener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println("LIIIIIIIIIIIIIST");
            Object ob=e.getSource();
            popupitem item=(popupitem)ob;
            if(item.popupgetfile().isDirectory()){
                currentContent.removeAll();
                currentContent.revalidate();
                currentContent.repaint();
                directoryPanel.removeAll();
                directoryPanel.revalidate();
                directoryPanel.repaint();
                openallbreadcrumb(item.popupgetfile());
                opendir(item.popupgetfile());

            }
            else if(item.popupgetfile().canExecute()){

                //System.out.println("EEEEXXXXEEEE");
                Runtime runtime = Runtime.getRuntime();
                try {

                    File parent=new File(item.popupgetfile().getParentFile().getAbsolutePath());

                    runtime.exec(item.popupgetfile().getPath(),null,parent);


                } catch (IOException ex) {
                    Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                try {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(item.popupgetfile());
                } catch (IOException ex) {
                    Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
        
    }
     public void findFile(String name,File file,String suffix,JPopupMenu searchpop)
    {
        File[] list = file.listFiles();
        if(list!=null){
        for (File fil : list)
        {
            if (fil.isDirectory())
            {
                findFile(name,fil,suffix,searchpop);
                if(suffix==null || suffix.equals("dir")){
                    if(fil.getName().toLowerCase().contains(name.toLowerCase())){
                        //System.out.println(fil.getAbsolutePath()+" ___dir "+fil.getName());
                        popupitem item = new popupitem(fil);
                        searchpop.add(item);
                        ActionListener sl=new poplistener();
                        item.addActionListener(sl);
                    }
                }
                
                
            }
            
            else if (fil.getName().toLowerCase().contains(name.toLowerCase()))
            {
                if(suffix!=null){
                    if(!suffix.equals("dir")){
                        if(fil.getName().endsWith(suffix)){
                           //System.out.println(fil.getAbsolutePath()+" "+fil.getName());
                            popupitem item = new popupitem(fil);
                            searchpop.add(item);
                            ActionListener sl=new poplistener();
                            item.addActionListener(sl);
                        }  
                    }
                }
                else{
                    //System.out.println(fil.getAbsolutePath()+" ___ "+fil.getName());
                    popupitem item = new popupitem(fil);
                    searchpop.add(item);
                    ActionListener sl=new poplistener();
                    item.addActionListener(sl);
                }
            }
        }
        }
    }
    public class searchlistener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            String text;
            String suffix;
            text=textField.getText();
            //System.out.println("fulltext="+text);
              
            JScrollPopupMenu searchpop= new JScrollPopupMenu("search");
            
            if(text.contains(" ")){
                String[] array;
                boolean res;

                array=text.split(" ");
                text=array[0];
                int len=array.length;
                //System.out.println("text="+text);
                //System.out.println(len);
                if(len>1){
                    suffix=null;
                    if(array[1].startsWith("type:")){
                        suffix=array[1].substring(5);
                        //System.out.println("suffix="+suffix);
                    }
                }else{
                    suffix=null;
                }
                
                
            }
            else{
                suffix=null;
            }
            File myfile=new File(dirstate);
           // System.out.println("dir="+dirstate);
            findFile(text,myfile,suffix,searchpop);
//            searchpop.setLocation(300,40);
//            searchpop.setVisible(true);
            searchpop.show(frame, 500, 40);
        }
    }

    public static long folderSize(File directory) {
        long length = 0;
        File[] list=directory.listFiles();
        if(list!=null){
        
            for (File file : list) {
                if (file.isDirectory()){
                    if(file==null){
                        //System.out.println(file.getAbsolutePath());
                    }
                    length += folderSize(file);
                    length+=file.length();
                }   
                else
                    length += file.length();
            }
        }
        else{
            //System.out.println("ouuuups!!!!!!!!!!!");
        }
        return length;
      }
      public class propertieslistener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
             
            JDialog modal=new JDialog(frame);
            modal.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            modal.setLayout(new GridLayout(10,1));
            JLabel name=new JLabel(filenode.getFile().getName());
            JLabel pathname=new JLabel(filenode.getFile().getAbsolutePath());
            
            long result; 
            if(filenode.getFile().isDirectory()){
                result=folderSize(filenode.getFile());
                result=result+filenode.getFile().length();
            }
            else{
                result=filenode.getFile().length();
            }
            JLabel size=new JLabel(result+" bytes");
           
            
            JLabel perm=new JLabel(" Permissions:");
            JCheckBox read=new JCheckBox("Readable");
            JCheckBox nonread=new JCheckBox("Non-Readable");
          
            if(filenode.getFile().canRead()){
                if(filenode.getFile().setReadable(false)){
                    read.setSelected(true);
                    nonread.setSelected(false);
                    filenode.getFile().setReadable(true);
                }
                else{
                    read.setEnabled(false);
                    nonread.setEnabled(false);
                }
                
            }
            else{
                if(filenode.getFile().setReadable(true)){
                    read.setSelected(false);
                    nonread.setSelected(true);
                    filenode.getFile().setReadable(false);
                }
                else{
                     read.setEnabled(false);
                    nonread.setEnabled(false);
                }
            }
            JCheckBox exec=new JCheckBox("Executable");
            JCheckBox nonexec=new JCheckBox("Non-Executable");
            if(filenode.getFile().canExecute()){
                if(filenode.getFile().setExecutable(false)){
                    exec.setSelected(true);
                    nonexec.setSelected(false);
                    filenode.getFile().setExecutable(true);
                }
                else{
                    exec.setEnabled(false);
                    nonexec.setEnabled(false);
                }
                
            }
            else{
                if(filenode.getFile().setExecutable(true)){
                    exec.setSelected(false);
                    nonexec.setSelected(true);
                    filenode.getFile().setExecutable(false);
                }
                else{
                    exec.setEnabled(false);
                    nonexec.setEnabled(false);
                }
               
            }
            JCheckBox write=new JCheckBox("Writable");
            JCheckBox nonwrite=new JCheckBox("Non-Writable");
            if(filenode.getFile().canWrite()){
                if(filenode.getFile().setWritable(false)){
                    write.setSelected(true);
                    nonwrite.setSelected(false);
                    filenode.getFile().setWritable(true);
                }
                else{
                    write.setEnabled(false);
                    nonwrite.setEnabled(false);
                }
                
            }
            else{
                if(filenode.getFile().setWritable(true)){
                    write.setSelected(false);
                    nonwrite.setSelected(true);
                    filenode.getFile().setWritable(false);
                }
                else{
                    write.setEnabled(false);
                    nonwrite.setEnabled(false);
                }
               
            }
            modal.add(name);
            modal.add(pathname);
            modal.add(size);
            modal.add(perm);
            modal.add(read);
          
            ItemListener itListener = new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    boolean state;
                    state=true;
                    Object source = e.getItemSelectable();
                    if (e.getStateChange() == ItemEvent.DESELECTED) {
                        
                        state=false;
                    }
                    if(state==true){
                    if (source == read) {
                       
                        filenode.getFile().setReadable(state);
                        read.setSelected(state);
                        nonread.setSelected(!state);
                    } else if (source == nonread) {
                        filenode.getFile().setReadable(!state);
                        nonread.setSelected(state);
                        read.setSelected(!state);
                    } else if (source == exec) {
                        filenode.getFile().setExecutable(state);
                        exec.setSelected(state);
                        nonexec.setSelected(!state);
                    } else if (source == nonexec) {
                        filenode.getFile().setExecutable(!state);
                        nonexec.setSelected(state);
                        exec.setSelected(!state);
                    } else if (source == write) {
                        
                        filenode.getFile().setWritable(state);
                        write.setSelected(state);
                        nonwrite.setSelected(!state);
                    } else if (source == nonwrite) {
                        filenode.getFile().setWritable(!state);
                        nonwrite.setSelected(state);
                        write.setSelected(!state);
                    }
                }
                }
            };
            read.addItemListener(itListener);
            modal.add(nonread);
            nonread.addItemListener(itListener);
            modal.add(exec);
            exec.addItemListener(itListener);
            modal.add(nonexec);
            nonexec.addItemListener(itListener);
            modal.add(write);
            write.addItemListener(itListener);
            modal.add(nonwrite);
            nonwrite.addItemListener(itListener);
            modal.pack();
            modal.setVisible(true);
        
        }
        
    }
      
    public void deletexml(File file){
        File deletefile=file;
        String deletepath=deletefile.getAbsolutePath();
        try{
            File fXmlFile = new File(favpath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("directory");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    String namepath=eElement.getElementsByTagName("pathname").item(0).getTextContent();
                    boolean res=namepath.equals(deletepath);
                    if(res){
                        DELETE=true;
                        eElement.getParentNode().removeChild(eElement);
                    }
                }
            }
            DOMSource source = new DOMSource(doc);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(favpath);

            transformer.transform(source, result);

            showfav();
            } catch (TransformerException|IOException | ParserConfigurationException | DOMException | SAXException aex) {
                aex.printStackTrace();
            }
            favouritesPanel.removeAll();
            favouritesPanel.revalidate();
            favouritesPanel.repaint();
            showfav();

    }
    public class pastelistener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            dest=filenode.getFile();
           
            try {
                if(source.isDirectory()){
                    //System.out.println("IS DIRECTORY");
                    File destFile = new File(dest, source.getName());
                    try{
                        //System.out.println("tryyyyyyyyyyy");
                        Files.copy(source.toPath(), destFile.toPath());
                        dest=new File(dest.getAbsolutePath()+filenode.getFile().separator+source.getName());
                        copyFolder(source,dest);
                        if(CUT==true){
                        deleteFolder(source);
                        source.delete();
                        CUT=false;
                        currentContent.removeAll();
                        currentContent.revalidate();
                        currentContent.repaint();
                        String subname=filenode.getFile().getName();
                        int length=subname.length();
                        String subpath=filenode.getFile().getAbsolutePath();
                        int len=subpath.length();
                        subname=subpath.substring(0, (len-length));
                        File fi=new File(subname);
                        opendir(fi);
                        
                        File fav=new File(favpath);       
                        if(fav.exists()){        
                            File deletefile=new File(subpath);
                            deletexml(source);
                            if(DELETE){
                                addxml(dest);
                                DELETE=false;
                            }
                        }
                       

                        }
                    }
                    catch(FileAlreadyExistsException ex){
                        //System.out.println("catchhhhhhhhhhhhh");
                        JDialog modal=new JDialog(frame);
                        modal.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                        JLabel text=new JLabel("Are you sure you want to override this item?");
                        modal.add(text, BorderLayout.NORTH);
                        JPanel delpanel=new JPanel(new FlowLayout());

                        JButton yes=new JButton("Yes");
                        delpanel.add(yes);
                        JButton cancel=new JButton("Cancel");
                        cancel.setBackground(Color.lightGray);
                        delpanel.add(cancel);
                        modal.setVisible(true);
                        modal.setSize(new Dimension(320, 100));
                        //modal.pack();
                        modal.add(delpanel);
                        yes.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            //System.out.print("yyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
                            modal.dispose();
                            try{
                                deleteFolder(destFile);
                                destFile.delete();
                                Files.copy(source.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                dest=new File(dest.getAbsolutePath()+dest.separator+source.getName());
                                copyFolder(source,dest);
                                if(CUT==true){
                                    deleteFolder(source);
                                    source.delete();
                                    CUT=false;
                                    currentContent.removeAll();
                                    currentContent.revalidate();
                                    currentContent.repaint();
                                    String subname=filenode.getFile().getName();
                                    int length=subname.length();
                                    String subpath=filenode.getFile().getAbsolutePath();
                                    int len=subpath.length();
                                    subname=subpath.substring(0, (len-length));
                                    File fi=new File(subname);
                                    opendir(fi);
                                     File fav=new File(favpath);       
                                    if(fav.exists()){        
                                        File deletefile=new File(subpath);
                                        
                                        deletexml(source);
                                        if(DELETE){
                                            addxml(dest);
                                            DELETE=false;
                                        }
                                    }
                                    
                                }
                            }
                            catch(IOException ex){

                            }
                            
                            }
                        });
                        cancel.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                 modal.dispose();
                            } 
                        });
                    } 
                }
                else{
                    File destFile = new File(dest, source.getName());
                    try{
                       // System.out.println("tryyyyyyyyyyy");
                        Files.copy(source.toPath(), destFile.toPath());
                        if(CUT==true){
                            deleteFolder(source);
                            source.delete();
                            CUT=false;
                            currentContent.removeAll();
                            currentContent.revalidate();
                            currentContent.repaint();
                            String subname=filenode.getFile().getName();
                            int length=subname.length();
                            String subpath=filenode.getFile().getAbsolutePath();
                            int len=subpath.length();
                            subname=subpath.substring(0, (len-length));
                            File fi=new File(subname);
                            opendir(fi);
                            
                        }
                        
                    }
                    catch(FileAlreadyExistsException ex){
                        //System.out.println("catchhhhhhhhhhhhh");
                        JDialog modal=new JDialog(frame);
                        modal.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                        JLabel text=new JLabel("Are you sure you want to override this item?");
                        modal.add(text, BorderLayout.NORTH);
                        JPanel delpanel=new JPanel(new FlowLayout());

                        JButton yes=new JButton("Yes");
                        delpanel.add(yes);
                        JButton cancel=new JButton("Cancel");
                        cancel.setBackground(Color.lightGray);
                        delpanel.add(cancel);
                        modal.setVisible(true);
                        modal.setSize(new Dimension(320, 100));
                        //modal.pack();
                        modal.add(delpanel);
                        yes.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            //System.out.print("yyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
                            //override=true;
                            modal.dispose();
                            try{
                                Files.copy(source.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                if(CUT==true){
                                    deleteFolder(source);
                                    source.delete();
                                    CUT=false;
                                    currentContent.removeAll();
                                    currentContent.revalidate();
                                    currentContent.repaint();
                                    String subname=filenode.getFile().getName();
                                    int length=subname.length();
                                    String subpath=filenode.getFile().getAbsolutePath();
                                    int len=subpath.length();
                                    subname=subpath.substring(0, (len-length));
                                    File fi=new File(subname);
                                    opendir(fi);
                                         
                                }
                            }
                            catch(IOException ex){

                            }
                            
                            }
                        });
                        cancel.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                override=false;
                                //System.out.print("ccccccccccccccccccccccc");
                                 modal.dispose();
                            } 
                        });
                    }   
                } 
            } catch (IOException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            STH=false;
        } 
    }

    public class deletelistener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            JDialog modal=new JDialog(frame);
            modal.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            JLabel text=new JLabel("Are you sure you want to delete this item?");
            modal.add(text, BorderLayout.NORTH);
            JPanel delpanel=new JPanel(new FlowLayout());
           
            JButton delete=new JButton("Delete");
            delete.setBackground(Color.red);
            delpanel.add(delete);
            JButton cancel=new JButton("Cancel");
            cancel.setBackground(Color.lightGray);
            delpanel.add(cancel);
            modal.setVisible(true);
            modal.setSize(new Dimension(320, 100));
            //modal.pack();
            modal.add(delpanel);
            delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
 
                    deleteFolder(filenode.getFile());
                    filenode.getFile().delete();
                    //CUT=false;

                    } catch (IOException ex) {
                        Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    File fav=new File(favpath);
                      
                    if(fav.exists()){        
                        File deletefile=filenode.getFile();
                        deletexml(deletefile);
                    }

                    modal.dispose();
                    currentContent.removeAll();
                    currentContent.revalidate();
                    currentContent.repaint();
                    String subname=filenode.getFile().getName();
                    int length=subname.length();
                    String subpath=filenode.getFile().getAbsolutePath();
                    int len=subpath.length();
                    subname=subpath.substring(0, (len-length));
                    File fi=new File(subname);
                            opendir(fi);
                }
            });
            cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                     modal.dispose();
                } 
            });
        }
    }
     public class renamelistener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            JDialog modal=new JDialog(frame);
            modal.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            JTextField field =new JTextField(filenode.getName(),12);
            
            modal.setLayout(new FlowLayout());
            modal.add(field);
            JButton rename=new JButton("Rename");
            modal.setVisible(true);
            
            modal.add(rename);
            modal.pack();
            rename.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String newname=field.getText();

                       // try {
                    String subname=filenode.getFile().getName();
                    int length=subname.length();
                    String subpath=filenode.getFile().getAbsolutePath();
                    int len=subpath.length();
                    subname=subpath.substring(0, (len-length));

                    newname=subname+filenode.getFile().separator+newname;  
                    File newfile=new File(newname);


                    filenode.getFile().renameTo(newfile);

                    modal.dispose();
                    currentContent.removeAll();
                    currentContent.revalidate();
                    currentContent.repaint();
                    File fi=new File(subname);
                    opendir(fi);

                    File fav=new File(favpath);       
                    if(fav.exists()){        
                        File deletefile=filenode.getFile();
                        deletexml(deletefile);
                        if(DELETE){
                            addxml(newfile);
                            DELETE=false;
                        }
                    }
                }
            });        
        }
    }
    
    public void addxml(File file){
        String path=file.getAbsolutePath();
        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(favpath);
            Element root = doc.getDocumentElement();

            Element directory = doc.createElement("directory");

            root.appendChild(directory);
            Attr attr = doc.createAttribute("idname");
            attr.setValue(path);
            directory.setAttributeNode(attr);

            Element PathName = doc.createElement("pathname");
            PathName.appendChild(doc.createTextNode(path));
            directory.appendChild(PathName);

            DOMSource source = new DOMSource(doc);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(favpath);
            try {
                transformer.transform(source, result);
            } catch (TransformerException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch(ParserConfigurationException pce){

        }
        catch(SAXException sae){

        }
        catch(IOException ioe){


        } catch (TransformerConfigurationException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
        favouritesPanel.removeAll();
        favouritesPanel.revalidate();
        favouritesPanel.repaint();
        showfav();

    }
    
        
    
    private static void copyFolder(File sourceFolder, File destinationFolder) throws IOException
    {
        if (sourceFolder.isDirectory()) 
        {
            if (!destinationFolder.exists()) 
            {  
                destinationFolder.mkdir();
                //System.out.println("Directory created :: " + destinationFolder);
            }
            String files[] = sourceFolder.list();
            if(files!=null){
                for (String file : files) 
                {
                    File srcFile= new File(sourceFolder, file);
                    File destFile = new File(destinationFolder, file);
                    copyFolder(srcFile, destFile);
                }
            }
        }
        else
        {         
            Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
            //System.out.println("File copied :: " + destinationFolder);
        }
    }
    
    private static void deleteFolder(File sourceFolder) throws IOException{
        if (sourceFolder.isDirectory()) {
            //System.out.println( sourceFolder.getAbsolutePath()+"GO FOR DELETION");
            String files[] = sourceFolder.list();
            if(files!= null){
                for (String file : files) 
                {

                    File srcFile= new File(sourceFolder, file);


                    deleteFolder(srcFile);
                }
            }
            sourceFolder.delete();
        }
        else
        {
            //System.out.println( sourceFolder.getAbsolutePath()+"HHHHOOOOOMMMMEEEERRR");
            sourceFolder.delete();
            //System.out.println("File deleted :: " + sourceFolder.getAbsolutePath());
        }
    }
    private Icon geticon(String filename,File file){
        String extension;
        File path;
        Icon icon=null;
        
        try{
            if(filename.lastIndexOf(".") != -1 && filename.lastIndexOf(".") != 0){
                extension= filename.substring(filename.lastIndexOf(".")+1);
            }
            else {
                extension=null;
            }

            if (file.isDirectory()){
                 path=new File(iconspath+"folder.png");
                // path=new File("./icons/"+"folder.png");
            }
            else{
                path=new File(iconspath+extension+".png");
                 //path=new File("./icons/"+extension+".png");
                if(!path.exists()){
                    path=new File(iconspath+"question.png");
                    //path=new File("./icons/"+"question.png");
                }   
            }
           

            URI uri = path.toURI();
            URL url;
            url = uri.toURL();

            icon=new ImageIcon(url);
            return(icon);

            }
            catch(MalformedURLException io){

            }
        
        return(icon);

    }

    public void openallbreadcrumb(File file){
        String dirname=file.getAbsolutePath();
        //System.out.println(dirname);
        boolean res=homepath.contains(dirname);
            int letters=dirname.length();
            String[] arrays;
            
            arrays=dirname.split(file.separator);
            int len=arrays.length;
           // System.out.print(len);
            String temp="";
           
            for(int i=0;i<len;i++){
                if(i>0){
                    JLabel symbol=new JLabel(">");
                    directoryPanel.add(symbol);
                    
                }
                //System.out.println(arrays[i]+i);
                
                temp=temp+file.separator+arrays[i];
                if(i==0 && OS.contains("win")){
                     temp=arrays[i];
                }
                if(i==1 && !OS.contains("win")){
                    temp=file.separator+arrays[i];
                }
                
                dirstate=temp;
                //System.out.println(temp);
                File h=new File(temp);
                String name=h.getName();
                if(i==0){
                    if(OS.contains("mac")){
                        name="/";
                    }
                    else if(OS.contains("win")){
                        name="C:";
                    }
                    else {
                        name="/";
                    }
                }
                dirbutton dbutton=new dirbutton(name,h);
                directoryPanel.add(dbutton);

                MouseListener ml=new dirlistener();
                dbutton.addMouseListener(ml);
            }
            if(len==0){
                
                String name;
                if(OS.contains("mac")){
                       name="/";
                }
                else if(OS.contains("win")){
                    name="C:";
                }
                else {
                    name="/";
                }
                temp=temp+name;
                dirstate=temp;
                File h=new File(temp);
                dirbutton dbutton=new dirbutton(name,h);
                directoryPanel.add(dbutton);
                MouseListener ml=new dirlistener();
                dbutton.addMouseListener(ml);
                
            }
    }    

    public File[] alphabetically(File file){
     
        String[] paths = file.list();
        if(paths!=null){
        int count=paths.length;
        
        String temp;
        
        for (int i = 0; i < count; i++) 
        {
            for (int j = i + 1; j < count; j++) { 
                if (paths[i].compareToIgnoreCase(paths[j])>0) 
                {
                    temp = paths[i];
                    paths[i] = paths[j];
                    paths[j] = temp;
                }
            }
        }
        File[] newfiles=new File[count];
        for(int i=0;i<count;i++){
            newfiles[i]=new File(file.getAbsolutePath()+"/"+paths[i]);
        }
        File[] tempfile=new File[count];
        File[] totalfile=new File[count];
        int counter1=0;
        int counter2=0;
        for(int i=0;i<count;i++){
            
            if(newfiles[i].isDirectory()){
                totalfile[counter1]=newfiles[i];
                counter1++;
            }
            else{
                tempfile[counter2]=newfiles[i];
                counter2++;
            }
        }
        
        for(int j=0;j<counter2;j++){
            totalfile[counter1]=tempfile[j];
            counter1++;
        }
       
       return(totalfile);
        }
        else{
            //System.out.println("uegwfruil");
            return(null);
        }
        
        
    }
    public void opendir(File file){
        File[] totalfile=alphabetically(file);
        if(totalfile !=null){
            
            for (File fil : totalfile) {
                String filename=fil.getName();
                Icon icon=geticon(filename,fil);

                mybutton contentbutton=new mybutton(filename,icon,fil);
                currentContent.add(contentbutton);
                MouseListener ml=new filelistener();
                contentbutton.addMouseListener(ml);
            }
        }
//        else{
//            System.out.println("muamuamuamua");
//        }
    }
    public class filelistener implements MouseListener{ 

        @Override
         public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    CutItem.setEnabled(false);
                    CopyItem.setEnabled(false);
                    PasteItem.setEnabled(false);                   
                    RenameItem.setEnabled(false);
                    DeleteItem.setEnabled(false);
                    AddToFavouritesItem.setEnabled(false);
                    PropertiesItem.setEnabled(false);
                    Object ob=e.getSource();
                    mybutton but=(mybutton)ob;
                    if(but.isDir()){
                        currentContent.removeAll();
                        currentContent.revalidate();
                        currentContent.repaint();
                        directoryPanel.removeAll();
                        directoryPanel.revalidate();
                        directoryPanel.repaint();
                        openallbreadcrumb(but.getFile());
                        opendir(but.getFile());
                        
                    }
                    else if(but.getFile().canExecute()){
                        
                        //System.out.println("EEEEXXXXEEEE");
                        Runtime runtime = Runtime.getRuntime();
                        try {
                            
                            File parent=new File(but.getFile().getParentFile().getAbsolutePath());
                            
                            runtime.exec(but.getFile().getPath(),null,parent);
                           
                           
                        } catch (IOException ex) {
                            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
                        }
//                            try {
//                                Runtime runTime = Runtime.getRuntime();
//
//                                String executablePath = but.getFile().getPath();
//
//                                Process process = runTime.exec(executablePath);
//                            } catch (IOException exe) {
//                                exe.printStackTrace();
//                            }
                    }
                    else{
                        
                        try {
                            Desktop desktop = Desktop.getDesktop();
                            desktop.open(but.getFile());
                        } catch (IOException ex) {
                            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                else if( SwingUtilities.isLeftMouseButton(e) ){
                    if(filenode!=null){
                        filenode.defaultBackground();
                    }
                    CutItem.setEnabled(true);
                    CopyItem.setEnabled(true);
                    if(STH==true){
                    PasteItem.setEnabled(true);   
                    }
                    RenameItem.setEnabled(true);
                    DeleteItem.setEnabled(true);
                    AddToFavouritesItem.setEnabled(true);
                    
                    
                    PropertiesItem.setEnabled(true);
                    
                    Object ob=e.getSource();
                    mybutton but=(mybutton)ob;
                    but.greyBackground();
                    filenode=new copycutNode(but.getFile(),but);
                    if(!filenode.getFile().isDirectory()){
                       AddToFavouritesItem.setEnabled(false); 
                    }
                    
                
                    
                }
                else if(SwingUtilities.isRightMouseButton(e)){
                    if(filenode!=null){
                        filenode.defaultBackground();
                    }
                    Object ob=e.getSource();
                    mybutton but=(mybutton)ob;
                    but.greyBackground();
                    filenode=new copycutNode(but.getFile(),but);
                    
                    CutItem.setEnabled(false);
                    CopyItem.setEnabled(false);
                    PasteItem.setEnabled(false);                   
                    RenameItem.setEnabled(false);
                    DeleteItem.setEnabled(false);
                    AddToFavouritesItem.setEnabled(false);
                    PropertiesItem.setEnabled(false);
                    
                    JPopupMenu popup = new JPopupMenu();
                    JMenuItem Cut = new JMenuItem("Cut");
                    JMenuItem Copy = new JMenuItem("Copy");
                    JMenuItem Paste = new JMenuItem("Paste");
                    JMenuItem Rename = new JMenuItem("Rename");
                    JMenuItem Delete = new JMenuItem("Delete");
                    JMenuItem AddToFavourites = new JMenuItem("Add to Favourites");
                    JMenuItem Properties = new JMenuItem("Properties");
                    popup.add(Cut);
                    ActionListener cl=new cutlistener();
                    Cut.addActionListener(cl);
                    popup.add(Copy);
                    ActionListener ml=new copylistener();
                    Copy.addActionListener(ml);
                    popup.add(Paste);
                    ActionListener al=new pastelistener();
                   
                    Paste.addActionListener(al);
                    Paste.setEnabled(STH);
                     if(!filenode.getFile().isDirectory()){
                        Paste.setEnabled(false);
                    }
                    popup.add(Rename);
                    ActionListener rl=new renamelistener();
                    Rename.addActionListener(rl);
                    popup.add(Delete);
                    ActionListener dl=new deletelistener();
                    Delete.addActionListener(dl);
                    popup.add(AddToFavourites);
                    ActionListener fl=new addfavlistener();
                    AddToFavourites.addActionListener(fl);
                    if(!filenode.getFile().isDirectory()){
                        AddToFavourites.setEnabled(false);
                    }
                    popup.add(Properties);
                    ActionListener pl=new propertieslistener();
                    Properties.addActionListener(pl);
                    popup.show(e.getComponent(),e.getX(),e.getY());
                }
            }
            @Override
            public void mousePressed(MouseEvent me) {
            }
            @Override
            public void mouseReleased(MouseEvent me) {
            }
            @Override
            public void mouseEntered(MouseEvent me) {
            }
            @Override
            public void mouseExited(MouseEvent me) {
            }
        
    }
    public class dirlistener implements MouseListener{
         
        @Override
        public void mouseClicked(MouseEvent e) {
                
                currentContent.removeAll();
                currentContent.revalidate();
                currentContent.repaint();
                directoryPanel.removeAll();
                directoryPanel.revalidate();
                directoryPanel.repaint();
                Object ob=e.getSource();
                dirbutton but=(dirbutton)ob;
                
                opendir(but.getFile());
                openallbreadcrumb(but.getFile());
        }

        @Override
        public void mousePressed(MouseEvent me) {
            
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            
        }

        @Override
        public void mouseEntered(MouseEvent me) {
            
        }

        @Override
        public void mouseExited(MouseEvent me) {
            
        }
    }
}
