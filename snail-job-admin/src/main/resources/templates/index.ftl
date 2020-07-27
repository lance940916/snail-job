<#assign contextPath="${springMacroRequestContext.contextPath}" >
<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <title>蜗牛任务调度中心</title>
    <#import "./common/common.macro.ftl" as netCommon />

    <@netCommon.commonHead />
    <@netCommon.commonStyle />
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <#-- 头部区域 -->
    <@netCommon.commonHeader />

    <#-- 左侧菜单栏 -->
    <@netCommon.commonLeft "index" />

    <div class="layui-body">
        <!-- 内容主体区域 -->
        <div style="padding: 15px;">

            <fieldset class="layui-elem-field layui-field-title">
                <legend>系统任务状态</legend>
            </fieldset>
            <table class="layui-table" lay-size="lg">
                <tbody>
                <tr>
                    <td style="width: 100px;font-size: large;font-weight: bold;">任务数量</td>
                    <td>
                        <span id="total-job-amount"></span>
                    </td>
                    <td style="width: 100px;font-size: large;font-weight: bold;">执行器数量</td>
                    <td>
                        <span id="total-executor-amount"></span>
                    </td>
                    <td style="width: 100px;font-size: large;font-weight: bold;">调度次数</td>
                    <td>
                        <span id="total-trigger-amount"></span>
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
    // 如何避免别人通过直接请求接口获取数据
    layui.use(['element', 'jquery'], function () {
        let element = layui.element;
        let $ = layui.jquery;

        $('#total-job-amount').text(8);
        $('#total-executor-amount').text(3);
        $('#total-trigger-amount').text(23);

    });
</script>
</body>
</html>