package com.guy.${packageName}.${objectNameLower}.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guy.common.persistence.Page;
import com.guy.common.persistence.PropertyFilter;
import com.guy.common.web.BaseController;
import com.guy.${packageName}.${objectNameLower}.entity.${objectName};
import com.guy.${packageName}.${objectNameLower}.service.${objectName}Service;

/**
* 类名称：${objectName}Controller
* 创建人：blank
* 创建时间：${nowDate?string("yyyy-MM-dd")}
*/
@Controller
@RequestMapping(value="${packageName}/${objectNameLower}")
public class ${objectName}Controller extends BaseController {

@Autowired
${objectName}Service ${objectNameLower}Service;
/**
* 默认页面
*/
@RequestMapping(method = RequestMethod.GET)
public String list() {
return "${packageName}/${objectNameLower}List";
}

/**
* 获取json
*/
@RequiresPermissions("${packageName}:${objectNameLower}:view")
@RequestMapping(value="json",method = RequestMethod.POST)
@ResponseBody
public Map
<String, Object> ${objectNameLower}List(HttpServletRequest request) {
Page<${objectName}> page = getPage(request);
List
<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
    page = ${objectNameLower}Service.search(page, filters);
    return getEasyUIData(page);
    }

    /**
    * 添加跳转
    *
    * @param model
    */
    @RequiresPermissions("${packageName}:${objectNameLower}:add")
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
    model.addAttribute("${objectNameLower}", new ${objectName}());
    model.addAttribute("action", "create");
    return "${packageName}/${objectNameLower}Form";
    }

    /**
    * 添加字典
    *
    * @param ${objectNameLower}
    * @param model
    */
    @RequiresPermissions("${packageName}:${objectNameLower}:add")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid ${objectName} ${objectNameLower}, Model model) {
${objectNameLower}Service.save(${objectNameLower});
    return "success";
    }

    /**
    * 修改跳转
    *
    * @param id
    * @param model
    * @return
    */
    @RequiresPermissions("${packageName}:${objectNameLower}:update")
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Integer id, Model model) {
    model.addAttribute("${objectNameLower}", ${objectNameLower}Service.get(id));
    model.addAttribute("action", "update");
    return "${packageName}/${objectNameLower}Form";
    }

    /**
    * 修改
    *
    * @param ${objectNameLower}
    * @param model
    * @return
    */
    @RequiresPermissions("${packageName}:${objectNameLower}:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@Valid @ModelAttribute @RequestBody ${objectName} ${objectNameLower},Model model) {
${objectNameLower}Service.update(${objectNameLower});
    return "success";
    }

    /**
    * 删除
    *
    * @param id
    * @return
    */
    @RequiresPermissions("${packageName}:${objectNameLower}:delete")
    @RequestMapping(value = "delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Integer id) {
${objectNameLower}Service.delete(id);
    return "success";
    }

    @ModelAttribute
    public void get${objectName}(@RequestParam(value = "id", defaultValue = "-1") Integer id,Model model) {
    if (id != -1) {
    model.addAttribute("${objectNameLower}", ${objectNameLower}Service.get(id));
    }
    }

    }
