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
    <@netCommon.commonLeft "log" />

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
                            <select id="searchGroupNameID" name="groupName" autocomplete="off"
                                    lay-filter="searchGroupNameFilter">
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
                        <button id="showDelLogLayerBtn" type="button" class="layui-btn layui-btn-normal">清理日志</button>
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

<#-- 日志详情弹窗 -->
<div id="logDetailLayer" class="layui-row" style="display:none;margin-right: 20px;">
    <div class="layui-col-lg12">
        <form id="logDetailFormId" class="layui-form" pane style="margin-top: 20px;" lay-filter="logDetailForm">
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">执行地址</label>
                <div class="layui-input-block">
                    <input class="layui-text" name="executor_address" disabled/>
                </div>
            </div>
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">执行参数</label>
                <div class="layui-input-block">
                    <textarea class="layui-textarea" name="executor_param" disabled></textarea>
                </div>
            </div>
        </form>
    </div>
</div>

<#-- 清理日志弹窗 -->
<div id="delLogLayer" class="layui-row" style="display:none;margin-right: 20px;">
    <div class="layui-col-lg12">
        <blockquote class="layui-elem-quote layui-bg-red">注意：如不选择分组和任务将清空所选时间段内的所有日志！！！</blockquote>
        <form id="delLogFormID" class="layui-form" pane style="margin-top: 20px;" lay-filter="delLogForm">
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">执行器</label>
                    <div class="layui-input-inline">
                        <select id="delGroupNameID" name="group_name" autocomplete="off"
                                lay-filter="delGroupNameFilter">
                            <option value="">请选择</option>
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">任务</label>
                    <div class="layui-input-inline">
                        <select id="delJobID" name="job_id" autocomplete="off">
                            <option value="">请选择</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">时间范围</label>
                <div class="layui-input-block">
                    <input type="text" id="timeRangeID" name="time_range" class="layui-input" required
                           lay-verify="required"
                           placeholder="请选择时间范围" autocomplete="off"/>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button lay-submit class="layui-btn layui-btn-fluid" lay-filter="delLogBtn">点击清理</button>
                </div>
            </div>
        </form>
    </div>
</div>

<#-- 公共 JS -->
<@netCommon.commonScript />

<#-- 单行操作按钮 -->
<script type="text/html" id="operateToolbar">
    <button type="button" class="layui-btn layui-btn-sm" lay-event="kill">终止执行</button>
</script>

