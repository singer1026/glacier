(function() {
    var A = window["athene"],W = window,D = document,PAGE = A.page,UTIL = A.util,ENV = A.env;
    var DIALOG = A.dialog,WINDOWMANAGER = A.windowmanager,LISTFIELD = A.listfield;
    PAGE.addBusiness({
        taskManager:function (dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                width:800,
                height:400,
                title:"任务管理",
                url:"taskList",
                iconCls:"icon-task-manager",
                initPosition:$(dom).find(".dm-icon").offset(),
                onSetSize:function(w, h) {
                    var grid = athene.grid.get("task-list-" + this.number);
                    if (grid) {
                        var sfh = this.el.find(".search-frame").outerHeight(true);
                        var sbh = this.el.find(".search-button").outerHeight(true);
                        grid.setSize(w, h - sfh - sbh);
                    }
                }
            });
        },
        taskAdd:function(p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:"新增任务",
                url:"taskAdd",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{openerNumber:p.openerNumber},
                width:400,
                height:400,
                onClose:function() {
                    var grid = athene.grid.get("task-list-" + p.openerNumber);
                    grid && grid.refreshSubmit();
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        },
        taskEdit:function (p, dom) {
            WINDOWMANAGER.get("window-manager-default").openWindow({
                title:p.title,
                url:"taskEdit",
                initPosition:$(dom).offset(),
                maxButton:false,
                data:{id:p.id,openerNumber:p.openerNumber},
                width:400,
                height:400,
                onClose:function() {
                    var grid = athene.grid.get("task-list-" + p.openerNumber);
                    grid && grid.refreshSubmit();
                    if (this.floatMessage) {
                        athene.message.openMessage({message:this.floatMessage});
                    }
                }
            });
        }
    });
})();