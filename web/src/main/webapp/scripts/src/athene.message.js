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