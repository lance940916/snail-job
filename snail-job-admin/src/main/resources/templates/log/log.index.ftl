<#-- 自定义变量 -->
<#assign contextPath="${springMacroRequestContext.contextPath}" >

<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <title>蜗牛任务调度中心</title>
    <#import "../common/common.macro.ftl" as netCommon />
    <@netCommon.commonHead />
    <@netCommon.commonStyle />
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <#-- 头部区域 -->
    <@netCommon.commonHeader />

    <#-- 左侧菜单栏 -->
    <@netCommon.commonLeft "info" />

    <div class="layui-body">
        <!-- 内容主体区域 -->
        <div style="padding: 15px;">

            <fieldset class="layui-elem-field layui-field-title">
                <legend>日志管理</legend>
            </fieldset>

            <#-- 搜索 -->
            <form class="layui-form layui-form-pane">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">分组</label>
                        <div class="layui-input-block">
                            <select id="searchGroupNameID" name="groupName" autocomplete="off">
                                <option value="">请选择</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">任务</label>
                        <div class="layui-input-block">
                            <select id="searchJobID" name="jobId" autocomplete="off">
                                <option value="">请选择</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">调度结果</label>
                        <div class="layui-input-block">
                            <select name="triggerCode" autocomplete="off">
                                <option value="">请选择</option>
                                <option value="200">成功</option>
                                <option value="500">失败</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">执行结果</label>
                        <div class="layui-input-block">
                            <select name="execCode" autocomplete="off">
                                <option value="">请选择</option>
                                <option value="200">成功</option>
                                <option value="500">失败</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <button lay-submit class="layui-btn" lay-filter="searchBtn">搜索</button>
                    </div>
                </div>
            </form>

            <#-- 数据表格 -->
            <table class="layui-hide" id="dataTableID" lay-filter="dataTable"></table>

        </div>
    </div>

    <!-- 底部固定区域 -->
    <@netCommon.commonFooter />
</div>

<div id="execLayer" class="layui-row" style="display:none;margin-right: 20px;">
    <div class="layui-col-lg12">
        <form id="execFormId" class="layui-form" pane style="margin-top: 20px;" lay-filter="execForm">
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">执行参数</label>
                <div class="layui-input-block">
                    <textarea class="layui-textarea" name="executor_param" placeholder=""></textarea>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button lay-submit class="layui-btn" lay-filter="execBtn">执行</button>
                </div>
            </div>
        </form>
    </div>
</div>

<#-- 公共 JS -->
<@netCommon.commonScript />

<script type="text/html" id="operateBtnTpl">
    <div class="layui-btn-group">
        <button type="button" class="layui-btn">详情</button>
        <button type="button" class="layui-btn ">终止执行</button>
    </div>
</script>

<script>
    let element = layui.element;
    let layer = layui.layer;
    let table = layui.table;
    let $ = layui.jquery;
    let form = layui.form;
    let cur_selected_obj = undefined;

    !function() {
        // 渲染表格
        table.render({
            elem: '#dataTableID',
            url: '${contextPath}/log',
            cols: [[
                {field: 'group_name', title: '执行器'},
                {field: 'group_name', title: '执行器'},
                {field: 'trigger_time', title: '调度时间'},
                {field: 'trigger_code', title: '调度结果'},
                {field: 'exec_time', title: '执行时间'},
                {field: 'exec_code', title: '执行结果'},
                {field: 'author', title: '负责人'},
                {fixed: 'right', title: '操作', toolbar: '#showOperate'},
            ]],
            page: true,
            response: {
                statusCode: 200
            },
            parseData: function(res) {
                return {
                    "code": res.code,
                    "msg": res.msg,
                    "count": res.content.total,
                    "data": res.content.list
                };
            }
        });

        // 渲染分组下拉框
        $.get('${contextPath}/group/list_all', function (ret) {
            if (ret.code !== 200) {
                layer.error('渲染任务分组异常');
                return;
            }
            let contentArray = ret.content;
            contentArray.forEach(function (item) {
                $('#searchGroupNameID').append("<option value='" + item.name + "'>" + item.title + "</option>")
            })
            form.render();
        });

        // 渲染路由策略
        $.get('${contextPath}/info/list_route', function (ret) {
            if (ret.code !== 200) {
                layer.error('渲染路由策略异常');
                return;
            }
            let contentArray = ret.content;
            $("select[name='executor_route_strategy']").each(function (index, elem) {
                let _this = $(this);
                contentArray.forEach(function (item) {
                    _this.append("<option value='" + item.name + "'>" + item.desc + "</option>")
                })
            })
            form.render();
        });
    }();

    // 监听执行器的变更
    form.on('', function (data) {

    });

    // 搜索提交
    form.on('submit(searchBtn)', function (data) {
        cur_selected_obj = undefined;
        table.reload('dataTableID', {
            where: data.field,
            page: {
                curr: 1
            }
        });
        return false;
    });

    // 执行一次
    form.on('submit(execBtn)', function (data) {
        let id = cur_selected_obj.data.id;
        $.post('${contextPath}/exec/' + id, {exec_param: data.executor_param}, function (ret) {
            layer.closeAll('page');
        });
    });
</script>
</body>
</html>