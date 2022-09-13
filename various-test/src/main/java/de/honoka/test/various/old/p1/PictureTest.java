package de.honoka.test.various.old.p1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class PictureTest {
	
	public static void main(String[] args) throws Exception {
		BufferedImage img;
		final String rootPath = "classpath:resource", outPath = "./files/out";
		File[] files = new File(rootPath).listFiles();
		for(File f : files) {
			if(f.isDirectory()) continue;
			String filename = f.getName();
			String realName = filename.substring(0, filename.lastIndexOf("."));
			img = ImageIO.read(f);
			int width = img.getWidth(), height = img.getHeight();
			int smallerWidth = width < height ? width : height;
			
			BufferedImage newImg = new BufferedImage(smallerWidth, smallerWidth, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = newImg.createGraphics();
			graphics.drawImage(img, 0, 0, null);
			graphics.dispose();
			
			Image sizedNewImg = newImg.getScaledInstance(500, 500, BufferedImage.SCALE_DEFAULT);
			newImg = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
			graphics = newImg.createGraphics();
			graphics.drawImage(sizedNewImg, 0, 0, null);
			graphics.dispose();
			
			File newFile = new File(outPath + "/(new)" + realName + ".bmp");
			if(newFile.exists()) {
				newFile.delete();
				newFile.createNewFile();
			}
			OutputStream o = new FileOutputStream(newFile);
			ImageIO.write(newImg, "bmp", o);
			o.close();
		}
	}
	
}
