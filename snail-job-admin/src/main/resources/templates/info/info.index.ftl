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
    <@netCommon.commonLeft "job" />

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
                        <button id="addBtn" type="button" class="layui-btn layui-btn-normal">新增</button>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">名称</label>
                        <div class="layui-input-block">
                            <input type="text" name="groupName" class="layui-input" />
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">负责人</label>
                        <div class="layui-input-block">
                            <input type="text" name="author" class="layui-input" />
                        </div>
                    </div>
                    <div class="layui-inline">
                        <button lay-submit class="layui-btn" lay-filter="searchBtn">搜索</button>
                    </div>
                </div>
            </form>

            <#-- 数据表格 -->
            <table class="layui-hide" id="dataTable" lay-filter="dataTable"></table>

        </div>
    </div>

    <!-- 底部固定区域 -->
    <@netCommon.commonFooter />
</div>

<#-- 编辑 -->
<div id="editLayer" class="layui-row" style="display:none;">
    <div class="layui-col-lg10 layui-col-lg-offset1">
        <form id="editFormID" class="layui-form layui-form-pane" pane style="margin-top: 20px;">
            <div class="layui-form-item layui-hide">
                <div class="layui-input-block">
                    <input type="text" name="id" class="layui-input" />
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">分组</label>
                <div class="layui-input-block">
                    <select name="groupName">
                        <option value="">请选择分组</option>
                        <option value="010">北京</option>
                        <option value="021">上海</option>
                        <option value="0571">杭州</option>
                    </select>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">唯一标识</label>
                <div class="layui-input-block">
                    <input type="text" name="name" required lay-verify="required" class="layui-input" />
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">注册类型</label>
                <div class="layui-input-block">
                    <input type="radio" name="type" value="0" title="自动">
                    <input type="radio" name="type" value="1" title="手动">
                </div>
            </div>
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">地址列表</label>
                <div class="layui-input-block">
                    <textarea name="addressList" placeholder="地址列表使用英文逗号分开" class="layui-textarea">
                    </textarea>
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

<#-- 公共 JS -->
<@netCommon.commonScript />

<#-- 表格工具栏 -->
<script type="text/html" id="showOperate">
    <a class="layui-btn layui-btn-sm" lay-event="edit">编辑</a>
    <a class="layui-btn layui-btn-danger layui-btn-sm" lay-event="del">删除</a>
</script>

<script type="text/html" id="triggerStatusTpl">
    {{# if(d.triggerStatus == 0){ }}
        已停止
    {{# } else if(d.triggerStatus == 1){ }}
        运行中
    {{# } }}
</script>

<script>
    let element = layui.element;
    let layer = layui.layer;
    let table = layui.table;
    let $ = layui.jquery;
    let form = layui.form;

    !function() {
        // 渲染表格
        table.render({
            elem: '#dataTable',
            url: '${contextPath}/info',
            cols: [[
                {field: 'id', title: 'ID', fixed: 'left', width: 50, unresize: true},
                {field: 'description', title: '描述'},
                {field: 'cron', title: 'CRON表达式'},
                {field: 'executorHandler', title: '方法'},
                {field: 'author', title: '负责人'},
                {field: 'triggerStatus', title: '状态', templet: '#triggerStatusTpl'},
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
        table.on('tool(dataTable)', function(obj){
            let data = obj.data;
            let eventName = obj.event;
            if (eventName === 'show') {
                // 查看地址
                let addrArray = data.addressList.split(",");
                let htmlContent = '<div>';
                for (let i = 0; i < addrArray.length; i++) {
                    htmlContent +=  (i + 1) + ': ' + addrArray[i];
                    htmlContent += '<br/>';
                }
                htmlContent += '</div>';
                layer.open({
                    content: htmlContent
                });
            } else if (eventName === 'edit') {
                // 把信息都回显上去
                form.val('editForm', {
                    'id': data.id,
                    'title': data.title,
                    'name': data.name,
                    'type': data.type,
                    'addressList': data.addressList
                });
                form.render('radio');

                // 回显
                layer.open({
                    type: 1,
                    title: '编辑分组信息',
                    area: '350px',
                    content: $('#editLayer')
                })
            } else if (eventName === 'del') {
                // 删除
                layer.confirm('是否删除该条记录?', {icon: 3, title:'提示'}, function(index){
                    layer.close(index);
                    $.ajax({
                        url: '${contextPath}/group/' + data.id,
                        type: 'post',
                        data: {
                            _method: 'delete'
                        },
                        success: function (ret) {
                            layer.alert('删除成功');

                            // 刷新表格
                            table.reload('dataTable');
                        }
                    })
                });
            }
        });
    }();

    // 添加弹窗
    $('#addBtn').click(function () {
        $('#editFormID')[0].reset();
        form.render('radio');

        layer.open({
            type: 1,
            title: '添加分组信息',
            area: '350px',
            content: $('#editLayer')
        })
    })

    // 编辑表单提交
    form.on('submit(saveBtn)', function (data) {
        let field = data.field;
        let method = field.id === '' ? 'post' : 'put';
        $.ajax({
            url: '${contextPath}/group?_method=' + method,
            type: 'post',
            contentType: 'application/json',
            data: JSON.stringify(field),
            success: function (ret) {
                layer.close(layer.index);
                layer.alert('保存成功');

                // 刷新表格
                table.reload('dataTable');
            }
        });
        return false;
    });

    // 搜索提交
    form.on('submit(searchBtn)', function (data) {
        let field = data.field;
        layer.alert(JSON.stringify(field));
        table.reload('dataTable', {
            where: {
                title: field.title,
                name: field.name
            },
            page: {
                curr: 1
            }
        });
        return false;
    });
</script>
</body>
</html>