package com.guy.system.user.web;

import com.guy.common.persistence.Page;
import com.guy.common.persistence.PropertyFilter;
import com.guy.common.web.BaseController;
import com.guy.system.areainfo.service.AreaInfoService;
import com.guy.system.station.entity.Station;
import com.guy.system.station.service.StationService;
import com.guy.system.user.entity.User;
import com.guy.system.user.service.UserOrgService;
import com.guy.system.user.service.UserRoleService;
import com.guy.system.user.service.UserService;
import com.guy.util.UserUtil;
import com.guy.util.excel.ExcelUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 用户controller
 *
 * @author blank
 * @date 2015年1月13日
 */
@Controller
@RequestMapping("system/user")
public class UserController extends BaseController {

    @Autowired
    AreaInfoService areaInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserOrgService userOrgService;
    @Autowired
    private StationService stationService;

    /**
     * 默认页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "system/userList";
    }

    /**
     * 获取用户json
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "json", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
        Page<User> page = getPage(request);
        page.setOrderBy("sort");
        page.setOrder(Page.ASC);
        User user = UserUtil.getCurrentUser();
        //查看是否为普通用户
        boolean bool = userRoleService.isNormal(user);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        //如果是普通用户则只能看自己的
        if (bool) {
            PropertyFilter filterUserID = new PropertyFilter("EQI_id",
                    user.getId().toString());
            filters.add(filterUserID);
        }
        page = userService.search(page, filters);
        return getEasyUIData(page);
    }

    /**
     * 添加用户跳转
     *
     * @param model
     */
    @RequiresPermissions("sys:user:add")
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("action", "create");
        return "system/userForm";
    }

    /**
     * 添加用户
     *
     * @param user
     */
    @RequiresPermissions("sys:user:add")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid User user, HttpSession session) {
        try {
//            String areaCode="";
//            int code=Integer.parseInt(user.getAreaCode());
//            areaCode = areaInfoService.get(code).getAreaCode();
//            user.setAreaCode(areaCode);
            userService.save(user);
        } catch (Exception e) {

            e.printStackTrace();
            return "添加异常：" + e.toString();
        }
        return "success";
    }

    /**
     * 修改用户跳转
     *
     * @param id
     * @param model
     * @return
     */
    @RequiresPermissions("sys:user:update")
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Integer id, Model model) {
        try {
            User user = userService.get(id);
//            String areaCode=user.getAreaCode();
//            AreaInfo areaInfo=areaInfoService.getInfoByCode(areaCode);
//            int code=areaInfo.getId();
//            user.setAreaCode(code+"");
            model.addAttribute("user", user);
            model.addAttribute("action", "update");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "system/userForm";
    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @RequiresPermissions("sys:user:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@Valid @ModelAttribute @RequestBody User user, HttpSession session) {
//        String areaCode="";
//        int code=Integer.parseInt(user.getAreaCode());
//        areaCode = areaInfoService.get(code).getAreaCode();
//        user.setAreaCode(areaCode);
        userService.update(user);
        session.setAttribute("user", user);
        return "success";
    }


    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequiresPermissions("sys:user:delete")
    @RequestMapping(value = "delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Integer id) {
        userService.delete(id);
        return "success";
    }

    /**
     * 弹窗页-用户拥有的角色
     *
     * @param id
     * @param model
     * @return
     */
    @RequiresPermissions("sys:user:roleView")
    @RequestMapping(value = "{userId}/userRole")
    public String getUserRole(@PathVariable("userId") Integer id, Model model) {
        model.addAttribute("userId", id);
        return "system/userRoleList";
    }

    /**
     * 弹窗页-用户所在机构
     *
     * @param id
     * @param model
     * @return
     */
    @RequiresPermissions("sys:user:orgView")
    @RequestMapping(value = "{userId}/userOrg")
    public String getUserOrg(@PathVariable("userId") Integer id, Model model) {
        model.addAttribute("userId", id);
        return "system/userOrgList";
    }

    /**
     * 获取用户拥有的角色ID集合
     *
     * @param id
     * @return
     */
    @RequiresPermissions("sys:user:roleView")
    @RequestMapping(value = "{id}/role")
    @ResponseBody
    public List<Integer> getRoleIdList(@PathVariable("id") Integer id) {
        return userRoleService.getRoleIdList(id);
    }

    /**
     * 获取用户拥有的机构ID集合
     *
     * @param id
     * @return
     */
    @RequiresPermissions("sys:user:orgView")
    @RequestMapping(value = "{id}/org")
    @ResponseBody
    public List<Integer> getOrgIdList(@PathVariable("id") Integer id) {
        return userOrgService.getOrgIdList(id);
    }


    /**
     * 修改用户拥有的角色
     *
     * @param id
     * @param newRoleList
     * @return
     */
    @RequiresPermissions("sys:user:roleUpd")
    @RequestMapping(value = "{id}/updateRole")
    @ResponseBody
    public String updateUserRole(@PathVariable("id") Integer id, @RequestBody List<Integer> newRoleList) {
        userRoleService.updateUserRole(id, userRoleService.getRoleIdList(id), newRoleList);
        return "success";
    }

    /**
     * 修改用户所在的部门
     *
     * @param id
     * @param newRoleList
     * @return
     */
    @RequiresPermissions("sys:user:orgUpd")
    @RequestMapping(value = "{id}/updateOrg")
    @ResponseBody
    public String updateUserOrg(@PathVariable("id") Integer id, @RequestBody List<Integer> newRoleList) {
        try {
            userOrgService.updateUserOrg(id, userOrgService.getOrgIdList(id), newRoleList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 修改密码跳转
     */
    @RequestMapping(value = "updatePwd", method = RequestMethod.GET)
    public String updatePwdForm(Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("user"));
        return "system/updatePwd";
    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "updatePwd", method = RequestMethod.POST)
    @ResponseBody
    public String updatePwd(String oldPassword, @Valid @ModelAttribute @RequestBody User user, HttpSession session) {
        if (userService.checkPassword((User) session.getAttribute("user"), oldPassword)) {
            userService.updatePwd(user);
            session.setAttribute("user", user);
            return "success";
        } else {
            return "fail";
        }

    }

    @RequiresPermissions("sys:user:initpwd")
    @RequestMapping(value = "{id}/initPwd", method = RequestMethod.POST)
    @ResponseBody
    public String initPwd(@PathVariable("id") Integer id) {
        try {
            User user = userService.get(id);
            Hibernate.initialize(user.getName());
//            System.out.println(user.getName());
            user.setPassword("123456");
            user.setPlainPassword("123456");
            userService.updatePwd(user);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }

    /**
     * Ajax请求校验loginName是否唯一。
     */
    @RequestMapping(value = "checkLoginName")
    @ResponseBody
    public String checkLoginName(String loginName) {
        if (userService.getUser(loginName) == null) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * ajax请求校验原密码是否正确
     *
     * @param oldPassword
     * @return
     */
    @RequestMapping(value = "checkPwd")
    @ResponseBody
    public String checkPwd(String oldPassword, HttpSession session) {
        if (userService.checkPassword((User) session.getAttribute("user"), oldPassword)) {
            return "true";
        } else {
            return "false";
        }
    }

    @RequiresPermissions("sys:user:import")
    @RequestMapping(value = "import")
    @ResponseBody
    public String importExcel(@RequestParam(value = "sj") MultipartFile sj, HttpServletRequest request) throws Exception {
        InputStream file = sj.getInputStream();
        try {
            List<List<Object>> list = ExcelUtil.readExcelByList(file, 1);//获取导入数据对象集合
            file.close();
            for (List<Object> entity : list) {
                User user = new User();
                user.setLoginName(entity.get(0).toString());
                user.setName(entity.get(1).toString());
                user.setPassword("123456");
                user.setPlainPassword("123456");
                user.setGender(Short.valueOf(entity.get(3).toString()));
                Station station = stationService.getByName(entity.get(2).toString().trim());
                user.setStation(station);
                userService.save(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "导入数据异常！";
        }

        return "success";
    }

    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2
     * Preparable二次部分绑定的效果,先根据form的id从数据库查出Task对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getUser(@RequestParam(value = "id", defaultValue = "-1") Integer id, Model model) {
        if (id != -1) {
            try {
                User user = userService.get(id);
//                String areaCode=user.getAreaCode();
//                AreaInfo areaInfo=areaInfoService.getInfoByCode(areaCode);
//                int code=areaInfo.getId();
//                user.setAreaCode(code+"");
                model.addAttribute("user", user);
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
