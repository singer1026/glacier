<@ExistError/>
<#if !error??>
<div style="line-height:20px;">
    <div class="fl">
        <div class="fl" style="width:120px;font-weight:bold;color:orange;padding-bottom:2px;">
            昨日盈利
        </div>
        <div class="fl" style="text-align:right;padding-right:20px;width:80px;">
            <#if yesterdayProfit?starts_with("-")>
                <span class="cgreen">${yesterdayProfit}</span>
                <#elseif yesterdayProfit=="0.0">
                ${yesterdayProfit}
                <#else>
                    <span class="cr">${yesterdayProfit}</span>
            </#if>
        </div>
        <div class="clear"/>
    </div>
    <div class="fl">
        <div class="fl" style="width:120px;font-weight:bold;color:orange;padding-bottom:2px;">
            昨日账户余额
        </div>
        <div class="fl" style="text-align:right;padding-right:20px;width:80px;">
            <#if yesterdayBalance?starts_with("-")>
                <span class="cgreen">${yesterdayBalance}</span>
                <#elseif yesterdayBalance=="0.0">
                ${yesterdayBalance}
                <#else>
                    <span class="cr">${yesterdayBalance}</span>
            </#if>
        </div>
        <div class="clear"/>
    </div>
    <div class="fl">
        <div class="fl" style="width:120px;font-weight:bold;color:orange;padding-bottom:2px;">
            今日支出总和
        </div>
        <div class="fl" style="text-align:right;padding-right:20px;width:80px;">
        ${expenseTotal}
        </div>
        <div class="clear"/>
    </div>
    <div class="fl">
        <div class="fl" style="width:120px;font-weight:bold;color:orange;padding-bottom:2px;">
            今日收入总和
        </div>
        <div class="fl" style="text-align:right;padding-right:20px;width:80px;">
        ${incomeTotal}
        </div>
        <div class="clear"/>
    </div>
    <div class="fl">
        <div class="fl" style="width:120px;font-weight:bold;color:orange;padding-bottom:2px;">
            今日盈利
        </div>
        <div class="fl" style="text-align:right;padding-right:20px;width:80px;">
            <#if dayProfit?starts_with("-")>
                <span class="cgreen">${dayProfit}</span>
                <#elseif dayProfit=="0.0">
                ${dayProfit}
                <#else>
                    <span class="cr">${dayProfit}</span>
            </#if>
        </div>
        <div class="clear"/>
    </div>
    <div class="fl">
        <div class="fl" style="width:120px;font-weight:bold;color:orange;padding-bottom:2px;">
            今日账户余额
        </div>
        <div class="fl" style="text-align:right;padding-right:20px;width:80px;">
            <#if todayBalance?starts_with("-")>
                <span class="cgreen">${todayBalance}</span>
                <#elseif todayBalance=="0.0">
                ${todayBalance}
                <#else>
                    <span class="cr">${todayBalance}</span>
            </#if>
        </div>
        <div class="clear"/>
    </div>
    <div class="clear"/>
</div>
</#if>