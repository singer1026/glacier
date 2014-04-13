window["athene"]["grid"] || (function() {
    var A = window["athene"],W = window,D = document,DOM = A.dom,ENV = A.env,UTIL = A.util;
    var B = {
        grids:[]
    };
    var Grid = function(p) {
        this._constructor(p);
        this._init();
        this._render();
    };
    var GP = {
        _constructor:function(p) {
            B.grids.push(this);
            this.cellPL = 5;
            this.cellPR = 3;
            this.cellBRW = 1;
            this.cellMoveW = 10;
            this.items = [];
            this.totalCount = 0;
            this.changed = false;
            this.paramMap = undefined;
            this.defaultMap = {};
            this.showMask = true;
            this.selectItems = [];
            A.apply(this, p);
        },
        _init:function() {
            this.xtype = "grid";
            this.el = $I(this.id),e = this.el.get(0);
            DOM.addClass(e, "grid-default");
            this.gridCls && DOM.addClass(e, this.gridCls);
            this.width = this.width || this.el.parent().innerWidth();
            this.borderLW = UTIL.fetchNumber(this.el.css("borderLeftWidth"));
            this.borderRW = UTIL.fetchNumber(this.el.css("borderRightWidth"));
            A.window.get(this.binding).addItem(this);
            this.onInit && this.onInit.call(this);
        },
        _render:function() {
            var innerWidth = this.width - this.borderLW - this.borderRW;
            this.el.empty().append("<div class='grid-head'></div><div class='grid-body'></div><div class='grid-page'></div>");
            this.headEl = this.el.children(".grid-head");
            this.bodyEl = this.el.children(".grid-body");
            this.pageEl = this.el.children(".grid-page");
            this.el.get(0).style.width = innerWidth + "px";
            this.headEl.get(0).style.width = innerWidth + "px";
            this.bodyEl.get(0).style.width = innerWidth + "px";
            this.pageEl.get(0).style.width = innerWidth + "px";
            this._renderHead();
            this.clearBody();
            this.pagination === true && this._renderPagination();
            this._bindEvent();
            this.onRender && this.onRender.call(this);
            this.items.length > 0 && this._decorate();
        },
        _renderHead:function() {
            var offset = this.cellPL + this.cellPR;
            var html = [],colModels = this.colModels,len = colModels.length;
            var innerWidth = this.width - this.borderLW - this.borderRW;
            var remainWidth = innerWidth,cellMoveW = this.cellMoveW;
            html.push("<table cellpadding='0' cellspacing='0'><thead>");
            if (this.hasRowNumber === true) {
                html.push("<th style='border-right: 1px solid #d0d0d0;'><div class='grid-cell grid-row-number'></div></th>");
                remainWidth -= 30;
            }
            if (this.selectModel === "multiSelect") {
                html.push("<th style='border-right: 1px solid #d0d0d0;'><div class='grid-checkbox-wrap'>");
                html.push("<span class='grid-checkbox' style='display:none;' buz_type='all' buz_status='unchecked'>");
                html.push("</span></div></th>");
                remainWidth -= 22;
            } else if (this.selectModel === "singleSelect") {
                html.push("<th style='border-right: 1px solid #d0d0d0;'><div class='grid-radio-wrap'></div></th>");
                remainWidth -= 22;
            }
            for (var i = 0; i < len; i++) {
                var colModel = colModels[i];
                colModel.width = colModel.width || 100;
                var colWidth = colModel.width;
                var title = colModel.title,thWidth = 0;
                if (false !== colModel.hasTips && (colModel.formatter === undefined || colModel.formatter === null)) {
                    colModel.hasTips = true;
                }
                if (false !== colModel.resizeable) {
                    colModel.resizeable = true;
                }
                html.push("<th>");
                if (this.percent === true) {
                    if ((i + 1) < len) {
                        var colWidthPx = parseInt(colWidth * innerWidth);
                        colModel.widthPx = colWidthPx;
                        remainWidth -= colWidthPx;
                    } else {
                        var colWidthPx = remainWidth;
                        colModel.widthPx = colWidthPx;
                        remainWidth = 0;
                    }
                    colWidth = colModel.widthPx;
                }
                thWidth = colWidth - offset;
                html.push("<div class='grid-title-inner' biz_index='" + i + "' style='width:" + (colWidth) + "px;'>");
                html.push("<div class='grid-cell' style='width:" + (thWidth - cellMoveW) + "px;'");
                if (title && colModel.hasTips === true) {
                    html.push(" title='" + title + "'");
                }
                html.push(">");
                html.push(title || "");
                html.push("</div>");
                if (colModel.resizeable === true) {
                    html.push("<div class='grid-cell-move-right'></div>");
                } else {
                    html.push("<div class='grid-cell-static-right'></div>");
                }
                html.push("</div></th>");
            }
            html.push("</thead></table>");
            this.headEl.html(html.join(""));
        },
        _bindEvent:function() {
            this.el.bind({
                mouseover:function(e) {
                    var tar = e.target;
                    if (tar.nodeName === "A") {
                        DOM.addClass(tar, "link-hover");
                        e.stopPropagation();
                        return;
                    }
                },
                mouseout:function(e) {
                    var tar = e.target;
                    if (tar.nodeName === "A") {
                        DOM.removeClass(tar, "link-hover");
                        e.stopPropagation();
                        return;
                    }
                }
            });
            this.bodyEl.scroll($.proxy(function(e) {
                this.headEl.scrollLeft(this.bodyEl.scrollLeft());
            }, this));
            if (this.selectModel === "multiSelect") {
                this.el.click($.proxy(function(e) {
                    var tar = e.target,cls = tar.className,nodeName = tar.nodeName;
                    if (cls.contains("checkbox") && nodeName === "SPAN") {
                        if (tar.getAttribute("buz_type") === "all") {
                            var items = this.items,len = items.length;
                            var buzStatus = tar.getAttribute("buz_status");
                            for (var i = 0; i < len; i++) {
                                if (buzStatus === "checked") {
                                    this.removeSelectRow(items[i], null, false);
                                    this._triggerCheckAll.call(this);
                                } else {
                                    this.addSelectRow(items[i], null, false);
                                    this._triggerCheckAll.call(this);
                                }
                            }
                        } else {
                            var status = tar.getAttribute("buz_status");
                            if (status === "unchecked") {
                                var item = this.getItemByDom(tar);
                                item && this.addSelectRow(item, $(tar));
                            } else {
                                var item = this.getItemByDom(tar);
                                item && this.removeSelectRow(item, $(tar));
                            }
                        }
                    } else if (cls.indexOf("grid-cell") !== -1 && nodeName === "DIV") {
                        var trEl = $(tar).parents("tr");
                        if (trEl.length > 0) {
                            var trCls = trEl.get(0).className;
                            if (trCls.contains("grid-tr-odd") || trCls.contains("grid-tr-even")) {
                                var checkEl = trEl.children("td.grid-checkbox-td").find(".grid-checkbox");
                                var checkDom = checkEl.get(0);
                                var status = checkDom.getAttribute("buz_status");
                                if (status) {
                                    if (status === "unchecked") {
                                        var item = this.getItemByDom(checkDom);
                                        item && this.addSelectRow(item, checkEl, true);
                                    } else {
                                        var item = this.getItemByDom(checkDom);
                                        item && this.removeSelectRow(item, checkEl, true);
                                    }
                                }
                            }
                        }
                    }
                }, this));
            }
            this.headEl.mousedown($.proxy(function(e) {
                var tar = e.target , cls = tar.className;
                if (cls === "grid-cell-move-right") {
                    var documentEl = $(D),bodyEl = $("body"),el = $(tar);
                    var index = el.parent("div.grid-title-inner").attr("biz_index"),offset = el.offset();
                    var height = this.headEl.outerHeight() + this.bodyEl.innerHeight();
                    bodyEl.css("cursor", "col-resize").append("<div id='grid-move-proxy' biz_index='" + index + "' style='height:"
                        + height + "px;left:" + e.pageX + "px;top:" + offset.top + "px;'></div>");
                    bodyEl.closeSelect();
                    documentEl.mousemove(this._moveColumn);
                    documentEl.one("mouseup", $.proxy(this._resetColumn, this));
                    ENV.set("gridMoveProxy", $I("grid-move-proxy"));
                    ENV.set("gridMousePos", {pageX:e.pageX});
                    ENV.set("gridMousePosInit", {pageX:e.pageX});
                }
            }, this));
        },
        _resetColumn:function(e) {
            var proxy = ENV.get("gridMoveProxy"),index = proxy.attr("biz_index");
            var colModel = this.colModels[index];
            if (this.percent === true) {
                colWidth = colModel.widthPx;
            } else {
                colWidth = colModel.width;
            }
            var mp = ENV.get("gridMousePos"),mpi = ENV.get("gridMousePosInit");
            var documentEl = $(D),offset = mp.pageX - mpi.pageX,tarWidth = colWidth + offset;
            if (tarWidth > 20) {
                this.setColumnWidth(index, tarWidth);
                this._adjustHead();
            }
            $("body").css("cursor", "default").openSelect(),
                proxy.remove();
            documentEl.unbind("mousemove", this._moveColumn);
            ENV.set("gridMoveProxy", null);
            ENV.set("gridMousePos", null);
            ENV.set("gridMousePosInit", null);
        },
        _moveColumn:function(e) {
            var proxy = ENV.get("gridMoveProxy"),mp = ENV.get("gridMousePos");
            if (!proxy || !mp)return;
            var ox = e.pageX - mp.pageX,pos = proxy.position();
            var tx = pos.left + ox,o = proxy.get(0);
            o.style.left = tx + "px";
            mp.pageX = e.pageX;
            e.stopPropagation();
        },
        setColumnWidth:function(index, width) {
            var colModels = this.colModels,len = colModels.length;
            index = parseInt(index);
            var colModel = colModels[index];
            if (!colModel || !width || width < 20)return;
            if (this.percent === true) {
                colModel.widthPx = width;
            } else {
                colModel.width = width;
            }
            var offset = this.cellPL + this.cellPR;
            var cellBRW = this.cellBRW,cellMoveW = this.cellMoveW;
            var headInnerEl = this.headEl.children("table").find("div[biz_index='" + index + "']");
            var headCellEl = headInnerEl.children("div.grid-cell");
            if (headInnerEl.find(".grid-cell-move-right").length > 0) {
                headInnerEl.width(width);
                headCellEl.width(width - offset - cellMoveW);
            }
            var emptyEl = this.bodyEl.find(".empty-div");
            if (emptyEl.length > 0) {
                var hw = this.headEl.children("table").outerWidth();
                emptyEl.width(hw);
            } else {
                var bodyCellEl = this.bodyEl.children("table").find("div[biz_index='" + index + "']");
                bodyCellEl.width(width - offset - cellBRW);
            }
        },
        _decorate:function() {
            this._decorateRows();
            (this.pagination === true) && this._resetPagination();
            this._adjustHead();
        },
        clearBody:function() {
            var hw = this.headEl.children("table").outerWidth();
            var html = "<div class='empty-div' style='width:" + hw + "px;'/>";
            this.bodyEl.html(html);
        },
        _adjustHead:function() {
            var bodyDom = this.bodyEl.get(0);
            var bow = bodyDom.offsetWidth;
            var bcw = bodyDom.clientWidth;
            var boh = bodyDom.offsetHeight;
            var bch = bodyDom.clientHeight;
            var offsetw = bow - bcw, offseth = boh - bch;
            var firstTrEl = this.headEl.children("table").find("tr:first");
            firstTrEl.children(".grid-head-filler-wrap").remove();
            if (offsetw > 0 && offseth > 0) {
                firstTrEl.append("<th class='grid-head-filler-wrap'><div class='grid-head-filler' style='width:" + offsetw + "px;'></div></th>")
            }
        },
        _isChecked:function(item) {
            if (this.selectModel === "multiSelect") {
                var getValueField = this._getValueField;
                var selectItems = this.selectItems,len = selectItems.length;
                for (var i = 0; i < len; i++) {
                    var selectItem = selectItems[i];
                    if (getValueField.call(this, selectItem) === getValueField.call(this, item)) {
                        return true;
                    }
                }
            } else if (this.selectModel === "singleSelect") {
                var selectItem = this.selectItem;
                if (selectItem) {
                    var getValueField = this._getValueField;
                    if (getValueField.call(this, selectItem) === getValueField.call(this, item)) {
                        return true;
                    }
                }
            }
            return false;
        },
        _getValueField:function(item) {
            if (item === undefined || item === null)return undefined;
            var formatValueField = this.formatValueField;
            if (formatValueField) {
                return formatValueField.call(this, item);
            } else {
                return item[this.valueField];
            }
        },
        _decorateRows:function() {
            var html = [],items = this.items,itemLen = items.length;
            var colModels = this.colModels,colLen = colModels.length;
            var isMultiSelect = ( this.selectModel === "multiSelect");
            var isSingleSelect = ( this.selectModel === "singleSelect");
            var offset = this.cellPL + this.cellPR + this.cellBRW;
            var getValueField = this._getValueField;
            html.push("<table cellpadding='0' cellspacing='0'><tbody>");
            for (var i = 0; i < itemLen; i++) {
                var item = items[i],num = (i + 1) % 2;
                var value = getValueField.call(this, item);
                html.push("<tr");
                html.push(num < 1 ? " class='grid-tr-even" : " class='grid-tr-odd");
                var isChecked = false;
                if (isMultiSelect || isSingleSelect) {
                    isChecked = this._isChecked(item);
                }
                if (isMultiSelect && isChecked) {
                    html.push(" grid-tr-checked");
                }
                html.push("'>");
                if (this.hasRowNumber === true) {
                    html.push("<td>");
                    html.push("<div class='grid-cell grid-row-number'>");
                    html.push(i + 1);
                    html.push("</div></td>");
                }
                if (isMultiSelect) {
                    html.push("<td class='grid-checkbox-td'><div class='grid-checkbox-wrap'>");
                    if (isChecked === true) {
                        html.push("<span class='grid-checkbox grid-checkbox-checked' buz_type='single'");
                        html.push(" buz_value='" + value + "' buz_status='checked'>");
                    } else {
                        html.push("<span class='grid-checkbox' buz_type='single'");
                        html.push(" buz_value='" + value + "' buz_status='unchecked'>");
                    }
                    html.push("</span></div></td>");
                } else if (isSingleSelect) {
                    html.push("<td class='grid-radio-td'><div class='grid-radio-wrap'>");
                    if (isChecked === true) {
                        html.push("<span class='grid-radio grid-radio-checked' buz_type='single'");
                        html.push(" buz_value='" + value + "' buz_status='checked'>");
                    } else {
                        html.push("<span class='grid-radio' buz_type='single'");
                        html.push(" buz_value='" + value + "' buz_status='unchecked'>");
                    }
                    html.push("</span></div></td>");
                }
                for (var j = 0; j < colLen; j++) {
                    html.push("<td>");
                    var colModel = colModels[j],colWidth = colModel.width;
                    var value = item[colModel.key];
                    colModel.formatter && (value = colModel.formatter(value, item, i, j));
                    value = ((value !== undefined && value !== null) ? value + "" : "");
                    if (this.percent === true) {
                        colWidth = colModel.widthPx;
                    }
                    var tdWidth = colWidth - offset;
                    html.push("<div class='grid-cell' biz_index='" + j + "' style='width:" + tdWidth + "px;'");
                    if (value && colModel.hasTips === true) {
                        html.push(" title='" + value + "'");
                    }
                    html.push(">");
                    html.push(value);
                    html.push("</div></td>");
                }
                html.push("</tr>");
            }
            html.push("</tbody></table>");
            this.bodyEl.html(html.join(""));
            isMultiSelect && this._triggerCheckAll.call(this);
        },
        _destory:function() {
            var grids = B.grids,len = grids.length;
            for (var i = 0; i < len; i++) {
                var item = grids[i];
                if (this.id == item.id) {
                    grids.splice(i, 1);
                    break;
                }
            }
            this.pageEl.find(".page-size").unbind().remove();
            this.pageEl.unbind().remove();
            this.bodyEl.unbind().remove();
            this.headEl.unbind().remove();
            this.el.unbind().remove();
        },
        getBindingWindow:function() {
            return A.window.get(this.binding);
        },
        setSize:function(w, h) {
            var needAdjust = false;
            if (w && typeof(w) === "number" && w > 0 && w > 0) {
                var innerWidth = w - this.borderLW - this.borderRW;
                if (innerWidth > 0) {
                    this.width = w;
                    this.el.get(0).style.width = innerWidth + "px";
                    this.headEl.get(0).style.width = innerWidth + "px";
                    this.bodyEl.get(0).style.width = innerWidth + "px";
                    this.pageEl.get(0).style.width = innerWidth + "px";
                    if (this.percent === true) {
                        var remainWidth = this.width;
                        var colModels = this.colModels,len = colModels.length;
                        if (this.hasRowNumber === true) {
                            remainWidth -= 30;
                        }
                        if (this.selectModel === "multiSelect" || this.selectModel === "singleSelect") {
                            remainWidth -= 22;
                        }
                        for (var i = 0; i < len; i++) {
                            var colModel = colModels[i],colWidth = colModel.width;
                            if ((i + 1) < len) {
                                var colWidthPx = parseInt(colWidth * remainWidth);
                                colModel.widthPx = colWidthPx;
                                remainWidth -= colWidthPx;
                            } else {
                                var colWidthPx = remainWidth;
                                colModel.widthPx = colWidthPx;
                                remainWidth = 0;
                            }
                            this.setColumnWidth(i, colWidthPx);
                        }
                    }
                    needAdjust = true;
                }
            }
            if (h && typeof(h) === "number" && h >= 0 && h > 0) {
                var innerHeight = h;
                var hh = this.headEl.outerHeight();
                var ph = this.pageEl.outerHeight();
                var bodyH = innerHeight - hh - ph;
                if (innerHeight > 0 && bodyH > 0) {
                    this.height = h;
                    this.el.get(0).style.height = innerHeight + "px";
                    this.bodyEl.get(0).style.height = bodyH + "px";
                    needAdjust = true;
                }
            }
            needAdjust === true && this._adjustHead();
        }
    };
    A.apply(GP, {
        _excute:function(p) {
            var data = {query:true,args:json(this.paramMap)};
            if (this.pagination) {
                data["pageSize"] = this.pageSize;
                data["curPage"] = p.curPage;
            }
            $.ajax({
                url: this.url,
                data:data,
                dataType:"json",
                context:this,
                beforeSend:function() {
                    this.getBindingWindow().showMask("正在加载数据，请稍等...");
                },
                success: function(d) {
                    if (d.returnCode === 3) {
                        athene.dialog.alertException(d.exceptionDesc);
                        return;
                    }
                    if (d && typeof(d) === "object") {
                        for (var t in d) {
                            this[t] = d[t];
                        }
                    }
                    this._decorate.call(this, d);
                    this.onQuery && this.onQuery.call(this, d);
                    this.getBindingWindow().hideMask();
                }
            });
        },
        query:function(p) {
            this.paramMap = A.util.merge(this.defaultMap, p);
            this._excute({curPage: 1});
        },
        first:function() {
            this._excute({curPage:1});
        },
        last:function() {
            this._excute({curPage:this.totalPage});
        },
        next:function() {
            this._excute({curPage:this.curPage + 1});
        },
        previous:function() {
            this._excute({curPage:this.curPage - 1});
        }  ,
        refresh:function() {
            this.curPage !== undefined && this._excute({curPage:this.curPage});
        },
        refreshSubmit:function() {
            if (this.submit === true && ENV.get("refreshSubmit") === true) {
                this.refresh();
                this.submit = false;
            }
        },
        setPageSize:function(val) {
            if (val) {
                this.pageSize = val;
                this.curPage !== undefined && this.first();
            }
        }
    });
    A.apply(GP, {
        getSelectRow:function() {
        },
        getSelectRows:function() {
            return this.selectItems;
        },
        getItemByDom:function(dom) {
            var buzValue = dom.getAttribute("buz_value");
            if (buzValue) {
                return this.getItem(buzValue);
            } else {
                return null;
            }
        },
        getItem:function(buzValue) {
            var items = this.items,len = items.length;
            if (buzValue == undefined || len == 0)return null;
            var getValueField = this._getValueField;
            for (var i = 0; i < len; i++) {
                var item = items[i];
                if (getValueField.call(this, item) == buzValue) {
                    return item;
                }
            }
            return null;
        },
        addRow:function(item) {
            if (this.pagination || typeof(item) !== "object") return;
            this.items.push(item);
            this.totalCount++;
            this.changed = true;
            this._decorateRows();
            return this;
        },
        replaceRow:function(i, item) {
            if (this.pagination || typeof(item) !== "object") return;
            if (i >= this.items.length)return;
            this.items.splice(i, 1, item);
            this.changed = true;
            this._decorateRows();
            return this;
        },
        removeRow:function(i) {
            if (this.pagination || typeof(i) !== "number") return;
            if (i >= this.items.length)return;
            this.items.splice(i, 1);
            this.totalCount--;
            this.changed = true;
            this._decorateRows();
            return this;
        } ,
        clearSelectRows:function() {
            this.selectItems = [];
            this._decorateRows();
            this.onClearSelectRows && this.onClearSelectRows.call(this);
        },
        removeSelectRow:function(item, el, trigger) {
            var removeItem = undefined,valueField = this.valueField;
            var selectItems = this.selectItems,len = selectItems.length;
            if (item && len > 0) {
                for (var i = 0; i < len; i++) {
                    var selectItem = selectItems[i];
                    if (item[valueField] === selectItem[valueField]) {
                        removeItem = selectItem;
                        selectItems.splice(i, 1);
                        var checkboxEl = el || this.bodyEl.find("span[buz_value='" + selectItem[valueField] + "']");
                        if (checkboxEl.length > 0) {
                            this._triggerRow(checkboxEl, false);
                            DOM.removeClass(checkboxEl.get(0), "grid-checkbox-checked");
                            checkboxEl.attr("buz_status", "unchecked");
                        }
                        break;
                    }
                }
            }
            if (this.onRemoveSelectRow && removeItem) {
                trigger === false || this._triggerCheckAll.call(this);
                this.onRemoveSelectRow.call(this, removeItem);
            }
            return this;
        },
        _triggerRow:function(checkEl, flag) {
            if (checkEl && checkEl.length > 0) {
                var trEl = checkEl.parents("tr");
                if (trEl.length > 0) {
                    if (flag === true) {
                        DOM.addClass(trEl.get(0), "grid-tr-checked");
                    } else {
                        DOM.removeClass(trEl.get(0), "grid-tr-checked");
                    }
                }
            }
        },
        addSelectRow:function(item, el, trigger) {
            if (item) {
                var checkboxEl = el || this.bodyEl.find("span[buz_value='" + item[this.valueField] + "']");
                if (checkboxEl.length > 0) {
                    this._triggerRow(checkboxEl, true);
                    var status = checkboxEl.attr("buz_status");
                    if (status === "unchecked") {
                        DOM.addClass(checkboxEl.get(0), "grid-checkbox-checked");
                        checkboxEl.attr("buz_status", "checked");
                        this.selectItems.push(item);
                        trigger === false || this._triggerCheckAll.call(this);
                        this.onAddSelectRow && this.onAddSelectRow.call(this, item);
                    }
                }
            }
            return this;
        },
        _triggerCheckAll:function() {
            if (this.selectModel === "multiSelect") {
                var checkAllEl = this.headEl.find(".grid-checkbox");
                if (this.items.length > 0) {
                    checkAllEl.show();
                    var uncheckLen = this.bodyEl.find(".grid-checkbox[buz_status='unchecked']").length;
                    if (uncheckLen === 0) {
                        checkAllEl.attr("buz_status", "checked");
                        DOM.addClass(checkAllEl.get(0), "grid-checkbox-checked");
                    } else {
                        checkAllEl.attr("buz_status", "unchecked");
                        DOM.removeClass(checkAllEl.get(0), "grid-checkbox-checked");
                    }
                } else {
                    checkAllEl.attr("buz_status", "unchecked");
                    checkAllEl.hide();
                }
            }
        },
        setSelectRows:function(p) {
            if (p instanceof Array) {
                this.selectItems = [];
                var len = p.length;
                for (var i = 0; i < len; i++) {
                    this.selectItems.push(p[i]);
                }
            }
            return this;
        }
    });
    A.apply(GP, {
        _renderPagination:function() {
            var html = [];
            html.push("<div class='grid-page-bg'><span style='float:left;'><table cellpadding='0' cellspacing='0' border='0'><tbody><tr>");
            html.push("<td><span class='grid-page-button grid-page-first-disabled' buz_type='first' buz_status='disable'></span></td>");
            html.push("<td><span class='grid-page-button grid-page-prev-disabled' buz_type='prev' buz_status='disable'></span></td>");
            html.push("<td><span class='grid-page-split'></span></td>");
            html.push("<td><span class='grid-page-content'>第</span>");
            html.push("<span class='cur-page grid-page-number'>0</span>");
            html.push("<span class='grid-page-content'>页</span></td>");
            html.push("<td style='padding-left:4px;'><span class='grid-page-content'>共</span>");
            html.push("<span class='total-page grid-page-number'>0</span>");
            html.push("<span class='grid-page-content'>页</span></td>");
            html.push("<td><span class='grid-page-split'></span></td>");
            html.push("<td><span class='grid-page-button grid-page-next-disabled' buz_type='next' buz_status='disable'></span></td>");
            html.push("<td><span class='grid-page-button grid-page-last-disabled' buz_type='last' buz_status='disable'></span></td>");
            html.push("<td><span class='grid-page-split'></span></td>");
            html.push("<td><span class='grid-page-button grid-page-refresh-disabled' buz_type='refresh' buz_status='disable'></span></td>");
            html.push("<td><span class='grid-page-split'></span></td>");
            html.push("<td><span class='page-size-wrap'><select class='page-size'><option value='10'>10</option>");
            html.push("<option value='20'>20</option><option value='30'>30</option><option value='40'>40</option>");
            html.push("<option value='50'>50</option></select></span></td></tr></tbody></table></span>");
            html.push("<span style='float:right;'><table cellpadding='0' cellspacing='0' border='0'><tbody><tr>");
            html.push("<td><span class='total-count'>没有数据需要显示</span></td></tr></tbody></table></span>");
            html.push("<span style='clear:both;'></span>");
            html.push("</div>");
            this.pageEl.html(html.join(""));
            this._bindPaginationEvent();
        },
        _bindPaginationEvent:function() {
            this.pageEl.bind({
                click:$.proxy(function(e) {
                    var tar = e.target,type = tar.getAttribute("buz_type");
                    var status = tar.getAttribute("buz_status");
                    if (type && status === "enable") {
                        if (type === "first") {
                            this.first();
                        } else if (type === "prev") {
                            this.previous();
                        } else if (type === "next") {
                            this.next();
                        } else if (type === "last") {
                            this.last();
                        } else if (type === "refresh") {
                            this.refresh();
                        }
                        e.stopPropagation();
                    }
                }, this),
                mousemove:function(e) {
                    var tar = e.target,type = tar.getAttribute("buz_type");
                    var status = tar.getAttribute("buz_status");
                    if (type && status === "enable") {
                        DOM.replaceClass(tar, "grid-page-button grid-page-" + type + "-hover");
                        e.stopPropagation();
                    }
                },
                mouseout:function(e) {
                    var tar = e.target,type = tar.getAttribute("buz_type");
                    var status = tar.getAttribute("buz_status");
                    if (type && status === "enable") {
                        DOM.replaceClass(tar, "grid-page-button grid-page-" + type);
                        e.stopPropagation();
                    }
                },
                mousedown:function(e) {
                    var tar = e.target,type = tar.getAttribute("buz_type");
                    var status = tar.getAttribute("buz_status");
                    if (type && status === "enable") {
                        DOM.addClass(tar, "grid-page-button grid-page-" + type + "-active");
                        e.stopPropagation();
                    }
                },
                mouseup:function(e) {
                    var tar = e.target,type = tar.getAttribute("buz_type");
                    var status = tar.getAttribute("buz_status");
                    if (type && status === "enable") {
                        DOM.removeClass(tar, "grid-page-button grid-page-" + type + "-active");
                        e.stopPropagation();
                    }
                }
            });
            this.pageEl.find(".page-size").change($.proxy(function(e) {
                var value = this.pageEl.find(".page-size").val();
                this.setPageSize(parseInt(value));
            }, this));
        },
        _resetPagination:function() {
            var pageEl = this.pageEl;
            var firstBtn = pageEl.find("span[buz_type='first']");
            var prevBtn = pageEl.find("span[buz_type='prev']");
            var nextBtn = pageEl.find("span[buz_type='next']");
            var lastBtn = pageEl.find("span[buz_type='last']");
            var refreshBtn = pageEl.find("span[buz_type='refresh']");
            var curPageSpan = pageEl.find("span.cur-page");
            var totalPageSpan = pageEl.find("span.total-page");
            var totalCountSpan = pageEl.find("span.total-count");
            if (this.curPage && this.totalCount) {
                this._enableButton(refreshBtn);
                curPageSpan.html(this.curPage);
                totalPageSpan.html(this.totalPage);
                totalCountSpan.html("共找到" + this.totalCount + "条记录");
                if (this.curPage < 2) {
                    this._disableButton(firstBtn);
                    this._disableButton(prevBtn);
                } else {
                    this._enableButton(firstBtn);
                    this._enableButton(prevBtn);
                }
                if (this.curPage >= this.totalPage) {
                    this._disableButton(nextBtn);
                    this._disableButton(lastBtn);
                } else {
                    this._enableButton(nextBtn);
                    this._enableButton(lastBtn);
                }
            } else {
                this._disableButton(firstBtn);
                this._disableButton(prevBtn);
                this._disableButton(nextBtn);
                this._disableButton(lastBtn);
                this._disableButton(refreshBtn);
                curPageSpan.html("0");
                totalPageSpan.html("0");
                totalCountSpan.html("没有数据需要显示");
            }
        },
        _enableButton:function(btn) {
            var btnStatus = btn.attr("buz_status");
            if (btnStatus === "disable") {
                var btnType = btn.attr("buz_type");
                DOM.replaceClass(btn.get(0), "grid-page-button grid-page-" + btnType);
                btn.attr("buz_status", "enable");
            }
        },
        _disableButton:function(btn) {
            var btnStatus = btn.attr("buz_status");
            if (btnStatus === "enable") {
                var btnType = btn.attr("buz_type");
                DOM.replaceClass(btn.get(0), "grid-page-button grid-page-" + btnType + "-disabled");
                btn.attr("buz_status", "disable");
            }
        }
    });
    Grid.prototype = GP;
    var C = {
        getAll:function() {
            return B.grids;
        },
        get:function(p) {
            var grids = B.grids,len = grids.length;
            for (var i = 0; i < len; i++) {
                var item = grids[i];
                if (p == item.id) {
                    return item;
                }
            }
            return null;
        },
        create:function(p) {
            var grid = new Grid(p);
            return {widgetId:grid.id,xtype:"grid"};
        }
    };
    A.addModule("grid", C);
})();