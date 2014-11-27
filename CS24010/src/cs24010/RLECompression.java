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

import com.sun.imageio.plugins.bmp.BMPImageReader;
import com.sun.imageio.plugins.bmp.BMPImageWriter;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.bmp.BMPImageWriteParam;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;

public class RLECompression implements Runnable {
    private final BufferedImage image;
    BufferedImage imageRLE = null;    
    private final ImageData imageData = new ImageData();
    byte[] imageByteArray;
    private static int sz;
    int[] pixelData;
    int[] newPixelData;
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
        //pixelData = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        writeToFiles();
        byte[] runLengthEncode = getRunLength();
        runLengthEncode = setHeader(runLengthEncode);
        runLengthEncode = checkForEOF(runLengthEncode);
        //newPixelData = getRunLength();
        //getRunLength();
        runLengthEncode = decode(runLengthEncode);
        stringToImage(runLengthEncode);
        System.out.println("Finished Compressing");
        return imageRLE;
    }
    
    public byte[] checkForEOF(byte[] runLengthEncode){
        /*for(int i =0; i< runLengthEncode.length; i++){
            if(i < runLengthEncode.length && runLengthEncode[i] == -1){
                runLengthEncode[i] = 1;
            }             
        }*/
        /*runLengthEncode[runLengthEncode.length -4] = 0;
        runLengthEncode[runLengthEncode.length -3] = 0;
        runLengthEncode[runLengthEncode.length -2] = 0;
        runLengthEncode[runLengthEncode.length -1] = 1;*/
        sz = imageByteArray.length;
        int j = 8;
        for(int i = runLengthEncode.length - 8; i < runLengthEncode.length; i++){
            if(j > 0)
                runLengthEncode[i] = imageByteArray[sz - j];
            j--;
        }
        return runLengthEncode;
    }
    
    public byte[] setHeader(byte[] runLengthEncode){
        for(int i =0; i < 54; i++){
            runLengthEncode[i] = imageByteArray[i];
        }
        runLengthEncode[30] = 1;
        runLengthEncode[31] = 0;
        runLengthEncode[32] = 0;
        runLengthEncode[33] = 0;        
        return runLengthEncode;
    }
    
    
    public void writeToFiles(){        
        try{
            PrintWriter fos1 = new PrintWriter("original.txt");
            //PrintWriter fos2 = new PrintWriter("compressed.txt");
            fos1.print(Arrays.toString(pixelData));
            //byte[] b = x.getBytes();
            //fos2.print(Arrays.toString(b));
            fos1.close();
            //fos2.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }
    
    public void stringToImage(byte[] runLengthEncode){        
        ByteArrayInputStream bais = new ByteArrayInputStream(runLengthEncode);        
        try{
            PrintWriter pw = new PrintWriter("runLength2.txt");
            PrintWriter pw2 = new PrintWriter("original2.txt");
            for(int i = 0; i < imageByteArray.length; i++)
                pw2.print(imageByteArray[i]+"("+i+"), ");
            for(int i = 0; i < runLengthEncode.length; i++)
                pw.print(runLengthEncode[i]+"("+i+"), ");
            pw.close();
            pw2.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        try{ 
            File file = new File("runLength.txt");  
            BMPImageWriteParam imageWriteParam = new BMPImageWriteParam();
            imageWriteParam.setCompressionMode(2);
            imageWriteParam.setCompressionType("BI_RLE8");
            imageWriteParam.setTopDown(true);
            BMPImageWriter bmpImageWriter =(BMPImageWriter)ImageIO.getImageWritersByFormatName("bmp").next();                   
            FileImageOutputStream output = new FileImageOutputStream(file);            
            bmpImageWriter.setOutput(output);            
            
            
            BMPImageReader bmpImageReader =(BMPImageReader)ImageIO.getImageReader(bmpImageWriter);
            ImageInputStream iis = ImageIO.createImageInputStream(bais);
            bmpImageReader.setInput(iis);
            ImageReadParam param = bmpImageReader.getDefaultReadParam();
            imageRLE = bmpImageReader.read(0, param);
            
            
            IIOImage outImage = new IIOImage(imageRLE, null, null);
            bmpImageWriter.write(null, outImage,imageWriteParam);
            bmpImageWriter.dispose();
            bmpImageReader.dispose();
            
            //imageRLE = imageData.createBufferedImage(imageByteArray ,runLengthEncode);      
            //ImageIO.write(imageRLE, "BMP", new File("newImage.png"));
            bais.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        
        if(imageRLE == null)
            System.out.println("imageRLE is null");
        
    }
    
    public byte[] getRunLength(){
        ByteArrayOutputStream dest = new ByteArrayOutputStream();  
        byte lastByte = imageByteArray[0];
        int matchCount = 1;        
        for(int i=55; i < imageByteArray.length; i++){
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
    
    public byte[] decode(byte[] source){
        System.out.println(imageByteArray.length);
        byte[] decodedImage = new byte[imageByteArray.length];
        for(int i = 0; i < 55; i++){
            decodedImage[i] = source[i];
        }
        for(int i = 55; i < source.length; i++){
            int runLength = 0;
            byte currentByte = 0;
            if(i < source.length - 1){
                currentByte = source[i+1];
                runLength = source[i];
            }
            else break;
            while(i < i * runLength){
                if(source[i+1] != currentByte){
                    break;                    
                }
                decodedImage[i] = currentByte;
                i++;
            }
        }
        return decodedImage;
        /*
        try{
            PrintWriter pw = new PrintWriter("DecodedImage.txt");
            for(int i = 0; i < decodedImage.length; i++)
                pw.print(decodedImage[i]+", ");
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(decodedImage);
        try{
            BufferedImage newImage = ImageIO.read(bais);
            return newImage;
        }catch(Exception e){
            System.out.println("returning null");
            return null;
        }*/
        
    }
    
    @Override
    public void run(){
        compress();
    }
    
}
