package main;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.image.BufferedImage;

public class ImageViewer extends JPanel {
    boolean isRunning = false;
    BufferedImageManager bim;
    BufferedImage backBuffer;
    Graphics backBufferGraphics;
    private int squareSize;
    private ThreadPoolManager threadPool;

    public ImageViewer(int squareSize, String filename) {
        this.squareSize = squareSize;

        // Open Original Image
        bim = new BufferedImageManager(squareSize, filename);
        BufferedImage whole = bim.getWholeImage();
        
        backBuffer = new BufferedImage(whole.getWidth() * 2, whole.getHeight(), BufferedImage.TYPE_INT_RGB);
        backBufferGraphics = backBuffer.getGraphics();
        backBufferGraphics.drawImage(whole, 0, 0, this);

        // Use thread pool approach in order to eliminate overhead for spawning a new thread for each task
        threadPool = new ThreadPoolManager();
    }

    public void processImage(boolean isMultiThreaded) {
        long startTime = System.currentTimeMillis();

        BufferedImage whole = bim.getWholeImage();
        boolean isProcessingDone = false;
        if(isMultiThreaded) {
            for(int y=0; y<whole.getHeight(); y+=squareSize)
            for(int x=0; x<whole.getWidth(); x+=squareSize) {
                ImageChunkProcessor icp = new ImageChunkProcessor(this, x, y, squareSize);
                threadPool.submitTask(icp);
            }
            threadPool.shutdown();
            isProcessingDone = threadPool.awaitTermination();

            
        } else {
            for(int y=0; y<whole.getHeight(); y+=squareSize)
            for(int x=0; x<whole.getWidth(); x+=squareSize) {
                ImageChunkProcessor icp = new ImageChunkProcessor(this, x, y, squareSize);
                icp.run();
            }
            isProcessingDone = true;
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Processing took : " + elapsedTime + "ms");

        // Wait for tasks to complete and save final image
        if(isProcessingDone) {
            System.out.println("Processing DONE");
            // Pick right side of buffer image, which contains better version 
            BufferedImage img = bim.getWholeImage();
            int w = img.getWidth();
            int h = img.getHeight();
            BufferedImage res = backBuffer.getSubimage(w / 2, 0, w / 2, h);
            bim.createResultFile(res);
        }
        else {
            System.out.println("Something went wrong");
        }
    }

    public Color[][] getPixelArray() {
        return bim.getPixelArray();
    }

    public void triggerRepaint(int x, int y, Color c) {
        update(x,y,c);
        repaint();
    }

    /* This method is called after a thread that has calculated average for a square
    // We have to specify this as synchronized in order to prevent race condition to change the color of square
    // Because each thread is working on different chunks of pixel array there is no race condition for wrongly 
    // overwriting bufferImage
    // Only issue is for setting the color, Alternatively we could have use a mutex lock
    */
    private synchronized void update(int x, int y, Color c) {
        backBufferGraphics.setColor(c);
        backBufferGraphics.fillRect(x, y, squareSize, squareSize);
    }

    public void paint(Graphics g) {
        // Repaint screen
        g.drawImage(backBuffer, 0, 0, null);

        // Write some text for info (Simple and Advanced AVG)
        Font font = g.getFont().deriveFont(20.0f);
        g.setFont(font);
        g.setColor(Color.WHITE);
        int x = bim.getWholeImage().getWidth()/4;
        int y = 50;
        g.drawString("Simple AVG", x, y);
        g.drawString("Advanced AVG", x + 300, y);
    }
}
