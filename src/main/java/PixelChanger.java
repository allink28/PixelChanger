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
        getSetPixels.randomLightDarkSquares(60);
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


                copyPixelAround(x, y, p, 10);
            }
        }
    }

    private void randomLightDarkSquares(int gridSize) {
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; x += gridSize) {
            for (int y = 0; y < height; y += gridSize) {
                double random = Math.random();
                if (random > .85) {
                    lightenAround(x, y, gridSize);
                } else if (random < .15){
                    darkenAround(x, y, gridSize);
                }
            }
        }
    }

    /**
     * Alternates darkening a grid square and lightening a grid
     * @param gridSize Must be an odd number
     */
    private void lightDarkSquares(int gridSize) {
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; x += gridSize) {
            for (int y = 0; y < height; y += gridSize) {
                if (((x+y) & 1) == 0) {
                    darkenAround(x, y, gridSize);
                } else {
                    lightenAround(x, y, gridSize);
                }
            }
        }
    }

    private void darkenAround(int x, int y, int distance) {
        for (int i = 0; i < distance && x + i < img.getWidth(); ++i) {
            for (int j = 0; j < distance && y + j < img.getHeight(); ++j) {
                img.setRGB(x+i, y+j, darkenPixel(img.getRGB(x+i, y+j)));
            }
        }
    }

    private void lightenAround(int x, int y, int distance) {
        for (int i = 0; i < distance && x + i < img.getWidth(); ++i) {
            for (int j = 0; j < distance && y + j < img.getHeight(); ++j) {
                img.setRGB(x+i, y+j, lightenPixel(img.getRGB(x+i, y+j)));
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
                copyPixelAround(x, y, p, pixelSize);
            }
        }
    }

    private void copyPixelAround(int x, int y, int p, int distance) {
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
                    img.setRGB(x, y, darkenPixel(img.getRGB(x, y)));
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
                img.setRGB(x, y, darkenPixel(img.getRGB(x, y)));
            }
        }
    }

    private void horizontalLines(int distance) {
        int width = img.getWidth();
        int height = img.getHeight();
        for (int y = 0; y < height; y += distance) {
            for (int x = 0; x < width; ++x) {
                img.setRGB(x, y, darkenPixel(img.getRGB(x, y)));
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
                    img.setRGB(x, y, darkenPixel(img.getRGB(x, y)));
                }
            }
        }
    }

    /**
     * @return Returns a darkened pixel
     */
    private int darkenPixel(int p) {
        double darkenFactor = 6;

        //get alpha
        int a = (p>>24) & 0xff;
        //get red
        int r = (int) (((p>>16) & 0xff) / darkenFactor);
        //get green
        int g = (int) (((p>>8) & 0xff) / darkenFactor);
        //get blue
        int b = (int) ((p & 0xff) / darkenFactor);

        //set the pixel value
        return (a<<24) | (r<<16) | (g<<8) | b;
    }

    private int lightenPixel(int p) {
        double lightenFactor = 6;

        //get alpha
        int a = (p>>24) & 0xff;
        //get red
        int r = Math.min((int)(((p>>16) & 0xff) * lightenFactor), 255);
        //get green
        int g = Math.min((int)(((p>>8) & 0xff) * lightenFactor), 255);
        //get blue
        int b = Math.min((int)((p & 0xff) * lightenFactor), 255);

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