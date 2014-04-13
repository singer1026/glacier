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