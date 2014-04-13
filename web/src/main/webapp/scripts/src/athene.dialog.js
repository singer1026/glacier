window["athene"]["dialog"] || (function() {
    var A = window["athene"],W = window,D = document,ENV = A.env,DOM = A.dom,UTIL = A.util,INTERACTION = A.interaction;
    var B = {
        DIALOG_PREFIX:"dialog-",
        multiple : 2
    };
    var Dialog = function(p) {
        this._constructor(p);
        this._render();
        this._bindEvent();
    };
    var DP = {
        _constructor:function(p) {
            F.dialogs.push(this);
            this.width = 400;
            this.cls = "dialog-default";
            A.apply(this, p);
        },
        _render:function() {
            var html = ENV.get("dialog").replaceAll("{random}", this.number);
            html = html.replaceAll("{dialogIcon}", this.iconCls);
            $("body").append(html);
            this.id = B.DIALOG_PREFIX + this.number,
                this.el = $I(this.id),
                DOM.addClass(this.el.get(0), this.cls);
            this.el.find(".dialog-title").html(this.title || ""),
                this.el.find(".dialog-top-center").closeSelect();
            this._renderType();
            this.height = this.height || this.el.find(".dialog-content").outerHeight(true);
            this._show();
        },
        setSize:function(w, h, flag) {
            var isBack = false;
            var els = this.el.get(0).style;
            var dcs = this.el.find(".dialog-content").get(0).style;
            if (typeof(w) === "number" && w > 0) {
                els.width = (w + 5 * 2) + "px";
                dcs.width = w + "px";
                flag !== false && (this.width = w);
                isBack = true;
            }
            if (typeof(h) === "number" && h > 0) {
                dcs.height = h + "px";
                els.height = (h + 26 + 5) + "px";
                flag !== false && (this.height = h);
                isBack = true;
            }
            isBack === true && this.onSetSize && this.onSetSize.call(this, w, h);
        },
        _renderType:function() {
            var el = this.el;
            if (this.type === "confirm") {
                el.find(".dialog-content-button-other").remove();
            } else {
                el.find(".dialog-content-button-confirm").remove();
            }
            if (this.type == "alertException") {
                var textarea = "<textarea readonly='true' class='dialog-content-message-exception'>" + this.message + "</textarea>";
                el.find(".dialog-content-message-word").html(textarea);
            } else {
                el.find(".dialog-content-message-word").html(this.message);
            }
        },
        _bindEvent:function() {
            var close;
            if (this.type === "confirm") {
                close = this.el.find(".dialog-button-close");
                this.el.find(".dialog-button-ok").click($.proxy(function() {
                    this.close($.proxy(function() {
                        this.callback && this.callback.call(this.scope || this, true);
                    }, this));
                }, this));
                this.el.find(".dialog-button-cancel").click($.proxy(function() {
                    this.close($.proxy(function() {
                        this.callback && this.callback.call(this.scope || this, false);
                    }, this));
                }, this));
            } else {
                close = this.el.find(".dialog-button-close,.dialog-button-ok");
            }
            close.click($.proxy(function() {
                this.close();
            }, this));
            this.draggable = INTERACTION.draggable({
                src:"dialog-icon-title-" + this.number,
                tar:this.id
            });
        },
        showAt:function(x, y) {
            x = parseInt(x),y = parseInt(y);
            var els = this.el.get(0).style;
            els.left = x + "px";
            els.top = y + "px";
        },
        _initPosition:function() {
            var el = this.el,
                documentEl = $(D);
            var w = this.width,h = this.height;
            var size = A.core.getActiveWindowSize();
            var dScrollTop = documentEl.scrollTop(),
                dScrollLeft = documentEl.scrollLeft();
            var position = {};
            position.leftInit = (size.width - (this.width + 5 * 2) * B.multiple) / 2 + dScrollLeft;
            position.topInit = (size.height - (this.height + 26 + 5) * B.multiple) / 2 + dScrollTop;
            position.left = (size.width - (this.width + 5 * 2)) / 2 + dScrollLeft;
            position.top = (size.height - (this.height + 26 + 5)) / 2 + dScrollTop;
            this.position = position;
        },
        _show:function() {
            this._initPosition();
            var position = this.position;
            this.showAt(position.leftInit, position.topInit);
            this.setSize(this.width * B.multiple, this.height * B.multiple, false);
            this.el.css("visibility", "visible").css("fontSize", "24px").css("zIndex", 20001).css("opacity", 0).show();
            this._showOverlay();
            var elConfig = {
                opacity:1,
                width:this.width,
                height:this.height,
                fontSize:12,
                top:position.top,
                left:position.left};
            this.animate(elConfig, function() {
                this.el.find(".dialog-title").width(this.width - 44 - 16 - 4);
            });
        },
        animate:function(elConfig, callback, s) {
            if (this.isAnimate === true)return;
            this.isAnimate = true;
            var speed = s || ENV.get("speed");
            if (this.animateStep === undefined || this.animateStep === null)this.animateStep = 0;
            this.el.animate({opacity:elConfig.opacity,top:elConfig.top + "px",left:elConfig.left + "px",fontSize:elConfig.fontSize + "px",
                width:(elConfig.width + 5 * 2) + "px",height:(elConfig.height + 26 + 5) + "px"}, speed, $.proxy(function() {
                this.animateStep++;
            }, this));
            this.el.find(".dialog-content").animate({width:elConfig.width + "px",height:elConfig.height + "px"}, speed,
                $.proxy(function() {
                    this.animateStep++;
                }, this));
            var polling = function() {
                if (this.animateStep === 2) {
                    this.animateStep = 0;
                    this.isAnimate = false;
                    callback && callback.call(this);
                } else {
                    W.setTimeout($.proxy(polling, this), 50);
                }
            };
            W.setTimeout($.proxy(polling, this), speed);
        },
        _showOverlay:function() {
            this.beforeShowOverlay && this.beforeShowOverlay.call(this);
            var size = A.core.getActiveWindowSize();
            $("body").append("<div id='dialog-mask-overlay' class='mask-overlay' style='width:"
                + size.width + "px;height:" + size.height + "px;top:0px;left:0px;z-index:20000;overflow:hidden;opacity:0;'/>");
            $I("dialog-mask-overlay").show().animate({opacity: 0.7}, ENV.get("speed"));
        },
        _closeOverlay:function() {
            var overlay = $I("dialog-mask-overlay");
            overlay.fadeOut(ENV.get("speed"), $.proxy(function() {
                overlay.remove();
                this.onCloseOverlay && this.onCloseOverlay.call(this);
            }, this));
        },
        close:function(callback) {
            var position = this.position;
            var elConfig = {
                opacity:0,
                width:this.width * B.multiple,
                height:this.height * B.multiple,
                fontSize:24,
                top:position.topInit,
                left:position.leftInit};
            this._closeOverlay();
            this.animate(elConfig, function() {
                var dialogs = F.dialogs,len = dialogs.length;
                for (var i = 0; i < len; i++) {
                    var item = dialogs[i];
                    if (this.id == item.id) {
                        dialogs.splice(i, 1);
                        break;
                    }
                }
                this.draggable.destory();
                this.draggable = null;
                this.el.find(".dialog-button-close").unbind().remove();
                this.el.find(".dialog-button-ok").unbind().remove();
                this.el.find(".dialog-button-cancel").unbind().remove();
                this.el.unbind().remove();
                this.el = null;
                W.setTimeout($.proxy(function() {
                    callback && callback();
                    this.scope = null;
                }, this), 100);
            });
        }
    };
    Dialog.prototype = DP;
    var F = {
        dialogs:[],
        openDialog:function(p) {
            var dialogs = F.dialogs,len = dialogs.length;
            if (len > 0) {
                dialogs[0].close(function() {
                    new Dialog(p);
                });
            } else {
                new Dialog(p);
            }
        }
    };
    var R = {
        alert:function(message) {
            if (!message) return;
            var p = {
                message:message,
                number:A.util.getTimestamp(),
                type:"alert",
                iconCls:"dialog-icon-alert"
            };
            F.openDialog(p);
        },
        alertException:function(message) {
            if (!message) return;
            var p = {
                width:650,
                message:message,
                number:A.util.getTimestamp(),
                type:"alertException",
                iconCls:"dialog-icon-error"
            };
            F.openDialog(p);
        },
        warning:function(message) {
            if (!message) return;
            var p = {
                message:message,
                number:A.util.getTimestamp(),
                type:"alert",
                iconCls:"dialog-icon-warning"
            };
            F.openDialog(p);
        },
        error:function(message) {
            if (!message) return;
            var p = {
                message:message,
                number:A.util.getTimestamp(),
                type:"alert",
                iconCls:"dialog-icon-error"
            };
            F.openDialog(p);
        },
        ok:function(message) {
            if (!message) return;
            var p = {
                message:message,
                number:A.util.getTimestamp(),
                type:"alert",
                iconCls:"dialog-icon-ok"
            };
            F.openDialog(p);
        },
        confirm:function(message, callback, scope, width) {
            if (!message || !callback) return;
            var p = {
                message:message,
                scope:scope,
                callback:callback,
                number:A.util.getTimestamp(),
                type:"confirm",
                iconCls:"dialog-icon-confirm"
            };
            width && (p.width = width);
            F.openDialog(p);
        }
    };
    A.addModule("dialog", R);
})();