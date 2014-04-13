window["athene"]["desktop"] || (function() {
    var A = window["athene"],W = window,D = document,ENV = A.env,UTIL = A.util;
    var TASKBAR = A.taskbar,DMENUBAR = A.dmenubar,WINDOWMANAGER = A.windowmanager;
    var B = {
        desktops:[]
    };
    var Desktop = function(p) {
        this._constructor(p);
        this._render();
    };
    var DP = {
        _constructor:function(p) {
            B.desktops.push(this);
            this.number = "default";
            for (var k in p) {
                this[k] = p[k];
            }
        },
        _render:function() {
            $("body").append("<div id='desktop-" + this.number + "' class='desktop'><div id='desktop-c-" + this.number + "' class='desktop-c'>" +
                "<div id='desktop-menubar-" + this.number + "'></div><div id='window-manager-" + this.number + "'></div></div>" +
                "<div id='desktop-t-" + this.number + "' class='desktop-t'></div></div>");
            this.el = $I("desktop-" + this.number);
            this.taskbar = TASKBAR.create({
                id:"desktop-t-" + this.number,
                binding:"desktop-" + this.number
            });
            this.dmenubar = DMENUBAR.create({
                id:"desktop-menubar-" + this.number,
                binding:"desktop-" + this.number
            });
            this.windowManager = WINDOWMANAGER.create({
                id:"window-manager-" + this.number,
                binding:"desktop-" + this.number
            });
            this.id = "desktop-" + this.number;
            delete this.number;
        },
        setSize:function(w, h) {
            if (typeof(w) === "number" && w > 0 && typeof(h) === "number" && h > 0) {
                var dtEl = this.el.find(".desktop-t");
                var dth = dtEl.outerHeight(true),dch = h - dth;
                if (dch > 0) {
                    var els = this.el.get(0).style;
                    els.width = w + "px";
                    els.height = h + "px";
                    var dcs = this.el.find(".desktop-c").get(0).style;
                    dcs.width = w + "px";
                    dcs.height = dch + "px";
                    DMENUBAR.get(this.dmenubar.widgetId).setSize(w, dch);
                    var taskbarWidget = TASKBAR.get(this.taskbar.widgetId);
                    taskbarWidget.setSize(w, null);
                    taskbarWidget.setPosition(0, dch);
                    this.width = w;
                    this.height = h;
                }
            }
        },
        getDmenubar:function() {
            return UTIL.getWidght(this.dmenubar);
        },
        getTaskbar:function() {
            return UTIL.getWidght(this.taskbar);
        },
        getWindowManager:function() {
            return A.windowmanager.get(this.windowManager.widgetId);
        },
        getSizeWithoutTaskbar:function() {
            var dth = this.el.find(".desktop-t").outerHeight(true);
            return {width:this.width,height:this.height - dth};
        }
    };
    Desktop.prototype = DP;
    var C = {
        create:function(p) {
            var desktop = new Desktop(p);
            return {widgetId:desktop.id,xtype:"desktop"};
        },
        get:function(id) {
            var desktops = B.desktops,len = desktops.length;
            for (var i = 0; i < len; i++) {
                var item = desktops[i];
                if (id === item.id) {
                    return item;
                }
            }
            return null;
        },
        getAll:function() {
            return B.desktops;
        }
    };
    A.addModule("desktop", C);
})();