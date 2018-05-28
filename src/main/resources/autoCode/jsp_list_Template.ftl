<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp"%>
    <script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

</head>
<body style="font-family: '微软雅黑'">
<div id="tb" style="padding:5px;height:auto">
    <div>
        <form id="searchFrom" action="">
            <!--<input type="text" name="filter_LIKES_name" class="easyui-validatebox" data-options="width:150,prompt: '名称'"/>-->
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" plain="true" iconCls="icon-search" onclick="cx()">查询</a>
        </form>

        <shiro:hasPermission name="${packageName}:${objectNameLower}:add">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
               onclick="add();">添加</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="${packageName}:${objectNameLower}:delete">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
               data-options="disabled:false" onclick="del()">删除</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="${packageName}:${objectNameLower}:update">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
               onclick="upd()">修改</a>
        </shiro:hasPermission>
    </div>

</div>
<table id="dg"></table>
<div id="dlg"></div>
<script type="text/javascript">
    var dg;
    $(function () {
        dg = $('#dg').datagrid({
            method: "post",
            url: '${ctx}/${packageName}/${objectNameLower}/json',
            fit: true,
            fitColumns: true,
            border: false,
            striped: true,
            idField: 'id',
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'id', title: 'id', hidden: true}
            ]],
            enableHeaderClickMenu: false,
            enableHeaderContextMenu: false,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });

    //弹窗增加
    function add() {
        d = $("#dlg").dialog({
            title: '添加',
            width: 380,
            height: 250,
            href: '${ctx}/${packageName}/${objectNameLower}/create',
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    $("#mainform").submit();
                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }]
        });
    }

    //删除
    function del() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {
            if (data) {
                $.ajax({
                    type: 'get',
                    url: "${ctx}/${packageName}/${objectNameLower}/delete/" + row.id,
                    success: function (data) {
                        successTip(data, dg);
                    }
                });
            }
        });
    }

    //弹窗修改
    function upd() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        d = $("#dlg").dialog({
            title: '修改',
            width: 380,
            height: 250,
            href: '${ctx}/${packageName}/${objectNameLower}/update/' + row.id,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '修改',
                handler: function () {
                    $('#mainform').submit();
                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }]
        });
    }

    //创建查询对象并查询
    function cx() {
        var obj = $("#searchFrom").serializeObject();
        dg.datagrid('reload', obj);
    }

</script>
</body>
</html>