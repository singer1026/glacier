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