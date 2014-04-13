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