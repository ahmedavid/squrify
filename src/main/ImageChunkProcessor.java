package main;

import java.awt.Color;

public class ImageChunkProcessor implements Runnable {
    private int squareSize;
    private int startX;
    private int startY;
    private int sq;
    ImageViewer viewer;

    public ImageChunkProcessor(ImageViewer viewer, int startX, int startY, int squareSize) {
        this.startX = startX;
        this.startY = startY;
        this.viewer = viewer;
        this.squareSize = squareSize;
        this.sq = squareSize * squareSize;
    }

    // Naive Algorithm to calculate average color in square
    public void calcAverageColor() {
        Color[][] pixelArray = viewer.getPixelArray();
        int totalR = 0, totalG = 0, totalB = 0;
        for (int i = startY; i < startY + squareSize; i++)
            for (int j = startX; j < startX + squareSize; j++) {
                totalR += pixelArray[i][j].getRed();
                totalG += pixelArray[i][j].getGreen();
                totalB += pixelArray[i][j].getBlue();
            }
        int newR = totalR / sq;
        int newG = totalG / sq;
        int newB = totalB / sq;
        Color newColor = new Color(newR, newG, newB);


        for (int i = startY; i < startY + squareSize; i++)
        for (int j = startX; j < startX + squareSize; j++)
            pixelArray[i][j] = newColor;

        viewer.triggerRepaint(startX,startY,newColor);
    }

    // Slightly better algorithm that utilises sqrt to preserve lightness in square
    // https://www.youtube.com/watch?v=LKnqECcg6Gw
    public void calcAverageColorAdvanced() {
        Color[][] pixelArray = viewer.getPixelArray();
        int totalR = 0, totalG = 0, totalB = 0;
        for (int i = startY; i < startY + squareSize; i++){
            for (int j = startX; j < startX + squareSize; j++) {
                int red = pixelArray[i][j].getRed();
                int green = pixelArray[i][j].getGreen();
                int blue = pixelArray[i][j].getBlue();
                totalR += red * red;
                totalG += green * green;
                totalB += blue * blue;
            }
        }
        int newR = (int) Math.sqrt(totalR / sq);
        int newG = (int) Math.sqrt(totalG / sq);
        int newB = (int) Math.sqrt(totalB / sq);

        Color newColor = new Color(newR, newG, newB);

        for (int i = startY; i < startY + squareSize; i++)
        for (int j = startX; j < startX + squareSize; j++)
            pixelArray[i][j] = newColor;
            
        viewer.triggerRepaint(startX,startY,newColor);
    }

    @Override
    public void run() {
        System.out.print("Current thread : " + Thread.currentThread().getName());
        // Process right side with advanced algorith to see the difference
        if(startX >= viewer.getPixelArray().length / 2) {
            System.out.print(" is Executing Advanced Average\n");
            calcAverageColorAdvanced();

        } else {
            System.out.print(" is Executing Simple Average\n");
            calcAverageColor();
        }
    }
}
