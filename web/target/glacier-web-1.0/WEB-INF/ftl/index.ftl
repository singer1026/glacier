<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<#include "head.ftl"/>
<body style="background-color:#3a6ea5;">
<div id="advance-img" style="position:absolute;top:-10000px;left:-10000px">
    <img src="${basePath}images/dialog/dialog_icon.png">
    <img src="${basePath}images/dialog/dialog_default_bg.gif">
    <img src="${basePath}images/dialog/dialog_default_x.gif">
    <img src="${basePath}images/window/window-default-bg.png">
    <img src="${basePath}images/window/window-default-x.png">
    <img src="${basePath}images/window/window_task_all.png">
    <img src="${basePath}images/window/window-icon-16.png">
    <img src="${basePath}images/window/window-icon-32.png">
    <img src="${basePath}images/window/window-icon-48.png">
    <img src="${basePath}images/window/window_toolbar.png">
    <img src="${basePath}images/window/window_start.png">
    <img src="${basePath}images/button/button_gray.png">
    <img src="${basePath}images/button/button_icon.png">
    <img src="${basePath}images/form/checkbox.gif">
    <img src="${basePath}images/form/input.png">
    <img src="${basePath}images/grid/grid-pagebar.png">
    <img src="${basePath}images/grid/grid-bg.png">
    <img src="${basePath}images/grid/row-over.gif">
    <img src="${basePath}images/message/bg.gif">
    <img src="${basePath}images/message/tool-sprites.gif">
    <img src="${basePath}images/mask/gb_tip_layer.png">
    <img src="${basePath}images/mask/loading_big.png">
