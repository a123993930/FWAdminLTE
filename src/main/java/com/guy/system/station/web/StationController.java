package com.guy.system.station.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.guy.system.user.service.UserService;
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
import com.guy.system.station.entity.Station;
import com.guy.system.station.service.StationService;

/**
 * 类名称：StationController
 * 创建人：blank
 * 创建时间：2018-01-19
 */
@Controller
@RequestMapping(value = "system/station")
public class StationController extends BaseController {

    @Autowired
    StationService stationService;
    @Autowired
    UserService userService;
    /**
     * 默认页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "system/stationList";
    }

    /**
     * 获取json
     */
    @RequiresPermissions("system:station:view")
    @RequestMapping(value = "json", method = RequestMethod.POST)
    @ResponseBody
    public Map
            <String, Object> stationList(HttpServletRequest request) {
        Page<Station> page = getPage(request);
        List
                <PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        page = stationService.search(page, filters);
        return getEasyUIData(page);
    }
    @RequiresPermissions("system:station:view")
    @RequestMapping(value = "jsonAll", method = RequestMethod.POST)
    @ResponseBody
    public List<Station> stationAll() {
       return stationService.getAll();
    }
    /**
     * 添加跳转
     *
     * @param model
     */
    @RequiresPermissions("system:station:add")
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("station", new Station());
        model.addAttribute("action", "create");
        return "system/stationForm";
    }

    /**
     * 添加字典
     *
     * @param station
     * @param model
     */
    @RequiresPermissions("system:station:add")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid Station station, Model model) {
        stationService.save(station);
        return "success";
    }

    /**
     * 修改跳转
     *
     * @param id
     * @param model
     * @return
     */
    @RequiresPermissions("system:station:update")
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("station", stationService.get(id));
        model.addAttribute("action", "update");
        return "system/stationForm";
    }

    /**
     * 修改
     *
     * @param station
     * @param model
     * @return
     */
    @RequiresPermissions("system:station:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@Valid @ModelAttribute @RequestBody Station station, Model model) {
        stationService.update(station);
        return "success";
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequiresPermissions("system:station:delete")
    @RequestMapping(value = "delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Integer id) {
        stationService.delete(id);
        return "success";
    }

    @ModelAttribute
    public void getStation(@RequestParam(value = "id", defaultValue = "-1") Integer id, Model model) {
        if (id != -1) {
            model.addAttribute("station", stationService.get(id));
        }
    }

}
