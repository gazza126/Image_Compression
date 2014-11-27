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
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class ImageHolder extends JFrame {
    ImagePanels pnlrleImage, pnlhuffImage, pnlimage;
    private BufferedImage rleImage, huffImage, image;
    private Thread rleCompressThread;    
    RLECompression rleCompress;    
    private ImageChecker imageObserver = new ImageChecker();
    ImageData imageData = new ImageData();
    
    ImageHolder(){
        
    }
    
    ImageHolder(Image image){
        
        System.out.println("Buffering image");
        this.image =imageData.toBufferedImage(image);        
        System.out.println("creating RLE compression thread");
        rleCompress = new RLECompression(image, imageObserver);        
        System.out.println("Starting threads");
        startThread();       
    }
    
    public void startThread(){
        rleCompressThread = new Thread(rleCompress, "RLECompression");        
        rleCompressThread.start();        
        while(rleCompressThread.isAlive()){
            try{
                Thread.sleep(1);
            }
            catch(Exception e){
                
            }
        }
        
    }
    
    public void showIt(String title){
        this.setTitle(title);
        this.setVisible(true);
    }
}
