package com.guy.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;



public class FtpApche {
	private static FTPClient ftpClient = new FTPClient();
	private static String encoding = System.getProperty("file.encoding");


	/**
	 * 上传的文件或文件夹
	 *
	 * @param url
	 * @param port
	 * @param username
	 * @param password
	 * @param path
	 * @param localPath
	 * @return
	 */
	public static boolean upload(String url, int port, String username,
								 String password, String path, String localPath) {
		boolean result = false;

		try {
			int reply;
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftpClient.connect(url);
			// ftp.connect(url, port);// 连接FTP服务器
			// 登录
			ftpClient.login(username, password);
			ftpClient.setControlEncoding(encoding);
			// 检验是否连接成功
			reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				System.out.println("连接失败");
				ftpClient.disconnect();
				return result;
			}

			// 转移工作目录至指定目录下
			ftpClient.changeWorkingDirectory(path);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			// if (change) {
			File file = new File(localPath);

			if (file.isDirectory()) {
				ftpClient.makeDirectory(file.getName());
				ftpClient.changeWorkingDirectory(file.getName());
				String[] files = file.list();
				for (int i = 0; i < files.length; i++) {
					File file1 = new File(file.getPath() + "\\" + files[i]);
					if (file1.isDirectory()) {
						upload(url, port, username, password, path, localPath);
						result = ftpClient.changeToParentDirectory();
					} else {
						FileInputStream input = new FileInputStream(file1);
						result = ftpClient.storeFile(file1.getName(), input);
						input.close();
					}
				}
			} else {
				File file2 = new File(file.getPath());
				FileInputStream input = new FileInputStream(file2);
				ftpClient.storeFile(file2.getName(), input);
				input.close();
			}

			ftpClient.logout();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}

	/**
	 * @param url
	 * @param port
	 * @param username
	 * @param password
	 * @param remotePath
	 * @param fileName
	 * @param localPath
	 * @return
	 */
	public static boolean downFiles(String url, int port, String username,
									String password, String remotePath, String localPath) {
		boolean result = false;
		try {
			int reply;
			ftpClient.setControlEncoding(encoding);

			/*
			 * 为了上传和下载中文文件，有些地方建议使用以下两句代替 new
			 * String(remotePath.getBytes(encoding),"iso-8859-1")转码。 经过测试，通不过。
			 */
			// FTPClientConfig conf = new
			// FTPClientConfig(FTPClientConfig.SYST_NT);
			// conf.setServerLanguageCode("zh");

			ftpClient.connect(url, port);
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftpClient.login(username, password);// 登录
			// 设置文件传输类型为二进制
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			// 获取ftp登录应答代码
			reply = ftpClient.getReplyCode();
			// 验证是否登陆成功
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				System.err.println("FTP server refused connection.");
				return result;
			}
			// 转移到FTP服务器目录至指定的目录下
			ftpClient.changeWorkingDirectory(new String(remotePath
					.getBytes(encoding), "iso-8859-1"));
			//创建本地用于保存远程文件的位置
			String localDirName=localPath+"//"+remotePath;
			File localDir=new File(localDirName);
			localDir.mkdir();
			// 获取文件列表
			FTPFile[] fs = ftpClient.listFiles();
			for (FTPFile ff : fs) {
				if (ff.isFile()) {
					File localFile = new File(localDirName+"\\" + ff.getName());
					OutputStream is = new FileOutputStream(localFile);
					ftpClient.retrieveFile(ff.getName(), is);
					is.close();
				}

			}

			ftpClient.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}

	public static boolean download(String url, int port, String username,
								   String password, String remotePath, String localPath) {
		boolean result = false;
		try {
			int reply;
			ftpClient.setControlEncoding(encoding);
			/*
			 * 为了上传和下载中文文件，有些地方建议使用以下两句代替 new
			 * String(remotePath.getBytes(encoding),"iso-8859-1")转码。 经过测试，通不过。
			 */

			ftpClient.connect(url, port);

			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftpClient.login(username, password);// 登录
			// 设置文件传输类型为二进制
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			// 获取ftp登录应答代码
			reply = ftpClient.getReplyCode();
			// 验证是否登陆成功
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				System.err.println("FTP server refused connection.");
				return result;
			}
			// // 转移到FTP服务器目录至指定的目录下
			// ftpClient.changeWorkingDirectory(new String(remotePath
			// .getBytes(encoding), "iso-8859-1"));

			File temp = new File(localPath);
			if (!temp.exists()) {
				temp.mkdirs();
			}
			// 判断是否是目录
			if (isDir(remotePath)) {
				String[] names = ftpClient.listNames();
				for (int i = 0; i < names.length; i++) {
					result=downFiles(url, port, username, password, remotePath+"\\"+names[i], localPath);
				}

			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// 判断是否是目录
	public static boolean isDir(String fileName) {
		try {
			// 切换目录，若当前是目录则返回true,否则返回true。
			boolean falg = ftpClient.changeWorkingDirectory(fileName);
			return falg;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	/**
	 * 获取数据日期
	 * @param name
	 * @return
	 */
	public static String sjrq(String name){
		String[] names=name.split("_");
		String rq=names[2].substring(6,14);
		return rq;
	}

	public static void main(String[] args) {
		String name="SZHY_ZJXX_13000020151004000001";
		String[] names=name.split("_");
		String s=names[2].substring(6,14);
		System.out.println(s);
	}

}