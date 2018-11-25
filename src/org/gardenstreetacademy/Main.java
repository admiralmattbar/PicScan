package org.gardenstreetacademy;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) throws IOException {


        if(args.length != 1){
            System.out.println("Please list filename of image including extension (.png, .jpg, .bmp).");
            System.out.println("Make sure image is in same directory as .jar file.");
            System.out.println("Filename is the only argument this program takes.");
            return;
        }

        String imageName = args[0].substring(0, args[0].length() - 4);
        String imageFileName = imageName + "_TEST.png";
        String codeFileName = "neomatrix_function_" + imageName + ".txt";
        PrintWriter arduinoCode = new PrintWriter(codeFileName, "UTF-8");

        int picWidth = 0;
        int picHeight = 0;

        BufferedImage newImage = null;
        File f2 = null;



        BufferedImage image = null;
        File f = null;

        try {

            f = new File("./" + args[0]);
            //image = new BufferedImage(picWidth, picHeight, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(f);
            picWidth = image.getWidth();
            picHeight = image.getHeight();

            System.out.println("Width: " + picWidth + " Height: " + picHeight);
            System.out.println("Reading complete.");

        } catch(IOException e) {
            System.out.println("Error: " + e);
        }




        newImage = new BufferedImage(picWidth, picHeight, BufferedImage.TYPE_INT_ARGB);

        arduinoCode.println("//Past this into a function to call the image in lights...");
        arduinoCode.println("void " + imageName + "()"); //Create function name.
        arduinoCode.println("{");
        arduinoCode.println("    matrix.fillScreen(0);"); //Start function wiping screen (no pixels left from previous image)
        arduinoCode.println("");


        //Go through image starting at upper left (0, 0).
        if(picWidth > 0 && picHeight > 0) { //Make sure pic is valid size.
            int y;
            for(y=0; y<picHeight; y++){
                int x;
                for(x=0; x<picWidth; x++) {
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
        }
        else {
            System.out.println("Image size must be greater than 0 x 0. Either image size invalid or filename doesn't exist.");
            return;
        }

        arduinoCode.println("}");
        arduinoCode.close();
        System.out.println("");


        //write image
        try{
            f2 = new File("./" + imageFileName);
            ImageIO.write(newImage, "png", f2);
            System.out.println("Writing complete.");
        }catch(IOException e){
            System.out.println("Error: "+e);
        }

        System.out.println("");
        System.out.println("Width: " + picWidth + " Height: " + picHeight);
        System.out.println("Check " + imageFileName + " for accuracy.");
        System.out.println("Check " + codeFileName + " for NeoMatrix function.");
    }
}
