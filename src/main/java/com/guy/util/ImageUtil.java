package com.guy.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtil {
	static BufferedImage image;

	static void createImage(String fileLocation) throws IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fileLocation);
			ImageIO.write(image, "jpg", out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	public static void graphicsGeneration(String name, int jifen, String phone,
			String inimg, String outimg) throws IOException {
		int imageWidth = 360;
		int imageHeight = 200;
		image = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, imageWidth, imageHeight);
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("楷体", 1, 18));
		graphics.drawString("姓名:" + name, 170, 45);
		graphics.drawString("囧囧币:" + jifen, 170, 80);
		graphics.drawString("手机号:" + phone, 170, 115);
		ImageIcon imageIcon = new ImageIcon(inimg);
		imageIcon.setImage(imageIcon.getImage().getScaledInstance(160, 160,
				Image.SCALE_DEFAULT));
		graphics.drawImage(imageIcon.getImage(), 0, 0, null);
		createImage(outimg);
	}

}
