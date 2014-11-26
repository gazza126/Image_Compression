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
import java.awt.Image;
public class ImageChecker implements java.awt.image.ImageObserver{
    
    @Override
    public boolean imageUpdate(Image image, int a, int b, int c, int d, int e){
        return false;
    }
}
