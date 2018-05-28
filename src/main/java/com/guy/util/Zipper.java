package com.guy.util;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 解压文件
 */
public class Zipper {

	/**
	 * 压缩
	 * 
	 * @param zipFileName
	 *            压缩产生的zip包文件名--带路径,如果为null或空则默认按文件名生产压缩文件名
	 * @param relativePath
	 *            相对路径，默认为空
	 * @param directory
	 *            文件或目录的绝对路径
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void zip(String zipFileName, String relativePath,
						   String directory) throws IOException {
		String fileName = zipFileName;
		if (fileName == null || fileName.trim().equals("")) {
			File temp = new File(directory);
			if (temp.isDirectory()) {
				fileName = directory + ".zip";
			} else {
				if (directory.indexOf(".") > 0) {
					fileName = directory.substring(0,
							directory.lastIndexOf("."))
							+ "zip";
				} else {
					fileName = directory + ".zip";
				}
			}
		}
		ZipOutputStream zos = new ZipOutputStream(
				new FileOutputStream(fileName));
		try {
			zip(zos, relativePath, directory);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (null != zos) {
				try {
					zos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/** */
	/**
	 * 压缩
	 * 
	 * @param zos
	 *            压缩输出流
	 * @param relativePath
	 *            相对路径
	 * @param absolutPath
	 *            文件或文件夹绝对路径
	 * @throws IOException
	 */
	private static void zip(ZipOutputStream zos, String relativePath,
			String absolutPath) throws IOException {
		File file = new File(absolutPath);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				File tempFile = files[i];
				if (tempFile.isDirectory()) {
					String newRelativePath = relativePath + tempFile.getName()
							+ File.separator;
					zip(zos, newRelativePath, tempFile.getPath());
				} else {
					zipFile(zos, tempFile, relativePath);
				}
			}
		} else {
			zipFile(zos, file, relativePath);
		}
	}

	/** */
	/**
	 * 压缩文件
	 * 
	 * @param zos
	 *            压缩输出流
	 * @param file
	 *            文件对象
	 * @param relativePath
	 *            相对路径
	 * @throws IOException
	 */
	private static void zipFile(ZipOutputStream zos, File file,
			String relativePath) throws IOException {
		ZipEntry entry = new ZipEntry(relativePath + file.getName());
		zos.putNextEntry(entry);
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			int BUFFERSIZE = 2 << 10;
			int length = 0;
			byte[] buffer = new byte[BUFFERSIZE];
			while ((length = is.read(buffer, 0, BUFFERSIZE)) >= 0) {
				zos.write(buffer, 0, length);
			}
			zos.flush();
			zos.closeEntry();
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (null != is) {
				is.close();
			}
		}
	}

	/**
	 * 解压文件，支持中文路径及中文文件名 支持rar以及zip格式
	 * 
	 * @param zipFile
	 * @param outFilePath
	 * @param mode
	 */
	@SuppressWarnings("rawtypes")
	public static boolean unfile(File zipFile, String outFilePath,
			String fileName) {
		boolean flag = false;
		try {
			String unfile = fileName.substring(fileName.lastIndexOf('.') + 1);
			if (unfile == "zip" || unfile.equals("zip")) {
				ZipFile zf = new ZipFile(zipFile);
				FileOutputStream fos;
				byte[] buf = new byte[1024];
				for (Enumeration em = zf.entries(); em.hasMoreElements();) {
					ZipEntry ze = (ZipEntry) em.nextElement();
					if (ze.isDirectory()) {
						continue;
					}
					DataInputStream dis = new DataInputStream(
							zf.getInputStream(ze));
					String currentFileName = ze.getName();
					int dex = currentFileName.lastIndexOf('/');
					String currentoutFilePath = outFilePath;
					if (dex > 0) {
						currentoutFilePath += currentFileName.substring(0, dex)
								+ File.separator;
						File currentFileDir = new File(currentoutFilePath);
						currentFileDir.mkdirs();
					}
					fos = new FileOutputStream(outFilePath + ze.getName());
					int readLen = 0;
					while ((readLen = dis.read(buf, 0, 1024)) > 0) {
						fos.write(buf, 0, readLen);
					}
					fos.flush();
					dis.close();
					fos.close();
				}
				zf.close();
				flag = true;
			}
			if (unfile == "rar" || unfile.equals("rar")) {
				String unrarCmd = "C:\\Program Files\\WinRAR\\UnRar x ";
				unrarCmd += zipFile + " " + outFilePath;
				try {
					Runtime rt = Runtime.getRuntime();
					Process pre = rt.exec(unrarCmd);
					InputStreamReader isr = new InputStreamReader(
							pre.getInputStream());
					BufferedReader bf = new BufferedReader(isr);
					String line = null;
					while ((line = bf.readLine()) != null) {
						line = line.trim();
						if ("".equals(line)) {
							continue;
						}
						System.out.println(line);
					}
					bf.close();
					flag = true;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}
