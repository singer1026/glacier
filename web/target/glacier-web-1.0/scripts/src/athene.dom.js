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