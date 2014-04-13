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