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
import java.util.Observer;
import java.util.Observable;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.plugins.bmp.BMPImageWriteParam;
import javax.imageio.spi.ImageReaderSpi;


public class RLECompression implements Runnable {
    private BufferedImage image;
    BufferedImage imageRLE = null;
    private ImageChecker imageObserver;
    private ImageData imageData = new ImageData();
    byte[] imageByteArray;
    private static int sz;
    RLECompression(Image image, ImageChecker imageObserver){
        this.image = (BufferedImage)image;       
        
        sz = imageData.getRows() * imageData.getColumns();
        
        
        
        
        
        
    }
    
    public BufferedImage getRLE(){
        return imageRLE;
    }
    
    private BufferedImage compress(){
        
        System.out.println("Compressing");
        imageByteArray = imageData.byteArray(image);
        byte[] runLengthEncode = getRunLength();        
        //System.out.println(runLengthEncode);
        stringToImage(runLengthEncode);
        System.out.println("Finished Compressing");
        return imageRLE;
    }
    
    
    public void writeToFiles(String x){        
        try{
            PrintWriter fos1 = new PrintWriter("original.txt");
            PrintWriter fos2 = new PrintWriter("compressed.txt");
            fos1.print(Arrays.toString(imageByteArray));
            byte[] b = x.getBytes();
            fos2.print(Arrays.toString(b));
            fos1.close();
            fos2.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }
    
    public BufferedImage stringToImage(byte[] runLengthEncode){        
        ByteArrayInputStream bais = new ByteArrayInputStream(runLengthEncode);
        try{
            PrintWriter pw = new PrintWriter("runLength2.txt");
            PrintWriter pw2 = new PrintWriter("original2.txt");
            for(int i = 0; i < imageByteArray.length; i++)
                pw2.print(imageByteArray[i]+", ");
            for(int i = 0; i < runLengthEncode.length; i++)
                pw.print(runLengthEncode[i]);
            pw.close();
            pw2.close();
        }catch(Exception e){
            
        }
        
        try{
            
            
            File file = new File("runLength.txt");
            imageRLE = ImageIO.read(bais);
            
            ImageIO.write(imageRLE, "bmp", new File("newImage.bmp"));
            
        }catch(IOException e){
            e.printStackTrace();
        }
        //decode(runLengthEncode);
        if(imageRLE == null)
            System.out.println("imageRLE is null");
        return imageRLE;
    }
    
    public byte[] getRunLength(){
        ByteArrayOutputStream dest = new ByteArrayOutputStream();  
        byte lastByte = imageByteArray[0];
        int matchCount = 1;
        for(int i=1; i < imageByteArray.length; i++){
            byte thisByte = imageByteArray[i];
            if (lastByte == thisByte) {
                matchCount++;
            }
            else {
                dest.write((byte)matchCount);  
                dest.write((byte)lastByte);
                matchCount=1;
                lastByte = thisByte;
            }                
        }
        dest.write((byte)matchCount);  
        dest.write((byte)lastByte);
        return dest.toByteArray();
    }
    
    public BufferedImage decode(String source){
        StringBuffer dest = new StringBuffer();
        Pattern pattern = Pattern.compile("[0-9]+[-]?+[0-9]");
        Matcher matcher = pattern.matcher(source);
        while(matcher.find()){
            int number = Integer.parseInt(matcher.group());
            matcher.find();
            while(number-- != 0){
                dest.append(matcher.group());
            }
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(dest.toString().getBytes());
        try{
            imageRLE = ImageIO.read(bais);
        }catch(Exception e){
            return null;
        }
        return null;
    }
    
    @Override
    public void run(){
        compress();
    }
    
}
