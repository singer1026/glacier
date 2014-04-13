<@ExistError/>
<div class="fm-div">
    <div id="password-edit-${number}" style="margin-left:20px;">
    <@Password td="false" label="输入原密码" binding="passwordOld"/>
    <@Password td="false" label="输入新密码" binding="passwordNew"/>
    <@Password td="false" label="确认新密码" binding="passwordConfirm"/>
        <div style="text-align:center;margin-left:-30px;">
        <@Button label="保 存" iconCls="icon-save">
            athene.form.get('password-edit-${number}').update();
        </@Button>
        <@Button label="关 闭" iconCls="icon-close">
            athene.window.get('${number}').close();
        </@Button>
        </div>
    </div>
</div>
<script type="text/javascript">
    $I("password-edit-${number}").doFocus();
    athene.form.create({
        id:"password-edit-${number}",
        binding:"${number}",
        updateUrl:"passwordUpdate",
        onUpdate:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "修改成功！";
            win.close();
        },
        items:[
            {id:"password-old-${number}",type:"Password"},
            {id:"password-new-${number}",type:"Password"},
            {id:"password-confirm-${number}",type:"Password"}
        ]
    });
</script>