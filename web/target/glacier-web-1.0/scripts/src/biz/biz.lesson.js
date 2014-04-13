(function() {
    var A = window["athene"],W = window,D = document,PAGE = A.page,UTIL = A.util,ENV = A.env;
    var DIALOG = A.dialog,WINDOWMANAGER = A.windowmanager,LISTFIELD = A.listfield;
    PAGE.addBusiness({
        lessonManager:function (dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                width:800,
                height:500,
                title:"课程管理",
                url:"lessonList",
                initPosition:$(dom).find(".dm-icon").offset(),
                iconCls:"icon-lesson-manager",
                onSetSize:function(w, h) {
                    var grid = athene.grid.get("lesson-list-" + this.number);
                    if (grid) {
                        var sfh = this.el.find(".search-frame").outerHeight(true);
                        var sbh = this.el.find(".search-button").outerHeight(true);
                        grid.setSize(w, h - sfh - sbh);
                    }
                }
            });
        },
        lessonAdd:function(p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"新增课程",
                url:"lessonAdd",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{openerNumber:p.openerNumber},
                width:600,
                height:500,
                onClose:function() {
                    var grid = athene.grid.get("lesson-list-" + p.openerNumber);
                    grid && grid.refreshSubmit();
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        },
        lessonEdit:function(p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"编辑课程",
                url:"lessonEdit",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{id:p.id,openerNumber:p.openerNumber},
                width:600,
                height:500,
                onClose:function() {
                    var grid = athene.grid.get("lesson-list-" + p.openerNumber);
                    grid && grid.refreshSubmit();
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        },
        lessonTeacher:function(p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"选择教师",
                url:"lessonTeacher",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{openerNumber:p.openerNumber},
                width:600,
                height:500
            });
        },
        lessonTeacherSelect:function(p) {
            var rows = LISTFIELD.get("teacher-name-list-" + p.number).getItems();
            if (rows.length == 0) {
                $I("teacher-name-list-" + p.number + "-error").show().find(".fm-error-content").html("请选择一个教师");
                return;
            }
            var win = A.window.get(p.number);
            win.onClose = function() {
                var listfield = LISTFIELD.get("teacher-name-list-" + p.openerNumber);
                listfield && listfield.setItems(rows);
            };
            win.close();
        },
        lessonStudent:function(p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"选择学生",
                url:"lessonStudent",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{openerNumber:p.openerNumber},
                width:600,
                height:500
            });
        },
        lessonStudentSelect:function(p) {
            var rows = LISTFIELD.get("student-name-list-" + p.number).getItems();
            if (rows.length == 0) {
                $I("student-name-list-" + p.number + "-error").show().find(".fm-error-content").html("请选择一个学生");
                return;
            }
            var win = A.window.get(p.number);
            win.onClose = function() {
                var listfield = LISTFIELD.get("student-name-list-" + p.openerNumber);
                listfield && listfield.setItems(rows);
            };
            win.close();
        },
        lessonPlanAdd:function(p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"新增课程安排",
                url:"lessonPlanAdd",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{openerNumber:p.openerNumber},
                width:350,
                height:300
            });
        },
        lessonDayStatistics:function(dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"课程统计分布",
                url:"lessonDayStatistics",
                initPosition:$(dom).offset(),
                width:1235,
                height:500
            });
        },
        lessonPlanEdit:function(p, dom) {
            var row = A.grid.get(p.gridId).items[p.index];
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"编辑课程安排",
                url:"lessonPlanEdit",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{lessonPlan:row,index:p.index,openerNumber:p.openerNumber},
                width:350,
                height:300
            });
        }
    });
})();