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