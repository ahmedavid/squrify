# Welcome to Squarify!
### The best software to ~~ruin~~ squrify all your precious images

## Classes
#### Main.java
Resposible for initializing a JFrame window. And kicking off processing.
#### BufferedImageManager.java

 - Opens image file
 - Creates a BufferedImage object to represent image state for display
   progress
 - Also has  2D Color[][] pixelArray which threads are working on
 - Finally it has createResultFile() method to save final result to disk. 

#### ImageChunkProcessor.java

 - Implements Runnable Interface so that it can be run concurrently.
 - calcAverageColor() method uses naive approach to calculate average
   color in a square
 - calcAverageColorAdvanced() method uses slightly better approach to
   preserve lightness better in a square. Idea is to add to the total,
   the square of the pixel component and use square root when
   calculating final pixel components (RGB).
   https://www.youtube.com/watch?v=LKnqECcg6Gw

I use both methods to demonstrate the difference side by side. If you use big enough squresize you will observe that right side image squares are slightly brighter.


#### ThreadPoolManager.java

 - Using Java standard Executor API, maintain a pool of threads.
 - All we have to do is to create instance of  ImageChunkProcessor
   object with given coordinates of chunk of the array and submit with
   submitTask() method.

#### ImageViewer.java

 - processImage() method kicks of processing in "Single" or
   "Multithreaded" mode depending on preference.
 - Intializes and submits tasks to thread pool in multi mode.
 - update() method gets called by instances of ImageChunkProcessor to
   refresh screen in order to reflect changes in color array.

