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