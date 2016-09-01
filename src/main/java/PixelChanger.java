package main.java;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Created by allenpreville on 9/1/16.
 * Just for fun.
 * Based on https://www.dyclassroom.com/image-processing-project/how-to-get-and-set-pixel-value-in-java
 */
public class PixelChanger{
    private BufferedImage img = null;

    public static void main(String args[])throws IOException{
        PixelChanger getSetPixels = new PixelChanger("src/main/resources/pic.jpg");
        getSetPixels.messingAround();
        getSetPixels.saveImage("src/main/resources/out/editedPicture.jpg");
    }

    private PixelChanger(String pictureLocation) {
        loadImage(pictureLocation);
    }

    private void messingAround() {
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                //get pixel value
                int p = img.getRGB(x,y);
                //get alpha
                int a = (p>>24) & 0xff;
                //get red
                int r = (p>>16) & 0xff;
                //get green
                int g = (p>>8) & 0xff;
                //get blue
                int b = p & 0xff;

                if (x % 10 == 0 || y % 10 == 0) {
                    img.setRGB(x, y, darkenPixel(x, y));
                }

                //set the pixel value
//                p = (a<<24) | (r<<16) | (g<<8) | b;
//                img.setRGB(x, y, p);
            }
        }
    }

    /**
     * Creates a grid by darkening pixels
     * @param gridSize The number of pixels tall & wide the grid will be.
     */
    private void darkGrid(int gridSize) {
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (x % gridSize == 0 || y % gridSize == 0) {
                    img.setRGB(x, y, darkenPixel(x, y));
                }
            }
        }
    }

    /**
     * @return Returns a darkened pixel at the given coordinate
     */
    private int darkenPixel(int x, int y) {
        //get pixel value
        int p = img.getRGB(x,y);
        //get alpha
        int a = (p>>24) & 0xff;
        //get red
        int r = ((p>>16) & 0xff) / 2;
        //get green
        int g = ((p>>8) & 0xff) / 2;
        //get blue
        int b = (p & 0xff) / 2;

        //set the pixel value
        return (a<<24) | (r<<16) | (g<<8) | b;
    }

    private void loadImage(String loadLocation) {
        try{
            File f = new File(loadLocation);
            img = ImageIO.read(f);
        }catch(IOException e){
            System.err.println(e);
        }
    }

    private void saveImage(String saveLocation) {
        try{
            File f = new File(saveLocation);
            ImageIO.write(img, "jpg", f);
        }catch(IOException e){
            System.err.println(e);
        }
    }
}