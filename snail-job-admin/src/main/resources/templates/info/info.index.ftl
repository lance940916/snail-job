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
                <legend>任务管理</legend>
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
                        <label class="layui-form-label">名称</label>
                        <div class="layui-input-block">
                            <input type="text" name="name" class="layui-input" autocomplete="off" />
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">负责人</label>
                        <div class="layui-input-block">
                            <input type="text" name="author" class="layui-input" autocomplete="off" />
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

<#-- 编辑 -->
<div id="editLayer" class="layui-row" style="display:none;margin-right: 20px;">
    <div class="layui-col-lg12">
        <form id="editFormID" class="layui-form" pane style="margin-top: 20px;" lay-filter="editForm">
            <div class="layui-form-item layui-hide">
                <div class="layui-input-inline">
                    <input class="layui-input" name="id" value="" />
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">任务名称</label>
                    <div class="layui-input-inline">
                        <input class="layui-input" name="name" required lay-verify="required" autocomplete="off" />
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">Cron</label>
                    <div class="layui-input-inline">
                        <input class="layui-input" name="cron" required lay-verify="required" autocomplete="off" />
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">路由策略</label>
                    <div class="layui-input-inline">
                        <select name="executor_route_strategy" required lay-verify="required">
                            <option value="">请选择</option>
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">任务分组</label>
                    <div class="layui-input-inline">
                        <select id="editGroupNameId" name="group_name" required lay-verify="required">
                            <option value="">请选择</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">负责人</label>
                    <div class="layui-input-inline">
                        <input class="layui-input" name="author" required lay-verify="required" autocomplete="off" />
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">报警邮箱</label>
                    <div class="layui-input-inline">
                        <input class="layui-input" name="alarm_email" autocomplete="off" />
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">超时时间</label>
                    <div class="layui-input-inline">
                        <input class="layui-input" name="executor_timeout" autocomplete="off" />
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">重试次数</label>
                    <div class="layui-input-inline">
                        <input class="layui-input" name="executor_fail_retry_count" autocomplete="off" />
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">JobHandler</label>
                <div class="layui-input-inline">
                    <input class="layui-input" name="executor_handler" required lay-verify="required" autocomplete="off" />
                </div>
            </div>
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">执行参数</label>
                <div class="layui-input-block">
                    <textarea class="layui-textarea" name="executor_param" placeholder=""></textarea>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button lay-submit class="layui-btn" lay-filter="saveBtn">保存</button>
                </div>
            </div>
        </form>
    </div>
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

<#-- 表格工具栏 -->
<script type="text/html" id="toolbarID">
    <div class="layui-btn-group">
        <button class="layui-btn layui-btn-sm" lay-event="add">新增</button>
        <button class="layui-btn layui-btn-sm" lay-event="edit">编辑</button>
        <button class="layui-btn layui-btn-sm" lay-event="copy">复制</button>
        <button class="layui-btn layui-btn-sm" lay-event="del">删除</button>
    </div>
    <div class="layui-btn-group">
        <button class="layui-btn layui-btn-sm" lay-event="exec">执行一次</button>
        <button class="layui-btn layui-btn-sm" lay-event="nextTriggerTime">下次执行时间</button>
        <button class="layui-btn layui-btn-sm" lay-event="log">查询日志</button>
    </div>
    <div class="layui-btn-group">
        <button class="layui-btn layui-btn-sm" lay-event="start">启动</button>
        <button class="layui-btn layui-btn-sm" lay-event="stop">停止</button>
    </div>
</script>

