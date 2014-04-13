/**
 * jQuery Cookie plugin
 *
 * Copyright (c) 2010 Klaus Hartl (stilbuero.de)
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 */
jQuery.cookie = function (key, value, options) {

    // key and at least value given, set cookie...
    if (arguments.length > 1 && String(value) !== "[object Object]") {
        options = jQuery.extend({}, options);

        if (value === null || value === undefined) {
            options.expires = -1;
        }

        if (typeof options.expires === 'number') {
            var days = options.expires, t = options.expires = new Date();
            t.setDate(t.getDate() + days);
        }

        value = String(value);

        return (document.cookie = [
            encodeURIComponent(key), '=',
            options.raw ? value : encodeURIComponent(value),
            options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
            options.path ? '; path=' + options.path : '',
            options.domain ? '; domain=' + options.domain : '',
            options.secure ? '; secure' : ''
        ].join(''));
    }

    // key and possibly options given, get cookie...
    options = value || {};
    var result, decode = options.raw ? function (s) {
        return s;
    } : decodeURIComponent;
    return (result = new RegExp('(?:^|; )' + encodeURIComponent(key) + '=([^;]*)').exec(document.cookie)) ? decode(result[1]) : null;
};
/**
 * jQuery JSON Plugin
 * version: 2.3 (2011-09-17)
 *
 * This document is licensed as free software under the terms of the
 * MIT License: http://www.opensource.org/licenses/mit-license.php
 *
 * Brantley Harris wrote this plugin. It is based somewhat on the JSON.org
 * website's http://www.json.org/json2.js, which proclaims:
 * "NO WARRANTY EXPRESSED OR IMPLIED. USE AT YOUR OWN RISK.", a sentiment that
 * I uphold.
 *
 * It is also influenced heavily by MochiKit's serializeJSON, which is
 * copyrighted 2005 by Bob Ippolito.
 */

