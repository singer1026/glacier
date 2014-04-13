<div id="win-{random}" class="win {cls}">
    <table cellspacing="0" cellpadding="0" border="0">
        <tr>
            <td class="win-top-left"></td>
            <td class="win-top-center">
                <div class="win-top-center-left">
                    <div class="win-icon-wrap">
                        <div class="win-icon win-icon-default"></div>
                    </div>
                    <div id="win-title-{random}" class="win-title"></div>
                    <div class="clear"/>
                </div>
                <div class="win-top-center-right">
                    <div class="win-button win-button-min"
                         onmouseover="athene.dom.addClass(this,'win-button-min-over');"
                         onmouseout="athene.dom.removeClass(this,'win-button-min-over');" title="最小化"/>
                    <div class="win-button win-button-max"
                         onmouseover="athene.dom.addClass(this,'win-button-max-over');"
                         onmouseout="athene.dom.removeClass(this,'win-button-max-over');" title="最大化"/>
                    <div class="win-button win-button-max-able"
                         onmouseover="athene.dom.addClass(this,'win-button-max-able-over');"
                         onmouseout="athene.dom.removeClass(this,'win-button-max-able-over');" title="还原"/>
                    <div class="win-button win-button-close"
                         onmouseover="athene.dom.addClass(this,'win-button-close-over');"
                         onmouseout="athene.dom.removeClass(this,'win-button-close-over');" title="关闭"/>
                    <div class="clear"/>
                </div>
                <div class="clear"/>
            </td>
            <td class="win-top-right"></td>
        </tr>
        <tr>
            <td class="win-center-left"></td>
            <td class="win-center-center">
                <div class="win-content"></div>
            </td>
            <td class="win-center-right"></td>
        </tr>
        <tr>
            <td class="win-bottom-left"></td>
            <td class="win-bottom-center"></td>
            <td class="win-bottom-right"></td>
        </tr>
    </table>
</div>
