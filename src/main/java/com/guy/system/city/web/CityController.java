package com.guy.system.city.web;

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
import com.guy.system.city.entity.City;
import com.guy.system.city.service.CityService;

/** 
 * 类名称：CityController
 * 创建人：blank 
 * 创建时间：2016-12-13
 */
@Controller
@RequestMapping(value="system/city")
public class CityController extends BaseController {

	@Autowired
	CityService cityService;
	/**
	 * 默认页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "system/cityList";
	}

	/**
	 * 获取json
	 */
	@RequiresPermissions("system:city:view")
	@RequestMapping(value="json",method = RequestMethod.POST)
	@ResponseBody
	public List<City> cityList(HttpServletRequest request) {
	    String pid = request.getParameter("parentid");
	    return cityService.getCityByParentId(Integer.parseInt(pid));
	}
	/*public Map<String, Object> cityList(HttpServletRequest request) {
		Page<City> page = getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = cityService.search(page, filters);
		return getEasyUIData(page);
	}*/
	
	@RequestMapping(value="projson",method = RequestMethod.GET)
	@ResponseBody
	public List<City> proList(HttpServletRequest request) {
	    List<City> data= cityService.getProvince();
	    return data;
	}
	
	@RequestMapping(value="cityjson",method = RequestMethod.GET)
	@ResponseBody
	public List<City> ctList(HttpServletRequest request) {
	    List<City> data= cityService.getCity();
        return data;
	}
	
	@RequestMapping(value="districtjson",method = RequestMethod.GET)
	@ResponseBody
	public  List<City> dstList(HttpServletRequest request) {
	    List<City> data= cityService.getDistrict();
        return data;
	}
	
	/**
	 * 添加跳转
	 * 
	 * @param model
	 */
	@RequiresPermissions("system:city:add")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("city", new City());
		model.addAttribute("action", "create");
		return "system/cityForm";
	}

	/**
	 * 添加字典
	 * 
	 * @param city
	 * @param model
	 */
	@RequiresPermissions("system:city:add")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid City city, Model model) {
		cityService.save(city);
		return "success";
	}

	/**
	 * 修改跳转
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("system:city:update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("city", cityService.get(id));
		model.addAttribute("action", "update");
		return "system/cityForm";
	}

	/**
	 * 修改
	 * 
	 * @param city
	 * @param model
	 * @return
	 */
	@RequiresPermissions("system:city:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid @ModelAttribute @RequestBody City city,Model model) {
		cityService.update(city);
		return "success";
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("system:city:delete")
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id") Integer id) {
		cityService.delete(id);
		return "success";
	}
	
	@ModelAttribute
	public void getCity(@RequestParam(value = "id", defaultValue = "-1") Integer id,Model model) {
		if (id != -1) {
			model.addAttribute("city", cityService.get(id));
		}
	}

}
