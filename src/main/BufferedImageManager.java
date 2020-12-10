package main;

import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

public class BufferedImageManager {
    private BufferedImage bufferedImage;
    Color[][] pixelArray;
    private int imgWidth;
    private int imgHeight;
    private int squareSize;

    public BufferedImageManager(int squareSize, String filename) {
        this.squareSize = squareSize;
        openImage(filename);
        initPixelArray();
    }

    public BufferedImage getWholeImage() {
        return bufferedImage;
    }

    private void openImage(String filename) {
        URL url = this.getClass().getResource(filename);
        try {
            BufferedImage readImage = ImageIO.read(url);
            // Divide and multiply by squre size to get rid of extra pixels (Resize)
            imgWidth = (readImage.getWidth() / squareSize) * squareSize;
            imgHeight = (readImage.getHeight() / squareSize) * squareSize;
            readImage = readImage.getSubimage(0, 0, imgWidth, imgHeight);
            imgWidth *= 2;
            // Allocate Screen space to render both approaches of calculating average for demo purposes (Simple and Advanced)
            bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics g = bufferedImage.getGraphics();
            g.drawImage(readImage,0,0,null);
            g.drawImage(readImage,imgWidth/2,0,null);
        } catch (IOException e) {
            System.out.println("Original image not found !!!");
            e.printStackTrace();
        }
    }

    private void initPixelArray() {
        pixelArray = new Color[imgHeight][imgWidth];
        for(int y=0; y<imgHeight; y++)
        for(int x=0; x<imgWidth; x++) {
            int pixel = bufferedImage.getRGB(x, y);
            Color color = new Color(pixel,false);
            pixelArray[y][x] = color;
        }
    }

    public Color[][] getPixelArray() {
        return pixelArray;
    }

    public void createResultFile(BufferedImage result) {
        File resultImg = new File("result.jpg");
        try {
            ImageIO.write(result, "jpg", resultImg);
            System.out.println("result.jpg successfuly saved");
        } catch (IOException e) {
            System.out.println("Failed to save result image!");
            e.printStackTrace();
        }
    }
}
