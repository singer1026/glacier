window["athene"]["listfield"] || (function() {
    var A = window["athene"],W = window,D = document,DOM = A.dom,ENV = A.env,UTIL = A.util;
    var B = {
        listFields:[]
    };
    var ListField = function(p) {
        this._constructor(p);
        this._init();
        this._render();
        this._bindEvent();
    };
    var LP = {
        _constructor:function(p) {
            if (!p.id)throw Error("ListField控件必须设置id");
            if (!p.binding)throw Error("ListField控件必须设置binding");
            if (!p.listKey)throw Error("ListField控件必须设置listKey");
            if (!p.listValue)throw Error("ListField控件必须设置listValue");
            B.listFields.push(this);
            this.items = [];
            this.removeable = true;
            this.contentWidth = 66;
            for (var k in p) {
                this[k] = p[k];
            }
        },
        _init:function() {
            this.xtype = "listfield";
            this.changed = false;
            this.el = $I(this.id);
            A.window.get(this.binding).addItem(this);
        },
        _render:function() {
            var arr = [];
            arr.push("<fieldset class='listfield-default'><legend>");
            arr.push(this.title || "");
            arr.push("</legend><div class='listfield-wrap'><div class='clear'></div></div></fieldset>");
            this.el.append(arr.join(""));
            this.items.length > 0 && this._renderItems();
        },
        _bindEvent:function() {
            this.el.find(".listfield-wrap").click($.proxy(function(e) {
                var tar = e.target,cls = tar.className;
                if (cls.contains("icon-remove")) {
                    var value = tar.getAttribute("biz_value");
                    this.removeItem(value);
                }
            }, this));
        },
        _renderItems:function() {
            var html = [],items = this.items,len = items.length;
            for (var i = 0; i < len; i++) {
                var item = items[i];
                html.push(this._generateItem(i, item));
            }
            html.push("<div class='clear'></div>");
            this.el.children("fieldset").children("div").html(html.join(""));
        },
        _generateItem:function(i, item) {
            var html = [];
            html.push("<div class='listfield-item'>");
            html.push("<div class='listfield-item-content' style='width:" + this.contentWidth + "px;'>");
            if (this.formatter) {
                html.push(this.formatter.call(this, i, item) || "");
            } else {
                html.push(item[this.listKey] || "");
            }
            html.push("</div>");
            if (this.removeable === true) {
                var value = item[this.listValue];
                html.push("<div class='listfield-item-icon-wrap'><div class='listfield-item-icon icon-remove cp'");
                html.push(" biz_value='" + value + "'/></div></div>");
            }
            html.push("</div>");
            return html.join("");
        },
        setItems:function(p) {
            if (p instanceof Array && p.length > 0) {
                var listFields = p,len = listFields.length;
                this.items = [];
                for (var i = 0; i < len; i++) {
                    this.items.push(listFields[i]);
                }
                this._renderItems();
                this.changed = true;
                this.onSetItems && this.onSetItems.call(this, p);
            }
            return this;
        },
        getItems:function() {
            return this.items;
        },
        clearItems:function() {
            if (this.items.length > 0) {
                this.items = [];
                this._renderItems();
                this.changed = true;
                this.onClearItems && this.onClearItems.call(this);
            }
            return this;
        },
        addItem:function(item) {
            if (item) {
                var listValue = this.listValue;
                var items = this.items,len = items.length;
                for (var i = 0; i < len; i++) {
                    var existItem = items[i];
                    if (existItem[listValue] === item[listValue]) {
                        return;
                    }
                }
                items.push(item);
                var html = this._generateItem(len, item);
                this.el.find(".listfield-wrap").children(".clear").before(html);
                this.changed = true;
                this.onAddItem && this.onAddItem.call(this, item);
            }
            return this;
        },
        removeItem:function(value) {
            if (value == undefined || this.items.length == 0)return this;
            var removeItem = undefined,listValue = this.listValue;
            var items = this.items,len = items.length;
            for (var i = 0; i < len; i++) {
                var item = items[i];
                if (typeof(value) === "object") {
                    if (item[listValue] === value[listValue]) {
                        removeItem = item;
                        items.splice(i, 1);
                        break;
                    }
                } else {
                    if (item[listValue] == value) {
                        removeItem = item;
                        items.splice(i, 1);
                        break;
                    }
                }
            }
            if (removeItem) {
                this._renderItems();
                this.changed = true;
                this.onRemoveItem && this.onRemoveItem.call(this, removeItem);
            }
            return this;
        },
        _destory:function() {
            var listFields = B.listFields,len = listFields.length;
            for (var i = 0; i < len; i++) {
                var item = listFields[i];
                if (this.id === item.id) {
                    listFields.splice(i, 1);
                    break;
                }
            }
            this.el.find(".listfield-wrap").unbind();
            this.el.remove();
        }
    };
    ListField.prototype = LP;
    var F = {
        create:function(p) {
            var listField = new ListField(p);
            return {widgetId:listField.id,xtype:"listfield"};
        },
        get:function(id) {
            var listFields = B.listFields,len = listFields.length;
            for (var i = 0; i < len; i++) {
                var item = listFields[i];
                if (id === item.id) {
                    return item;
                }
            }
            return null;
        },
        getAll:function() {
            return B.listFields;
        }
    };
    A.addModule("listfield", F);
})();