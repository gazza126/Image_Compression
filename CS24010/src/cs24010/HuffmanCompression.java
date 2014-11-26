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

import cs24010.ImageChecker;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Arrays;
import java.lang.*;
import java.lang.reflect.Array;

public class HuffmanCompression implements Runnable {
    
    private final BufferedImage image;     
    private final ImageData imageData = new ImageData();
    private File file;
    private PrintWriter writer;
    byte[] imageInByte;
    
    HuffmanCompression(Image image, ImageChecker imageObserver){
        this.image = (BufferedImage)image;
        
    }
    
    public BufferedImage compress(){
        BufferedImage huffmanImage = null;
        imageInByte = imageData.byteArray(image);
        createHuffmanTrie();
        
        return huffmanImage;
    }
    
    
    
    public void createHuffmanTrie(){
        int sz = imageInByte.length;
        Array[][][] huffmanTrie = new Array[sz][sz][sz];
        
    }
    
    
    //private native void imageStruct();
    
    public void run(){
        compress();
    }
}
