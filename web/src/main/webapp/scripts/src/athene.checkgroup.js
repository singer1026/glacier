window["athene"]["checkgroup"] || (function() {
    var A = window["athene"],W = window,D = document,DOM = A.dom,ENV = A.env,UTIL = A.util;
    var B = {
        checkGroups:[]
    };
    var CheckGroup = function(p) {
        this._constructor(p);
        this._init();
        this._render();
        this._bindEvent();
    };
    CheckGroup.prototype = {
        _constructor:function(p) {
            if (!p.id)throw Error("CheckGroup控件必须设置id");
            if (!p.binding)throw Error("CheckGroup控件必须设置binding");
            if (!p.listKey)throw Error("CheckGroup控件必须设置listKey");
            if (!p.listValue)throw Error("CheckGroup控件必须设置listValue");
            B.checkGroups.push(this);
            this.items = [];
            this.contentWidth = 65;
            this.selectItems = [];
            for (var k in p) {
                this[k] = p[k];
            }
        },
        _init:function() {
            this.changed = false;
            this.xtype = "checkgroup";
            this.el = $I(this.id);
            this.el.closeSelect();
            A.window.get(this.binding).addItem(this);
            DOM.addClass(this.el.get(0), "checkgroup-default");
        },
        _render:function() {
            var html = [];
            html.push("<div class='checkgroup-wrap'><div class='clear'></div></div>");
            this.el.html(html.join(""));
            this.items.length > 0 && this._renderItems();
        },
        _renderItems:function() {
            var html = []
            var items = this.items,len = items.length;
            for (var i = 0; i < len; i++) {
                var item = items[i];
                html.push(this._generateItem(i, item));
            }
            html.push("<div class='clear'></div>");
            this.el.find(".checkgroup-wrap").html(html.join(""));
        },
        _generateItem:function(i, item) {
            var html = [],listKey = this.listKey,content = item[listKey] || "";
            var listValue = this.listValue,value = item[listValue];
            var isCheck = this._isChecked(item);
            html.push("<div class='checkgroup-item'><div class='checkgroup-item-icon-wrap'>");
            html.push("<div class='checkgroup-item-icon");
            isCheck && html.push(" checkgroup-item-icon-checked");
            html.push("' biz_value='" + value + "'");
            if (isCheck) {
                html.push(" biz_status='checked'");
            } else {
                html.push(" biz_status='uncheck'");
            }
            html.push("></div></div><div class='checkgroup-item-content' style='width:");
            html.push(this.contentWidth + "px;'>" + content + "</div></div>");
            return html.join("");
        },
        _bindEvent:function() {
            this.el.find(".checkgroup-wrap").click($.proxy(function(e) {
                var tar = e.target,cls = tar.className;
                if (cls.contains("checkgroup-item-icon") || cls.contains("checkgroup-item-content")) {
                    var dom = null;
                    if (cls.contains("checkgroup-item-content")) {
                        var el = $(tar).parent().find(".checkgroup-item-icon");
                        el.length > 0 && (dom = el.get(0));
                    } else {
                        dom = tar;
                    }
                    if (dom) {
                        var value = dom.getAttribute("biz_value");
                        var status = dom.getAttribute("biz_status");
                        if (value && status) {
                            var item = this.getItem(value);
                            if (item) {
                                if (status === "checked") {
                                    this.removeSelectItem(item);
                                } else if (status === "uncheck") {
                                    this.addSelectItem(item);
                                }
                            }
                        }
                    }
                }
            }, this));
        },
        _isChecked:function(item) {
            var listValue = this.listValue;
            var selectItems = this.selectItems,len = selectItems.length;
            for (var i = 0; i < len; i++) {
                var selectItem = selectItems[i];
                if (selectItem[listValue] === item[listValue]) return true;
            }
            return false;
        },
        getItem:function(value) {
            if (value == undefined || this.items.length == 0)return null;
            var listValue = this.listValue;
            var items = this.items,len = items.length;
            for (var i = 0; i < len; i++) {
                var item = items[i];
                if (item[listValue] == value) {
                    return item;
                }
            }
            return null;
        },
        addSelectItem:function(item) {
            if (item) {
                this.selectItems.push(item);
                var value = item[this.listValue];
                var el = this.el.find("div.checkgroup-item-icon[biz_value='" + value + "']");
                if (el.length > 0) {
                    DOM.addClass(el.get(0), "checkgroup-item-icon-checked");
                    this.changed = true;
                    el.attr("biz_status", "checked");
                    this.onAddSelectItem && this.onAddSelectItem.call(this, item);
                }
            }
            return this;
        },
        removeSelectItem:function(item) {
            var removeItem = undefined;
            if (item) {
                var listValue = this.listValue;
                var selectItems = this.selectItems,len = selectItems.length;
                for (var i = 0; i < len; i++) {
                    var selectItem = selectItems[i];
                    if (selectItem[listValue] === item[listValue]) {
                        removeItem = selectItem;
                        selectItems.splice(i, 1);
                        break;
                    }
                }
                if (removeItem) {
                    var value = removeItem[listValue];
                    var el = this.el.find("div.checkgroup-item-icon[biz_value='" + value + "']");
                    if (el.length > 0) {
                        DOM.removeClass(el.get(0), "checkgroup-item-icon-checked");
                        this.changed = true;
                        el.attr("biz_status", "uncheck");
                        this.onRemoveSelectItem && this.onRemoveSelectItem.call(this, removeItem);
                    }
                }
            }
            return this;
        },
        _destory:function() {
            var checkGroups = B.checkGroups,len = checkGroups.length;
            for (var i = 0; i < len; i++) {
                var item = checkGroups[i];
                if (this.id === item.id) {
                    checkGroups.splice(i, 1);
                    break;
                }
            }
            this.el.find(".checkgroup-wrap").unbind();
            this.el.remove();
        }
    };
    var F = {
        create:function(p) {
            var checkGroup = new CheckGroup(p);
            return {widgetId:checkGroup.id,xtype:"checkgroup"};
        },
        get:function(id) {
            var checkGroups = B.checkGroups,len = checkGroups.length;
            for (var i = 0; i < len; i++) {
                var item = checkGroups[i];
                if (id === item.id) {
                    return item;
                }
            }
            return null;
        },
        getAll:function() {
            return B.checkGroups;
        }
    };
    A.addModule("checkgroup", F);
})();