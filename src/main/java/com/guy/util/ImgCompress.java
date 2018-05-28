package com.guy.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片压缩处理
 * 
 */
public class ImgCompress {
	private String path;
	private Image img;
	private int width;
	private int height;

	public ImgCompress(String path) throws IOException {
		this.path = path;
	}

	public void resizeFix(int w, int h) throws IOException {
		File file = new File(this.path);
		img = ImageIO.read(file);
		width = img.getWidth(null);
		height = img.getHeight(null);
		if (width / height > w / h) {
			resizeByWidth(w);
		} else {
			resizeByHeight(h);
		}
	}

	public void resizeByWidth(int w) throws IOException {
		int h = height * w / width;
		resize(w, h);
	}

	public void resizeByHeight(int h) throws IOException {
		int w = width * h / height;
		resize(w, h);
	}

	public void resize(int w, int h) throws IOException {
		BufferedImage image = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);
		image.getGraphics().drawImage(img, 0, 0, w, h, null);
		File destFile = new File(path);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(destFile);
			ImageIO.write(image, "jpg", out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
}