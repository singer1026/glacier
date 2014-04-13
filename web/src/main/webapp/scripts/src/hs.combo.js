window["athene"]["combo"] || (function() {
    var A = window["athene"],W = window,D = document,ENV = A.env,UTIL = A.util,DOM = A.dom;
    var B = {
        combos:[],
        activeCombo:null
    };
    $(function() {
        var container = $I("body-combo-container");
        if (container.length === 0) {
            $("body").append("<div id='body-combo-container' style='display:block;'/>");
        }
    });
    var Combo = function(p) {
        this._constructor(p);
        this._init();
        this._render();
    };
    var CP = {
        _constructor:function(p) {
            this.items = [];
            this.displayItems = [];
            this.displayField = "name";
            this.valueField = "id";
            this.filterDelay = 500;
            this.upDownDealy = 100;
            this.readOnly = false;
            this.editable = true;
            for (var k in p) {
                this[k] = p[k];
            }
        },
        _init:function() {
            B.combos.push(this);
            this.isShow = false;
            this.ajaxRefresh = false;
            this.renderCount = 0;
            this.itemHeight = 22;
            this.filterField = this.filterField || this.displayField;
            this.lastVal = "";
            this.selectItem = null;
            this.el = $I(this.id);
            this.imgEl = this.el.next("img");
            this.hiddenEl = this.el.prev("input:hidden");
        },
        _render:function() {
            var container = $I("body-combo-container"),html = [];
            html.push("<div id='" + this.id + "-combo' class='combo-defalut-list' style='display:none;'/>");
            container.append(html.join(""));
            this.comboEl = $I(this.id + "-combo");
            this._inputRender();
            this._triggerEvent();
        },
        _renderAll:function() {
            this._renderItems(this.filterItems(""));
            this.lastFilter = false;
        },
        _renderEmpty:function() {
            this._renderItems([]);
            this.lastFilter = false;
        },
        _renderFilter:function(valNew) {
            this._renderItems(this.filterItems(valNew));
            this.lastFilter = true;
        },
        filterItems:function(key) {
            var displayItems = [],items = this.items,len = items.length;
            if (key === "") {
                for (var i = 0; i < len; i++) {
                    displayItems.push(items[i]);
                }
            } else {
                var filterField = this.filterField;
                var filterCount = this.filterCount;
                for (var i = 0; i < len; i++) {
                    var item = items[i];
                    if ((item[filterField] + "").startWith(key + "")) {
                        displayItems.push(item);
                    }
                    if (filterCount && (displayItems.length >= filterCount)) {
                        break;
                    }
                }
            }
            this.displayItems = displayItems;
            this.lastVal = key + "";
            return displayItems;
        },
        _renderItems:function(items) {
            var html = [],len = items.length;
            var displayField = this.displayField,valueField = this.valueField;
            var liWidth = this.liWidth;
            html.push("<ul>");
            if (len === 0) {
                html.push("<li class='combo-item'>无数据</li>");
            } else {
                for (var i = 0; i < len; i++) {
                    var item = items[i],display = item[displayField],value = item[valueField];
                    html.push("<li class='combo-item");
                    if (value === this.selectValue) {
                        html.push(" combo-item-checked");
                    }
                    html.push("'");
                    if (liWidth) {
                        html.push(" style='width:" + liWidth + "px;'");
                    }
                    html.push(" id='" + (this.id + "item-" + i) + "'");
                    html.push(" buz_value='" + value + "'");
                    html.push(" buz_value_type='" + typeof(value) + "'");
                    html.push(">");
                    if (this.formatDisplay) {
                        html.push(this.formatDisplay.call(this, item));
                    } else {
                        html.push(display);
                    }
                    html.push("</li>");
                }
            }
            html.push("</ul>");
            this.comboEl.html(html.join(""));
            this.renderCount++;
        },
        _getEvent:function(type) {
            var ME = this,el = this.el,comboEl = this.comboEl;
            var imgEvent = function(e) {
                ME.el.focus();
                ME.show();
                e.stopPropagation();
            };
            if (type === "img") return  imgEvent;
            var up = function() {
                if (this.continuousUp === true) {
                    var upProxy = $.proxy(up, this);
                    var liEl = this.getActiveEl(),nextEl = liEl.prev();
                    if (nextEl.length > 0) {
                        liEl = nextEl;
                    }
                    DOM.addClass(liEl.get(0), "combo-item-checked");
                    this.adjustScroll(liEl, "top");
                    setTimeout(upProxy, this.upDownDealy);
                }
            };
            var down = function() {
                if (this.continuousDown === true) {
                    var downProxy = $.proxy(down, this);
                    var liEl = this.getActiveEl(),nextEl = liEl.next();
                    if (nextEl.length > 0) {
                        liEl = nextEl;
                    }
                    DOM.addClass(liEl.get(0), "combo-item-checked");
                    this.adjustScroll(liEl, "bottom");
                    setTimeout(downProxy, this.upDownDealy);
                }
            };
            var keydownEvent = function(e) {
                var keyCode = e.keyCode;
                if (keyCode === 40) {
                    if (ME.isShow === true) {
                        ME.continuousDown = true;
                        down.call(ME);
                    } else {
                        ME.show();
                    }
                    ME.el.unbind("keydown");
                    e.stopPropagation();
                } else if (keyCode === 38) {
                    if (ME.isShow === true) {
                        ME.continuousUp = true;
                        up.call(ME);
                    }
                    ME.el.unbind("keydown");
                    e.stopPropagation();
                } else if (keyCode === 13) {
                    if (ME.isShow === true) {
                        var liEl = ME.getActiveEl();
                        if (liEl.length > 0) {
                            var item = ME.getItemByDom(liEl.get(0));
                            if (item) {
                                ME.addSelectItem(item);
                                ME.el.focus();
                            }
                        }
                        $(D).click();
                    }
                }
            };
            if (type === "keydown") return  keydownEvent;
            var blurEvent = function(e) {
                var val = ME.el.val();
                if (val) {
                    ME.selectDisplay && ME.el.val(ME.selectDisplay);
                } else {
                    ME.removeSelectItem();
                }
            };
            if (type === "blur") return  blurEvent;
            var keyupEvent = function(e) {
                var keyCode = e.keyCode;
                if (keyCode === 40) {
                    ME.continuousDown = false;
                    el.keydown(keydownEvent);
                    e.stopPropagation();
                } else if (keyCode === 38) {
                    ME.continuousUp = false;
                    el.keydown(keydownEvent);
                    e.stopPropagation();
                } else if (keyCode === 13) {
                } else {
                    var valNew = el.val();
                    var valOld = ME.lastVal;
                    var proxy = {
                        lastInputVal:valNew,
                        lastRenderVal:valOld,
                        scope:ME,
                        render:function() {
                            var valNew = el.val();
                            if (this.lastInputVal == valNew && this.lastRenderVal != valNew) {
                                if (ME.isShow === false) {
                                    ME.show(true);
                                }
                                ME._renderFilter(valNew);
                                ME._afterShow();
                            }
                        }
                    };
                    setTimeout($.proxy(proxy.render, proxy), ME.filterDelay);
                    e.stopPropagation();
                }
            };
            if (type === "keyup") return  keyupEvent;
            var comboElEvent = {
                click:function(e) {
                    var tar = e.target,nodeName = tar.nodeName,cls = tar.className;
                    if (nodeName === "LI" && cls.contains("combo-item")) {
                        var item = ME.getItemByDom(tar);
                        if (item) {
                            ME.addSelectItem(item);
                            ME.el.focus();
                            return true;
                        }
                    }
                    e.stopPropagation();
                },
                mouseover:function(e) {
                    var tar = e.target,nodeName = tar.nodeName,cls = tar.className;
                    if (nodeName === "LI" && cls.contains("combo-item")) {
                        DOM.removeClass(ME.overId, "combo-item-over");
                        DOM.addClass(tar, "combo-item-over");
                        ME.overId = tar.id;
                    }
                    e.stopPropagation();
                },
                mouseout:function(e) {
                    var tar = e.target,nodeName = tar.nodeName,cls = tar.className;
                    if (nodeName === "LI" && cls.contains("combo-item")) {
                        DOM.removeClass(ME.overId, "combo-item-over");
                        ME.overId = null;
                    }
                    e.stopPropagation();
                }
            };
            if (type === "comboEl") return  comboElEvent;
        },
        _triggerEvent:function() {
            var ME = this,el = this.el,comboEl = this.comboEl;
            if (this.readOnly === false) {
                this.imgEl.click(this._getEvent("img"));
                this.imgEl.css("cursor", "pointer");
                el.blur(this._getEvent("blur"));
                el.keydown(this._getEvent("keydown"));
                el.keyup(this._getEvent("keyup"));
                comboEl.bind(this._getEvent("comboEl"));
                if (this.editable) {
                    el.get(0).removeAttribute("readOnly");
                    $(el.get(0)).removeClass("ux-item-readOnly");
                }
            } else {
                this.imgEl.unbind("click");
                this.imgEl.css("cursor", "default");
                el.unbind("blur");
                el.unbind("keydown");
                el.unbind("keyup");
                comboEl.unbind("click");
                comboEl.unbind("mouseover");
                comboEl.unbind("mouseout");
                el.get(0).setAttribute("readOnly", "readOnly");
                $(el.get(0)).addClass("ux-item-readOnly");
            }
        },
        _inputRender :  function() {
            var ME = this,el = this.el;
            if (this.editable) {
                el.get(0).removeAttribute("readOnly");
                $(el.get(0)).removeClass("ux-item-readOnly");
            } else {
                el.get(0).setAttribute("readOnly", "readOnly");
                $(el.get(0)).addClass("ux-item-readOnly");
            }
        },
        setEditable : function(flag) {
            if (typeof(flag) === "boolean" && this.editable !== flag) {
                this.editable = flag;
                this._inputRender();
            }
        },
        setReadOnly:function(flag) {
            if (typeof(flag) === "boolean" && this.readOnly !== flag) {
                this.readOnly = flag;
                this._triggerEvent();
            }
        },
        getActiveEl:function() {
            var ulEl = this.comboEl.children("ul");
            var activeEl = ulEl.children(".combo-item-checked,.combo-item-over");
            var len = activeEl.length;
            var rtnEl = null;
            for (var i = 0; i < len; i++) {
                var dom = activeEl[i];
                if (DOM.hasClass(dom, "combo-item-checked")) {
                    rtnEl = $(dom);
                    break;
                }
            }
            if (rtnEl === null || rtnEl.length === 0) {
                for (var j = 0; j < len; j++) {
                    var dom = activeEl[j];
                    if (DOM.hasClass(dom, "combo-item-over")) {
                        rtnEl = $(dom);
                        break;
                    }
                }
            }
            for (var k = 0; k < len; k++) {
                var dom = activeEl[k];
                DOM.replaceClass(dom, "combo-item");
            }
            if (!rtnEl || rtnEl.length === 0) {
                rtnEl = ulEl.children(":first");
            }
            return rtnEl;
        },
        adjustScroll:function(liEl, position) {
            var comboEl = this.comboEl,comboElTop = comboEl.offset().top;
            var itemHeight = this.itemHeight;
            var liElTop = liEl.offset().top;
            var offset = liElTop - comboElTop - 1;
            if (position === "top") {
                if (offset < 0) {
                    comboEl.get(0).scrollTop -= this.itemHeight;
                }
            } else if (position === "bottom") {
                var innerHeight = comboEl.get(0).clientHeight;

                if (offset > (innerHeight - itemHeight)) {
                    comboEl.get(0).scrollTop += itemHeight;
                }
            } else if (position === "center") {
                var innerHeight = comboEl.get(0).clientHeight;
                var positionTop = parseInt((innerHeight - itemHeight) / 2 + comboElTop);
                var offsetTop = liElTop - positionTop;
                comboEl.get(0).scrollTop += offsetTop;
            }
        },
        addSelectItem:function(item) {
            if (item) {
                this.removeSelectItem();
                var value = item[this.valueField];
                if (this.formatDisplay) {
                    display = this.formatDisplay.call(this, item);
                } else {
                    display = item[this.displayField];
                }
                this.selectItem = item;
                this.selectDisplay = display;
                this.selectValue = value;
                this.el.val(display);
                this.hiddenEl.val(value);
                this.onSelectItem && this.onSelectItem.call(this, item);
            }
        },
        removeSelectItem:function() {
            var item = this.selectItem;
            if (item) {
                this.onRemoveSelectItem && this.onRemoveSelectItem.call(this, item);
            }
            this.selectItem = null;
            this.selectDisplay = null;
            this.selectValue = null;
            this.el.val("");
            this.hiddenEl.val("");
        },
        setValue:function(value) {
            var items = this.items,len = items.length;
            var valueField = this.valueField;
            var selectItem = null;
            for (var i = 0; i < len; i++) {
                var item = items[i];
                if (item[valueField] === ("" + value)) {
                    selectItem = item;
                }
            }
            if (selectItem) {
                R.hideActive();
                this.addSelectItem(selectItem);
            }
        },
        show:function(empty) {
            if (this.isShow !== true) {
                var el = this.el,imgEl = this.imgEl;
                var outerWidth = this.listWidth || (el.outerWidth() + imgEl.outerWidth());
                this.liWidth = outerWidth - 8;
                if (empty === true) {
                    this._renderEmpty();
                } else {
                    this._prepareShow();
                }
                var comboEl = this.comboEl,comboStyle = comboEl.get(0).style;
                var offset = el.offset();
                var outerHeight = el.outerHeight();
                comboStyle.left = offset.left + "px";
                comboStyle.top = (offset.top + outerHeight - 1) + "px";
                comboStyle.width = (outerWidth - 2) + "px";
                comboStyle.display = "block";
                this.isShow = true,B.activeCombo = this;
                this._afterShow();
            }
        },
        _prepareShow:function() {
            R.hideActive();
            if (this.renderCount === 0 || (this.lastFilter === true && this.lastVal !== "")) {
                this._renderAll();
                return;
            }
            if (this.ajaxRefresh === true) {
                this._renderAll();
                this.ajaxRefresh = false;
            }
        },
        _afterShow:function() {
            this._adjustHeight();
            var ulEl = this.comboEl.children("ul");
            var oldEl = ulEl.children(".combo-item-checked,.combo-item-over");
            for (var i = 0; i < oldEl.length; i++) {
                DOM.replaceClass(oldEl[i], "combo-item");
            }
            var selectEl = ulEl.children("li[buz_value='" + this.selectValue + "']");
            if (selectEl.length > 0) {
                DOM.addClass(selectEl.get(0), "combo-item-checked");
                this.adjustScroll(selectEl, "center");
            } else {
                var selectItemEl = ulEl.children(":first");
                selectItemEl.length > 0 && DOM.addClass(selectItemEl.get(0), "combo-item-over");
                this.overId = selectItemEl.attr("id");
                this.comboEl.scrollTop(0);
            }
            this._appendEvent();
        },
        _adjustHeight:function() {
            var h = this.height;
            if (h) {
                var ulHeight = this.displayItems.length * this.itemHeight;
                var comboEl = this.comboEl,comboStyle = comboEl.get(0).style;
                h = h - 2;
                if (ulHeight > h) {
                    comboStyle.height = h + "px";
                } else {
                    comboStyle.height = "auto";
                }
            } else {
                comboStyle.height = "auto";
            }
        },
        ajaxRefreshItems:function(params, url) {
            var postUrl = url || this.url;
            if (postUrl) {
                $.ajax({
                    url: postUrl,
                    data:params,
                    cache:false,
                    type:"POST",
                    dataType:"html",
                    context:this,
                    beforeSend:function() {
                    },
                    success: function(response) {
                        var resp = eval("(" + response + ')');
                        if (resp) {
                            if (resp.returnCode === 0) {
                                var dsr = resp.dataSetResult;
                                if (dsr && dsr instanceof Array && dsr.length > 0) {
                                    var data = dsr[0].data;
                                    if (data && data instanceof Array && data.length > 0) {
                                        R.hideActive();
                                        this.selectItem = null;
                                        this.selectDisplay = null;
                                        this.selectValue = null;
                                        this.el.val("");
                                        this.hiddenEl.val("");
                                        this.items = data;
                                        this.ajaxRefresh = true;
                                        this.onRefreshItems && this.onRefreshItems.call(this, data);
                                    }
                                }
                            } else {
                                athene.dialog.error(resp.errorInfo);
                            }
                        } else {
                            athene.dialog.error("无效的返回");
                        }
                    },
                    complete:function() {
                    }
                });
            }
        },
        _appendEvent:function() {
            var ME = this;
            var stop = function(e) {
                e.stopPropagation();
            };
            $(document).one("click", function() {
                R.hideActive();
                ME.el.val(ME.selectDisplay || "");
                ME.el.unbind("click", stop);
            });
            this.el.bind("click", stop);
        },
        getItemByDom:function(dom) {
            var buzValue = dom.getAttribute("buz_value");
            var buzValueType = dom.getAttribute("buz_value_type");
            if (buzValue && buzValueType) {
                return this.getItem(buzValue, buzValueType);
            } else {
                return null;
            }
        },
        getItem:function(buzValue, buzValueType) {
            var items = this.displayItems,len = items.length;
            if (buzValue == undefined || buzValueType == undefined || len == 0)return null;
            var itemValue = UTIL.convert(buzValue, buzValueType);
            for (var i = 0; i < len; i++) {
                var item = items[i];
                if (item[this.valueField] === itemValue) {
                    return item;
                }
            }
            return null;
        },
        hide:function() {
            if (this.isShow === true) {
                this.comboEl.hide();
                this.isShow = false;
            }
        },
        getSelectionData:function() {
            return this.selectItem;
        }
    };
    Combo.prototype = CP;
    var R = {
        get:function(p) {
            var combos = B.combos,len = combos.length;
            for (var i = 0; i < len; i++) {
                if (combos[i].id == p) {
                    return combos[i];
                }
            }
            return null;
        },
        getAll:function() {
            return B.combos;
        },
        create:function(p) {
            if (!p || !p.id)throw Error("配置对象或者id未定义");
            var combo = this.get(p.id);
            if (combo)throw Error("已存在相同id的Combo，请先销毁原先的Combo");
            return new Combo(p);
        },
        hideActive:function() {
            if (B.activeCombo) {
                B.activeCombo.hide();
                B.activeCombo = null;
            }
        }
    };
    A.addModule("combo", R);
})();