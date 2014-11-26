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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;

public class HuffmanCompression2 implements Runnable {
    
    private static int R;
    private static BufferedImage image;
    
    HuffmanCompression2(BufferedImage image){
        this.image = image;
    }
    
    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;
        
        Node(char ch, int freq, Node left, Node right){
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }
        
        private boolean isLeaf() {
            assert(left == null && right == null) || (left != null && right != null);
            return (left == null && right == null);
        }
        
        @Override
        public int compareTo(Node that){
            return this.freq - that.freq;
        }
    }
    
    public static void compress(){
        ImageData imageData = new ImageData();
        imageData.loadRGBArrays(image);  
        R = imageData.getRows() * imageData.getColumns();
        FileWriter fw;
        File newImageCompressed = new File("Huffman_Compressed.txt");
        try{
         fw = new FileWriter(newImageCompressed);
        }catch (Exception iOException){
            fw = new FileWriter("Huffman_compressed.txt", true);
            System.out.println("file not found, creating file now");
        }
        int[] input = imageData.getImageData();
        
        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++)
            freq[input[i]]++;
        
        Node root = buildTrie(freq);
        
        String[] st = new String[R];
        buildCode(st, root, "");
        
        writeTrie(root);
        
        fw.write(input.length);
        
        for(int i = 0; i < input.length; i++){
            String code = st[input[i]];
            for (int j = 0; j < code.length(); j++){
                if (code.charAt(j) == '0') {
                    fw.write('0');                    
                }
                else if (code.charAt(j) == '1'){
                    fw.write('1');
                }
                else throw new IllegalStateException("Illegal State");
            }
        }
        fw.close();        
    }
    
    private static Node buildTrie(int[] freq){
        
        MinPQ<Node> pq = new MinPQ<Node>();
        for(char i = 0; i < R; i++){
            if(freq[i] > 0)
                pq.insert(new Node(i, freq[i], null, null));
        }
    }
    
    @Override
    public void run(){
        compress();
    }
}
