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
        getSetPixels.tileEffect(9);
        getSetPixels.saveImage("src/main/resources/out/editedPicture.jpg");
    }

    private PixelChanger(String pictureLocation) {
        loadImage(pictureLocation);
    }

    private void messingAround() {
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; x += 10) {
            for (int y = 0; y < height; y += 10) {
                //get pixel value
                int p = img.getRGB(x,y);
//                //get alpha
//                int a = (p>>24) & 0xff;
//                //get red
//                int r = (p>>16) & 0xff;
//                //get green
//                int g = (p>>8) & 0xff;
//                //get blue
//                int b = p & 0xff;


                setPixelsAround(x, y, p, 10);
            }
        }
    }

    /**
     * 'Pixelizes' the image by copying one pixel onto the pixels around it
     * @param pixelSize The side length of the new 'pixels' that the image will be made of
     */
    private void pixelize(int pixelSize) {
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; x += pixelSize) {
            for (int y = 0; y < height; y += pixelSize) {
                //get pixel value
                int p = img.getRGB(x,y);
                setPixelsAround(x, y, p, pixelSize);
            }
        }
    }

    private void setPixelsAround(int x, int y, int p, int distance) {
        for (int i = 0; i < distance && x + i < img.getWidth(); ++i) {
            for (int j = 0; j < distance && y + j < img.getHeight(); ++j) {
                img.setRGB(x+i, y+j, p);
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
            int xMod = x % gridSize;
            for (int y = 0; y < height; ++y) {
                if (xMod == 0 || y % gridSize == 0) {
                    img.setRGB(x, y, darkenPixel(x, y));
                }
            }
        }
    }

    private void tileEffect(int size) {
        pixelize(size);
        darkGrid(size);
    }

    private void verticalLines(int distance) {
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; x += distance) {
            for (int y = 0; y < height; ++y) {
                img.setRGB(x, y, darkenPixel(x, y));
            }
        }
    }

    private void horizontalLines(int distance) {
        int width = img.getWidth();
        int height = img.getHeight();
        for (int y = 0; y < height; y += distance) {
            for (int x = 0; x < width; ++x) {
                img.setRGB(x, y, darkenPixel(x, y));
            }
        }
    }

    private void diagonalLines(int distance) {
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; ++x) {
            int xMod = x % distance;
            for (int y = 0; y < height; ++y) {
                if ( xMod == (y % distance)) {
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