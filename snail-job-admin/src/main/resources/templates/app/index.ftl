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
    <@netCommon.commonLeft "app" />

    <div class="layui-body">
        <!-- 内容主体区域 -->
        <div style="padding: 15px;">

            <fieldset class="layui-elem-field layui-field-title">
                <legend>应用管理</legend>
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
                            <input type="text" name="title" class="layui-input" autocomplete="off"/>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">唯一标识</label>
                        <div class="layui-input-block">
                            <input type="text" name="name" class="layui-input" autocomplete="off"/>
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
<div id="editLayer" class="layui-row" style="display:none;">
    <div class="layui-col-lg10 layui-col-lg-offset1">
        <form id="editFormID" class="layui-form layui-form-pane" pane style="margin-top: 20px;" lay-filter="editForm">
            <div class="layui-form-item layui-hide">
                <div class="layui-input-block">
                    <input type="text" name="id" class="layui-input"/>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">名称</label>
                <div class="layui-input-block">
                    <input type="text" name="title" required lay-verify="required" class="layui-input"
                           autocomplete="off"/>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">唯一标识</label>
                <div class="layui-input-block">
                    <input type="text" name="name" required lay-verify="required" class="layui-input"
                           autocomplete="off"/>
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
                    <textarea name="address_list" placeholder="地址列表使用英文逗号分开" class="layui-textarea"></textarea>
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

<#-- 查看执行器地址 -->
<script type="text/html" id="showExecAddr">
    <a class="layui-btn layui-btn-normal layui-btn-sm" lay-event="show">查看</a>
</script>

<#-- 表格工具栏 -->
<script type="text/html" id="operateToolbar">
    <div class="layui-btn-group">
        <button class="layui-btn layui-btn-sm" lay-event="edit">编辑</button>
        <button class="layui-btn layui-btn-danger layui-btn-sm" lay-event="del">删除</button>
    </div>
</script>

<script type="text/html" id="typeTpl">
    {{# if(d.type == 0){ }}
    自动注册
    {{# } else if(d.type == 1){ }}
    手动注册
    {{# } }}
</script>

<script>
    let element = layui.element;
    let layer = layui.layer;
    let table = layui.table;
    let $ = layui.jquery;
    let form = layui.form;

    !function () {
        // 渲染表格
        table.render({
            elem: '#dataTableID',
            url: '${contextPath}/app',
            cols: [[
                {field: 'id', title: 'ID', fixed: 'left', width: 100, unresize: true},
                {field: 'title', title: '名称'},
                {field: 'name', title: '唯一标识'},
                {field: 'type', title: '注册类型', templet: '#typeTpl'},
                {title: '执行器地址', toolbar: '#showExecAddr'},
                {fixed: 'right', title: '操作', toolbar: '#operateToolbar'},
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
        table.on('tool(dataTable)', function (obj) {
            let data = obj.data;
            let eventName = obj.event;
            if (eventName === 'show') {
                // 查看地址
                let htmlContent = '<div>';
                let addrList = data.address_list;
                if (addrList === undefined || addrList === '') {
                    htmlContent += '没有注册的机器';
                } else {
                    let addrArray = addrList.split(",");
                    for (let i = 0; i < addrArray.length; i++) {
                        htmlContent += (i + 1) + ': ' + addrArray[i];
                        htmlContent += '<br/>';
                    }
                }
                htmlContent += '</div>';
                layer.open({
                    resize: false,
                    content: htmlContent
                });
            } else if (eventName === 'edit') {
                // 把信息都回显上去
                form.val('editForm', data);
                form.render('radio');

                // 回显
                layer.open({
                    type: 1,
                    title: '编辑执行器信息',
                    area: '350px',
                    resize: false,
                    content: $('#editLayer')
                })
            } else if (eventName === 'del') {
                // 删除
                layer.confirm('是否删除该条记录?', {icon: 3, title: '提示'}, function (index) {
                    layer.close(index);
                    $.ajax({
                        url: '${contextPath}/app/' + data.id + '?_method=delete',
                        type: 'post',
                        success: function (ret) {
                            // 刷新表格
                            table.reload('dataTableID');
                        }
                    })
                });
            }
        });
        // 表格单击事件
        table.on('row(dataTable)', function (obj) {
            if (obj.tr.hasClass("snail-bg-selected")) {
                obj.tr.removeClass('snail-bg-selected');
            } else {
                obj.tr.addClass('snail-bg-selected').siblings().removeClass('snail-bg-selected');
            }
        });
    }();

    // 添加弹窗
    $('#addBtn').click(function () {
        $('#editFormID')[0].reset();
        form.render('radio');

        layer.open({
            type: 1,
            title: '添加执行器信息',
            area: '350px',
            resize: false,
            content: $('#editLayer')
        })
    })

    // 编辑表单提交
    form.on('submit(saveBtn)', function (data) {
        let field = data.field;
        let method = field.id === '' ? 'post' : 'put';
        $.ajax({
            url: '${contextPath}/app?_method=' + method,
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
                table.reload('dataTableID');
            }
        });
        return false;
    });

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

</script>
</body>
</html>