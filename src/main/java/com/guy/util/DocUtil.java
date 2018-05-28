package com.guy.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Map;

/**
 * 类名称：DocUtil
 * 创建人：blank
 * 创建时间：2018-01-18
 */
public class DocUtil {

    /**
     *  实例
     *  DocUtil docUtil=new DocUtil();
         Map<String, Object> dataMap=new HashMap<String, Object>();
         dataMap.put("name", "Joanna");
         dataMap.put("examNum", "111111111111");
         dataMap.put("IDCard", "222222222222222222");
         dataMap.put("carModel", "C1");
         dataMap.put("drivingSchool", "测试驾校");
         dataMap.put("busyType", "初次申领");
         dataMap.put("examDate", "2016-03-10");
         dataMap.put("orderCount", "第1次");
         dataMap.put("userImg1", docUtil.getImageStr("D:\\Img\\userImg1.png"));
         dataMap.put("userImg2", docUtil.getImageStr("D:\\Img\\userImg2.png"));
         dataMap.put("firstExamTime", "12:41:17-12:44:38");
         dataMap.put("firstExamScores", "0分，不及格");
         dataMap.put("firstDeductItem", "12:44:15 20102 1号倒车入库，车身出线 扣100分");
         dataMap.put("firstPic1", docUtil.getImageStr("D:\\Img\\firstPic1.png"));
         dataMap.put("firstPic2", docUtil.getImageStr("D:\\Img\\firstPic2.png"));
         dataMap.put("firstPic3", docUtil.getImageStr("D:\\Img\\firstPic3.png"));
         dataMap.put("secondExamTime", "12:46:50-13:05:37");
         dataMap.put("secondExamScores", "90分，通过");
         dataMap.put("secondDeductItem", "");
         dataMap.put("secondPic1", docUtil.getImageStr("D:\\Img\\secondPic1.png"));
         dataMap.put("secondPic2", docUtil.getImageStr("D:\\Img\\secondPic2.png"));
         dataMap.put("secondPic3", docUtil.getImageStr("D:\\Img\\secondPic3.png"));
         docUtil.createDoc(dataMap, "baseDoc", "D:\\yanqiong.doc");
     }
     */
    public Configuration configure = null;

    public DocUtil() {
        configure = new Configuration();
        configure.setDefaultEncoding("utf-8");
    }

    /**
     * 根据Doc模板生成word文件
     *
     * @param dataMap      需要填入模板的数据
     * @param downloadType 文件名称
     * @param savePath     保存路径
     */
    public void createDoc(Map<String, Object> dataMap, String downloadType, String savePath) throws IOException, TemplateException {

            //加载需要装填的模板
            Template template = null;
            //设置模板装置方法和路径，FreeMarker支持多种模板装载方法。可以重servlet，classpath,数据库装载。
            //加载模板文件，放在testDoc下
//            configure.setClassForTemplateLoading(this.getClass(), "/");
            configure.setDirectoryForTemplateLoading(new File(PathUtil.getClassResources() + "/docTemplate"));
            //设置对象包装器
//            configure.setObjectWrapper(new DefaultObjectWrapper());
            //设置异常处理器
            configure.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            //定义Template对象，注意模板类型名字与downloadType要一致
            template = configure.getTemplate(downloadType + ".xml");
            File outFile = new File(savePath);
            Writer out = null;
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"));
            template.process(dataMap, out);
            out.close();

    }

    public String getImageStr(String imgFile) {
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
}