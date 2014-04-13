window["athene"]["taskbar"] || (function() {
    var A = window["athene"],W = window,D = document,ENV = A.env,TASK = A.task;
    var B = {
        taskbars:[]
    };
    var Taskbar = function(p) {
        this._constructor(p);
        this._init();
        this._render();
        this.setSize();
        this._bindEvent();
    };
    var TP = {
        _constructor:function(p) {
            B.taskbars.push(this);
            for (var k in p) {
                this[k] = p[k];
            }
        },
        _init:function(p) {
            this.el = $I(this.id);
            this.list = [];
        },
        _render:function() {
            var html = [];
            html.push("<div class='taskbar-left'><div class='taskbar-start-wrap'><div class='taskbar-start' onmouseover=\"athene.dom.addClass(this,'taskbar-start-hover');\"" +
                " onmouseout=\"athene.dom.removeClass(this,'taskbar-start-hover');\"></div></div><div class='taskbar-task-list-wrap'>");
            html.push("<div class='taskbar-task-list'></div></div></div><div class='taskbar-right'>");
            html.push("<div class='win-min-all' onmouseover=\"athene.dom.addClass(this,'win-min-all-hover');\"");
            html.push(" onmouseout=\"athene.dom.replaceClass(this,'win-min-all')\"");
            html.push(" onmousedown=\"athene.dom.addClass(this,'win-min-all-active');\"");
            html.push(" onmouseup=\"athene.dom.removeClass(this,'win-min-all-active');\"></div></div>");
            this.el.append(html.join(""));
            this.el.closeSelect();
            this.listEl = this.el.find(".taskbar-task-list");
        },
        _bindEvent:function() {
            this.el.find(".taskbar-start").click(function() {
                A.dialog.ok("designed by 艺术人生？？");
            });
            this.el.find(".win-min-all").click($.proxy(function() {
                this.getBindingDesktop().getWindowManager().minAllWindow();
            }, this));
        },
        setSize:function(w, h) {
            if (typeof(w) === "number" && w > 0) {
                var swrapW = this.el.find(".taskbar-start-wrap").outerWidth(true);
                var minAll = this.el.find(".taskbar-right").outerWidth(true);
                var tlw = w - swrapW - minAll;
                if (tlw > 0) {
                    this.el.width(w);
                    this.el.find(".taskbar-task-list-wrap").width(tlw);
                }
            }
        },
        setPosition:function(x, y) {
            var els = this.el.get(0).style;
            els.top = y + "px";
            els.left = x + "px";
        },
        appendTask:function(p) {
            if (!p)throw Error("TaskConfig无效");
            p.binding = this.id;
            var task = TASK.create(p);
            this.list.push(task);
            this.setActiveTask(task.widgetId);
        },
        setActiveTask:function(id) {
            if (this.activeTask) {
                var activeTask = TASK.get(this.activeTask);
                activeTask && activeTask.setUnactive();
                delete this.activeTask;
            }
            if (id) {
                var task = TASK.get(id);
                if (task) {
                    task.setActive();
                    this.activeTask = id;
                }
            }
        },
        removeTask:function(id) {
            var list = this.list,len = list.length;
            for (var i = 0; i < len; i++) {
                var item = list[i];
                if (item.widgetId === id) {
                    var task = TASK.get(id);
                    task.destroy();
                    list.splice(i, 1);
                    break;
                }
            }
        },
        getBindingDesktop:function() {
            return A.desktop.get(this.binding);
        }
    };
    Taskbar.prototype = TP;
    var C = {
        create:function(p) {
            var taskbar = new Taskbar(p);
            return {widgetId:taskbar.id,xtype:"taskbar"};
        },
        get:function(id) {
            var taskbars = B.taskbars,len = taskbars.length;
            for (var i = 0; i < len; i++) {
                var item = taskbars[i];
                if (id === item.id) {
                    return item;
                }
            }
            return null;
        },
        getAll:function() {
            return B.taskbars;
        }
    };
    A.addModule("taskbar", C);
})();