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
    <@netCommon.commonLeft "log" />

    <div class="layui-body">
        <!-- 内容主体区域 -->
        <div style="padding: 15px;">

            <h1>任务数量: 8</h1>
            <h1>执行器数量: 8</h1>
            <h1>调度次数: 8</h1>

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