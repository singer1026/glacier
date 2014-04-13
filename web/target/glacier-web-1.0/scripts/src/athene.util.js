window["athene"]["util"] || (function() {
    var A = window["athene"],W = window;
    var C = {
        merge:function(s, t) {
            var rtnMap = {};
            if (s && typeof(s) === "object") {
                for (var o in s) {
                    rtnMap[o] = s[o];
                }
            }
            if (t && typeof(t) === "object") {
                for (var o in t) {
                    rtnMap[o] = t[o];
                }
            }
            return rtnMap;
        },
        getTimestamp:function() {
            return new Date().getTime() + "";
        },
        random:function(p) {
            var random = Math.random();
            return p ? parseInt(random * p) : random;
        },
        isException:function(s) {
            return (s === "ValidationException" || s === "OptimisticLockException") ? false : true;
        },
        isValidationException:function(s) {
            return (s === "ValidationException" ) ? true : false;
        },
        getWidght:function(o) {
            if (o && o.xtype && o.widgetId) {
                var xtype = o.xtype,widgetId = o.widgetId;
                if (xtype === "task") {
                    return A.task.get(widgetId);
                } else if (xtype === "taskbar") {
                    return A.taskbar.get(widgetId);
                } else if (xtype === "desktop") {
                    return A.desktop.get(widgetId);
                } else if (xtype === "dmenubar") {
                    return A.dmenubar.get(widgetId);
                } else if (xtype === "windowmanager") {
                    return A.windowmanager.get(widgetId);
                } else if (xtype === "grid") {
                    return A.grid.get(widgetId);
                } else if (xtype === "checkgroup") {
                    return A.checkgroup.get(widgetId);
                } else if (xtype === "listfield") {
                    return A.listfield.get(widgetId);
                } else if (xtype === "window") {
                    return A.window.get(widgetId);
                } else if (xtype === "form") {
                    return A.form.get(widgetId);
                } else if (xtype === "dialog") {
                    return A.dialog.get(widgetId);
                }
            }
            return null;
        },
        fetchNumber:function(s) {
            var number = s.replace(/[^\d]/g, "");
            return number ? parseInt(number) : number;
        }
    };
    A.addModule("util", C);
})();