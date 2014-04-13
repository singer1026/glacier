window["athene"]["form"] || (function() {
    var A = window["athene"],W = window,D = document;
    var B = {
        forms:[]
    };
    var Form = function(p) {
        this._constructor(p);
        this._init();
    };
    var FP = {
        _constructor:function(p) {
            B.forms.push(this);
            for (var k in p) {
                this[k] = p[k];
            }
        },
        _init:function() {
            A.window.get(this.binding).addItem(this);
        },
        getRow:function() {
            if (!this.items) throw Error("数组定义有错误");
            var formEl = $I(this.id),o = {};
            var items = this.items,len = items.length;
            for (var i = 0; i < len; i++) {
                var item = items[i],type = item.type;
                if (type === "Grid") {
                    var grid = A.grid.get(item.id);
                    if (grid && grid.name && grid.items.length > 0) {
                        var name = grid.name;
                        o[name] = grid.items;
                        o[name + "Changed"] = grid.changed;
                    }
                } else if (type === "ListField") {
                    var listfield = A.listfield.get(item.id);
                    if (listfield && listfield.name && listfield.items.length > 0) {
                        var name = listfield.name;
                        o[name] = listfield.items;
                        o[name + "Changed"] = listfield.changed;
                    }
                } else if (type === "CheckGroup") {
                    var checkGroup = A.checkgroup.get(item.id);
                    if (checkGroup && checkGroup.name && checkGroup.selectItems.length > 0) {
                        var name = checkGroup.name;
                        o[name] = checkGroup.selectItems;
                        o[name + "Changed"] = checkGroup.changed;
                    }
                } else {
                    var el = formEl.find("#" + item.id);
                    if (el.length > 0) {
                        var dom = el.get(0);
                        if (dom && dom.name && dom.value) {
                            o[dom.name] = dom.value;
                        }
                    }
                }
            }
            return o;
        },
        getConfirmRow:function() {
            if (!this.items) throw Error("数组定义有错误");
            var formEl = $I(this.id),html = [];
            var items = this.items,len = items.length;
            for (var i = 0; i < len; i++) {
                var item = items[i];
                var label = item.label,type = item.type;
                if (!label || !type)continue;
                if (type === "Grid") {
                    var grid = A.grid.get(item.id);
                    if (grid && grid.name && grid.items.length > 0) {
                        html.push("<div class='fwb form-label'>" + item.label + "：</div>");
                        var gridItems = grid.items,gridLen = gridItems.length;
                        for (var k = 0; k < gridLen; k++) {
                            html.push("<div class='ti2'>");
                            var gridItem = gridItems[k];
                            var colModels = grid.colModels,colModelsLen = colModels.length;
                            for (var j = 0; j < colModelsLen; j++) {
                                var colModel = colModels[j];
                                var key = colModel.key;
                                if (key && gridItem[key]) {
                                    var value = gridItem[key];
                                    html.push(value + " ");
                                }
                            }
                            html.push("</div>");
                        }
                    }
                } else if (type === "CheckGroup") {
                    var checkgroup = A.checkgroup.get(item.id);
                    if (checkgroup && checkgroup.name && checkgroup.selectItems.length > 0) {
                        var selectItems = checkgroup.selectItems,selectLen = selectItems.length;
                        html.push("<div class='fwb form-label'>" + item.label + "：</div>");
                        html.push("<div class='ti2'>");
                        for (var k = 0; k < selectLen; k++) {
                            var checkgroupItem = selectItems[k];
                            html.push(checkgroupItem[checkgroup.listKey] + " ");
                            if ((k + 1) % 5 === 0 && (k + 1) !== selectLen) {
                                html.push("</div><div class='ti2'>");
                            }
                        }
                        html.push("</div>");
                    }
                } else if (type === "ListField") {
                    var listfield = A.listfield.get(item.id);
                    if (listfield && listfield.name && listfield.items.length > 0) {
                        var listKey = listfield.listKey;
                        var listfieldItems = listfield.items,listfieldLen = listfieldItems.length;
                        html.push("<div class='fwb form-label'>" + item.label + "：</div>");
                        html.push("<div class='ti2'>");
                        for (var k = 0; k < listfieldLen; k++) {
                            var listfieldItem = listfieldItems[k];
                            html.push(listfieldItem[listKey] + " ");
                            if ((k + 1) % 5 === 0 && (k + 1) !== listfieldLen) {
                                html.push("</div><div class='ti2'>");
                            }
                        }
                        html.push("</div>");
                    }
                } else if (type === "ComboBox") {
                    var el = formEl.find("#" + item.id);
                    if (el.length > 0) {
                        var dom = el.get(0);
                        if (dom && dom.name && dom.value) {
                            var optionEl = el.children("option[value='" + dom.value + "']");
                            html.push("<div><span class='fwb form-label'>" + item.label + "：</span>");
                            html.push("<span>" + optionEl.text() + "</span></div>");
                        }
                    }
                }
                else {
                    var el = formEl.find("#" + item.id);
                    if (el.length > 0) {
                        var dom = el.get(0);
                        if (dom && dom.name && dom.value) {
                            html.push("<div><span class='fwb form-label'>" + item.label + "：</span>");
                            html.push("<span>" + dom.value + "</span></div>");
                        }
                    }
                }
            }
            return html.join("");
        },
        save:function() {
            this.handler("saveUrl", "messageSave", "onSave");
        },
        update:function() {
            this.handler("updateUrl", "messageUpdate", "onUpdate");
        },
        remove:function() {
            this.handler("removeUrl", "messageRemove", "onRemove");
        },
        handler:function(u, b, after) {
            if (!u || !this[u]) throw Error("url未定义");
            var container = $I(this.id),url = this[u];
            var row = this.getRow(),confirmRow = this.getConfirmRow();
            var data = {number:this.binding,args:json(row)};
            var request = function(flag) {
                if (flag === false)return;
                $.ajax({
                    url: url,
                    context:this,
                    data:data,
                    beforeSend:function() {
                        container.find(".fm-error").hide().find(".fm-error-content").empty();
                        this.getBindingWindow().showMask("正在提交数据，请稍等...");
                    },
                    dataType:"json",
                    success: function(d) {
                        if (d.returnCode === 0) {
                            if (this.onSuccess) {
                                this.onSuccess.call(this, d);
                            }
                            var handler = this[after];
                            if (handler && typeof(handler) === "function") {
                                handler.call(this, row, d);
                            }
                        } else if (d.returnCode === 1) {
                            var list = d.validationErrors,len = list.length;
                            for (var i = 0; i < len; i++) {
                                var item = list[i];
                                var el = container.find("#" + item.id);
                                el.show().find(".fm-error-content").html(item.message);
                            }
                        } else if (d.returnCode === 4 || d.returnCode === 2) {
                            A.dialog.error(d.exceptionDesc);
                        } else {
                            A.dialog.alertException(d.exceptionDesc);
                        }
                    },
                    complete:function() {
                        this.getBindingWindow().hideMask();
                    }
                });
            };
            if (b && this[b]) {
                A.dialog.confirm(this[b](confirmRow, row), request, this, this.dialogWidth);
            } else {
                request.call(this);
            }
        },
        getBindingWindow:function() {
            return A.window.get(this.binding);
        },
        _destory:function() {
            var forms = B.forms,len = forms.length;
            for (var i = 0; i < len; i++) {
                var item = forms[i];
                if (this.id === item.id) {
                    forms.splice(i, 1);
                }
            }
        }
    };
    Form.prototype = FP;
    var F = {
        create:function(p) {
            var form = new Form(p);
            return {widgetId:form.id,xtype:"form"};
        },
        get:function(id) {
            var forms = B.forms,len = forms.length;
            for (var i = 0; i < len; i++) {
                var item = forms[i];
                if (id === item.id) {
                    return item;
                }
            }
            return null;
        },
        getAll:function() {
            return B.forms;
        }
    };
    A.addModule("form", F);
})();