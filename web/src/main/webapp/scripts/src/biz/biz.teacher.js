(function() {
    var A = window["athene"],W = window,D = document,PAGE = A.page,UTIL = A.util,ENV = A.env;
    var DIALOG = A.dialog,WINDOWMANAGER = A.windowmanager,LISTFIELD = A.listfield;
    PAGE.addBusiness({
        teacherManager:function (dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                width:900,
                height:400,
                title:"教师管理",
                url:"teacherList",
                initPosition:$(dom).find(".dm-icon").offset(),
                iconCls:"icon-teacher-manager",
                onSetSize:function(w, h) {
                    var grid = athene.grid.get("teacher-list-" + this.number);
                    if (grid) {
                        var sfh = this.el.find(".search-frame").outerHeight(true);
                        var sbh = this.el.find(".search-button").outerHeight(true);
                        grid.setSize(w, h - sfh - sbh);
                    }
                }
            });
        },
        teacherAdd:function(p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"新增教师",
                url:"teacherAdd",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{openerNumber:p.openerNumber},
                width:600,
                height:500,
                onClose:function() {
                    var grid = athene.grid.get("teacher-list-" + p.openerNumber);
                    if (grid)grid.refreshSubmit();
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        },
        teacherEdit:function (p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:p.title,
                url:"teacherEdit",
                initPosition:$(dom).offset(),
                data:{id:p.id,openerNumber:p.openerNumber},
                width:600,
                height:500,
                maxButton:false,
                onClose:function() {
                    var grid = athene.grid.get("teacher-list-" + p.openerNumber);
                    if (grid)grid.refreshSubmit();
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        },
        teacherView:function (p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:p.title,
                url:"teacherView",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{id:p.id,openerNumber:p.openerNumber},
                width:500,
                height:400
            });
        },
        teacherScheduleView:function (p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:p.title,
                url:"teacherScheduleView",
                initPosition:$(dom).offset(),
                data:{id:p.id},
                width:1100,
                height:500
            });
        },
        teacherScheduleExcel:function(teacherId) {
            var url = "teacherScheduleExcel";
            if (teacherId) {
                url += "?action=download";
                url += ("&id=" + teacherId);
                window.open(url);
            }
        }
    });
})();