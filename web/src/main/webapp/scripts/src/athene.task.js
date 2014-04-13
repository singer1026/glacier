window["athene"]["task"] || (function() {
    var A = window["athene"],W = window,D = document,DOM = athene.dom,ENV = A.env;
    var B = {
        tasks:[]
    };
    var Task = function(p) {
        this._constructor(p);
        this._init();
        this._render();
        this._bindEvent();
    };
    var TP = {
        _constructor:function(p) {
            if (!p)throw new Error("Task未设置config");
            if (!p.binding)throw new Error("Task未设置binding");
            if (!p.number)throw new Error("Task未设置number");
            if (!p.title)throw new Error("Task未设置title");
            B.tasks.push(this);
            A.apply(this, p);
        },
        _init:function() {
            var n = this.number;
            this.id = "taskbar-task-" + n;
            this.windowId = "win-" + n;
        },
        _render:function() {
            var html = [];
            html.push("<div id='" + this.id + "' class='taskbar-task-wrap'><div class='taskbar-task'>");
            html.push("<div class='taskbar-task-icon " + (this.iconCls ? this.iconCls + "-32" : "task-icon-default") + "'></div>");
            html.push("<div class='taskbar-task-content'>" + this.title + "</div></div></div>");
            A.taskbar.get(this.binding).el.find(".taskbar-task-list").append(html.join(""));
            this.el = $I(this.id);
            this._renderProxy();
        },
        _renderProxy:function() {
            var elProxy = this.el.clone().appendTo("body").addClass("taskbar-task-proxy").attr("id", this.id + "-proxy");
            if (this.initPosition === undefined || this.initPosition === null) {
                var size = this.getBindingTaskbar().getBindingDesktop().getSizeWithoutTaskbar();
                var initPosition = {
                    top:size.height + 2,
                    left:-(42 + 2)
                };
                this.initPosition = initPosition;
            }
            var initPosition = this.initPosition;
            elProxy.width(1).height(1).css("top", initPosition.top + "px").css("left", initPosition.left + "px");
            var offset = this.el.offset();
            var elConfig = {
                opacity:1,
                width:160,
                height:40,
                left:offset.left,
                top:offset.top
            };
            this.animate(elProxy, elConfig, function() {
                this.el.css("opacity", 1).css("visibility", "visible").show();
                elProxy.remove();
            }, ENV.get("task_speed"));
        },
        destroy:function() {
            var elProxy = this.el.clone().appendTo("body").addClass("taskbar-task-proxy").attr("id", this.id + "-proxy");
            var initPosition = this.initPosition;
            var offset = this.el.offset();
            elProxy.css("top", offset.top + "px").css("left", offset.left + "px");
            var elConfig = {
                opacity:0,
                width:0,
                height:0,
                left:initPosition.left,
                top:initPosition.top
            };
            this.el.hide();
            this.animate(elProxy, elConfig, function() {
                var tasks = B.tasks,len = tasks.length;
                for (var i = 0; i < len; i++) {
                    var item = tasks[i];
                    if (this.id === item.id) {
                        tasks.splice(i, 1);
                        break;
                    }
                }
                elProxy.remove();
                this.el.unbind().remove();
                this.el = null;
            }, ENV.get("task_speed"));
        },
        animate:function(el, elConfig, callback, s) {
            if (this.isAnimate === true)return;
            this.isAnimate = true;
            var speed = s || ENV.get("speed");
            el.animate({width:elConfig.width + "px",height:elConfig.height + "px",opacity:elConfig.opacity,
                top:elConfig.top + "px",left:elConfig.left + "px"}, speed, $.proxy(function() {
                this.isAnimate = false;
                callback && callback.call(this);
            }, this));
        },
        setActive:function() {
            DOM.addClass(this.el.find(".taskbar-task").get(0), "taskbar-task-hover");
        },
        setUnactive:function() {
            DOM.removeClass(this.el.find(".taskbar-task").get(0), "taskbar-task-hover");
        },
        _bindEvent:function() {
            this.el.mousedown($.proxy(function() {
                var windowId = this.windowId;
                var taskbar = A.taskbar.get(this.binding);
                var desktop = A.desktop.get(taskbar.binding);
                var windowManager = A.windowmanager.get(desktop.windowManager.widgetId);
                var activeWindow = windowManager.getActiveWindow();
                if (activeWindow && activeWindow.id === windowId) {
                    windowManager.hideWindow(windowId);
                } else {
                    windowManager.frontWindow(windowId);
                }
            }, this));
        },
        getBindingTaskbar:function() {
            return A.taskbar.get(this.binding);
        }
    };
    Task.prototype = TP;
    var C = {
        create:function(p) {
            var task = new Task(p);
            return {widgetId:task.id,xtype:"task"};
        },
        get:function(id) {
            var tasks = B.tasks,len = tasks.length;
            for (var i = 0; i < len; i++) {
                var item = tasks[i];
                if (id === item.id) {
                    return item;
                }
            }
            return null;
        },
        getAll:function() {
            return B.tasks;
        }
    };
    A.addModule("task", C);
})();