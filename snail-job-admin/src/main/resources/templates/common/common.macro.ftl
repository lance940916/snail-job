<#-- 自定义变量 -->
<#assign contextPath="${springMacroRequestContext.contextPath}" >

<#-- 公共 Head -->
<#macro commonHead>
    <meta charset="utf-8">
    <link rel="shortcut icon" href="${contextPath}/static/favicon.ico">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
</#macro>

<#-- 公共 CSS -->
<#macro commonStyle>
    <link rel="stylesheet" href="${contextPath}/static/css/layui.css">
    <link rel="stylesheet" href="${contextPath}/static/css/snail.css">
</#macro>

<#-- 公共 JS -->
<#macro commonScript>
    <script src="${contextPath}/static/layui.all.js"></script>
</#macro>

<#-- Header -->
<#macro commonHeader>
    <div class="layui-header">
        <div class="layui-logo">蜗牛任务调度中心</div>
        <#-- 右侧菜单栏 -->
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:void(0);">
                    欢迎: 蜗牛本尊
                </a>
                <dl class="layui-nav-child">
                    <dd><a href="">修改资料</a></dd>
                    <dd><a href="">注销</a></dd>
                </dl>
            </li>
        </ul>
    </div>
</#macro>

<#-- 侧边栏 -->
<#macro commonLeft pageName>
    <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
            <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
            <ul class="layui-nav layui-nav-tree">
                <li class="layui-nav-item <#if pageName=="index">layui-this</#if>">
                    <a href="${contextPath}/">控制台</a>
                </li>
                <li class="layui-nav-item <#if pageName=="group">layui-this</#if>">
                    <a href="${contextPath}/group_page">执行器管理</a>
                </li>
                <li class="layui-nav-item <#if pageName=="info">layui-this</#if>">
                    <a href="${contextPath}/info_page">任务管理</a>
                </li>
                <li class="layui-nav-item <#if pageName=="log">layui-this</#if>">
                    <a href="${contextPath}/log_page">日志管理</a>
                </li>
            </ul>
        </div>
    </div>
</#macro>

<#-- 底部栏 -->
<#macro commonFooter>
    <#--<div class="layui-footer">
        <strong>
            Copyright &copy; 2020-${.now?string('yyyy')} 蜗牛大师
        </strong>
    </div>-->
</#macro>