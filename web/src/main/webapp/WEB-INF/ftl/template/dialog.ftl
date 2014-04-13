<div id="dialog-{random}" class="dialog">
    <table cellspacing="0" cellpadding="0" border="0">
        <tr>
            <td class="dialog-top-left"></td>
            <td class="dialog-top-center">
                <div id="dialog-icon-title-{random}" class="dialog-icon-title">
                    <div class="dialog-icon">
                        <span class='dialog-icon-img-small {dialogIcon}-small'/>
                    </div>
                    <div class="dialog-title"></div>
                </div>
                <div class="dialog-button-close" onmouseover="athene.dom.addClass(this,'dialog-button-close-over');"
                     onmouseout="athene.dom.removeClass(this,'dialog-button-close-over');" title="关闭"/>
                <div class="clear"/>
            </td>
            <td class="dialog-top-right"></td>
        </tr>
        <tr>
            <td class="dialog-center-left"></td>
            <td class="dialog-center-center">
                <div class="dialog-content">
                    <div class="dialog-content-message">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td class="dialog-content-message-icon" valign="top">
                                    <span class='dialog-icon-img {dialogIcon}'/>
                                </td>
                                <td class="dialog-content-message-word" valign="top"></td>
                            </tr>
                        </table>
                    </div>
                    <div class="dialog-content-button dialog-content-button-confirm">
                    <@Button label="确 认" cls="btn-gray dialog-button-ok" iconCls="icon-confirm"></@Button>
                            <@Button label="取 消" cls="btn-gray dialog-button-cancel" iconCls="icon-cancel"></@Button>
                    </div>
                    <div class="dialog-content-button dialog-content-button-other">
                    <@Button label="确 认" cls="btn-gray dialog-button-ok" iconCls="icon-confirm"></@Button>
                    </div>
                </div>
            </td>
            <td class="dialog-center-right"></td>
        </tr>
        <tr>
            <td class="dialog-bottom-left"></td>
            <td class="dialog-bottom-center"></td>
            <td class="dialog-bottom-right"></td>
        </tr>
    </table>
</div>