</div>
<script type="text/javascript">
    (function() {
        var A = window["athene"],W = window,D = document;
        var B = {
            baseRoleList:${baseRoleList},
            roleList:${roleList},
            userInfo:${userInfo}
        };
        var C = {
            hasBaseRole:function(baseRoleName) {
                if (baseRoleName) {
                    var list = B.baseRoleList,len = list.length;
                    for (var i = 0; i < len; i++) {
                        if (baseRoleName === list[i]) return true;
                    }
                }
                return false;
            },
            hasRole:function(roleName) {
                if (roleName) {
                    var list = B.roleList,len = list.length;
                    for (var i = 0; i < len; i++) {
                        if (roleName === list[i]) return true;
                    }
                }
                return false;
            },
            getUserInfo:function() {
                return B.userInfo;
            }
        };
        A.addModule("security", C);
    })();

    athene.env.set("refreshSubmit", true);
    athene.env.set("desktopMenu", "${template_desktopMenu}");
    athene.env.set("window", "${template_window}");
    athene.env.set("message", "${template_message}");
    athene.env.set("dialog", "${template_dialog}");
    athene.env.set("mask", "${template_mask}");

    function getWallpaper() {
        page.showMask("正在加载桌面壁纸，请稍后...", false);
        var image = new Image(),screen = window.screen;
        var x = screen.width,y = screen.height;
        var src = "getWallpaper.jpg?x=" + x + "&y=" + y;
        image.onload = function() {
        <#if hasBaseRole("base_today_task_notification_query")>
            window.setTimeout(athene.core.queryTodayTaskNotification, 1000 * 14);
            window.setInterval(athene.core.queryTodayTaskNotification, 1000 * 60 * 4);
            <#else>
                window.setInterval(athene.core.KS, 1000 * 60 * 4);
        </#if>
            var A = window["athene"],UTIL = A.util,ENV = athene.env,RANDOM = UTIL.random;
            var userInfo = A.security.getUserInfo();
            var mHtml = [];
            mHtml.push("<div class='ti2 fs13'>尊敬的<span class='fwb' style='color:#ff4500;'>");
            mHtml.push(userInfo.name);
            mHtml.push("</span>，欢迎您登陆琴音汇钢琴音乐艺术中心。");
            if (!ENV.get("isIE6") && !ENV.get("isIE7") && !ENV.get("isIE8")) {
                mHtml.push("由于测试时间少，如果发现bug，请及时告知本人。");
            } else {
                mHtml.push("您的浏览器为");
                if (ENV.get("isIE8")) {
                    mHtml.push("Internet Explore 8.0");
                } else if (ENV.get("isIE7")) {
                    mHtml.push("Internet Explore 7.0");
                } else if (ENV.get("isIE6")) {
                    mHtml.push("Internet Explore 6.0");
                } else {
                    mHtml.push("未知版本");
                }
                mHtml.push("，动画效果关闭，系统动画仅支持Chrome、FireFox、Internet Explore 9.0。");
            }
            mHtml.push("</div>");
            var openMessage = function() {
                A.message.openMessage({
                    message:mHtml.join(""),
                    title:"欢迎",
                    delay:10000,
                    width:240,
                    height:150
                });
            };
            page.hideMask(function() {
                athene.desktop.create();
                var desktop = $I("desktop-default");
                adjustBody(),$(window).resize(adjustBody);
                var url = "url(${basePath}" + src + ") no-repeat scroll 0 0 #3a6ea5";
                if (!ENV.get("isIE6") && !ENV.get("isIE7") && !ENV.get("isIE8")) {
                    var html = [],h = desktop.outerHeight(),w = desktop.outerWidth();
                    var numbers = [1,2,3,4,5,6,7,8];
                    var getNumber = function() {
                        var list = numbers,len = list.length;
                        var random = RANDOM(len);
                        var item = list[random];
                        list.splice(random, 1);
                        return item;
                    };
                    var allSelect = [];
                    for (var j = 1; j < 9; j++) {
                        html.push("<div id='wallpaper-opacity-" + j + "' style='position:absolute;opacity:0;");
                        html.push("background:" + url);
                        html.push(";z-index:15000;width:" + w + "px;height:" + h + "px;");
                        if (j === 1 || j === 2 || j === 3) {
                            html.push("top:-" + h + "px;");
                        } else if (j === 4 || j === 5) {
                            html.push("top:0px;");
                        } else {
                            html.push("top:" + h + "px;");
                        }
                        if (j === 1 || j === 4 || j === 6) {
                            html.push("left:-" + w + "px;");
                        } else if (j === 2 || j === 7) {
                            html.push("left:0px;");
                        } else {
                            html.push("left:" + w + "px;");
                        }
                        html.push("'></div>");
                        allSelect.push("#wallpaper-opacity-" + j);
                        j < 8 && allSelect.push(",");
                    }
                    $("body").append(html.join(""));
                    var len = RANDOM(2) + 1,select = [];
                    var speed = RANDOM(1000) + 1001;
                    for (var i = 0; i < len; i++) {
                        var count = i + 1;
                        select.push("#wallpaper-opacity-" + getNumber());
                        count < len && select.push(",");
                    }
                    $(select.join("")).animate({left:"0px",top:"0px",opacity:"1"}, speed, null, function() {
                        $(allSelect.join("")).remove();
                        desktop.css("background", url);
                        athene.desktop.get("desktop-default").getDmenubar().renderMenus(${desktopMenus});
                        window.setTimeout(openMessage, 1000);
                    });
                } else {
                    desktop.css("background", url);
                    A.desktop.get("desktop-default").getDmenubar().renderMenus(${desktopMenus});
                    window.setTimeout(openMessage, 1000);
                }
            });
        };
        image.src = src;
    }

    function adjustBody() {
        var winEl = $(window),bodyEl = $("body");
        var windowW = winEl.width(),windowH = winEl.height();
        bodyEl.width(windowW);
        bodyEl.height(windowH);
        athene.desktop.get("desktop-default").setSize(windowW, windowH);
    }

    $(function() {
        if (athene.env.get("isIE6") || athene.env.get("isIE7")) {
            $("body").empty();
            alert("本系统不支持IE6和IE7");
            return;
        }
        getWallpaper();
    });
</script>
</body>
</html>