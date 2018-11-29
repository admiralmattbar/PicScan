package org.gardenstreetacademy;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    public static void main(String args[]) throws IOException {


        if(args.length < 1){
            System.out.println("Please list filename of image including extension (.png, .jpg, .bmp).");
            System.out.println("Make sure image is in same directory as .jar file.");
            System.out.println("This program takes the filenames of the pictures (including extensions) as the only arguments.");
            return;
        }



        System.out.println(args.length);

        String[] imageName = new String[args.length];
        String[] imageFileName = new String[args.length];
        String[] codeFileName = new String[args.length];

        int[] picWidth = new int[args.length];
        int[] picHeight = new int[args.length];

        int i;
        for(i=0; i<args.length; i++) {

            imageName[i] = args[i].substring(0, args[i].length() - 4);

            imageFileName[i]= imageName[i] + "_TEST.png";
            codeFileName[i] = "neomatrix_function_" + imageName[i] + ".txt";
            PrintWriter arduinoCode = new PrintWriter(codeFileName[i], "UTF-8");



            BufferedImage newImage = null;
            File f2 = null;


            BufferedImage image = null;
            File f = null;

            try {

                f = new File("./" + args[i]);
                //image = new BufferedImage(picWidth, picHeight, BufferedImage.TYPE_INT_ARGB);
                image = ImageIO.read(f);
                picWidth[i] = image.getWidth();
                picHeight[i] = image.getHeight();

                System.out.println("Width: " + picWidth[i] + " Height: " + picHeight[i]);
                System.out.println("Reading complete.");

            } catch (IOException e) {
                System.out.println("Error: " + e);
            }


            newImage = new BufferedImage(picWidth[i], picHeight[i], BufferedImage.TYPE_INT_ARGB);

            arduinoCode.println("void " + imageName[i] + "()"); //Create function name.
            arduinoCode.println("{");
            arduinoCode.println("    //Past this function into Arduino IDE and call it in setup() or loop() to get the image...");
            arduinoCode.println("    matrix.fillScreen(0);"); //Start function wiping screen (no pixels left from previous image)
            arduinoCode.println("");


            //Go through image starting at upper left (0, 0).
            System.out.println(imageName[i] + " Details...");
            if (picWidth[i] > 0 && picHeight[i] > 0) { //Make sure pic is valid size.
                int y;
                for (y = 0; y < picHeight[i]; y++) {
                    int x;
                    for (x = 0; x < picWidth[i]; x++) {
                        int rgbValue = image.getRGB(x, y); //Get ARGB data of pixel
                        int red = (rgbValue >> 16) & 0xff; //Find integer values for RGB
                        int green = (rgbValue >> 8) & 0xff;
                        int blue = rgbValue & 0xff;

                        System.out.println("x=" + x + ", y=" + y + "   " + "(" + red + ", " + green + ", " + blue + ")");

                        if (!(red == 0 && green == 0 && blue == 0))
                            arduinoCode.println("    matrix.drawPixel(" + x + ", " + y + ", matrix.Color(" + red + ", " + green + ", " + blue + "));");

                        newImage.setRGB(x, y, rgbValue);
                    }
                }
            } else {
                System.out.println("Image size must be greater than 0 x 0. Either image size invalid or filename doesn't exist.");
                return;
            }

            arduinoCode.println("");
            arduinoCode.println("    matrix.show()");

            arduinoCode.println("}");
            arduinoCode.close();
            System.out.println("");


            //write image
            try {
                f2 = new File("./" + imageFileName[i]);
                ImageIO.write(newImage, "png", f2);
                System.out.println("Writing complete.");
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }

            System.out.println("");
        }

        int j;
        for(j=0; i<args.length; j++) {

            System.out.println("Check " + imageFileName[j] + " for accuracy.");
            System.out.println("Check " + codeFileName[j] + " for NeoMatrix function.");
            System.out.println("Width: " + picWidth[j] + " Height: " + picHeight[j]);
        }
    }
}