<#-- 转义表格字段 -->
<script type="text/html" id="triggerCodeTpl">
    {{# if(d.trigger_code == 0){ }}
    <span style="color: #009688;">未调度</span>
    {{# } else if(d.trigger_code == 200){ }}
    <span style="color: dodgerblue;">成功</span>
    {{# } else if(d.trigger_code == 500){ }}
    <span style="color: red;">失败</span>
    {{# } else { }}
    <span style="color: #009688;">未知</span>
    {{# } }}
</script>
<script type="text/html" id="execCodeTpl">
    {{# if(d.exec_code == 0){ }}
    <span style="color: #009688;">未执行</span>
    {{# } else if(d.exec_code == 200){ }}
    <span style="color: dodgerblue;">成功</span>
    {{# } else if(d.exec_code == 500){ }}
    <span style="color: red;">失败</span>
    {{# } else { }}
    <span style="color: #009688;">未知</span>
    {{# } }}
</script>
<script type="text/html" id="showExecParam">
    <a class="layui-btn layui-btn-normal layui-btn-sm" lay-event="show">查看</a>
</script>

<script>
    let element = layui.element;
    let layer = layui.layer;
    let table = layui.table;
    let $ = layui.jquery;
    let form = layui.form;
    let laydate = layui.laydate;

    !function () {
        // 渲染表格
        table.render({
            elem: '#dataTableID',
            url: '${contextPath}/log',
            cols: [[
                {type: 'numbers'},
                {field: 'job_name', title: '任务名称'},
                {field: 'executor_address', title: '执行地址'},
                {field: 'executor_handler', title: 'JobHandler'},
                {title: '执行参数', toolbar: '#showExecParam'},
                {field: 'trigger_time', title: '调度时间', minWidth: 165},
                {field: 'trigger_code', title: '调度结果', templet: '#triggerCodeTpl'},
                {field: 'exec_time', title: '执行时间', minWidth: 165,},
                {field: 'exec_code', title: '执行结果', templet: '#execCodeTpl'},
                {fixed: 'right', title: '操作', minWidth: 145, toolbar: '#operateToolbar'},
            ]],
            page: true,
            response: {
                statusCode: 200
            },
            parseData: function (res) {
                return {
                    "code": res.code,
                    "msg": res.msg,
                    "count": res.content.total,
                    "data": res.content.list
                };
            }
        });

        // 渲染分组下拉框
        $.get('${contextPath}/app/list_all', function (ret) {
            if (ret.code !== 200) {
                layer.error('渲染任务分组异常');
                return;
            }
            let contentArray = ret.content;
            let htmlContent = "<option value=''>请选择</option>";
            contentArray.forEach(function (item) {
                htmlContent += "<option value='" + item.name + "'>" + item.title + "</option>";
            })
            // 渲染查询条件的分组
            $('#searchGroupNameID option').remove();
            $('#searchGroupNameID').append(htmlContent);

            // 渲染清理日志的分组
            $('#delGroupNameID option').remove();
            $('#delGroupNameID').append(htmlContent);
            form.render();
        });

        // 渲染时间组件
        laydate.render({
            elem: '#timeRangeID',
            type: 'date',
            range: true,
            format: 'yyyy-MM-dd HH:mm:ss'
        })
    }();

    table.on('tool(dataTable)', function (obj) {
        let data = obj.data;
        let eventName = obj.event;
        if (eventName === 'show') {
            layer.open({
                resize: false,
                content: data.executor_param
            });
        } else if (eventName === 'kill') {
            // 终止
            $.ajax({
                url: '${contextPath}/log/kill/' + data.id,
                type: 'POST',
                success: function (ret) {
                    if (ret.code === 200) {
                        layer.alert('终止成功');
                    } else {
                        layer.alert(ret.msg);
                    }
                }
            });
        } else {
            layer.alert('这是神马操作？？？');
        }
    });

    // 监听执行器的变更
    form.on('select(searchGroupNameFilter)', onGroupNameChange);
    form.on('select(delGroupNameFilter)', onGroupNameChange);

    function onGroupNameChange(data) {
        let $elem = $(data.elem);
        let $this = undefined;
        if ($elem.attr("id") === 'searchGroupNameID') {
            $this = $('#searchJobID');
        } else if ($elem.attr("id") === 'delGroupNameID') {
            $this = $('#delJobID');
        } else {
            layer.alert('内部错误');
            return false;
        }
        if (data.value === '') {
            $this.find('option').remove();
            $this.append("<option value=''>请选择</option>");
            form.render();
            return;
        }
        // 渲染路由策略
        $.get('${contextPath}/info/list_all', {groupName: data.value}, function (ret) {
            if (ret.code !== 200) {
                layer.error('渲染路由策略异常');
                return;
            }
            let contentArray = ret.content;
            let htmlContent = "<option value=''>请选择</option>";
            contentArray.forEach(function (item) {
                htmlContent += "<option value='" + item.id + "'>" + item.name + "</option>";
            })
            $this.children().remove();
            $this.append(htmlContent);
            form.render();
        });
    }

    // 搜索提交
    form.on('submit(searchBtn)', function (data) {
        table.reload('dataTableID', {
            where: data.field,
            page: {
                curr: 1
            }
        });
        return false;
    });

    // 监听清理日志按钮
    $('#showDelLogLayerBtn').click(function () {
        // 弹窗，选择要清理的时间段
        layer.open({
            type: 1,
            title: '删除日志',
            area: '700px',
            resize: false,
            offset: '100px',
            content: $('#delLogLayer')
        })
    });
    form.on('submit(delLogBtn)', function (data) {
        let field = data.field;

        // 拆分时间范围为两个时间字段
        let tr = field.time_range;
        let timeArray = tr.split(' - ');
        delete field.time_range;
        field.begin_date = timeArray[0];
        field.end_date = timeArray[1];

        // 请求删除
        $.ajax({
            url: '${contextPath}/log/batch_delete',
            type: 'post',
            contentType: 'application/json',
            data: JSON.stringify(field),
            success: function (ret) {
                if (ret.code === 200) {
                    layer.closeAll('page');
                    layer.alert('删除成功');
                } else {
                    layer.alert('服务器内部错误');
                }
            }
        });
        return false;
    });

</script>
</body>
</html>