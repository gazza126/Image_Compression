/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs24010;

/**
 *
 * @author gjj12
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;



public class StartCompression extends JFrame implements ActionListener {
    JPanel btnPanel;
    JButton guiStart;
    JButton guiExit;
    final JFileChooser fc = new JFileChooser();
    //int returnVal = fc.showOpenDialog(btnPanel);
    
    
    public StartCompression(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.setSize(325,200);
        
        btnPanel = new JPanel();
        add(btnPanel);
        
        guiStart = new JButton("Start Compression");
        guiStart.setPreferredSize(new Dimension(150,50));
        btnPanel.add(guiStart);
        
        guiExit = new JButton("Exit");
        guiExit.setPreferredSize(new Dimension(150,50));
        btnPanel.add(guiExit);
        
        guiStart.addActionListener(this);
        guiExit.addActionListener(this);
        
    }
    
    public void showIt(String title){
        this.setTitle(title);
        this.setVisible(true);
    }      
    
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == guiStart){            
            int returnVal = fc.showOpenDialog(this);
            try{
                if(returnVal == JFileChooser.APPROVE_OPTION){
                   File file = fc.getSelectedFile();
                
                   ImageHolder imageHolder = new ImageHolder(ImageIO.read(file));
                  imageHolder.showIt("Compression");
                
                }
            } catch(Exception iOException){
                
            }
            this.setVisible(false);
            
            
        }
    }
}
