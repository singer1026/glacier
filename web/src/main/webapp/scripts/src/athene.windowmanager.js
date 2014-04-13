window["athene"]["windowmanager"] || (function() {
    var A = window["athene"],W = window,D = document,ENV = A.env,UTIL = A.util,WINDOW = A.window;
    var B = {
        INDEX_STEP:3,
        windowManagers:[]
    };
    var WindowManager = function(p) {
        this._constructor(p);
        this._init();
    };
    var WP = {
        _constructor:function(p) {
            B.windowManagers.push(this);
            for (var k in p) {
                this[k] = p[k];
            }
        },
        _init:function(p) {
            this.el = $I(this.id);
            this.maxIndex = 1;
            this.list = [];
        },
        openWindow:function(p) {
            if (!p)throw new Error("Window未设置config");
            if (!p.width)throw new Error("Window未设置width");
            if (!p.height)throw new Error("Window未设置height");
            if (!p.title)throw new Error("Window未设置title");
            var windows = this.list,len = windows.length;
            for (var i = 0; i < len; i++) {
                var item = WINDOW.get(windows[i].widgetId);
                if (item.title === p.title) {
                    if (item.index === -1) {
                        this.showWindow(item.id);
                    } else {
                        this.frontWindow(item.id);
                    }
                    return;
                }
            }
            var number = UTIL.getTimestamp();
            p.number = number;
            var taskbar = A.taskbar.get(A.desktop.get(this.binding).taskbar.widgetId);
            taskbar.appendTask(p);
            this.increaseMaxIndex();
            p.index = this.maxIndex;
            p.binding = this.id;
            p.data = UTIL.merge({number:number}, p.data);
            windows.push(WINDOW.create(p));
        },
        showWindow:function(id) {
            var window = WINDOW.get(id);
            if (window && window.isAnimate === false && window.index === -1) {
                this.increaseMaxIndex();
                window.index = this.maxIndex;
                window._show();
            }
        },
        closeWindow:function(id) {
            var window = WINDOW.get(id);
            if (window && window.isAnimate === false) {
                var list = this.list,len = list.length;
                for (var i = 0; i < len; i++) {
                    var item = list[i];
                    if (id === item.widgetId) {
                        list.splice(i, 1);
                        break;
                    }
                }
                window._destory();
                this.reduceMaxIndex();
                this.setActiveWindow();
                var taskbar = A.taskbar.get(A.desktop.get(this.binding).taskbar.widgetId);
                taskbar.removeTask(window.taskId);
            }
        },
        hideWindow:function(id) {
            var window = WINDOW.get(id);
            if (window && window.isAnimate === false && window.index !== -1) {
                this.reduceMaxIndex();
                window._hide();
                this.setActiveWindow();
            }
        },
        maxWindow:function(id) {
            var window = WINDOW.get(id);
            if (window && window.isAnimate === false) {
                if (window.index === -1) {
                    this.showWindow(id);
                }
                window._max();
            }
        },
        restoreWindow:function(id) {
            var window = WINDOW.get(id);
            if (window && window.isAnimate === false) {
                window._restore();
            }
        },
        minAllWindow:function() {
            var list = this.list,len = list.length;
            for (var i = 0; i < len; i++) {
                this.hideWindow(list[i].widgetId);
            }
        },
        setActiveWindow:function() {
            var taskbar = A.taskbar.get(A.desktop.get(this.binding).taskbar.widgetId);
            var activeWindow = this.getActiveWindow();
            if (activeWindow) {
                taskbar.setActiveTask(activeWindow.taskId);
            } else {
                taskbar.setActiveTask(null);
            }
        },
        frontWindow:function(id) {
            var currentWindow = WINDOW.get(id);
            if (currentWindow) {
                if (currentWindow.index === -1) {
                    this.showWindow(id);
                }
                var currentIndex = currentWindow.index;
                var list = this.list,len = list.length;
                for (var i = 0; i < len; i++) {
                    var item = WINDOW.get(list[i].widgetId);
                    if (item.index > currentWindow.index && item.number !== currentWindow.number) {
                        var newIndex = item.index - B.INDEX_STEP;
                        item.setIndex(newIndex);
                    }
                }
                currentWindow.setIndex(this.maxIndex);
                this.setActiveWindow();
            }
        },
        getActiveWindow:function() {
            var maxIndex = this.maxIndex;
            var list = this.list,len = list.length;
            for (var i = 0; i < len; i++) {
                var item = WINDOW.get(list[i].widgetId);
                if (item && item.index === maxIndex) {
                    return item;
                }
            }
        },
        increaseMaxIndex:function() {
            this.maxIndex += B.INDEX_STEP;
        },
        reduceMaxIndex:function() {
            this.maxIndex -= B.INDEX_STEP;
        },
        getIndexInfo:function() {
            var rtnList = [];
            var list = this.list,len = list.length;
            for (var i = 0; i < len; i++) {
                var item = WINDOW.get(list[i].widgetId);
                rtnList.push("id:" + item.id + "|" + "index:" + item.index);
            }
            return rtnList;
        },
        getBindingDesktop:function() {
            return A.desktop.get(this.binding);
        }
    };
    WindowManager.prototype = WP;
    var C = {
        create:function(p) {
            var windowManager = new WindowManager(p);
            return {widgetId:windowManager.id,xtype:"windowmanager"};
        },
        get:function(id) {
            var windowManagers = B.windowManagers,len = windowManagers.length;
            for (var i = 0; i < len; i++) {
                var item = windowManagers[i];
                if (id === item.id || id === item.number) {
                    return item;
                }
            }
            return null;
        },
        getAll:function() {
            return B.windowManagers;
        },
        getFirst:function() {
            var windowManagers = B.windowManagers,len = windowManagers.length;
            return len > 0 ? windowManagers[0] : null;
        }
    };
    A.addModule("windowmanager", C);
})();