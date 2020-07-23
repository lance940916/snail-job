<#-- 公共 Head -->
<#macro commonHead>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>蜗牛任务调度中心</title>
</#macro>

<#-- 公共 CSS -->
<#macro commonStyle>
    <link rel="stylesheet" href="../static/css/layui.css">
</#macro>

<#-- 公共 JS -->
<#macro commonScript>
    <script src="../static/layui.js"></script>
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
                <li class="layui-nav-item <#if pageName=="index">layui-this</#if>"><a href="/">控制台</a></li>
                <li class="layui-nav-item <#if pageName=="group">layui-this</#if>"><a href="/group">执行器管理</a></li>
                <li class="layui-nav-item <#if pageName=="job">layui-this</#if>"><a href="/job">任务管理</a></li>
                <li class="layui-nav-item <#if pageName=="log">layui-this</#if>"><a href="/log">日志管理</a></li>
            </ul>
        </div>
    </div>
</#macro>

<#-- 底部栏 -->
<#macro commonFooter>
    <div class="layui-footer">
        <strong>
            Copyright &copy; 2020-${.now?string('yyyy')} 蜗牛大师
        </strong>
    </div>
</#macro>