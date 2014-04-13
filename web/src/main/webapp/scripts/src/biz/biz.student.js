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