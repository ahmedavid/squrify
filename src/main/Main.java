package main;

import javax.swing.JFrame;

class Main {
    // Usage : java squarify original.jpg 5 S
    public static void main(String[] args) {
        String filename = "original1.jpg";
        String mode = "S";
        int squareSize = 28;
        if(args.length < 3) {
            System.out.println("Error: Not enough or wrong arguments");
            System.out.println("Example Usage : [ java squarify original.jpg 5 S ]");
            System.exit(1);
        } else {
            filename = args[0];
            squareSize = Integer.parseInt(args[1]);
            mode = args[2];
        }
        // For simplicity, ImageViewer will be used as coordinator between thread pool and image buffer
        // Also to display image progression
        ImageViewer viewer = new ImageViewer(squareSize,filename);
        initWindow(viewer);

        boolean isMultithreaded = mode.equals("M");
        viewer.processImage(isMultithreaded);
    }

    private static void initWindow(ImageViewer viewer) {
        JFrame frame = new JFrame();
        frame.setBounds(0, 0, 800, 600);
        frame.setTitle("Squarify");
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(viewer);
    }
}