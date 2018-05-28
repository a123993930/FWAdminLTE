package com.guy.system.role.web;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
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
import com.guy.system.role.entity.Role;
import com.guy.system.role.service.RolePermissionService;
import com.guy.system.role.service.RoleService;
import com.guy.system.user.entity.User;

/**
 * 角色controller
 * @author blank
 * @date 2015年1月13日
 */
@Controller
@RequestMapping("system/role")
public class RoleController extends BaseController{
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private RolePermissionService rolePermissionService;
	
	/**
	 * 默认页面
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "system/roleList";
	}
	
	/**
	 * 角色集合(JSON)
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value="json",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<Role> page=getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = roleService.search(page, filters);
		return getEasyUIData(page);
	}
	
	/**
	 * 获取角色拥有的权限ID集合
	 * @param id
	 * @return
	 */
	@RequiresPermissions("sys:role:permView")
	@RequestMapping("{id}/json")
	@ResponseBody
	public List<Integer> getRolePermissions(@PathVariable("id") Integer id){
		List<Integer> permissionIdList=rolePermissionService.getPermissionIds(id);
		return permissionIdList;
	}

	/**
	 * 修改角色权限
	 * @param id
	 * @param newRoleIdList
	 * @param session
	 * @return
	 */
	@RequiresPermissions("sys:role:permUpd")
	@RequestMapping(value = "{id}/updatePermission")
	@ResponseBody
	public String updateRolePermission(@PathVariable("id") Integer id,@RequestBody List<Integer> newRoleIdList,HttpSession session){
		List<Integer> oldRoleIdList=rolePermissionService.getPermissionIds(id);
		
		//获取application中的sessions
		@SuppressWarnings("rawtypes")
		HashSet sessions=(HashSet) session.getServletContext().getAttribute("sessions");
		if (null!=sessions) {//当前如果有正在使用的用户，需要更新正在使用的用户的权限
			
			@SuppressWarnings("unchecked")
			Iterator<Session> iterator= sessions.iterator();
			PrincipalCollection pc=null;
			
			//遍历sessions
			while(iterator.hasNext()){
				HttpSession s=(HttpSession) iterator.next();
				User user=(User) s.getAttribute("user");
				//解決保存权限500错误
				if(user!=null){
					if(user.getId()==id){
						pc= (PrincipalCollection) s.getAttribute(String.valueOf(id));
						//清空该用户权限缓存
						rolePermissionService.clearUserPermCache(pc);
						s.removeAttribute(String.valueOf(id));
						break;
					}
				}

			}
		}
		
		rolePermissionService.updateRolePermission(id,oldRoleIdList ,newRoleIdList);
		
		return "success";
	}
	
	/**
	 * 添加角色跳转
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:add")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("role", new Role());
		model.addAttribute("action", "create");
		return "system/roleForm";
	}

	/**
	 * 添加角色
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:add")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid Role role,Model model) {
		roleService.save(role);
		return "success";
	}

	/**
	 * 修改角色跳转
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("role", roleService.get(id));
		model.addAttribute("action", "update");
		return "system/roleForm";
	}

	/**
	 * 修改角色
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid @ModelAttribute("role") Role role,Model model) {
		roleService.save(role);
		return "success";
	}

	/**
	 * 删除角色
	 * @param id
	 * @return
	 */
	@RequiresPermissions("sys:role:delete")
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id") Integer id) {
		roleService.delete(id);
		return "success";
	}
	
	@ModelAttribute
	public void getRole(@RequestParam(value = "id", defaultValue = "-1") Integer id, Model model) {
		if (id != -1) {
			model.addAttribute("role", roleService.get(id));
		}
	}
}
