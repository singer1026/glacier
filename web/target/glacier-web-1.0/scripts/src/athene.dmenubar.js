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