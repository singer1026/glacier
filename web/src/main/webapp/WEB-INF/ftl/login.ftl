<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<#include "head.ftl"/>
<body>
<div id="advance-img" style="position:absolute;top:-10000px;left:-10000px">
    <img src="${basePath}images/mask/gb_tip_layer.png">
    <img src="${basePath}images/mask/loading_big.png">
</div>
<table id="login-table" style="margin:0 auto;width:240px;">
    <tr>
        <td>
            <div class="fm-item" style="padding-left:45px;">
                <label for="login-id" class="fm-label" style="width:40px;margin-left:-45px;">登录名</label>
                <input id="login-id" type="text" name="loginId" class="fm-text-field"/>

                <div id="login-id-error" class="fm-error">
                    <div class="btn-icon icon-error fl"></div>
                    <div class="fm-error-content fl"></div>
                    <div class="clear"/>
                </div>
            </div>
        </td>
    </tr>
    <tr>
        <td>
            <div class="fm-item" style="padding-left:45px;">
                <label for="password" class="fm-label" style="width:40px;margin-left:-45px;">
                    密&nbsp;&nbsp;&nbsp;&nbsp;码</label>
                <input id="password" type="password" name="password" class="fm-text-field"/>

                <div id="password-error" class="fm-error">
                    <div class="btn-icon icon-error fl"></div>
                    <div class="fm-error-content fl"></div>
                    <div class="clear"/>
                </div>
            </div>
        </td>
    </tr>
    <tr>
        <td align="left">
            <div style="margin-left:-3px;">
            <@Button label="确 认" iconCls="icon-login">
                page.login();
            </@Button>
            <@Button label="重 置" iconCls="icon-reset">
                reset();
            </@Button>
            </div>
        </td>
    </tr>
    <tr>
        <td>
            <div style="padding:4px 0px 0px 1px;line-height: 20px;font-size: 13px;">
                <span style="font-weight: bold;">友情提示：</span>
                本系统不支持IE浏览器动画，为了能够体验到全部动画效果，<br/>请下载
                <a style="color: #ff4500;font-weight:normal;text-decoration: underline;"
                   href="http://app24630.imgcache.qzoneapp.com/app24630/ChromePlus1.6.3.1.zip"
                   target="_blank">谷歌浏览器绿色版</a>
            </div>
        </td>
    </tr>
</table>
<script type="text/javascript">
    $(function() {
        if (athene.env.get("isIE6") || athene.env.get("isIE7")) {
            $("body").empty();
            alert("本系统仅支持 ie8 chrome firefox");
            return;
        }
        athene.env.set("mask", "${template_mask}");
        athene.env.set("dialog", "${template_dialog}");
        $I("login-table").doFocus();
        $(document).keydown(function(e) {
            e.keyCode === 13 && page.login();
        });
    });

    function reset() {
        $("#login-table input").val("");
        $("#login-table").find(".fm-error").empty().hide();
    }

</script>
</body>
</html>