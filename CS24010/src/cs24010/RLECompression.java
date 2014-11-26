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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;


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
        ByteBuffer runLengthEncode = getRunLength();        
        System.out.println(runLengthEncode);
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
    
    public BufferedImage stringToImage(ByteBuffer runLengthEncode){
        byte[] dest = null;
        runLengthEncode.get(dest);
        ByteArrayInputStream bais = new ByteArrayInputStream(dest);
        try{
            imageRLE = ImageIO.read(new ByteArrayInputStream(dest));
        }catch(IOException e){
            
        }
        //decode(runLengthEncode);
        if(imageRLE == null)
            System.out.println("imageRLE is null");
        return imageRLE;
    }
    
    public ByteBuffer getRunLength(){
        ByteBuffer dest = ByteBuffer.wrap(new byte[imageByteArray.length]);
        dest.put(imageByteArray);
        dest.flip();
        for(int i =0; i < imageByteArray.length; i++){
            int runlength = 1;
            while(i+1 < imageByteArray.length && imageByteArray[i] == imageByteArray[i+1]){
                runlength++;
                i++;
                
            }     
            
            
            dest.putInt(runlength);            
            dest.put(imageByteArray[i]);
            
        }
        return dest;
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
