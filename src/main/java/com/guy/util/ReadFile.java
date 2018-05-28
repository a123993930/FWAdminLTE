package com.guy.util;

import com.guy.util.entity.FileInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    public ReadFile() {
    }


    List<FileInfo> fileInfoList = new ArrayList<>(0);

    /**
     * 读取某个文件夹下的所有文件
     */
    public List<FileInfo> readfile(String filepath) throws FileNotFoundException, IOException {

        File file = new File(filepath);
        if (!file.isDirectory()) {
            FileInfo fileInfo=makeFileInfo(file);
            fileInfoList.add(fileInfo);
        } else if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(filepath + "\\" + filelist[i]);
                if (!readfile.isDirectory()) {
                    FileInfo fileInfo=makeFileInfo(readfile);
                    fileInfoList.add(fileInfo);
                } else if (readfile.isDirectory()) {
                    readfile(filepath + "\\" + filelist[i]);
                }
            }

        }
        return fileInfoList;
    }

    public FileInfo makeFileInfo(File readfile) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setPath(readfile.getPath());
        fileInfo.setAbspath(readfile.getAbsolutePath());
        fileInfo.setName(readfile.getName());
        fileInfo.setCreateTime(DateUtil.transferLongToDate(DateUtil.FORMAT_FULL, readfile.lastModified()));
        return fileInfo;
    }

    /**
     * 删除某个文件夹下的所有文件夹和文件
     */
        
        
        /*public static boolean deletefile(String delpath)
                        throws FileNotFoundException, IOException {
                try {

                        File file = new File(delpath);
                        if (!file.isDirectory()) {
                                System.out.println("1");
                                file.delete();
                        } else if (file.isDirectory()) {
                                System.out.println("2");
                                String[] filelist = file.list();
                                for (int i = 0; i < filelist.length; i++) {
                                        File delfile = new File(delpath + "\\" + filelist[i]);
                                        if (!delfile.isDirectory()) {
                                                System.out.println("path=" + delfile.getPath());
                                                System.out.println("absolutepath="
                                                                + delfile.getAbsolutePath());
                                                System.out.println("name=" + delfile.getName());
                                                delfile.delete();
                                                System.out.println("删除文件成功");
                                        } else if (delfile.isDirectory()) {
                                                deletefile(delpath + "\\" + filelist[i]);
                                        }
                                }
                                file.delete();

                        }

                } catch (FileNotFoundException e) {
                        System.out.println("deletefile()   Exception:" + e.getMessage());
                }
                return true;
        }*/
    public static void main(String[] args) {
        ReadFile r = new ReadFile();
        try {
            List<FileInfo> fileInfoList = r.readfile("D:\\IJWork\\BuilderDiary\\target\\BuilderDiary\\WEB-INF\\classes\\2018\\1");
            System.out.println(fileInfoList.size());
            for (FileInfo f : fileInfoList) {
                System.out.println(f.getAbspath());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // deletefile("D:/file");
        System.out.println("ok");
    }

}

