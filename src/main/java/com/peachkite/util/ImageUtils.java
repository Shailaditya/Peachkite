package com.peachkite.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    public static String getImageDimension(String path)throws IOException{
        BufferedImage bimg = ImageIO.read(new File(path));
        return bimg.getWidth()+"*"+bimg.getHeight();
    }

}
