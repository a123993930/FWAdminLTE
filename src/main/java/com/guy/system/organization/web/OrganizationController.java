package com.guy.system.organization.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guy.common.persistence.Page;
import com.guy.common.persistence.PropertyFilter;
import com.guy.common.web.BaseController;
import com.guy.system.organization.entity.Organization;
import com.guy.system.organization.service.OrganizationService;

/**
 * 机构信息controller
 * @author blank
 * @date 2015年1月22日
 */
@Controller
@RequestMapping("system/organization")
public class OrganizationController extends BaseController{

	@Autowired
	private OrganizationService organizationService;

	/**
	 * 默认页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "system/orgList";
	}
	
	/**
	 * 获取机构信息json
	 */
	@RequestMapping(value="json",method = RequestMethod.POST)
	@ResponseBody
	public List<Organization> getData(HttpServletRequest request) {
	/*	 Page<Organization> page = getPage(request);
		 List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
         page = organizationService.search(page, filters);
		return getEasyUIData(page);*/
	    String pid = request.getParameter("parentid");
        return organizationService.getOrgByParentId(pid);
	}
	
	@RequestMapping(value="jsonKey",method = RequestMethod.POST)
	@ResponseBody
	public List<Organization> listByKeywords(HttpServletRequest request){
		 Page<Organization> page = getPage(request);
		 page.setPageSize(1000);
		 List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
         page = organizationService.search(page, filters);
		return page.getResult();
	}
	
	@RequestMapping(value="isCode",method = RequestMethod.POST)
    @ResponseBody
	public boolean isHasCode(HttpServletRequest request){
	    return organizationService.isHasCode(request.getParameter("orgCode"));
	}
	
	
	/**
	 * 添加机构信息跳转
	 * 
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("organization", new Organization());
		model.addAttribute("action", "create");
		return "system/orgForm";
	}

	/**
	 * 添加机构信息
	 * 
	 * @param organization
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid Organization organization, Model model) {
		organizationService.save(organization);
		return "success";
	}

	/**
	 * 修改机构信息跳转
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("organization", organizationService.get(id));
		model.addAttribute("action", "update");
		return "system/orgForm";
	}

	/**
	 * 修改机构信息
	 * 
	 * @param goodsType
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid @ModelAttribute @RequestBody Organization organization,Model model) {
		organizationService.update(organization);
		return "success";
	}

	/**
	 * 删除机构信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id") Integer id) {
		organizationService.delete(id);
		return "success";
	}
	
}
