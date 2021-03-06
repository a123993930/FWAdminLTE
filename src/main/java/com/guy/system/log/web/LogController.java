package com.guy.system.log.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guy.common.persistence.Page;
import com.guy.common.persistence.PropertyFilter;
import com.guy.common.web.BaseController;
import com.guy.system.log.entity.Log;
import com.guy.system.log.service.LogService;
import com.guy.system.user.entity.User;
import com.guy.util.excelTools.ExcelUtils;
import com.guy.util.excelTools.JsGridReportBase;
import com.guy.util.excelTools.TableData;

/**
 * 日志controller
 * @author blank
 * @date 2015年1月14日
 */
@Controller
@RequestMapping("system/log")
public class LogController extends BaseController{

	@Autowired
	private LogService logService;
	
	/**
	 * 默认页面
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(){
		return "system/logList";
	}
	
	/**
	 * 获取日志json
	 */
	@RequiresPermissions("sys:log:view")
	@RequestMapping("json")
	@ResponseBody
	public Map<String, Object> list(HttpServletRequest request) {
		Page<Log> logPage=getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		if(filters.size()>0){
			logPage.setPageNo(1);
		}
		logPage = logService.search(logPage, filters);
		//构造easyui表格数据
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("rows", logPage.getResult());
		map.put("total", logPage.getTotalCount());
		return map;
	}
	
	/**
	 * 删除日志
	 * @param id
	 */
	@RequiresPermissions("sys:log:delete")
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id") Integer id) {
		logService.delete(id);
		return "success";
	}
	
	/**
	 * 批量删除日志
	 * @param idList
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public String delete(@RequestBody List<Integer> idList) {
		logService.deleteLog(idList);
		return "success";
	}

	/**
	 * 导出excel
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception{
	    response.setContentType("application/msexcel;charset=UTF-8");
        List<Log> list = logService.getAll();//获取数据
        String title = "log";
        String[] hearders = new String[] {"操作编码", "详细描述","参数", "执行时间(mm)", "操作系统", "浏览器", "IP","MAC","操作者","操作时间"};//表头数组
        String[] fields = new String[] {"operationCode", "description","requestParam", "executeTime", "os", "browser", "ip","mac","creater","createDate"};//People对象属性数组
        TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders),fields);
        JsGridReportBase report = new JsGridReportBase(request, response);
        User user=(User) request.getSession().getAttribute("user");
        report.exportToExcel(title, user.getName(), td);
	}
}