<script type="text/html" id="triggerStatusTpl">
    <input type="checkbox" lay-skin="switch" lay-text="运行中|已停止" disabled
    {{# if(d.trigger_status == 1){ }} checked {{# } }}
    >
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
            url: '${contextPath}/info',
            toolbar: '#toolbarID',
            defaultToolbar: [],
            cols: [[
                {field: 'id', title: 'ID', fixed: 'left', width: 100, unresize: true},
                {field: 'name', title: '名称'},
                {field: 'cron', title: 'CRON表达式'},
                {field: 'executor_handler', title: '方法名'},
                {field: 'author', title: '负责人'},
                {field: 'trigger_status', title: '状态', templet: '#triggerStatusTpl'},
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
                $('#searchGroupNameID').append("<option value='" + item.name + "'>" + item.name + "</option>")
                $('#editGroupNameId').append("<option value='" + item.name + "'>" + item.name + "</option>")
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

    // 表格单击事件
    table.on('row(dataTable)', function(obj){
        if (obj.tr.hasClass("snail-bg-selected")) {
            obj.tr.removeClass('snail-bg-selected');
            cur_selected_obj = undefined;
        } else {
            obj.tr.addClass('snail-bg-selected').siblings().removeClass('snail-bg-selected');
            cur_selected_obj = obj;
        }
    });

    // 表格工具栏事件
    table.on('toolbar(dataTable)', function(obj){
        let eventName = obj.event;
        if (eventName !== 'add' && cur_selected_obj === undefined) {
            layer.msg('请先选择一行数据');
            return;
        }
        let eLayer = $('#editLayer');
        switch(eventName){
            case 'add':
                $('#editFormID')[0].reset();
                layer.open({
                    type: 1,
                    title: '添加任务',
                    area: '700px',
                    resize: false,
                    content: eLayer
                })
                break;
            case 'edit':
                form.val('editForm', cur_selected_obj.data);
                form.render('radio');
                layer.open({
                    type: 1,
                    title: '编辑任务',
                    area: '700px',
                    resize: false,
                    content: eLayer
                })
                break;
            case 'copy':
                $.post('${contextPath}/info/copy/' + cur_selected_obj.data.id, function (ret) {
                    if (ret.code === 200) {
                        cur_selected_obj = undefined;
                        table.reload('dataTableID');
                    } else {
                        layer.alert(ret.msg);
                    }
                });
                break;
            case 'del':
                layer.confirm('是否删除该条记录?', {icon: 3, title:'提示'}, function(index){
                    layer.close(index);
                    $.ajax({
                        url: '${contextPath}/info/' + cur_selected_obj.data.id,
                        type: 'post',
                        data: {_method: 'delete'},
                        success: function (ret) {
                            // 刷新表格
                            cur_selected_obj = undefined;
                            table.reload('dataTableID');
                        }
                    })
                });
                break;
            case 'exec':
                layer.open({
                    type: 1,
                    title: '执行任务',
                    area: '700px',
                    resize: false,
                    content: $('#execLayer')
                })
                break;
            case 'nextTriggerTime':
                let cron = cur_selected_obj.data.cron;
                $.get('${contextPath}/info/next_trigger_time', {cron: cron}, function (ret) {
                    if (ret.code !== 200) {
                        layer.alert(ret.msg);
                        return;
                    }
                    let htmlContent = '<div>';
                    let execTimeArray = ret.content;
                    for (let i = 0; i < execTimeArray.length; i++) {
                        htmlContent +=  (i + 1) + ': ' + execTimeArray[i];
                        htmlContent += '<br/>';
                    }
                    htmlContent += '</div>';
                    layer.open({
                        resize: false,
                        content: htmlContent
                    });
                });
                break;
            case 'log':
                layer.msg('查询日志');
                break;
            case 'start':
                if (cur_selected_obj.data.trigger_status === 0) {
                    $.post('${contextPath}/info/start/' + cur_selected_obj.data.id, function (ret) {
                        if (ret.code === 200) {
                            layer.alert('启动成功');
                            cur_selected_obj = undefined;
                            table.reload('dataTableID');
                        } else {
                            layer.alert(ret.msg);
                        }
                    });
                } else {
                    layer.alert('任务已启动');
                }
                break;
            case 'stop':
                if (cur_selected_obj.data.trigger_status === 1) {
                    $.post('${contextPath}/info/stop/' + cur_selected_obj.data.id, function (ret) {
                        if (ret.code === 200) {
                            layer.alert('停止成功');
                            cur_selected_obj = undefined;
                            table.reload('dataTableID');
                        } else {
                            layer.alert(ret.msg);
                        }
                    });
                } else {
                    layer.alert('任务已停止');
                }
                break;
            default:
                layer.msg('未知事件');
        }
    });

    // 编辑表单提交
    form.on('submit(saveBtn)', function (data) {
        let field = data.field;
        let method = field.id === '' ? 'post' : 'put';
        $.ajax({
            url: '${contextPath}/info?_method=' + method,
            type: 'post',
            contentType: 'application/json',
            data: JSON.stringify(field),
            success: function (ret) {
                if (ret.code !== 200) {
                    layer.alert(ret.msg);
                    return;
                }
                layer.closeAll('page');
                layer.alert("保存成功");

                // 刷新表格
                cur_selected_obj = undefined;
                table.reload('dataTableID');
            }
        });
        return false;
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