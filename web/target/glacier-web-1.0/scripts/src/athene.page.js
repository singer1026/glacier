window["athene"]["page"] || (function() {
    var A = window["athene"],W = window,D = document,G = A.grid,U = A.util,ENV = A.env;
    var DIALOG = A.dialog,WINDOWMANAGER = A.windowmanager,LISTFIELD = A.listfield;
    var B = {
        addBusiness:function(biz) {
            if (biz) {
                for (k in biz) {
                    if (this[k])throw Error(k + "已定义");
                    this[k] = biz[k];
                }
            }
        },
        login:function() {
            var container = $I("login-table");
            $.ajax({
                type: "POST",
                context:this,
                url: "doLogin",
                cache:false,
                data:{
                    args:json(container.getRow())
                },
                dataType:"json",
                beforeSend:function() {
                    this.showMask("正在登陆，请稍等...");
                    container.find(".fm-error").hide().find(".fm-error-content").empty();
                },
                success: function(d) {
                    this.hideMask(function() {
                        if (d.returnCode === 1) {
                            var list = d.validationErrors;
                            for (var i = 0; i < list.length; i++) {
                                var error = list[i];
                                container.find("#" + list[i].id).show().find(".fm-error-content").html(list[i].message);
                            }
                        } else if (d.returnCode === 3) {
                            alert(d.exceptionDesc);
                        } else if (d.returnCode === 0) {
                            window.location.href = ENV.get("baseUrl");
                        }
                    });
                }
            });
        },
        loginOut:function () {
            W.location.href = "loginOut";
        },
        showMask:function(msg, flag) {
            var loading = $I("mask-loading");
            if (loading.length === 0) {
                var html = ENV.get("mask").replaceAll("{mask-id}", "mask-loading").replaceAll(
                    "{overlay-id}", "mask-overlay");
                $("body").prepend(html);
                loading = $I("mask-loading").closeSelect();
            }
            loading.find(".mask-content").html(msg || "正在处理，请稍等...");
            var overlay = $I("mask-overlay");
            var size = A.core.getActiveWindowSize();
            var w = size.width,h = size.height;
            var ml = (w - loading.outerWidth(true)) / 2;
            var mt = (h - loading.outerHeight(true)) / 2;
            overlay.width(w),overlay.height(h);
            flag === false || overlay.css("opacity", 0.7).css("left", "0px").css("top", "0px").css("zIndex", 15000).show();
            loading.css("top", mt + "px"),loading.css("left", ml + "px").css("zIndex", 15001).show();
        },
        hideMask:function(callback) {
            $I("mask-loading").fadeOut(0, function() {
                $I("mask-overlay").fadeOut(ENV.get("speed"), function() {
                    callback && callback.call(W);
                });
            });
        },
        gridSearch:function (a, b) {
            var row = $I(a).getRow();
            athene.grid.get(b).query(row);
        },
        systemManager:function (dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                width:500,
                height:500,
                title:"系统管理",
                url:"systemManagerList",
                initPosition:$(dom).find(".dm-icon").offset(),
                maxButton:false,
                iconCls:"icon-system-manager"
            });
        },
        userOwnManager:function (dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                width:600,
                height:250,
                title:"个人资料更新",
                url:"userOwnEdit",
                initPosition:$(dom).find(".dm-icon").offset(),
                iconCls:"icon-user-own-manager",
                maxButton:false,
                onClose:function() {
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        },
        passwordManager:function (dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                width:350,
                height:220,
                title:"修改密码",
                url:"passwordEdit",
                initPosition:$(dom).offset(),
                maxButton:false,
                onClose:function() {
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        }
    };
    A.addModule("page", B);
    W["page"] = A.page;
})();