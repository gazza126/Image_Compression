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
    //private Thread huffmanCompressThread;
    RLECompression rleCompress;
    //HuffmanCompression huffmanCompress;
    private ImageChecker imageObserver = new ImageChecker();
    ImageData imageData = new ImageData();
    
    ImageHolder(){
        
    }
    
    ImageHolder(Image image){
        
        System.out.println("Buffering image");
        this.image =imageData.toBufferedImage(image);        
        System.out.println("creating RLE compression thread");
        rleCompress = new RLECompression(image, imageObserver);
        //huffmanCompress = new HuffmanCompression(image, imageObserver);
        System.out.println("Starting threads");
        startThread();
        System.out.println("Getting compressed image");
        rleImage = rleCompress.getRLE();
        System.out.println("adding rle image panel.");
        pnlrleImage = new ImagePanels(rleImage);
        add(pnlrleImage);
        System.out.println("panel added");
        revalidate();
        repaint();
        try{
            File newImage = new File("Saved.bmp");
            ImageIO.write(rleImage, "BMP_RLE8", newImage);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("something fucked up");
        }
    }
    
    public void startThread(){
        rleCompressThread = new Thread(rleCompress, "RLECompression");
        //huffmanCompressThread = new Thread(huffmanCompress, "HuffmanCompression");
        rleCompressThread.start();
        //huffmanCompressThread.start();
        while(rleCompressThread.isAlive()){ //|| huffmanCompressThread.isAlive()){
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
