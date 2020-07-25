<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <#import "../common/common.macro.ftl" as netCommon />
    <@netCommon.commonHead />
    <@netCommon.commonStyle />
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <#-- 头部区域 -->
    <@netCommon.commonHeader />

    <#-- 左侧菜单栏 -->
    <@netCommon.commonLeft "group" />

    <div class="layui-body">
        <!-- 内容主体区域 -->
        <div style="padding: 15px;">

            <fieldset class="layui-elem-field layui-field-title">
                <legend>执行器管理</legend>
            </fieldset>

            <table class="layui-table">
                <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th>名称</th>
                    <th>唯一标识</th>
                    <th>注册方式</th>
                    <th>在线机器</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>1</td>
                    <td>系统1</td>
                    <td>1234</td>
                    <td>自动注册</td>
                    <td>
                        <button class="layui-btn layui-btn-normal layui-btn-sm">
                            查看<span class="layui-badge layui-bg-gray">2</span>
                        </button>
                    </td>
                    <td>
                        <button type="button" class="layui-btn">编辑</button>
                        <button type="button" class="layui-btn">删除</button>
                    </td>
                </tr>
                <tr>
                    <td>2</td>
                    <td>系统2</td>
                    <td>1234</td>
                    <td>自动注册</td>
                    <td>
                        <button class="layui-btn layui-btn-normal layui-btn-sm">
                            查看<span class="layui-badge layui-bg-gray">2</span>
                        </button>
                    </td>
                    <td>
                        <button type="button" class="layui-btn">编辑</button>
                        <button type="button" class="layui-btn">删除</button>
                    </td>
                </tr>
                </tbody>
            </table>

        </div>
    </div>

    <!-- 底部固定区域 -->
    <@netCommon.commonFooter />
</div>
<@netCommon.commonScript />
<script>
    //JavaScript代码区域
    layui.use('element', function () {
        let element = layui.element;


    });
</script>
</body>
</html>