window["athene"]["window"] || (function() {
    var A = window["athene"],W = window,D = document,DOM = athene.dom,ENV = athene.env,UTIL = A.util,INTERACTION = A.interaction;
    var B = {
        windows:[]
    };
    var Window = function(p) {
        this._constructor(p);
        this._init();
        this._render();
        this._bindEvent();
    };
    var WP = {
        _constructor:function(p) {
            B.windows.push(this);
            this.cls = "win-default";
            this.maxButton = true;
            A.apply(this, p);
        },
        _init:function() {
            this.step = {};
            this.xtype = "window";
            this.items = [];
            this.status = "normal";
            this.isAnimate = false;
        },
        _render:function() {
            var n = this.number;
            var html = ENV.get("window").replaceAll("{random}", n).replaceAll("{cls}", this.cls);
            this.iconCls && (html = html.replaceAll("win-icon-default", this.iconCls + "-16"));
            A.windowmanager.get(this.binding).el.append(html);
            this.id = "win-" + n;
            this.taskId = "taskbar-task-" + n;
            this.el = $I(this.id);
            if (this.maxButton === false) {
                this.el.find(".win-button-max").remove();
                this.el.find(".win-button-max-able").remove();
            }
            this.el.find(".win-title").text(this.title);
            this.el.find(".win-top-center").closeSelect();
            var initPosition = this.initPosition;
            if (this.initPosition === undefined || this.initPosition === null) {
                var initPosition = {
                    top:0,
                    left:0
                };
                this.initPosition = initPosition;
            }
            this.showAt(initPosition.left, initPosition.top);
            this.setSize(this.width, this.height, false);
            this.el.width(1).height(1);
            this.el.css("visibility", "visible");
            this._show(this._decorate);
        },
        setSize:function(w, h, flag) {
            var isBack = false;
            var wcs = this.el.find(".win-content").get(0).style;
            var els = this.el.get(0).style;
            if (typeof(w) === "number" && w > 0) {
                wcs.width = w + "px";
                els.width = (w + 12) + "px";
                var btnBar = this.el.find(".win-top-center-right");
                var btnBarsWidth = btnBar.outerWidth(true);
                var iconWidth = this.el.find(".win-icon-wrap").outerWidth(true);
                this.el.find(".win-title").get(0).style.width = (w - btnBarsWidth - iconWidth - 2) + "px";
                flag !== false && (this.width = w);
                isBack = true;
            }
            if (typeof(h) === "number" && h > 0) {
                wcs.height = h + "px";
                els.height = (h + 32) + "px";
                flag !== false && (this.height = h);
                isBack = true;
            }
            isBack === true && this.onSetSize && this.onSetSize.call(this, w, h);
        },
        showAt:function(x, y) {
            x = parseInt(x),y = parseInt(y);
            var els = this.el.get(0).style;
            els.left = x + "px";
            els.top = y + "px";
        },
        _bindEvent:function() {
            this.draggable = INTERACTION.draggable({
                src:"win-title-" + this.number,
                tar:this.id
            });
            if (!ENV.get("isIE6") && !ENV.get("isIE7") && !ENV.get("isIE8")) {
                this.draggable.before = function() {
                    this.el.find(".win-content").children().hide();
                };
                this.draggable.after = function() {
                    this.el.find(".win-content").children().show();
                };
                this.draggable.scope = this;
            }
            var wm = A.windowmanager.get(this.binding);
            this.el.mousedown($.proxy(function() {
                wm.frontWindow(this.id);
            }, this));
            this.el.find(".win-button-close").click($.proxy(function() {
                wm.closeWindow(this.id);
            }, this));
            this.el.find(".win-button-min").click($.proxy(function() {
                wm.hideWindow(this.id);
            }, this));
            if (this.maxButton === true) {
                this.el.find(".win-button-max-able").click($.proxy(function(e) {
                    wm.restoreWindow(this.id);
                }, this));
                this.el.find(".win-button-max").click($.proxy(function(e) {
                    wm.maxWindow(this.id);
                }, this));
                this.el.find(".win-title").dblclick($.proxy(function() {
                    if (this.draggable.isRunning()) {
                        wm.maxWindow(this.id);
                    } else {
                        wm.restoreWindow(this.id);
                    }
                }, this));
            }
        },
        _decorate:function(d) {
            if (this.html) {
                this.setHtml(this.html);
                delete this.html;
            } else {
                $.ajax({
                    context:this,
                    dataType:"html",
                    url: this.url,
                    data:{args:json(this.data)},
                    success: function(d) {
                        this.setHtml(d);
                        this.hideMask();
                    },
                    beforeSend:function() {
                        this.showMask();
                    }
                });
            }
        },
        setHtml:function(html, callback) {
            W.setTimeout($.proxy(function() {
                var winContent = this.el.find(".win-content");
                var winContentW = winContent.outerWidth();
                var winContentH = winContent.outerHeight();
                var list = [
                    {top:0,left:winContentW},
                    {top:0,left:0 - winContentW},
                    {top:winContentH,left:0},
                    {top:winContentH,left:winContentW},
                    {top:winContentH,left:0 - winContentW},
                    {top:0,left:0}
                ];
                var config = list[UTIL.random(5)];
                winContent.css("overflowY", "hidden").css("position", "relative").css("left", config.left + "px").css("top", config.top + "px");
                winContent.html(html);
                this.isAnimate = true;
                winContent.animate({left:0,top:0}, ENV.get("window_speed"), $.proxy(function() {
                    winContent.css("position", "static").css("overflowY", "auto");
                    callback && callback.call(this);
                    this.onSetHtml && this.onSetHtml.call(this);
                    this.isAnimate = false;
                }, this));
            }, this), 50);
        },
        setIndex:function(i) {
            i && i > 0 && (this.index = i);
            this.el.css("zIndex", this.index);
        },
        animate:function(elConfig, callback, s) {
            if (this.isAnimate === true)return;
            this.isAnimate = true;
            var speed = s || ENV.get("speed");
            if (this.animateStep === undefined || this.animateStep === null)this.animateStep = 0;
            this.el.find(".win-title").get(0).style.width = "auto";
            this.el.animate({opacity:elConfig.opacity,top:elConfig.top + "px",left:elConfig.left + "px",
                width:elConfig.width + "px",height:elConfig.height + "px"}, speed, $.proxy(function() {
                this.animateStep++;
            }, this));
            this.el.find(".win-content").animate({width:(elConfig.width - 12) + "px",height:(elConfig.height - 32) + "px"}, speed,
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
        savePosition:function() {
            this.lastPosition = this.el.position();
        },
        _max:function() {
            if (this.isAnimate === true)return;
            this.savePosition();
            var size = this.getBindingWindowManager().getBindingDesktop().getSizeWithoutTaskbar();
            var elConfig = {
                opacity:"1",
                top:0,
                left:0,
                width:size.width,
                height:size.height
            };
            this.animate(elConfig, function() {
                this.setSize(size.width - 12, size.height - 32, false);
                this.el.find(".win-button-max").hide();
                this.el.find(".win-button-max-able").show();
                this.draggable.stop();
                this.status = "max";
                this.onMax && this.onMax.call(this);
            });
        },
        _restore:function() {
            if (this.isAnimate === true)return;
            var lastPosition = this.lastPosition;
            var elConfig = {
                opacity:"1",
                top:lastPosition.top,
                left:lastPosition.left,
                width:this.width,
                height:this.height
            };
            this.animate(elConfig, function() {
                this.setSize(this.width, this.height);
                this.el.find(".win-button-max-able").hide();
                this.el.find(".win-button-max").show();
                this.draggable.start();
                this.status = "normal";
                this.onRestore && this.onRestore.call(this);
            });
        },
        _show:function(callback) {
            if (this.isAnimate === true)return;
            if (this.status === "min") {
                this.setIndex();
                var info = this.getBindingTaskInfo();
                this.showAt(info.left, info.top);
                var elConfig = {
                    opacity:"1"
                };
                var isRunning = this.draggable.isRunning();
                if (isRunning) {
                    var lastPosition = this.lastPosition;
                    A.apply(elConfig, {
                        width:this.width,
                        height:this.height,
                        top:lastPosition.top,
                        left:lastPosition.left
                    });
                } else {
                    var size = this.getBindingWindowManager().getBindingDesktop().getSizeWithoutTaskbar();
                    A.apply(elConfig, {
                        top:0,
                        left:0,
                        width:size.width,
                        height:size.height
                    });
                }
                this.animate(elConfig, function() {
                    if (isRunning) {
                        this.setSize(elConfig.width, elConfig.height, false);
                        this.status = "normal";
                    } else {
                        this.setSize(elConfig.width - 12, elConfig.height - 32, false);
                        this.status = "max";
                    }
                }, ENV.get("window_speed"));
            } else if (this.status === "normal") {
                this.setIndex();
                var elConfig = {
                    opacity:"1",
                    top:15 + UTIL.random(10),
                    left:100 + UTIL.random(200),
                    width:this.width,
                    height:this.height
                };
                this.animate(elConfig, function() {
                    this.setSize(this.width, this.height);
                    this.status = "normal";
                    callback && typeof(callback) === "function" && callback.call(this);
                }, ENV.get("window_speed"));
            }
        },
        _hide:function() {
            if (this.isAnimate === true)return;
            this.status === "normal" && this.savePosition();
            var info = this.getBindingTaskInfo();
            var elConfig = {
                opacity:"0",
                top:info.top,
                left:info.left,
                width:info.width,
                height:info.height
            };
            this.animate(elConfig, function() {
                this.index = -1;
                this.status = "min";
            }, ENV.get("window_speed"));
        },
        close:function() {
            A.windowmanager.get(this.binding).closeWindow(this.id);
        },
        _destory:function() {
            var initPosition = this.initPosition;
            var elConfig = {
                opacity:"0",
                top:initPosition.top,
                left:initPosition.left,
                width:100,
                height:50
            };
            this.animate(elConfig, function() {
                var items = this.items,len = items.length;
                for (var i = 0; i < len; i++) {
                    var item = UTIL.getWidght(items[i]);
                    item && item._destory && item._destory.call(item);
                }
                var windows = B.windows,windowsLen = windows.length;
                for (var i = 0; i < windowsLen; i++) {
                    var item = windows[i];
                    if (this.id === item.id) {
                        windows.splice(i, 1);
                        break;
                    }
                }
                this.draggable.destory();
                this.draggable = null;
                this.el.find(".win-button-close").unbind().remove();
                this.el.find(".win-button-min").unbind().remove();
                this.el.find(".win-button-max").unbind().remove();
                this.el.find(".win-button-max-able").unbind().remove();
                this.el.find(".win-title").unbind().remove();
                this.el.unbind().remove();
                this.el = null;
                this.onClose && this.onClose.call(this);
            }, ENV.get("window_speed"));
        },
        addItem:function(widget) {
            this.items.push({widgetId:widget.id,xtype:widget.xtype});
        },
        showMask:function(msg) {
            var maskId = "win-mask-" + this.number;
            var overlayId = "win-overlay-" + this.number;
            var mask = this.el.find("#" + maskId);
            var winContent = this.el.find(".win-content");
            if (mask.length === 0) {
                var html = ENV.get("mask").replaceAll("{mask-id}", maskId).replaceAll("{overlay-id}", overlayId);
                winContent.before(html);
                mask = this.el.find("#" + maskId).closeSelect();
            }
            var overlay = this.el.find(".mask-overlay");
            var w = winContent.innerWidth(),h = winContent.innerHeight();
            var pos = winContent.position();
            var os = overlay.get(0).style;
            os.width = w + "px",os.height = h + "px";
            os.top = pos.top + "px",os.left = pos.left + "px";
            overlay.css("zIndex", this.index).css("opacity", 0.7).show()
            mask.find(".mask-content").html(msg || "正在加载页面，请稍等...");
            var mw = mask.outerWidth(true),mh = mask.outerHeight(true);
            var ml = (w - mw) / 2 + 13,mt = (h - mh) / 2 + 33;
            var ms = mask.get(0).style;
            ms.top = mt + "px",ms.left = ml + "px";
            mask.css("zIndex", this.index + 1).show();
            this.mask = mask;
            this.overlay = overlay;
        },
        hideMask:function(callback, scope) {
            if (this.mask) {
                this.mask.fadeOut(0, $.proxy(function() {
                    this.overlay.fadeOut(ENV.get("speed"), $.proxy(function() {
                        callback && callback.call(scope || this);
                    }, this));
                }, this));
            }
        },
        getBindingWindowManager:function() {
            return A.windowmanager.get(this.binding);
        },
        getBindingTaskInfo:function() {
            var taskEl = A.task.get(this.taskId).el;
            var offset = taskEl.offset();
            return {
                width:taskEl.outerWidth(true),
                height:taskEl.outerHeight(true),
                top:offset.top,
                left:offset.left
            };
        }
    };
    Window.prototype = WP;
    var F = {
        create:function(p) {
            var window = new Window(p);
            return {widgetId:window.id,xtype:"window"};
        },
        get:function(id) {
            var windows = B.windows,len = windows.length;
            for (var i = 0; i < len; i++) {
                var item = windows[i];
                if (id === item.id || id == item.number) {
                    return item;
                }
            }
            return null;
        },
        getAll:function() {
            return B.windows;
        }
    };
    A.addModule("window", F);
})();


