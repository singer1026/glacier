<@ExistError/>
<#if !error??>
<div style="line-height:20px;">
    <div class="fl">
        <div class="fl" style="width:120px;font-weight:bold;color:orange;padding-bottom:2px;">
            今日支出
        </div>
        <div class="fl" style="width:80px;">
            <span class="mr5">${expenseTotal}</span>
        </div>
        <div class="clear"/>
    </div>
    <div class="fl">
        <div class="fl" style="width:120px;font-weight:bold;color:orange;padding-bottom:2px;">
            今日收入
        </div>
        <div class="fl" style="width:80px;">
            <span class="mr5">${incomeTotal}</span>
        </div>
        <div class="clear"/>
    </div>
    <div class="fl">
        <div class="fl" style="width:120px;font-weight:bold;color:orange;padding-bottom:2px;">
            今日盈利
        </div>
        <div class="fl" style="width:80px;">
            <#if dayProfit?starts_with("-")>
                <span class="cgreen">${dayProfit}</span>
                <#elseif dayProfit=="0.0">
                    <span class="mr5">${dayProfit}</span>
                <#else>
                    <span class="cr">${dayProfit}</span>
            </#if>
        </div>
        <div class="clear"/>
    </div>
    <div class="clear"/>
</div>
</#if>