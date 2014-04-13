<@ExistError/>
<div class="fm-div">
    <table id="user-edit-${number}" style="width:550px;margin-left:0px;" cellpadding="0" cellspacing="0">
        <tr>
        <@LabelField label="登录名" name="loginId">
            <span class="fm-readonly">${user.loginId}</span>
            <input type="hidden" name="loginId" id="login-id-${number}" value="${user.loginId}"/>

            <div class="clear"/>
        </@LabelField>
        <@LabelField label="姓名">
            <span class="fm-readonly">${user.name}</span>

            <div class="clear"/>
        </@LabelField>
        </tr>
        <tr>
        <@TextField label="QQ" binding="user.qq"/>
        <@TextField label="电子邮件" binding="user.email"/>
        </tr>
        <tr>
        <@TextField label="固定电话" binding="user.telephone"/>
        <@TextField label="手机号码" binding="user.mobile" require="true"/>
        </tr>
        <tr>
            <td colspan="2">
            <@TextField td="false" label="地址" binding="user.address" style="width:440px;"/>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center" valign="top">
            <@Button label="保 存" iconCls="icon-save">
                athene.form.get('user-edit-${number}').update();
            </@Button>
            <@Button label="关 闭" iconCls="icon-close">
                athene.window.get('${number}').close();
            </@Button>
            <@Button label="修改密码" iconCls="icon-edit">
                page.passwordManager(this);
            </@Button>
            </td>
        </tr>
    </table>
</div>
<script type="text/javascript">
    $I("user-edit-${number}").doFocus();
    athene.form.create({
        id:"user-edit-${number}",
        binding:"${number}",
        updateUrl:"userOwnUpdate",
        dialogWidth:400,
        messageUpdate:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要更新的个人资料</div>" + confirmRow;
        },
        onUpdate:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "更新个人资料成功！";
            win.close();
        },
        items:[
            {id:"login-id-${number}",type:"Hidden"},
            {id:"qq-${number}",type:"TextField",label:"QQ"},
            {id:"email-${number}",type:"TextField",label:"电子邮件"},
            {id:"telephone-${number}",type:"TextField",label:"固定电话"},
            {id:"mobile-${number}",type:"TextField",label:"手机号码"},
            {id:"address-${number}",type:"TextField",label:"地址"}
        ]
    });
</script>