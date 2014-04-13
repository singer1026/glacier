window["athene"] || (function() {
    var W = window,D = document;
    var A = {
        addModule : function(a, b) {
            this[a] = this[a] || b;
        },
        apply:function(src, tar) {
            if (src && typeof(src) === "object" && tar && typeof(tar) === "object") {
                for (var k in tar) {
                    var tk = tar[k];
                    if (tk !== undefined && tk !== null) {
                        src[k] = tk;
                    }
                }
            }
        }
    };
    var B = {
        $I:function(i) {
            return jQuery("#" + i);
        },
        DI:function(i) {
            return D.getElementById(i);
        },
        KS:function () {
            if (W["log"])log.debug("try to keep session!");
            $.ajax({
                url: "keepSession"
            });
        },
        queryTodayTaskNotification:function() {
            $.ajax({
                url: "queryTodayTaskNotification",
                context:this,
                dataType:"json",
                success:function(d) {
                    if (d) {
                        if (d.returnCode === 4 || d.returnCode === 2) {
                            athene.dialog.error(d.exceptionDesc);
                            return;
                        } else if (d.returnCode === 3) {
                            athene.dialog.alertException(d.exceptionDesc);
                            return;
                        } else if (d.returnCode === 0) {
                            var html = [],items = d.data,len = items.length;
                            if (len > 0) {
                                for (var i = 0; i < len; i++) {
                                    var item = items[i];
                                    html.push("<div class='task-float'><span class='task-float-number'>" + (i + 1) + ".</span><a");
                                    if (item.taskNotificationStatus === "unread") {
                                        html.push(" class='task-float-unread'");
                                    }
                                    html.push(" href='javascript:void(0)'");
                                    html.push(" onclick=\"athene.page.taskEdit({openerNumber:101,title:'" + item.title + "',id:" + item.id + "},this);\">");
                                    html.push(item.title);
                                    html.push("</a></div>");
                                }
                                athene.message.openMessage({
                                    title:"今日任务一览",
                                    width:300,
                                    height:200,
                                    message:html.join(""),
                                    closeable:true
                                });
                            }
                        }
                    } else {
                        athene.dialog.error("无任何返回信息");
                    }
                }
            });
        },
        getLogConsole:function(f) {
            $.ajax({
                url: "getLogConsole",
                context:this,
                success:function(data) {
                    A.env.set("logConsole", data);
                    if (f)f.call(this);
                }
            });
        },
        getDesktopMenus:function(f) {
            $.ajax({
                url: "getDesktopMenus",
                context:this,
                success:function(d) {
                    var dmenubar = athene.desktop.get("desktop-default").getDmenubar();
                    dmenubar.list = d.data;
                    dmenubar.renderMenus();
                    f && f.call(this);
                }
            });
        },
        getActiveWindowSize:function() {
            var windowEl = $(W),documentEl = $(D);
            var windowW = windowEl.width(),windowH = windowEl.height();
            var documentW = documentEl.width(),documentH = documentEl.height();
            var w = (windowW >= documentW) ? windowW : documentW;
            var h = (windowH >= documentH) ? windowH : documentH;
            return {width:w,height:h};
        }
    };
    A.addModule("core", B);
    W["athene"] = A,W["$I"] = A.core.$I,W["DI"] = A.core.DI,W["json"] = $.toJSON;
})();