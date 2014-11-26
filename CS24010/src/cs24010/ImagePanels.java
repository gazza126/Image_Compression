/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs24010;

/**
 *
 * @author Gareth
 */

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImagePanels extends JPanel {
    BufferedImage image;
    ImagePanels(BufferedImage image){
        this.image = image;
        System.out.println("repainting");
        revalidate();
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(image,0,0,null);
    }
}