(function($) {

    var escapeable = /["\\\x00-\x1f\x7f-\x9f]/g,
        meta = {
            '\b': '\\b',
            '\t': '\\t',
            '\n': '\\n',
            '\f': '\\f',
            '\r': '\\r',
            '"' : '\\"',
            '\\': '\\\\'
        };

    /**
     * jQuery.toJSON
     * Converts the given argument into a JSON respresentation.
     *
     * @param o {Mixed} The json-serializble *thing* to be converted
     *
     * If an object has a toJSON prototype, that will be used to get the representation.
     * Non-integer/string keys are skipped in the object, as are keys that point to a
     * function.
     *
     */
    $.toJSON = typeof JSON === 'object' && JSON.stringify
        ? JSON.stringify
        : function(o) {

        if (o === null) {
            return 'null';
        }

        var type = typeof o;

        if (type === 'undefined') {
            return undefined;
        }
        if (type === 'number' || type === 'boolean') {
            return '' + o;
        }
        if (type === 'string') {
            return $.quoteString(o);
        }
        if (type === 'object') {
            if (typeof o.toJSON === 'function') {
                return $.toJSON(o.toJSON());
            }
            if (o.constructor === Date) {
                var month = o.getUTCMonth() + 1,
                    day = o.getUTCDate(),
                    year = o.getUTCFullYear(),
                    hours = o.getUTCHours(),
                    minutes = o.getUTCMinutes(),
                    seconds = o.getUTCSeconds(),
                    milli = o.getUTCMilliseconds();

                if (month < 10) {
                    month = '0' + month;
                }
                if (day < 10) {
                    day = '0' + day;
                }
                if (hours < 10) {
                    hours = '0' + hours;
                }
                if (minutes < 10) {
                    minutes = '0' + minutes;
                }
                if (seconds < 10) {
                    seconds = '0' + seconds;
                }
                if (milli < 100) {
                    milli = '0' + milli;
                }
                if (milli < 10) {
                    milli = '0' + milli;
                }
                return '"' + year + '-' + month + '-' + day + 'T' +
                    hours + ':' + minutes + ':' + seconds +
                    '.' + milli + 'Z"';
            }
            if (o.constructor === Array) {
                var ret = [];
                for (var i = 0; i < o.length; i++) {
                    ret.push($.toJSON(o[i]) || 'null');
                }
                return '[' + ret.join(',') + ']';
            }
            var name,
                val,
                pairs = [];
            for (var k in o) {
                type = typeof k;
                if (type === 'number') {
                    name = '"' + k + '"';
                } else if (type === 'string') {
                    name = $.quoteString(k);
                } else {
                    // Keys must be numerical or string. Skip others
                    continue;
                }
                type = typeof o[k];

                if (type === 'function' || type === 'undefined') {
                    // Invalid values like these return undefined
                    // from toJSON, however those object members
                    // shouldn't be included in the JSON string at all.
                    continue;
                }
                val = $.toJSON(o[k]);
                pairs.push(name + ':' + val);
            }
            return '{' + pairs.join(',') + '}';
        }
    };

    /**
     * jQuery.evalJSON
     * Evaluates a given piece of json source.
     *
     * @param src {String}
     */
    $.evalJSON = typeof JSON === 'object' && JSON.parse
        ? JSON.parse
        : function(src) {
        return eval('(' + src + ')');
    };

    /**
     * jQuery.secureEvalJSON
     * Evals JSON in a way that is *more* secure.
     *
     * @param src {String}
     */
    $.secureEvalJSON = typeof JSON === 'object' && JSON.parse
        ? JSON.parse
        : function(src) {

        var filtered =
            src
                .replace(/\\["\\\/bfnrtu]/g, '@')
                .replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']')
                .replace(/(?:^|:|,)(?:\s*\[)+/g, '');

        if (/^[\],:{}\s]*$/.test(filtered)) {
            return eval('(' + src + ')');
        } else {
            throw new SyntaxError('Error parsing JSON, source is not valid.');
        }
    };

    /**
     * jQuery.quoteString
     * Returns a string-repr of a string, escaping quotes intelligently.
     * Mostly a support function for toJSON.
     * Examples:
     * >>> jQuery.quoteString('apple')
     * "apple"
     *
     * >>> jQuery.quoteString('"Where are we going?", she asked.')
     * "\"Where are we going?\", she asked."
     */
    $.quoteString = function(string) {
        if (string.match(escapeable)) {
            return '"' + string.replace(escapeable, function(a) {
                var c = meta[a];
                if (typeof c === 'string') {
                    return c;
                }
                c = a.charCodeAt();
                return '\\u00' + Math.floor(c / 16).toString(16) + (c % 16).toString(16);
            }) + '"';
        }
        return '"' + string + '"';
    };

})(jQuery);
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
(function() {
    String.prototype.replaceAll = function(s1, s2) {
        return this.replace(new RegExp(s1, "gm"), s2);
    };
    String.prototype.contains = function(s) {
        return this.indexOf(s) !== -1;
    };
    String.prototype.endWith = function(str) {
        if (str == null || str == "" || this.length == 0 || str.length > this.length) {
            return false;
        }
        return this.substring(this.length - str.length) == str;
    };
    String.prototype.startsWith = function(str) {
        if (str == null || str == "" || this.length == 0 || str.length > this.length) {
            return false;
        }
        return this.substr(0, str.length) == str;
    };
    var A = window["athene"];
    var R = function() {
        return false;
    };
    $.fn.closeSelect = function() {
        if (A.env.get("isFF")) {
            this.get(0).style.MozUserSelect = "none";
        } else if (A.env.get("isWK")) {
            this.get(0).style.WebkitUserSelect = "none";
        } else {
            this.bind("selectstart", R);
        }
        return this;
    };
    $.fn.openSelect = function() {
        if (A.env.get("isFF")) {
            this.get(0).style.MozUserSelect = "auto";
        } else if (A.env.get("isWK")) {
            this.get(0).style.WebkitUserSelect = "auto";
        } else {
            this.unbind("selectstart", R);
        }
        return this;
    };
    $.fn.doFocus = function() {
        this.find(":text,:password,textarea").each(function(a, b) {
            if (!b.readOnly) {
                $(b).bind({
                    focus:function(e) {
                        A.dom.addClass(b, "input-focus");
                    },
                    blur:function(e) {
                        A.dom.removeClass(b, "input-focus");
                    }
                });
            }
        });
    };
    $.fn.getRow = function() {
        var arr = this.find(":text,input:checked,:password,:radio,:hidden,select,textarea");
        var o = {};
        arr.each(function(a, b) {
            if (b.name && b.value) {
                if (!o)o = {};
                var name = b.name;
                if (o[name]) {
                    o[name] += ("," + b.value);
                } else {
                    o[name] = b.value;
                }
            }
        });
        return o;
    };
})();
window["athene"]["env"] || (function() {
    var A = window["athene"],W = window,D = document;
    var B = {
        dragObj:null,
        mousePos:null
    };
    var F = {
        get:function(n) {
            return B[n];
        },
        getAll:function() {
            return B;
        },
        set:function(n, p) {
            B[n] = p;
        }
    };
    A.addModule("env", F);
    (function (win, doc) {
        var ie = (function() {
            var undef,
                v = 3,
                div = document.createElement('div'),
                all = div.getElementsByTagName('i');
            while (
                div.innerHTML = '<!--[if gt IE ' + (++v) + ']><i></i><![endif]-->',
                    all[0]
                ) {
            }
            return v > 4 ? v : undef;

        }());
        var isIE = false,
            isIE6 = false,
            isIE7 = false,
            isIE8 = false,
            isIE9 = false,
            isFF = false,
            isOP = false,
            isOP9 = false,
            isWK = false,
            isSF = false,
            isCR = false;
        if (ie) {
            isIE = true;
            ie === 6 && (isIE6 = true);
            ie === 7 && (isIE7 = true);
            ie === 8 && (isIE8 = true);
            ie === 9 && (isIE9 = true);
        } else {
            isFF = !!doc.getBoxObjectFor || 'mozInnerScreenX' in win,
                isOP = !!win.opera && !!win.opera.toString().indexOf('Opera'),
                isOP9 = /^function \(/.test([].sort),
                isWK = !!win.devicePixelRatio,
                isSF = /a/.__proto__ == '//',
                isCR = /s/.test(/a/.toString);
        }
        F.set("isIE", isIE);
        F.set("isIE6", isIE6);
        F.set("isIE7", isIE7);
        F.set("isIE8", isIE8);
        F.set("isIE9", isIE9);
        F.set("isFF", isFF);
        F.set("isOP", isOP);
        F.set("isOP9", isOP9);
        F.set("isWK", isWK);
        F.set("isSF", isSF);
        F.set("isCR", isCR);
    })(W, D);
    F.set("baseUrl", D.getElementsByTagName("base")[0].href);
    if (F.get("isIE6") || F.get("isIE7") || F.get("isIE8")) {
        F.set("speed", 0);
        F.set("window_speed", 0);
        F.set("task_speed", 0);
    } else {
        F.set("speed", 200);
        F.set("window_speed", 350);
        F.set("task_speed", 500);
    }
    $.ajaxSetup({
        type: "post",
        cache:false,
        timeout:1000 * 60 * 2,
        error:function (xhr, m, e) {
            if (xhr.status == 403) {
                A.dialog.error("无权限");
            } else if (xhr.status == 401) {
                window.location.href = F.get("baseUrl");
            }
        }
    });
})();
window["athene"]["dom"] || (function() {
    var A = window["athene"],W = window,D = document;
    var E = {
        getElement:function(e) {
            var o = undefined;
            switch (typeof e) {
                case "string" :
                    o = DI(e);
                    break;
                default :
                    o = e;
            }
            return o;
        },
        hasClass:function(e, cls) {
            var o = E.getElement(e);
            if (o) {
                var cn = o.className,cns = cn.split(" "),len = cns.length;
                for (var i = 0; i < len; i++) {
                    if (cns[i] == cls) {
                        return true;
                    }
                }
            }
            return false;
        },
        addClass:function(e, cls) {
            var o = E.getElement(e);
            if (o && E.hasClass(o, cls) === false) {
                var cn = o.className;
                cn.length == 0 ? cn = cls : cn += (" " + cls);
                o.className = cn;
            }
        },
        removeClass:function(e, cls) {
            var o = E.getElement(e);
            if (o) {
                var cn = o.className,cns = cn.split(" "),len = cns.length;
                for (var i = 0; i < len; i++) {
                    if (cns[i] == cls) {
                        cns.splice(i, 1);
                        break;
                    }
                }
            }
            o.className = cns.join(" ");
        },
        replaceClass:function(e, cls) {
            var o = E.getElement(e);
            if (o) {
                o.className = cls;
            }
        }
    };
    A.addModule("dom", E);
})();
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
window["athene"]["interaction"] || (function() {
    var A = window["athene"],W = window,D = document,ENV = A.env;
    var Draggable = function(p) {
        this._constructor(p);
        this._init();
        this._bindEvent();
    }
    var DP = {
        _constructor:function(p) {
            A.apply(this, p);
        },
        _init:function() {
            var srcEl = $I(this.src),tarEl = $I(this.tar);
            if (tarEl.css("position") !== "absolute")throw Error("source position must be absolute");
            this.start();
        },
        start:function() {
            var srcEl = $I(this.src);
            srcEl.css("cursor", "move");
            this.running = true;
        },
        stop:function() {
            var srcEl = $I(this.src);
            srcEl.css("cursor", "default");
            this.running = false;
        },
        isRunning:function() {
            return this.running;
        },
        mousemove:function(e) {
            var obj = ENV.get("dragObj"),mp = ENV.get("mousePos");
            if (!obj || !mp)return;
            if (this.beforeCalled !== true) {
                this.before && this.before.call(this.scope || this);
                this.beforeCalled = true;
            }
            var ox = e.pageX - mp.pageX,oy = e.pageY - mp.pageY;
            if (ENV.get("isIE6") || ENV.get("isIE7") || ENV.get("isIE8")) {
                var df = ENV.get("dottedFrame");
                var pos = df.position();
                var o = df.get(0);
            } else {
                var o = obj.get(0);
                var pos = obj.position();
            }
            var tx = pos.left + ox,ty = pos.top + oy;
            if (ty < 0)return;
            o.style.left = tx + "px",o.style.top = ty + "px";
            mp.pageX = e.pageX,mp.pageY = e.pageY;
        },
        mouseup:function(e) {
            if (ENV.get("isIE6") || ENV.get("isIE7") || ENV.get("isIE8")) {
                var df = ENV.get("dottedFrame"), el = ENV.get("dragObj");
                if (df && el) {
                    o = el.get(0),pos = df.position();
                    o.style.left = pos.left + "px";
                    o.style.top = pos.top + "px";
                    df.remove();
                }
            }
            $("body").openSelect();
            ENV.set("dottedFrame", null);
            ENV.set("dragObj", null);
            ENV.set("mousePos", null);
            var documentEl = $(D);
            documentEl.unbind("mousemove", $.proxy(this.mousemove, this));
            this.beforeCalled = false;
            this.after && this.after.call(this.scope || this);
        },
        mousedown:function(e) {
            if (this.running === true) {
                var bodyEl = $("body"), documentEl = $(D);
                var dragObj = $I(this.tar)
                if (ENV.get("isIE6") || ENV.get("isIE7") || ENV.get("isIE8")) {
                    var ts = dragObj.get(0).style;
                    var w = dragObj.outerWidth(true),h = dragObj.outerHeight(true);
                    var l = dragObj.position().left,t = dragObj.position().top;
                    var index = dragObj.css("z-index");
                    bodyEl.append("<div id='dotted-frame' class='dotted-frame' style='z-index:"
                        + (index + 1) + ";width:" + w + "px;height:" + h + "px;left:" + l + "px;top:" + t + "px;'></div>");
                    ENV.set("dottedFrame", $I("dotted-frame"));
                }
                ENV.set("dragObj", dragObj);
                ENV.set("mousePos", {pageX:e.pageX,pageY:e.pageY});
                bodyEl.closeSelect();
                documentEl.mousemove($.proxy(this.mousemove, this));
                documentEl.one("mouseup", $.proxy(this.mouseup, this));
            }
        },
        _bindEvent:function() {
            $I(this.src).mousedown($.proxy(this.mousedown, this));
        },
        destory:function() {
            $I(this.src).unbind("mousedown", this.mousedown);
            this.scope = null;
            this.before = null;
            this.after = null;
        }
    };
    Draggable.prototype = DP;
    var B = {
        draggable:function(p) {
            return new Draggable({
                src:p.src,
                tar:p.tar,
                before:p.before,
                after:p.after,
                scope:p.scope
            });
        }
    };
    A.addModule("interaction", B);
})();
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
window["athene"]["dmenubar"] || (function() {
    var A = window["athene"],W = window,D = document,ENV = A.env,DOM = A.dom,DMENU = A.dmenu;
    var B = {
        dmh:91,
        dmenubars:[]
    };
    var Dmenubar = function(p) {
        this._constructor(p);
        this._init();
    };
    var DP = {
        _constructor:function(p) {
            B.dmenubars.push(this);
            for (var k in p) {
                this[k] = p[k];
            }
        },
        _init:function(p) {
            this.el = $I(this.id);
            this.el.closeSelect();
            this.list = [];
        },
        _bindEvent:function() {
            var dms = this.el.find(".dm"),len = dms.length;
            dms.each(function(i, dom) {
                $(dom).mousedown(function(e) {
                    DP.focusMenu(dom);
                    e.stopPropagation();
                });
            });
            this.el.mousedown($.proxy(function(e) {
                this.focusMenu(null);
            }, this));
        },
        setSize:function(w, h) {
            if (typeof(w) === "number" && w > 0 && typeof(h) === "number" && h > 0) {
                var els = this.el.get(0).style;
                els.width = w + "px";
                els.height = h + "px";
                this.renderMenus();
            }
        },
        focusMenu:function(o) {
            if (this.activeMenu) {
                DOM.removeClass(this.activeMenu, "dm-focus");
                delete this.activeMenu;
            }
            if (o) {
                DOM.addClass(o, "dm-focus");
                this.activeMenu = o;
            }
        },
        renderMenus:function(menus) {
            menus && menus.length > 0 && (this.list = menus);
            var el = this.el,html = [],data = this.list,len = data.length;
            var dmh = B.dmh,elh = el.outerHeight(true);
            var limit = parseInt(elh / dmh),dmch = elh - 15;
            html.push("<div class='dm-container' style='height:" + dmch + "px;'>");
            for (var i = 0; i < len; i++) {
                var count = i + 1,menu = data[i];
                var s = ENV.get("desktopMenu");
                s = s.replaceAll("{ondblclick}", menu.onclick + "athene.dmenubar.get('" + this.id + "').focusMenu(null);");
                s = s.replaceAll("{content}", menu.description);
                menu.imageUrl && (s = s.replaceAll("dm-icon-default", menu.imageUrl + "-48"));
                html.push(s);
                if (count % limit === 0 && count != len) {
                    html.push("</div><div class='dm-container' style='height:" + dmch + "px;'>");
                }
            }
            html.push("</div>");
            el.html(html.join(""));
            this._bindEvent();
        }
    };
    Dmenubar.prototype = DP;
    var C = {
        create:function(p) {
            var dmenubar = new Dmenubar(p);
            return {widgetId:dmenubar.id,xtype:"dmenubar"};
        },
        get:function(id) {
            var dmenubars = B.dmenubars,len = dmenubars.length;
            for (var i = 0; i < len; i++) {
                var item = dmenubars[i];
                if (id === item.id) {
                    return item;
                }
            }
            return null;
        },
        getAll:function() {
            return B.dmenubars;
        }
    };
    A.addModule("dmenubar", C);
})();
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
window["athene"]["grid"] || (function() {
    var A = window["athene"],W = window,D = document,DOM = A.dom,ENV = A.env,UTIL = A.util;
    var B = {
        grids:[]
    };
    var Grid = function(p) {
        this._constructor(p);
        this._init();
        this._render();
    };
    var GP = {
        _constructor:function(p) {
            B.grids.push(this);
            this.cellPL = 5;
            this.cellPR = 3;
            this.cellBRW = 1;
            this.cellMoveW = 10;
            this.items = [];
            this.totalCount = 0;
            this.changed = false;
            this.paramMap = undefined;
            this.defaultMap = {};
            this.showMask = true;
            this.selectItems = [];
            A.apply(this, p);
        },
        _init:function() {
            this.xtype = "grid";
            this.el = $I(this.id),e = this.el.get(0);
            DOM.addClass(e, "grid-default");
            this.gridCls && DOM.addClass(e, this.gridCls);
            this.width = this.width || this.el.parent().innerWidth();
            this.borderLW = UTIL.fetchNumber(this.el.css("borderLeftWidth"));
            this.borderRW = UTIL.fetchNumber(this.el.css("borderRightWidth"));
            A.window.get(this.binding).addItem(this);
            this.onInit && this.onInit.call(this);
        },
        _render:function() {
            var innerWidth = this.width - this.borderLW - this.borderRW;
            this.el.empty().append("<div class='grid-head'></div><div class='grid-body'></div><div class='grid-page'></div>");
            this.headEl = this.el.children(".grid-head");
            this.bodyEl = this.el.children(".grid-body");
            this.pageEl = this.el.children(".grid-page");
            this.el.get(0).style.width = innerWidth + "px";
            this.headEl.get(0).style.width = innerWidth + "px";
            this.bodyEl.get(0).style.width = innerWidth + "px";
            this.pageEl.get(0).style.width = innerWidth + "px";
            this._renderHead();
            this.clearBody();
            this.pagination === true && this._renderPagination();
            this._bindEvent();
            this.onRender && this.onRender.call(this);
            this.items.length > 0 && this._decorate();
        },
        _renderHead:function() {
            var offset = this.cellPL + this.cellPR;
            var html = [],colModels = this.colModels,len = colModels.length;
            var innerWidth = this.width - this.borderLW - this.borderRW;
            var remainWidth = innerWidth,cellMoveW = this.cellMoveW;
            html.push("<table cellpadding='0' cellspacing='0'><thead>");
            if (this.hasRowNumber === true) {
                html.push("<th style='border-right: 1px solid #d0d0d0;'><div class='grid-cell grid-row-number'></div></th>");
                remainWidth -= 30;
            }
            if (this.selectModel === "multiSelect") {
                html.push("<th style='border-right: 1px solid #d0d0d0;'><div class='grid-checkbox-wrap'>");
                html.push("<span class='grid-checkbox' style='display:none;' buz_type='all' buz_status='unchecked'>");
                html.push("</span></div></th>");
                remainWidth -= 22;
            } else if (this.selectModel === "singleSelect") {
                html.push("<th style='border-right: 1px solid #d0d0d0;'><div class='grid-radio-wrap'></div></th>");
                remainWidth -= 22;
            }
            for (var i = 0; i < len; i++) {
                var colModel = colModels[i];
                colModel.width = colModel.width || 100;
                var colWidth = colModel.width;
                var title = colModel.title,thWidth = 0;
                if (false !== colModel.hasTips && (colModel.formatter === undefined || colModel.formatter === null)) {
                    colModel.hasTips = true;
                }
                if (false !== colModel.resizeable) {
                    colModel.resizeable = true;
                }
                html.push("<th>");
                if (this.percent === true) {
                    if ((i + 1) < len) {
                        var colWidthPx = parseInt(colWidth * innerWidth);
                        colModel.widthPx = colWidthPx;
                        remainWidth -= colWidthPx;
                    } else {
                        var colWidthPx = remainWidth;
                        colModel.widthPx = colWidthPx;
                        remainWidth = 0;
                    }
                    colWidth = colModel.widthPx;
                }
                thWidth = colWidth - offset;
                html.push("<div class='grid-title-inner' biz_index='" + i + "' style='width:" + (colWidth) + "px;'>");
                html.push("<div class='grid-cell' style='width:" + (thWidth - cellMoveW) + "px;'");
                if (title && colModel.hasTips === true) {
                    html.push(" title='" + title + "'");
                }
                html.push(">");
                html.push(title || "");
                html.push("</div>");
                if (colModel.resizeable === true) {
                    html.push("<div class='grid-cell-move-right'></div>");
                } else {
                    html.push("<div class='grid-cell-static-right'></div>");
                }
                html.push("</div></th>");
            }
            html.push("</thead></table>");
            this.headEl.html(html.join(""));
        },
        _bindEvent:function() {
            this.el.bind({
                mouseover:function(e) {
                    var tar = e.target;
                    if (tar.nodeName === "A") {
                        DOM.addClass(tar, "link-hover");
                        e.stopPropagation();
                        return;
                    }
                },
                mouseout:function(e) {
                    var tar = e.target;
                    if (tar.nodeName === "A") {
                        DOM.removeClass(tar, "link-hover");
                        e.stopPropagation();
                        return;
                    }
                }
            });
            this.bodyEl.scroll($.proxy(function(e) {
                this.headEl.scrollLeft(this.bodyEl.scrollLeft());
            }, this));
            if (this.selectModel === "multiSelect") {
                this.el.click($.proxy(function(e) {
                    var tar = e.target,cls = tar.className,nodeName = tar.nodeName;
                    if (cls.contains("checkbox") && nodeName === "SPAN") {
                        if (tar.getAttribute("buz_type") === "all") {
                            var items = this.items,len = items.length;
                            var buzStatus = tar.getAttribute("buz_status");
                            for (var i = 0; i < len; i++) {
                                if (buzStatus === "checked") {
                                    this.removeSelectRow(items[i], null, false);
                                    this._triggerCheckAll.call(this);
                                } else {
                                    this.addSelectRow(items[i], null, false);
                                    this._triggerCheckAll.call(this);
                                }
                            }
                        } else {
                            var status = tar.getAttribute("buz_status");
                            if (status === "unchecked") {
                                var item = this.getItemByDom(tar);
                                item && this.addSelectRow(item, $(tar));
                            } else {
                                var item = this.getItemByDom(tar);
                                item && this.removeSelectRow(item, $(tar));
                            }
                        }
                    } else if (cls.indexOf("grid-cell") !== -1 && nodeName === "DIV") {
                        var trEl = $(tar).parents("tr");
                        if (trEl.length > 0) {
                            var trCls = trEl.get(0).className;
                            if (trCls.contains("grid-tr-odd") || trCls.contains("grid-tr-even")) {
                                var checkEl = trEl.children("td.grid-checkbox-td").find(".grid-checkbox");
                                var checkDom = checkEl.get(0);
                                var status = checkDom.getAttribute("buz_status");
                                if (status) {
                                    if (status === "unchecked") {
                                        var item = this.getItemByDom(checkDom);
                                        item && this.addSelectRow(item, checkEl, true);
                                    } else {
                                        var item = this.getItemByDom(checkDom);
                                        item && this.removeSelectRow(item, checkEl, true);
                                    }
                                }
                            }
                        }
                    }
                }, this));
            }
            this.headEl.mousedown($.proxy(function(e) {
                var tar = e.target , cls = tar.className;
                if (cls === "grid-cell-move-right") {
                    var documentEl = $(D),bodyEl = $("body"),el = $(tar);
                    var index = el.parent("div.grid-title-inner").attr("biz_index"),offset = el.offset();
                    var height = this.headEl.outerHeight() + this.bodyEl.innerHeight();
                    bodyEl.css("cursor", "col-resize").append("<div id='grid-move-proxy' biz_index='" + index + "' style='height:"
                        + height + "px;left:" + e.pageX + "px;top:" + offset.top + "px;'></div>");
                    bodyEl.closeSelect();
                    documentEl.mousemove(this._moveColumn);
                    documentEl.one("mouseup", $.proxy(this._resetColumn, this));
                    ENV.set("gridMoveProxy", $I("grid-move-proxy"));
                    ENV.set("gridMousePos", {pageX:e.pageX});
                    ENV.set("gridMousePosInit", {pageX:e.pageX});
                }
            }, this));
        },
        _resetColumn:function(e) {
            var proxy = ENV.get("gridMoveProxy"),index = proxy.attr("biz_index");
            var colModel = this.colModels[index];
            if (this.percent === true) {
                colWidth = colModel.widthPx;
            } else {
                colWidth = colModel.width;
            }
            var mp = ENV.get("gridMousePos"),mpi = ENV.get("gridMousePosInit");
            var documentEl = $(D),offset = mp.pageX - mpi.pageX,tarWidth = colWidth + offset;
            if (tarWidth > 20) {
                this.setColumnWidth(index, tarWidth);
                this._adjustHead();
            }
            $("body").css("cursor", "default").openSelect(),
                proxy.remove();
            documentEl.unbind("mousemove", this._moveColumn);
            ENV.set("gridMoveProxy", null);
            ENV.set("gridMousePos", null);
            ENV.set("gridMousePosInit", null);
        },
        _moveColumn:function(e) {
            var proxy = ENV.get("gridMoveProxy"),mp = ENV.get("gridMousePos");
            if (!proxy || !mp)return;
            var ox = e.pageX - mp.pageX,pos = proxy.position();
            var tx = pos.left + ox,o = proxy.get(0);
            o.style.left = tx + "px";
            mp.pageX = e.pageX;
            e.stopPropagation();
        },
        setColumnWidth:function(index, width) {
            var colModels = this.colModels,len = colModels.length;
            index = parseInt(index);
            var colModel = colModels[index];
            if (!colModel || !width || width < 20)return;
            if (this.percent === true) {
                colModel.widthPx = width;
            } else {
                colModel.width = width;
            }
            var offset = this.cellPL + this.cellPR;
            var cellBRW = this.cellBRW,cellMoveW = this.cellMoveW;
            var headInnerEl = this.headEl.children("table").find("div[biz_index='" + index + "']");
            var headCellEl = headInnerEl.children("div.grid-cell");
            if (headInnerEl.find(".grid-cell-move-right").length > 0) {
                headInnerEl.width(width);
                headCellEl.width(width - offset - cellMoveW);
            }
            var emptyEl = this.bodyEl.find(".empty-div");
            if (emptyEl.length > 0) {
                var hw = this.headEl.children("table").outerWidth();
                emptyEl.width(hw);
            } else {
                var bodyCellEl = this.bodyEl.children("table").find("div[biz_index='" + index + "']");
                bodyCellEl.width(width - offset - cellBRW);
            }
        },
        _decorate:function() {
            this._decorateRows();
            (this.pagination === true) && this._resetPagination();
            this._adjustHead();
        },
        clearBody:function() {
            var hw = this.headEl.children("table").outerWidth();
            var html = "<div class='empty-div' style='width:" + hw + "px;'/>";
            this.bodyEl.html(html);
        },
        _adjustHead:function() {
            var bodyDom = this.bodyEl.get(0);
            var bow = bodyDom.offsetWidth;
            var bcw = bodyDom.clientWidth;
            var boh = bodyDom.offsetHeight;
            var bch = bodyDom.clientHeight;
            var offsetw = bow - bcw, offseth = boh - bch;
            var firstTrEl = this.headEl.children("table").find("tr:first");
            firstTrEl.children(".grid-head-filler-wrap").remove();
            if (offsetw > 0 && offseth > 0) {
                firstTrEl.append("<th class='grid-head-filler-wrap'><div class='grid-head-filler' style='width:" + offsetw + "px;'></div></th>")
            }
        },
        _isChecked:function(item) {
            if (this.selectModel === "multiSelect") {
                var getValueField = this._getValueField;
                var selectItems = this.selectItems,len = selectItems.length;
                for (var i = 0; i < len; i++) {
                    var selectItem = selectItems[i];
                    if (getValueField.call(this, selectItem) === getValueField.call(this, item)) {
                        return true;
                    }
                }
            } else if (this.selectModel === "singleSelect") {
                var selectItem = this.selectItem;
                if (selectItem) {
                    var getValueField = this._getValueField;
                    if (getValueField.call(this, selectItem) === getValueField.call(this, item)) {
                        return true;
                    }
                }
            }
            return false;
        },
        _getValueField:function(item) {
            if (item === undefined || item === null)return undefined;
            var formatValueField = this.formatValueField;
            if (formatValueField) {
                return formatValueField.call(this, item);
            } else {
                return item[this.valueField];
            }
        },
        _decorateRows:function() {
            var html = [],items = this.items,itemLen = items.length;
            var colModels = this.colModels,colLen = colModels.length;
            var isMultiSelect = ( this.selectModel === "multiSelect");
            var isSingleSelect = ( this.selectModel === "singleSelect");
            var offset = this.cellPL + this.cellPR + this.cellBRW;
            var getValueField = this._getValueField;
            html.push("<table cellpadding='0' cellspacing='0'><tbody>");
            for (var i = 0; i < itemLen; i++) {
                var item = items[i],num = (i + 1) % 2;
                var value = getValueField.call(this, item);
                html.push("<tr");
                html.push(num < 1 ? " class='grid-tr-even" : " class='grid-tr-odd");
                var isChecked = false;
                if (isMultiSelect || isSingleSelect) {
                    isChecked = this._isChecked(item);
                }
                if (isMultiSelect && isChecked) {
                    html.push(" grid-tr-checked");
                }
                html.push("'>");
                if (this.hasRowNumber === true) {
                    html.push("<td>");
                    html.push("<div class='grid-cell grid-row-number'>");
                    html.push(i + 1);
                    html.push("</div></td>");
                }
                if (isMultiSelect) {
                    html.push("<td class='grid-checkbox-td'><div class='grid-checkbox-wrap'>");
                    if (isChecked === true) {
                        html.push("<span class='grid-checkbox grid-checkbox-checked' buz_type='single'");
                        html.push(" buz_value='" + value + "' buz_status='checked'>");
                    } else {
                        html.push("<span class='grid-checkbox' buz_type='single'");
                        html.push(" buz_value='" + value + "' buz_status='unchecked'>");
                    }
                    html.push("</span></div></td>");
                } else if (isSingleSelect) {
                    html.push("<td class='grid-radio-td'><div class='grid-radio-wrap'>");
                    if (isChecked === true) {
                        html.push("<span class='grid-radio grid-radio-checked' buz_type='single'");
                        html.push(" buz_value='" + value + "' buz_status='checked'>");
                    } else {
                        html.push("<span class='grid-radio' buz_type='single'");
                        html.push(" buz_value='" + value + "' buz_status='unchecked'>");
                    }
                    html.push("</span></div></td>");
                }
                for (var j = 0; j < colLen; j++) {
                    html.push("<td>");
                    var colModel = colModels[j],colWidth = colModel.width;
                    var value = item[colModel.key];
                    colModel.formatter && (value = colModel.formatter(value, item, i, j));
                    value = ((value !== undefined && value !== null) ? value + "" : "");
                    if (this.percent === true) {
                        colWidth = colModel.widthPx;
                    }
                    var tdWidth = colWidth - offset;
                    html.push("<div class='grid-cell' biz_index='" + j + "' style='width:" + tdWidth + "px;'");
                    if (value && colModel.hasTips === true) {
                        html.push(" title='" + value + "'");
                    }
                    html.push(">");
                    html.push(value);
                    html.push("</div></td>");
                }
                html.push("</tr>");
            }
            html.push("</tbody></table>");
            this.bodyEl.html(html.join(""));
            isMultiSelect && this._triggerCheckAll.call(this);
        },
        _destory:function() {
            var grids = B.grids,len = grids.length;
            for (var i = 0; i < len; i++) {
                var item = grids[i];
                if (this.id == item.id) {
                    grids.splice(i, 1);
                    break;
                }
            }
            this.pageEl.find(".page-size").unbind().remove();
            this.pageEl.unbind().remove();
            this.bodyEl.unbind().remove();
            this.headEl.unbind().remove();
            this.el.unbind().remove();
        },
        getBindingWindow:function() {
            return A.window.get(this.binding);
        },
        setSize:function(w, h) {
            var needAdjust = false;
            if (w && typeof(w) === "number" && w > 0 && w > 0) {
                var innerWidth = w - this.borderLW - this.borderRW;
                if (innerWidth > 0) {
                    this.width = w;
                    this.el.get(0).style.width = innerWidth + "px";
                    this.headEl.get(0).style.width = innerWidth + "px";
                    this.bodyEl.get(0).style.width = innerWidth + "px";
                    this.pageEl.get(0).style.width = innerWidth + "px";
                    if (this.percent === true) {
                        var remainWidth = this.width;
                        var colModels = this.colModels,len = colModels.length;
                        if (this.hasRowNumber === true) {
                            remainWidth -= 30;
                        }
                        if (this.selectModel === "multiSelect" || this.selectModel === "singleSelect") {
                            remainWidth -= 22;
                        }
                        for (var i = 0; i < len; i++) {
                            var colModel = colModels[i],colWidth = colModel.width;
                            if ((i + 1) < len) {
                                var colWidthPx = parseInt(colWidth * remainWidth);
                                colModel.widthPx = colWidthPx;
                                remainWidth -= colWidthPx;
                            } else {
                                var colWidthPx = remainWidth;
                                colModel.widthPx = colWidthPx;
                                remainWidth = 0;
                            }
                            this.setColumnWidth(i, colWidthPx);
                        }
                    }
                    needAdjust = true;
                }
            }
            if (h && typeof(h) === "number" && h >= 0 && h > 0) {
                var innerHeight = h;
                var hh = this.headEl.outerHeight();
                var ph = this.pageEl.outerHeight();
                var bodyH = innerHeight - hh - ph;
                if (innerHeight > 0 && bodyH > 0) {
                    this.height = h;
                    this.el.get(0).style.height = innerHeight + "px";
                    this.bodyEl.get(0).style.height = bodyH + "px";
                    needAdjust = true;
                }
            }
            needAdjust === true && this._adjustHead();
        }
    };
    A.apply(GP, {
        _excute:function(p) {
            var data = {query:true,args:json(this.paramMap)};
            if (this.pagination) {
                data["pageSize"] = this.pageSize;
                data["curPage"] = p.curPage;
            }
            $.ajax({
                url: this.url,
                data:data,
                dataType:"json",
                context:this,
                beforeSend:function() {
                    this.getBindingWindow().showMask("正在加载数据，请稍等...");
                },
                success: function(d) {
                    if (d.returnCode === 3) {
                        athene.dialog.alertException(d.exceptionDesc);
                        return;
                    }
                    if (d && typeof(d) === "object") {
                        for (var t in d) {
                            this[t] = d[t];
                        }
                    }
                    this._decorate.call(this, d);
                    this.onQuery && this.onQuery.call(this, d);
                    this.getBindingWindow().hideMask();
                }
            });
        },
        query:function(p) {
            this.paramMap = A.util.merge(this.defaultMap, p);
            this._excute({curPage: 1});
        },
        first:function() {
            this._excute({curPage:1});
        },
        last:function() {
            this._excute({curPage:this.totalPage});
        },
        next:function() {
            this._excute({curPage:this.curPage + 1});
        },
        previous:function() {
            this._excute({curPage:this.curPage - 1});
        }  ,
        refresh:function() {
            this.curPage !== undefined && this._excute({curPage:this.curPage});
        },
        refreshSubmit:function() {
            if (this.submit === true && ENV.get("refreshSubmit") === true) {
                this.refresh();
                this.submit = false;
            }
        },
        setPageSize:function(val) {
            if (val) {
                this.pageSize = val;
                this.curPage !== undefined && this.first();
            }
        }
    });
    A.apply(GP, {
        getSelectRow:function() {
        },
        getSelectRows:function() {
            return this.selectItems;
        },
        getItemByDom:function(dom) {
            var buzValue = dom.getAttribute("buz_value");
            if (buzValue) {
                return this.getItem(buzValue);
            } else {
                return null;
            }
        },
        getItem:function(buzValue) {
            var items = this.items,len = items.length;
            if (buzValue == undefined || len == 0)return null;
            var getValueField = this._getValueField;
            for (var i = 0; i < len; i++) {
                var item = items[i];
                if (getValueField.call(this, item) == buzValue) {
                    return item;
                }
            }
            return null;
        },
        addRow:function(item) {
            if (this.pagination || typeof(item) !== "object") return;
            this.items.push(item);
            this.totalCount++;
            this.changed = true;
            this._decorateRows();
            return this;
        },
        replaceRow:function(i, item) {
            if (this.pagination || typeof(item) !== "object") return;
            if (i >= this.items.length)return;
            this.items.splice(i, 1, item);
            this.changed = true;
            this._decorateRows();
            return this;
        },
        removeRow:function(i) {
            if (this.pagination || typeof(i) !== "number") return;
            if (i >= this.items.length)return;
            this.items.splice(i, 1);
            this.totalCount--;
            this.changed = true;
            this._decorateRows();
            return this;
        } ,
        clearSelectRows:function() {
            this.selectItems = [];
            this._decorateRows();
            this.onClearSelectRows && this.onClearSelectRows.call(this);
        },
        removeSelectRow:function(item, el, trigger) {
            var removeItem = undefined,valueField = this.valueField;
            var selectItems = this.selectItems,len = selectItems.length;
            if (item && len > 0) {
                for (var i = 0; i < len; i++) {
                    var selectItem = selectItems[i];
                    if (item[valueField] === selectItem[valueField]) {
                        removeItem = selectItem;
                        selectItems.splice(i, 1);
                        var checkboxEl = el || this.bodyEl.find("span[buz_value='" + selectItem[valueField] + "']");
                        if (checkboxEl.length > 0) {
                            this._triggerRow(checkboxEl, false);
                            DOM.removeClass(checkboxEl.get(0), "grid-checkbox-checked");
                            checkboxEl.attr("buz_status", "unchecked");
                        }
                        break;
                    }
                }
            }
            if (this.onRemoveSelectRow && removeItem) {
                trigger === false || this._triggerCheckAll.call(this);
                this.onRemoveSelectRow.call(this, removeItem);
            }
            return this;
        },
        _triggerRow:function(checkEl, flag) {
            if (checkEl && checkEl.length > 0) {
                var trEl = checkEl.parents("tr");
                if (trEl.length > 0) {
                    if (flag === true) {
                        DOM.addClass(trEl.get(0), "grid-tr-checked");
                    } else {
                        DOM.removeClass(trEl.get(0), "grid-tr-checked");
                    }
                }
            }
        },
        addSelectRow:function(item, el, trigger) {
            if (item) {
                var checkboxEl = el || this.bodyEl.find("span[buz_value='" + item[this.valueField] + "']");
                if (checkboxEl.length > 0) {
                    this._triggerRow(checkboxEl, true);
                    var status = checkboxEl.attr("buz_status");
                    if (status === "unchecked") {
                        DOM.addClass(checkboxEl.get(0), "grid-checkbox-checked");
                        checkboxEl.attr("buz_status", "checked");
                        this.selectItems.push(item);
                        trigger === false || this._triggerCheckAll.call(this);
                        this.onAddSelectRow && this.onAddSelectRow.call(this, item);
                    }
                }
            }
            return this;
        },
        _triggerCheckAll:function() {
            if (this.selectModel === "multiSelect") {
                var checkAllEl = this.headEl.find(".grid-checkbox");
                if (this.items.length > 0) {
                    checkAllEl.show();
                    var uncheckLen = this.bodyEl.find(".grid-checkbox[buz_status='unchecked']").length;
                    if (uncheckLen === 0) {
                        checkAllEl.attr("buz_status", "checked");
                        DOM.addClass(checkAllEl.get(0), "grid-checkbox-checked");
                    } else {
                        checkAllEl.attr("buz_status", "unchecked");
                        DOM.removeClass(checkAllEl.get(0), "grid-checkbox-checked");
                    }
                } else {
                    checkAllEl.attr("buz_status", "unchecked");
                    checkAllEl.hide();
                }
            }
        },
        setSelectRows:function(p) {
            if (p instanceof Array) {
                this.selectItems = [];
                var len = p.length;
                for (var i = 0; i < len; i++) {
                    this.selectItems.push(p[i]);
                }
            }
            return this;
        }
    });
    A.apply(GP, {
        _renderPagination:function() {
            var html = [];
            html.push("<div class='grid-page-bg'><span style='float:left;'><table cellpadding='0' cellspacing='0' border='0'><tbody><tr>");
            html.push("<td><span class='grid-page-button grid-page-first-disabled' buz_type='first' buz_status='disable'></span></td>");
            html.push("<td><span class='grid-page-button grid-page-prev-disabled' buz_type='prev' buz_status='disable'></span></td>");
            html.push("<td><span class='grid-page-split'></span></td>");
            html.push("<td><span class='grid-page-content'>第</span>");
            html.push("<span class='cur-page grid-page-number'>0</span>");
            html.push("<span class='grid-page-content'>页</span></td>");
            html.push("<td style='padding-left:4px;'><span class='grid-page-content'>共</span>");
            html.push("<span class='total-page grid-page-number'>0</span>");
            html.push("<span class='grid-page-content'>页</span></td>");
            html.push("<td><span class='grid-page-split'></span></td>");
            html.push("<td><span class='grid-page-button grid-page-next-disabled' buz_type='next' buz_status='disable'></span></td>");
            html.push("<td><span class='grid-page-button grid-page-last-disabled' buz_type='last' buz_status='disable'></span></td>");
            html.push("<td><span class='grid-page-split'></span></td>");
            html.push("<td><span class='grid-page-button grid-page-refresh-disabled' buz_type='refresh' buz_status='disable'></span></td>");
            html.push("<td><span class='grid-page-split'></span></td>");
            html.push("<td><span class='page-size-wrap'><select class='page-size'><option value='10'>10</option>");
            html.push("<option value='20'>20</option><option value='30'>30</option><option value='40'>40</option>");
            html.push("<option value='50'>50</option></select></span></td></tr></tbody></table></span>");
            html.push("<span style='float:right;'><table cellpadding='0' cellspacing='0' border='0'><tbody><tr>");
            html.push("<td><span class='total-count'>没有数据需要显示</span></td></tr></tbody></table></span>");
            html.push("<span style='clear:both;'></span>");
            html.push("</div>");
            this.pageEl.html(html.join(""));
            this._bindPaginationEvent();
        },
        _bindPaginationEvent:function() {
            this.pageEl.bind({
                click:$.proxy(function(e) {
                    var tar = e.target,type = tar.getAttribute("buz_type");
                    var status = tar.getAttribute("buz_status");
                    if (type && status === "enable") {
                        if (type === "first") {
                            this.first();
                        } else if (type === "prev") {
                            this.previous();
                        } else if (type === "next") {
                            this.next();
                        } else if (type === "last") {
                            this.last();
                        } else if (type === "refresh") {
                            this.refresh();
                        }
                        e.stopPropagation();
                    }
                }, this),
                mousemove:function(e) {
                    var tar = e.target,type = tar.getAttribute("buz_type");
                    var status = tar.getAttribute("buz_status");
                    if (type && status === "enable") {
                        DOM.replaceClass(tar, "grid-page-button grid-page-" + type + "-hover");
                        e.stopPropagation();
                    }
                },
                mouseout:function(e) {
                    var tar = e.target,type = tar.getAttribute("buz_type");
                    var status = tar.getAttribute("buz_status");
                    if (type && status === "enable") {
                        DOM.replaceClass(tar, "grid-page-button grid-page-" + type);
                        e.stopPropagation();
                    }
                },
                mousedown:function(e) {
                    var tar = e.target,type = tar.getAttribute("buz_type");
                    var status = tar.getAttribute("buz_status");
                    if (type && status === "enable") {
                        DOM.addClass(tar, "grid-page-button grid-page-" + type + "-active");
                        e.stopPropagation();
                    }
                },
                mouseup:function(e) {
                    var tar = e.target,type = tar.getAttribute("buz_type");
                    var status = tar.getAttribute("buz_status");
                    if (type && status === "enable") {
                        DOM.removeClass(tar, "grid-page-button grid-page-" + type + "-active");
                        e.stopPropagation();
                    }
                }
            });
            this.pageEl.find(".page-size").change($.proxy(function(e) {
                var value = this.pageEl.find(".page-size").val();
                this.setPageSize(parseInt(value));
            }, this));
        },
        _resetPagination:function() {
            var pageEl = this.pageEl;
            var firstBtn = pageEl.find("span[buz_type='first']");
            var prevBtn = pageEl.find("span[buz_type='prev']");
            var nextBtn = pageEl.find("span[buz_type='next']");
            var lastBtn = pageEl.find("span[buz_type='last']");
            var refreshBtn = pageEl.find("span[buz_type='refresh']");
            var curPageSpan = pageEl.find("span.cur-page");
            var totalPageSpan = pageEl.find("span.total-page");
            var totalCountSpan = pageEl.find("span.total-count");
            if (this.curPage && this.totalCount) {
                this._enableButton(refreshBtn);
                curPageSpan.html(this.curPage);
                totalPageSpan.html(this.totalPage);
                totalCountSpan.html("共找到" + this.totalCount + "条记录");
                if (this.curPage < 2) {
                    this._disableButton(firstBtn);
                    this._disableButton(prevBtn);
                } else {
                    this._enableButton(firstBtn);
                    this._enableButton(prevBtn);
                }
                if (this.curPage >= this.totalPage) {
                    this._disableButton(nextBtn);
                    this._disableButton(lastBtn);
                } else {
                    this._enableButton(nextBtn);
                    this._enableButton(lastBtn);
                }
            } else {
                this._disableButton(firstBtn);
                this._disableButton(prevBtn);
                this._disableButton(nextBtn);
                this._disableButton(lastBtn);
                this._disableButton(refreshBtn);
                curPageSpan.html("0");
                totalPageSpan.html("0");
                totalCountSpan.html("没有数据需要显示");
            }
        },
        _enableButton:function(btn) {
            var btnStatus = btn.attr("buz_status");
            if (btnStatus === "disable") {
                var btnType = btn.attr("buz_type");
                DOM.replaceClass(btn.get(0), "grid-page-button grid-page-" + btnType);
                btn.attr("buz_status", "enable");
            }
        },
        _disableButton:function(btn) {
            var btnStatus = btn.attr("buz_status");
            if (btnStatus === "enable") {
                var btnType = btn.attr("buz_type");
                DOM.replaceClass(btn.get(0), "grid-page-button grid-page-" + btnType + "-disabled");
                btn.attr("buz_status", "disable");
            }
        }
    });
    Grid.prototype = GP;
    var C = {
        getAll:function() {
            return B.grids;
        },
        get:function(p) {
            var grids = B.grids,len = grids.length;
            for (var i = 0; i < len; i++) {
                var item = grids[i];
                if (p == item.id) {
                    return item;
                }
            }
            return null;
        },
        create:function(p) {
            var grid = new Grid(p);
            return {widgetId:grid.id,xtype:"grid"};
        }
    };
    A.addModule("grid", C);
})();
window["athene"]["log"] || (function() {
    var A = window["athene"],$I = A.core.$I,W = window,D = document;
    var B = {
        inited:false,
        logable:false,
        debugable:false,
        infoable:false
    };
    var C = {
        _getTime:function() {
            var now = new Date(),month = now.getMonth() + 1;
            return now.getFullYear() + "-" + month + "-" + now.getDate() + "&nbsp;" + now.toLocaleTimeString() + "&nbsp&nbsp;";
        },
        _print:function(p) {
            var c;
            switch (typeof p.content) {
                case "object":
                    c = $.toJSON(p.content);
                    break;
                default:
                    c = p.content;
            }
            var s = C._getTime() + p.tag + "&nbsp&nbsp;";
            var data = "<div class=\"log-content\"><span style=\"color:" + p.color + ";\">" + s + "</span>" + c + "</div>";
            B.console.append(data).scrollTop(99999999);
        }
    };
    var D = {
        LEVEL : {
            DEBUG:"debug",
            INFO:"info",
            ERROR:"error"
        },
        init:function(p, f) {
            if ((!p) || B.inited)return;
            if (p == "debug") {
                B.logable = true,B.debugable = true,B.infoable = true;
            } else if (p == "info") {
                B.logable = true,B.infoable = true;
            } else if (p == "error") {
                B.logable = true;
            } else {
                return;
            }
            A.window.open({
                title:"日志控制台",
                width:500,
                height:400,
                content:A.env.get("logConsole")
            });
            B.inited = true,B.console = $I("log-console"),W["log"] = this;
            if (f)f.call(this);
        },
        debug:function(p) {
            if (!B.logable || !B.debugable) return;
            C._print({tag:"DEBUG",color:"blue",content:p});
        },
        info:function(p) {
            if (!B.logable || !B.infoable) return;
            C._print({tag:"INFO",color:"black",content:p});
        },
        error:function(p) {
            if (!B.logable) return;
            C._print({tag:"ERROR",color:"red",content:p});
        },
        clear:function() {
            if (B.console)B.console.empty();
        }
    };
    A.addModule("log", D);
})();
window["athene"]["form"] || (function() {
    var A = window["athene"],W = window,D = document;
    var B = {
        forms:[]
    };
    var Form = function(p) {
        this._constructor(p);
        this._init();
    };
    var FP = {
        _constructor:function(p) {
            B.forms.push(this);
            for (var k in p) {
                this[k] = p[k];
            }
        },
        _init:function() {
            A.window.get(this.binding).addItem(this);
        },
        getRow:function() {
            if (!this.items) throw Error("数组定义有错误");
            var formEl = $I(this.id),o = {};
            var items = this.items,len = items.length;
            for (var i = 0; i < len; i++) {
                var item = items[i],type = item.type;
                if (type === "Grid") {
                    var grid = A.grid.get(item.id);
                    if (grid && grid.name && grid.items.length > 0) {
                        var name = grid.name;
                        o[name] = grid.items;
                        o[name + "Changed"] = grid.changed;
                    }
                } else if (type === "ListField") {
                    var listfield = A.listfield.get(item.id);
                    if (listfield && listfield.name && listfield.items.length > 0) {
                        var name = listfield.name;
                        o[name] = listfield.items;
                        o[name + "Changed"] = listfield.changed;
                    }
                } else if (type === "CheckGroup") {
                    var checkGroup = A.checkgroup.get(item.id);
                    if (checkGroup && checkGroup.name && checkGroup.selectItems.length > 0) {
                        var name = checkGroup.name;
                        o[name] = checkGroup.selectItems;
                        o[name + "Changed"] = checkGroup.changed;
                    }
                } else {
                    var el = formEl.find("#" + item.id);
                    if (el.length > 0) {
                        var dom = el.get(0);
                        if (dom && dom.name && dom.value) {
                            o[dom.name] = dom.value;
                        }
                    }
                }
            }
            return o;
        },
        getConfirmRow:function() {
            if (!this.items) throw Error("数组定义有错误");
            var formEl = $I(this.id),html = [];
            var items = this.items,len = items.length;
            for (var i = 0; i < len; i++) {
                var item = items[i];
                var label = item.label,type = item.type;
                if (!label || !type)continue;
                if (type === "Grid") {
                    var grid = A.grid.get(item.id);
                    if (grid && grid.name && grid.items.length > 0) {
                        html.push("<div class='fwb form-label'>" + item.label + "：</div>");
                        var gridItems = grid.items,gridLen = gridItems.length;
                        for (var k = 0; k < gridLen; k++) {
                            html.push("<div class='ti2'>");
                            var gridItem = gridItems[k];
                            var colModels = grid.colModels,colModelsLen = colModels.length;
                            for (var j = 0; j < colModelsLen; j++) {
                                var colModel = colModels[j];
                                var key = colModel.key;
                                if (key && gridItem[key]) {
                                    var value = gridItem[key];
                                    html.push(value + " ");
                                }
                            }
                            html.push("</div>");
                        }
                    }
                } else if (type === "CheckGroup") {
                    var checkgroup = A.checkgroup.get(item.id);
                    if (checkgroup && checkgroup.name && checkgroup.selectItems.length > 0) {
                        var selectItems = checkgroup.selectItems,selectLen = selectItems.length;
                        html.push("<div class='fwb form-label'>" + item.label + "：</div>");
                        html.push("<div class='ti2'>");
                        for (var k = 0; k < selectLen; k++) {
                            var checkgroupItem = selectItems[k];
                            html.push(checkgroupItem[checkgroup.listKey] + " ");
                            if ((k + 1) % 5 === 0 && (k + 1) !== selectLen) {
                                html.push("</div><div class='ti2'>");
                            }
                        }
                        html.push("</div>");
                    }
                } else if (type === "ListField") {
                    var listfield = A.listfield.get(item.id);
                    if (listfield && listfield.name && listfield.items.length > 0) {
                        var listKey = listfield.listKey;
                        var listfieldItems = listfield.items,listfieldLen = listfieldItems.length;
                        html.push("<div class='fwb form-label'>" + item.label + "：</div>");
                        html.push("<div class='ti2'>");
                        for (var k = 0; k < listfieldLen; k++) {
                            var listfieldItem = listfieldItems[k];
                            html.push(listfieldItem[listKey] + " ");
                            if ((k + 1) % 5 === 0 && (k + 1) !== listfieldLen) {
                                html.push("</div><div class='ti2'>");
                            }
                        }
                        html.push("</div>");
                    }
                } else if (type === "ComboBox") {
                    var el = formEl.find("#" + item.id);
                    if (el.length > 0) {
                        var dom = el.get(0);
                        if (dom && dom.name && dom.value) {
                            var optionEl = el.children("option[value='" + dom.value + "']");
                            html.push("<div><span class='fwb form-label'>" + item.label + "：</span>");
                            html.push("<span>" + optionEl.text() + "</span></div>");
                        }
                    }
                }
                else {
                    var el = formEl.find("#" + item.id);
                    if (el.length > 0) {
                        var dom = el.get(0);
                        if (dom && dom.name && dom.value) {
                            html.push("<div><span class='fwb form-label'>" + item.label + "：</span>");
                            html.push("<span>" + dom.value + "</span></div>");
                        }
                    }
                }
            }
            return html.join("");
        },
        save:function() {
            this.handler("saveUrl", "messageSave", "onSave");
        },
        update:function() {
            this.handler("updateUrl", "messageUpdate", "onUpdate");
        },
        remove:function() {
            this.handler("removeUrl", "messageRemove", "onRemove");
        },
        handler:function(u, b, after) {
            if (!u || !this[u]) throw Error("url未定义");
            var container = $I(this.id),url = this[u];
            var row = this.getRow(),confirmRow = this.getConfirmRow();
            var data = {number:this.binding,args:json(row)};
            var request = function(flag) {
                if (flag === false)return;
                $.ajax({
                    url: url,
                    context:this,
                    data:data,
                    beforeSend:function() {
                        container.find(".fm-error").hide().find(".fm-error-content").empty();
                        this.getBindingWindow().showMask("正在提交数据，请稍等...");
                    },
                    dataType:"json",
                    success: function(d) {
                        if (d.returnCode === 0) {
                            if (this.onSuccess) {
                                this.onSuccess.call(this, d);
                            }
                            var handler = this[after];
                            if (handler && typeof(handler) === "function") {
                                handler.call(this, row, d);
                            }
                        } else if (d.returnCode === 1) {
                            var list = d.validationErrors,len = list.length;
                            for (var i = 0; i < len; i++) {
                                var item = list[i];
                                var el = container.find("#" + item.id);
                                el.show().find(".fm-error-content").html(item.message);
                            }
                        } else if (d.returnCode === 4 || d.returnCode === 2) {
                            A.dialog.error(d.exceptionDesc);
                        } else {
                            A.dialog.alertException(d.exceptionDesc);
                        }
                    },
                    complete:function() {
                        this.getBindingWindow().hideMask();
                    }
                });
            };
            if (b && this[b]) {
                A.dialog.confirm(this[b](confirmRow, row), request, this, this.dialogWidth);
            } else {
                request.call(this);
            }
        },
        getBindingWindow:function() {
            return A.window.get(this.binding);
        },
        _destory:function() {
            var forms = B.forms,len = forms.length;
            for (var i = 0; i < len; i++) {
                var item = forms[i];
                if (this.id === item.id) {
                    forms.splice(i, 1);
                }
            }
        }
    };
    Form.prototype = FP;
    var F = {
        create:function(p) {
            var form = new Form(p);
            return {widgetId:form.id,xtype:"form"};
        },
        get:function(id) {
            var forms = B.forms,len = forms.length;
            for (var i = 0; i < len; i++) {
                var item = forms[i];
                if (id === item.id) {
                    return item;
                }
            }
            return null;
        },
        getAll:function() {
            return B.forms;
        }
    };
    A.addModule("form", F);
})();
window["athene"]["listfield"] || (function() {
    var A = window["athene"],W = window,D = document,DOM = A.dom,ENV = A.env,UTIL = A.util;
    var B = {
        listFields:[]
    };
    var ListField = function(p) {
        this._constructor(p);
        this._init();
        this._render();
        this._bindEvent();
    };
    var LP = {
        _constructor:function(p) {
            if (!p.id)throw Error("ListField控件必须设置id");
            if (!p.binding)throw Error("ListField控件必须设置binding");
            if (!p.listKey)throw Error("ListField控件必须设置listKey");
            if (!p.listValue)throw Error("ListField控件必须设置listValue");
            B.listFields.push(this);
            this.items = [];
            this.removeable = true;
            this.contentWidth = 66;
            for (var k in p) {
                this[k] = p[k];
            }
        },
        _init:function() {
            this.xtype = "listfield";
            this.changed = false;
            this.el = $I(this.id);
            A.window.get(this.binding).addItem(this);
        },
        _render:function() {
            var arr = [];
            arr.push("<fieldset class='listfield-default'><legend>");
            arr.push(this.title || "");
            arr.push("</legend><div class='listfield-wrap'><div class='clear'></div></div></fieldset>");
            this.el.append(arr.join(""));
            this.items.length > 0 && this._renderItems();
        },
        _bindEvent:function() {
            this.el.find(".listfield-wrap").click($.proxy(function(e) {
                var tar = e.target,cls = tar.className;
                if (cls.contains("icon-remove")) {
                    var value = tar.getAttribute("biz_value");
                    this.removeItem(value);
                }
            }, this));
        },
        _renderItems:function() {
            var html = [],items = this.items,len = items.length;
            for (var i = 0; i < len; i++) {
                var item = items[i];
                html.push(this._generateItem(i, item));
            }
            html.push("<div class='clear'></div>");
            this.el.children("fieldset").children("div").html(html.join(""));
        },
        _generateItem:function(i, item) {
            var html = [];
            html.push("<div class='listfield-item'>");
            html.push("<div class='listfield-item-content' style='width:" + this.contentWidth + "px;'>");
            if (this.formatter) {
                html.push(this.formatter.call(this, i, item) || "");
            } else {
                html.push(item[this.listKey] || "");
            }
            html.push("</div>");
            if (this.removeable === true) {
                var value = item[this.listValue];
                html.push("<div class='listfield-item-icon-wrap'><div class='listfield-item-icon icon-remove cp'");
                html.push(" biz_value='" + value + "'/></div></div>");
            }
            html.push("</div>");
            return html.join("");
        },
        setItems:function(p) {
            if (p instanceof Array && p.length > 0) {
                var listFields = p,len = listFields.length;
                this.items = [];
                for (var i = 0; i < len; i++) {
                    this.items.push(listFields[i]);
                }
                this._renderItems();
                this.changed = true;
                this.onSetItems && this.onSetItems.call(this, p);
            }
            return this;
        },
        getItems:function() {
            return this.items;
        },
        clearItems:function() {
            if (this.items.length > 0) {
                this.items = [];
                this._renderItems();
                this.changed = true;
                this.onClearItems && this.onClearItems.call(this);
            }
            return this;
        },
        addItem:function(item) {
            if (item) {
                var listValue = this.listValue;
                var items = this.items,len = items.length;
                for (var i = 0; i < len; i++) {
                    var existItem = items[i];
                    if (existItem[listValue] === item[listValue]) {
                        return;
                    }
                }
                items.push(item);
                var html = this._generateItem(len, item);
                this.el.find(".listfield-wrap").children(".clear").before(html);
                this.changed = true;
                this.onAddItem && this.onAddItem.call(this, item);
            }
            return this;
        },
        removeItem:function(value) {
            if (value == undefined || this.items.length == 0)return this;
            var removeItem = undefined,listValue = this.listValue;
            var items = this.items,len = items.length;
            for (var i = 0; i < len; i++) {
                var item = items[i];
                if (typeof(value) === "object") {
                    if (item[listValue] === value[listValue]) {
                        removeItem = item;
                        items.splice(i, 1);
                        break;
                    }
                } else {
                    if (item[listValue] == value) {
                        removeItem = item;
                        items.splice(i, 1);
                        break;
                    }
                }
            }
            if (removeItem) {
                this._renderItems();
                this.changed = true;
                this.onRemoveItem && this.onRemoveItem.call(this, removeItem);
            }
            return this;
        },
        _destory:function() {
            var listFields = B.listFields,len = listFields.length;
            for (var i = 0; i < len; i++) {
                var item = listFields[i];
                if (this.id === item.id) {
                    listFields.splice(i, 1);
                    break;
                }
            }
            this.el.find(".listfield-wrap").unbind();
            this.el.remove();
        }
    };
    ListField.prototype = LP;
    var F = {
        create:function(p) {
            var listField = new ListField(p);
            return {widgetId:listField.id,xtype:"listfield"};
        },
        get:function(id) {
            var listFields = B.listFields,len = listFields.length;
            for (var i = 0; i < len; i++) {
                var item = listFields[i];
                if (id === item.id) {
                    return item;
                }
            }
            return null;
        },
        getAll:function() {
            return B.listFields;
        }
    };
    A.addModule("listfield", F);
})();
window["athene"]["checkgroup"] || (function() {
    var A = window["athene"],W = window,D = document,DOM = A.dom,ENV = A.env,UTIL = A.util;
    var B = {
        checkGroups:[]
    };
    var CheckGroup = function(p) {
        this._constructor(p);
        this._init();
        this._render();
        this._bindEvent();
    };
    CheckGroup.prototype = {
        _constructor:function(p) {
            if (!p.id)throw Error("CheckGroup控件必须设置id");
            if (!p.binding)throw Error("CheckGroup控件必须设置binding");
            if (!p.listKey)throw Error("CheckGroup控件必须设置listKey");
            if (!p.listValue)throw Error("CheckGroup控件必须设置listValue");
            B.checkGroups.push(this);
            this.items = [];
            this.contentWidth = 65;
            this.selectItems = [];
            for (var k in p) {
                this[k] = p[k];
            }
        },
        _init:function() {
            this.changed = false;
            this.xtype = "checkgroup";
            this.el = $I(this.id);
            this.el.closeSelect();
            A.window.get(this.binding).addItem(this);
            DOM.addClass(this.el.get(0), "checkgroup-default");
        },
        _render:function() {
            var html = [];
            html.push("<div class='checkgroup-wrap'><div class='clear'></div></div>");
            this.el.html(html.join(""));
            this.items.length > 0 && this._renderItems();
        },
        _renderItems:function() {
            var html = []
            var items = this.items,len = items.length;
            for (var i = 0; i < len; i++) {
                var item = items[i];
                html.push(this._generateItem(i, item));
            }
            html.push("<div class='clear'></div>");
            this.el.find(".checkgroup-wrap").html(html.join(""));
        },
        _generateItem:function(i, item) {
            var html = [],listKey = this.listKey,content = item[listKey] || "";
            var listValue = this.listValue,value = item[listValue];
            var isCheck = this._isChecked(item);
            html.push("<div class='checkgroup-item'><div class='checkgroup-item-icon-wrap'>");
            html.push("<div class='checkgroup-item-icon");
            isCheck && html.push(" checkgroup-item-icon-checked");
            html.push("' biz_value='" + value + "'");
            if (isCheck) {
                html.push(" biz_status='checked'");
            } else {
                html.push(" biz_status='uncheck'");
            }
            html.push("></div></div><div class='checkgroup-item-content' style='width:");
            html.push(this.contentWidth + "px;'>" + content + "</div></div>");
            return html.join("");
        },
        _bindEvent:function() {
            this.el.find(".checkgroup-wrap").click($.proxy(function(e) {
                var tar = e.target,cls = tar.className;
                if (cls.contains("checkgroup-item-icon") || cls.contains("checkgroup-item-content")) {
                    var dom = null;
                    if (cls.contains("checkgroup-item-content")) {
                        var el = $(tar).parent().find(".checkgroup-item-icon");
                        el.length > 0 && (dom = el.get(0));
                    } else {
                        dom = tar;
                    }
                    if (dom) {
                        var value = dom.getAttribute("biz_value");
                        var status = dom.getAttribute("biz_status");
                        if (value && status) {
                            var item = this.getItem(value);
                            if (item) {
                                if (status === "checked") {
                                    this.removeSelectItem(item);
                                } else if (status === "uncheck") {
                                    this.addSelectItem(item);
                                }
                            }
                        }
                    }
                }
            }, this));
        },
        _isChecked:function(item) {
            var listValue = this.listValue;
            var selectItems = this.selectItems,len = selectItems.length;
            for (var i = 0; i < len; i++) {
                var selectItem = selectItems[i];
                if (selectItem[listValue] === item[listValue]) return true;
            }
            return false;
        },
        getItem:function(value) {
            if (value == undefined || this.items.length == 0)return null;
            var listValue = this.listValue;
            var items = this.items,len = items.length;
            for (var i = 0; i < len; i++) {
                var item = items[i];
                if (item[listValue] == value) {
                    return item;
                }
            }
            return null;
        },
        addSelectItem:function(item) {
            if (item) {
                this.selectItems.push(item);
                var value = item[this.listValue];
                var el = this.el.find("div.checkgroup-item-icon[biz_value='" + value + "']");
                if (el.length > 0) {
                    DOM.addClass(el.get(0), "checkgroup-item-icon-checked");
                    this.changed = true;
                    el.attr("biz_status", "checked");
                    this.onAddSelectItem && this.onAddSelectItem.call(this, item);
                }
            }
            return this;
        },
        removeSelectItem:function(item) {
            var removeItem = undefined;
            if (item) {
                var listValue = this.listValue;
                var selectItems = this.selectItems,len = selectItems.length;
                for (var i = 0; i < len; i++) {
                    var selectItem = selectItems[i];
                    if (selectItem[listValue] === item[listValue]) {
                        removeItem = selectItem;
                        selectItems.splice(i, 1);
                        break;
                    }
                }
                if (removeItem) {
                    var value = removeItem[listValue];
                    var el = this.el.find("div.checkgroup-item-icon[biz_value='" + value + "']");
                    if (el.length > 0) {
                        DOM.removeClass(el.get(0), "checkgroup-item-icon-checked");
                        this.changed = true;
                        el.attr("biz_status", "uncheck");
                        this.onRemoveSelectItem && this.onRemoveSelectItem.call(this, removeItem);
                    }
                }
            }
            return this;
        },
        _destory:function() {
            var checkGroups = B.checkGroups,len = checkGroups.length;
            for (var i = 0; i < len; i++) {
                var item = checkGroups[i];
                if (this.id === item.id) {
                    checkGroups.splice(i, 1);
                    break;
                }
            }
            this.el.find(".checkgroup-wrap").unbind();
            this.el.remove();
        }
    };
    var F = {
        create:function(p) {
            var checkGroup = new CheckGroup(p);
            return {widgetId:checkGroup.id,xtype:"checkgroup"};
        },
        get:function(id) {
            var checkGroups = B.checkGroups,len = checkGroups.length;
            for (var i = 0; i < len; i++) {
                var item = checkGroups[i];
                if (id === item.id) {
                    return item;
                }
            }
            return null;
        },
        getAll:function() {
            return B.checkGroups;
        }
    };
    A.addModule("checkgroup", F);
})();
window["athene"]["message"] || (function() {
    var A = window["athene"],W = window,D = document,ENV = A.env;
    var B = {
        MESSAGES:[]
    };
    var Message = function(p) {
        B.MESSAGES.push(this);
        this._constructor(p);
        this._render();
    };
    var FP = {
        _constructor:function(p) {
            this.speed = 500;
            this.height = 130;
            this.width = 200;
            this.delay = 2000;
            this.closeable = false;
            for (var k in p) {
                this[k] = p[k];
            }
        },
        _render:function() {
            var number = A.util.getTimestamp();
            $("body").append(ENV.get("message").replaceAll("{number}", number));
            this.id = "message-" + number;
            this.el = $I(this.id);
            this.el.closeSelect();
            this.closeable === false && this.el.find(".message-panel-close").remove();
            this._bindEvent();
            this.setTitle(this.title || "提示");
            this.setMessage(this.message);
            this.setSize(this.width, this.height);
            this.setPosition();
            this.start();
        },
        _bindEvent:function() {
            if (this.closeable === true) {
                this.el.find(".message-panel-close").click($.proxy(function(e) {
                    this.stop(true);
                    e.stopPropagation();
                }, this));
            }
        },
        setTitle:function(title) {
            title && this.el.find(".message-panel-title-font").html(title);
        },
        setMessage:function(message) {
            message && this.el.find(".message-panel-message-box").html(message);
        },
        setSize:function(w, h) {
            var el = this.el;
            el.width(w - 2);
            var messageBox = el.find(".message-panel-message");
            messageBox.height(h - 25 - 2 - 10).width(w - 2 - 10);
        },
        setPosition:function() {
            var wEl = $(W),els = this.el.get(0).style;
            var dw = wEl.width(),dh = wEl.height();
            els.top = dh + "px";
            els.left = (dw - this.width - 1) + "px";
        },
        setIndex:function() {
            var index = 1;
            if (this.index) {
                index = this.index;
            } else {
                var toolbarEl = $I("desktop-t-default");
                index = toolbarEl.css("zIndex") - 10;
                this.index = index;
            }
            this.el.css("zIndex", index);
        },
        start:function() {
            var el = this.el,speed = this.speed;
            var dh = $(W).height(),toolbarEl = $I("desktop-t-default");
            var th = toolbarEl.outerHeight(true);
            this.setIndex();
            el.show();
            if (this.closeable === false) {
                el.animate({top:(dh - this.height - th) + "px","zIndex": this.index}, speed).delay(this.delay).animate({top:dh + "px"}, speed, $.proxy(function() {
                    this.stop();
                }, this));
            } else {
                el.animate({top:(dh - this.height - th) + "px","zIndex": this.index}, speed);
            }
        },
        stop:function(animate) {
            var A = B.MESSAGES,len = A.length;
            for (var i = 0; i < len; i++) {
                var item = A[i];
                if (item && item.id === this.id) {
                    A.splice(i, 1);
                    break;
                }
            }
            this.closeable === true && this.el.find(".message-panel-close").unbind();
            if (animate === true) {
                this.el.fadeOut(ENV.get("speed"), $.proxy(function() {
                    this.el.remove();
                }, this));
            } else {
                this.el.remove();
            }
        }
    };
    Message.prototype = FP;
    var R = {
        getAll:function() {
            return B.MESSAGES;
        },
        openMessage:function(p) {
            var A = B.MESSAGES,len = A.length;
            for (var i = 0; i < len; i++) {
                var item = A[i];
                if (p.closeable === true) {
                    item && item.stop();
                } else {
                    if (item.closeable === true) {
                        item && item.setIndex();
                        item && (p.index = item.index + 1);
                    } else {
                        item && item.stop();
                    }
                }
            }
            new Message(p);
        }
    };
    A.addModule("message", R);
})();
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
(function() {
    var A = window["athene"],W = window,D = document,PAGE = A.page,UTIL = A.util,ENV = A.env;
    var DIALOG = A.dialog,WINDOWMANAGER = A.windowmanager,LISTFIELD = A.listfield;
    PAGE.addBusiness({
        teacherManager:function (dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                width:900,
                height:400,
                title:"教师管理",
                url:"teacherList",
                initPosition:$(dom).find(".dm-icon").offset(),
                iconCls:"icon-teacher-manager",
                onSetSize:function(w, h) {
                    var grid = athene.grid.get("teacher-list-" + this.number);
                    if (grid) {
                        var sfh = this.el.find(".search-frame").outerHeight(true);
                        var sbh = this.el.find(".search-button").outerHeight(true);
                        grid.setSize(w, h - sfh - sbh);
                    }
                }
            });
        },
        teacherAdd:function(p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"新增教师",
                url:"teacherAdd",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{openerNumber:p.openerNumber},
                width:600,
                height:500,
                onClose:function() {
                    var grid = athene.grid.get("teacher-list-" + p.openerNumber);
                    if (grid)grid.refreshSubmit();
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        },
        teacherEdit:function (p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:p.title,
                url:"teacherEdit",
                initPosition:$(dom).offset(),
                data:{id:p.id,openerNumber:p.openerNumber},
                width:600,
                height:500,
                maxButton:false,
                onClose:function() {
                    var grid = athene.grid.get("teacher-list-" + p.openerNumber);
                    if (grid)grid.refreshSubmit();
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        },
        teacherView:function (p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:p.title,
                url:"teacherView",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{id:p.id,openerNumber:p.openerNumber},
                width:500,
                height:400
            });
        },
        teacherScheduleView:function (p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:p.title,
                url:"teacherScheduleView",
                initPosition:$(dom).offset(),
                data:{id:p.id},
                width:1100,
                height:500
            });
        },
        teacherScheduleExcel:function(teacherId) {
            var url = "teacherScheduleExcel";
            if (teacherId) {
                url += "?action=download";
                url += ("&id=" + teacherId);
                window.open(url);
            }
        }
    });
})();
(function() {
    var A = window["athene"],W = window,D = document,PAGE = A.page,UTIL = A.util,ENV = A.env;
    var DIALOG = A.dialog,WINDOWMANAGER = A.windowmanager,LISTFIELD = A.listfield;
    PAGE.addBusiness({
        studentManager:function (dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                width:750,
                height:450,
                title:"学生管理",
                url:"studentList",
                initPosition:$(dom).find(".dm-icon").offset(),
                iconCls:"icon-student-manager",
                onSetSize:function(w, h) {
                    var grid = athene.grid.get("student-list-" + this.number);
                    if (grid) {
                        var sfh = this.el.find(".search-frame").outerHeight(true);
                        var sbh = this.el.find(".search-button").outerHeight(true);
                        grid.setSize(w, h - sfh - sbh);
                    }
                }
            });
        },
        studentEdit:function (p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:p.title,
                url:"studentEdit",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{id:p.id,openerNumber:p.openerNumber},
                width:600,
                height:500,
                onClose:function() {
                    var grid = athene.grid.get("student-list-" + p.openerNumber);
                    grid && grid.refreshSubmit();
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        },
        studentAdd:function (p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"新增学生",
                url:"studentAdd",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{openerNumber:p.openerNumber},
                width:600,
                height:500,
                onClose:function() {
                    var grid = athene.grid.get("student-list-" + p.openerNumber);
                    grid && grid.refreshSubmit();
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        },
        studentView:function (p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:p.title,
                url:"studentView",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{id:p.id,openerNumber:p.openerNumber},
                width:500,
                height:400
            });
        },
        studentListExcel:function(a) {
            var row = $I(a).getRow();
            var url = "studentListExcel";
            if (row) {
                url += "?action=download";
                for (var k in row) {
                    url += ("&" + k + "=" + row[k]);
                }
            }
            window.open(url);
        }
    });
})();
(function() {
    var A = window["athene"],W = window,D = document,PAGE = A.page,UTIL = A.util,ENV = A.env;
    var DIALOG = A.dialog,WINDOWMANAGER = A.windowmanager,LISTFIELD = A.listfield;
    PAGE.addBusiness({
        lessonManager:function (dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                width:800,
                height:500,
                title:"课程管理",
                url:"lessonList",
                initPosition:$(dom).find(".dm-icon").offset(),
                iconCls:"icon-lesson-manager",
                onSetSize:function(w, h) {
                    var grid = athene.grid.get("lesson-list-" + this.number);
                    if (grid) {
                        var sfh = this.el.find(".search-frame").outerHeight(true);
                        var sbh = this.el.find(".search-button").outerHeight(true);
                        grid.setSize(w, h - sfh - sbh);
                    }
                }
            });
        },
        lessonAdd:function(p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"新增课程",
                url:"lessonAdd",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{openerNumber:p.openerNumber},
                width:600,
                height:500,
                onClose:function() {
                    var grid = athene.grid.get("lesson-list-" + p.openerNumber);
                    grid && grid.refreshSubmit();
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        },
        lessonEdit:function(p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"编辑课程",
                url:"lessonEdit",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{id:p.id,openerNumber:p.openerNumber},
                width:600,
                height:500,
                onClose:function() {
                    var grid = athene.grid.get("lesson-list-" + p.openerNumber);
                    grid && grid.refreshSubmit();
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        },
        lessonTeacher:function(p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"选择教师",
                url:"lessonTeacher",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{openerNumber:p.openerNumber},
                width:600,
                height:500
            });
        },
        lessonTeacherSelect:function(p) {
            var rows = LISTFIELD.get("teacher-name-list-" + p.number).getItems();
            if (rows.length == 0) {
                $I("teacher-name-list-" + p.number + "-error").show().find(".fm-error-content").html("请选择一个教师");
                return;
            }
            var win = A.window.get(p.number);
            win.onClose = function() {
                var listfield = LISTFIELD.get("teacher-name-list-" + p.openerNumber);
                listfield && listfield.setItems(rows);
            };
            win.close();
        },
        lessonStudent:function(p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"选择学生",
                url:"lessonStudent",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{openerNumber:p.openerNumber},
                width:600,
                height:500
            });
        },
        lessonStudentSelect:function(p) {
            var rows = LISTFIELD.get("student-name-list-" + p.number).getItems();
            if (rows.length == 0) {
                $I("student-name-list-" + p.number + "-error").show().find(".fm-error-content").html("请选择一个学生");
                return;
            }
            var win = A.window.get(p.number);
            win.onClose = function() {
                var listfield = LISTFIELD.get("student-name-list-" + p.openerNumber);
                listfield && listfield.setItems(rows);
            };
            win.close();
        },
        lessonPlanAdd:function(p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"新增课程安排",
                url:"lessonPlanAdd",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{openerNumber:p.openerNumber},
                width:350,
                height:300
            });
        },
        lessonDayStatistics:function(dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"课程统计分布",
                url:"lessonDayStatistics",
                initPosition:$(dom).offset(),
                width:1235,
                height:500
            });
        },
        lessonPlanEdit:function(p, dom) {
            var row = A.grid.get(p.gridId).items[p.index];
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"编辑课程安排",
                url:"lessonPlanEdit",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{lessonPlan:row,index:p.index,openerNumber:p.openerNumber},
                width:350,
                height:300
            });
        }
    });
})();
(function() {
    var A = window["athene"],W = window,D = document,PAGE = A.page,UTIL = A.util,ENV = A.env;
    var DIALOG = A.dialog,WINDOWMANAGER = A.windowmanager,LISTFIELD = A.listfield;
    PAGE.addBusiness({
        expenseManager:function (dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                width:800,
                height:500,
                title:"支出管理",
                url:"expenseList",
                iconCls:"icon-expense-manager",
                initPosition:$(dom).find(".dm-icon").offset(),
                onSetHtml:function() {
                    PAGE.querySimpleTodayAccountStatistics(this.number);
                },
                onSetSize:function(w, h) {
                    var grid = athene.grid.get("expense-list-" + this.number);
                    if (grid) {
                        var sfh = this.el.find(".search-frame").outerHeight(true);
                        var sbh = this.el.find(".search-button").outerHeight(true);
                        var ash = this.el.find(".account-statistics-view").outerHeight(true);
                        grid.setSize(w, h - sfh - sbh - ash);
                    }
                }
            });
        },
        expenseAdd:function(p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"新增支出",
                url:"expenseAdd",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{openerNumber:p.openerNumber},
                width:350,
                height:400,
                onClose:function() {
                    var grid = athene.grid.get("expense-list-" + p.openerNumber);
                    grid && grid.refreshSubmit();
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        },
        expenseEdit:function (p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:p.title,
                url:"expenseEdit",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{id:p.id,openerNumber:p.openerNumber},
                width:350,
                height:400,
                onClose:function() {
                    var grid = athene.grid.get("expense-list-" + p.openerNumber);
                    grid && grid.refreshSubmit();
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        },
        expenseListExcel:function(a) {
            var row = $I(a).getRow();
            var url = "expenseListExcel";
            if (row) {
                url += "?action=download";
                for (var k in row) {
                    url += ("&" + k + "=" + row[k]);
                }
            }
            window.open(url);
        }
    });
})();
(function() {
    var A = window["athene"],W = window,D = document,PAGE = A.page,UTIL = A.util,ENV = A.env;
    var DIALOG = A.dialog,WINDOWMANAGER = A.windowmanager,LISTFIELD = A.listfield;
    PAGE.addBusiness({
        incomeManager:function (dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                width:800,
                height:500,
                title:"收入管理",
                url:"incomeList",
                iconCls:"icon-income-manager",
                initPosition:$(dom).find(".dm-icon").offset(),
                onSetHtml:function() {
                    PAGE.querySimpleTodayAccountStatistics(this.number);
                },
                onSetSize:function(w, h) {
                    var grid = athene.grid.get("income-list-" + this.number);
                    if (grid) {
                        var sfh = this.el.find(".search-frame").outerHeight(true);
                        var sbh = this.el.find(".search-button").outerHeight(true);
                        var ash = this.el.find(".account-statistics-view").outerHeight(true);
                        grid.setSize(w, h - sfh - sbh - ash);
                    }
                }
            });
        },
        incomeAdd:function(p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"新增收入",
                url:"incomeAdd",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{openerNumber:p.openerNumber},
                width:350,
                height:400,
                onClose:function() {
                    var grid = athene.grid.get("income-list-" + p.openerNumber);
                    grid && grid.refreshSubmit();
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        },
        incomeEdit:function (p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:p.title,
                url:"incomeEdit",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{id:p.id,openerNumber:p.openerNumber},
                width:350,
                height:400,
                onClose:function() {
                    var grid = athene.grid.get("income-list-" + p.openerNumber);
                    grid && grid.refreshSubmit();
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        },
        incomeListExcel:function(a) {
            var row = $I(a).getRow();
            var url = "incomeListExcel";
            if (row) {
                url += "?action=download";
                for (var k in row) {
                    url += ("&" + k + "=" + row[k]);
                }
            }
            window.open(url);
        }
    });
})();
(function() {
    var A = window["athene"],W = window,D = document,PAGE = A.page,UTIL = A.util,ENV = A.env;
    var DIALOG = A.dialog,WINDOWMANAGER = A.windowmanager,LISTFIELD = A.listfield;
    PAGE.addBusiness({
        taskManager:function (dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                width:800,
                height:400,
                title:"任务管理",
                url:"taskList",
                iconCls:"icon-task-manager",
                initPosition:$(dom).find(".dm-icon").offset(),
                onSetSize:function(w, h) {
                    var grid = athene.grid.get("task-list-" + this.number);
                    if (grid) {
                        var sfh = this.el.find(".search-frame").outerHeight(true);
                        var sbh = this.el.find(".search-button").outerHeight(true);
                        grid.setSize(w, h - sfh - sbh);
                    }
                }
            });
        },
        taskAdd:function(p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"新增任务",
                url:"taskAdd",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{openerNumber:p.openerNumber},
                width:400,
                height:400,
                onClose:function() {
                    var grid = athene.grid.get("task-list-" + p.openerNumber);
                    grid && grid.refreshSubmit();
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        },
        taskEdit:function (p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:p.title,
                url:"taskEdit",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{id:p.id,openerNumber:p.openerNumber},
                width:400,
                height:400,
                onClose:function() {
                    var grid = athene.grid.get("task-list-" + p.openerNumber);
                    grid && grid.refreshSubmit();
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        }
    });
})();
(function() {
    var A = window["athene"],W = window,D = document,PAGE = A.page,UTIL = A.util,ENV = A.env;
    var DIALOG = A.dialog,WINDOWMANAGER = A.windowmanager,LISTFIELD = A.listfield;
    PAGE.addBusiness({
        accountStatisticsManager:function (dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                width:550,
                height:500,
                title:"账户查询",
                url:"accountStatisticsList",
                initPosition:$(dom).find(".dm-icon").offset(),
                iconCls:"icon-account-statistics-manager",
                onSetHtml:function() {
                    PAGE.queryTodayAccountStatistics(this.number);
                },
                onSetSize:function(w, h) {
                    var grid = athene.grid.get("account-statistics-list-" + this.number);
                    if (grid) {
                        var sfh = this.el.find(".search-frame").outerHeight(true);
                        var sbh = this.el.find(".search-button").outerHeight(true);
                        var ash = this.el.find(".account-statistics-view").outerHeight(true);
                        grid.setSize(w, h - sfh - sbh - ash);
                    }
                }
            });
        },
        querySimpleTodayAccountStatistics :function (number) {
            var el = $I("simple-account-statistics-today-" + number);
            $.ajax({
                type: "POST",
                url: "accountStatisticsTodaySimpleView",
                cache:false,
                dataType:"html",
                beforeSend:function() {
                    el.html("<img style='margin:3px 5px;' src='images/mask/loading.gif'>");
                    var win = athene.window.get(number);
                    win && win.onSetSize && win.onSetSize.call(win, win.width, win.height);
                },
                success: function(d) {
                    el.empty().append(d);
                    var win = athene.window.get(number);
                    win && win.onSetSize && win.onSetSize.call(win, win.width, win.height);
                }
            });
        },
        queryTodayAccountStatistics :function (number) {
            var el = $I("account-statistics-today-" + number);
            $.ajax({
                type: "POST",
                url: "accountStatisticsTodayView",
                cache:false,
                dataType:"html",
                beforeSend:function() {
                    el.html("<img style='margin-left:5px;' src='images/mask/loading.gif'>");
                    var win = athene.window.get(number);
                    win && win.onSetSize && win.onSetSize.call(win, win.width, win.height);
                },
                success: function(d) {
                    el.html(d);
                    var win = athene.window.get(number);
                    win && win.onSetSize && win.onSetSize.call(win, win.width, win.height);
                }
            });
        },
        accountStatisticsListExcel:function(a) {
            var row = $I(a).getRow();
            var url = "accountStatisticsListExcel";
            if (row) {
                url += "?action=download";
                for (var k in row) {
                    url += ("&" + k + "=" + row[k]);
                }
            }
            window.open(url);
        },
        accountStatisticsGenerate:function(number) {
            DIALOG.confirm("确认重新生成统计账目？", function(flag) {
                if (flag === false)return;
                $.ajax({
                    type: "POST",
                    context:this,
                    url: "accountStatisticsGenerate",
                    cache:false,
                    dataType:"json",
                    beforeSend:function() {
                        this.showMask("正在生成统计账目，请稍等...");
                    },
                    success: function(d) {
                        this.hideMask(function() {
                            if (d.returnCode === 3) {
                                athene.dialog.alertException(d.exceptionDesc);
                            } else {
                                athene.message.openMessage({message:"重新生成统计账目成功"});
                            }
                        });
                    }
                });
            }, this, 300);
        }
    });
})();
