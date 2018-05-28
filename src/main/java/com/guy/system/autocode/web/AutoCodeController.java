package com.guy.system.autocode.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.guy.common.web.BaseController;
import com.guy.util.DelAllFile;
import com.guy.util.FileDownload;
import com.guy.util.FileZip;
import com.guy.util.Freemarker;
import com.guy.util.PathUtil;

@Controller
@RequestMapping(value="sys/autoCode")
public class AutoCodeController extends BaseController{
    
    /**
     * 默认页面
     */
    @RequestMapping(value="code", method = RequestMethod.GET)
    public String workList(Model model) {
        model.addAttribute("action", "proCode");
        return "system/codeForm";
    }
    /**
     * 生成代码
     */
    @RequestMapping(value="proCode", method = RequestMethod.POST)
    public void proCode(HttpServletResponse response,HttpServletRequest request) throws Exception{
        /* ============================================================================================= */
        String packageName = request.getParameter("packageName");           //包名                ========1
        String objectName = request.getParameter("objectName");             //类名                ========2
        Map<String,Object> root = new HashMap<String,Object>();     //创建数据模型
        root.put("packageName", packageName);                       //包名
        root.put("objectName", objectName);                         //类名
        root.put("objectNameLower", objectName.toLowerCase());      //类名(全小写)
        root.put("objectNameUpper", objectName.toUpperCase());      //类名(全大写)
        root.put("nowDate", new Date());                            //当前日期
        root.put("ctx", "${ctx}");
        root.put("action", "${action}");
        root.put("id", "${id}");
        String path=request.getSession().getServletContext().getRealPath("/") +"auto/code";
        DelAllFile.delFolder(path); //生成代码前,先清空之前生成的代码
        /* ============================================================================================= */
        
        String filePath = "auto/code/";                        //存放路径
        String ftlPath = "autoCode";                              //ftl路径
        
        /*生成controller*/
        Freemarker.printFile("controllerTemplate.ftl", root, "com/guy/"+packageName+"/"+objectName.toLowerCase()+"/web/"+objectName+"Controller.java", filePath, ftlPath);
        
        /*生成service*/
        Freemarker.printFile("serviceTemplate.ftl", root,  "com/guy/"+packageName+"/"+objectName.toLowerCase()+"/service/"+objectName+"Service.java", filePath, ftlPath);

        /*生成dao xml*/
        Freemarker.printFile("daoTemplate.ftl", root,  "com/guy/"+packageName+"/"+objectName.toLowerCase()+"/dao/"+objectName+"Dao.java", filePath, ftlPath);
        
        /*生成entity*/
        Freemarker.printFile("entityTemplate.ftl", root,  "com/guy/"+packageName+"/"+objectName.toLowerCase()+"/entity/"+objectName+".java", filePath, ftlPath);
        
        /*生成jsp页面*/
        Freemarker.printFile("jsp_list_Template.ftl", root, "views/"+packageName+"/"+objectName.toLowerCase()+"List.jsp", filePath, ftlPath);
        Freemarker.printFile("jsp_form_Template.ftl", root, "views/"+packageName+"/"+objectName.toLowerCase()+"Form.jsp", filePath, ftlPath);
        
        
        /*生成的全部代码压缩成zip文件*/
        FileZip.zip(PathUtil.getClasspath()+"auto/code", PathUtil.getClasspath()+"auto/"+objectName.toLowerCase()+"code.zip");
        
        /*下载代码*/
        FileDownload.fileDownload(response, PathUtil.getClasspath()+"auto/"+objectName.toLowerCase()+"code.zip", objectName.toLowerCase()+"code.zip");
        
    }

}
