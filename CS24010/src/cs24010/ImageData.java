/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs24010;

/**
 *
 * @author Gaz
 */
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageData {
    private int rows;
    private int columns;
    private int rowf;
    private int columnf;
    
    private static BufferedImage inputImage;
    private static BufferedImage outputImage;
    
    private short[][] red;
    private short[][] green;
    private short[][] blue;
    
    private int[] imageData;
    

    
    int M, N;
    
    ImageData(){
        
    }
    
    public static BufferedImage toBufferedImage(Image img)
{
    if (img instanceof BufferedImage)
    {
        return (BufferedImage) img;
    }

    // Create a buffered image with transparency
    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    // Draw the image on to the buffered image
    Graphics2D bGr = bimage.createGraphics();
    bGr.drawImage(img, 0, 0, null);
    bGr.dispose();

    // Return the buffered image
    return bimage;
}
    
    
    public byte[] byteArray(BufferedImage image){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageInByte = null;
        try{
            ImageIO.write(image, "BMP", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        
        return imageInByte;
    }
    
    public void loadRGBArrays(BufferedImage image){
        if(image.getHeight() != rows || image.getWidth() != columns){
            rows = image.getHeight();
            columns = image.getWidth();
            
            red = new short[rows][columns];
            green = new short[rows][columns];
            blue = new short[rows][columns];
        }
        
        imageData = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        int index;
        for(int row = 0; row < rows; row++){
            for (int col = 0; col < columns; col++){
                index = (row * columns) + col;
                unpackPixel(imageData[index], red, green, blue, row, col);
            }            
        }
        
    }
    
    private static void unpackPixel(int pixel, short[][] red, short[][] green, short[][] blue, int row, int col){
        red[row][col] = (short)((pixel >> 16) & 0xFF);
        green[row][col] = (short)((pixel >> 8) & 0xFF);
        blue[row][col] = (short)((pixel >> 0) & 0xFF);
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRowf() {
        return rowf;
    }

    public void setRowf(int rowf) {
        this.rowf = rowf;
    }

    public int getColumnf() {
        return columnf;
    }

    public void setColumnf(int columnf) {
        this.columnf = columnf;
    }

    public static BufferedImage getInputImage() {
        return inputImage;
    }

    public static void setInputImage(BufferedImage inputImage) {
        ImageData.inputImage = inputImage;
    }

    public static BufferedImage getOutputImage() {
        return outputImage;
    }

    public static void setOutputImage(BufferedImage outputImage) {
        ImageData.outputImage = outputImage;
    }

    public short[][] getRed() {
        return red;
    }

    public void setRed(short[][] red) {
        this.red = red;
    }

    public short[][] getGreen() {
        return green;
    }

    public void setGreen(short[][] green) {
        this.green = green;
    }

    public short[][] getBlue() {
        return blue;
    }

    public void setBlue(short[][] blue) {
        this.blue = blue;
    }
    
    public int[] getImageData(){
        return imageData;
    }

    public int getImageData(int i) {
        return imageData[i];
    }
    
    public int getImageDataLength(){
        return imageData.length;
    }

    public void setImageData(int[] imageData) {
        this.imageData = imageData;
    }

    public int getM() {
        return M;
    }

    public void setM(int M) {
        this.M = M;
    }

    public int getN() {
        return N;
    }

    public void setN(int N) {
        this.N = N;
    }
    
    
}
