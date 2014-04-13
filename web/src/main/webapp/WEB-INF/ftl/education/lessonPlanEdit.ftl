<@ExistError/>
<div id="lp-edit-${number}" style="width:350px;margin:5px auto;">
<@ComboBox td="false" label="周期" binding="lessonPlan.dayOfWeek" require="true"
list=dayOfWeekList listKey="name" listValue="value"/>
<@LabelField td="false" label="开始时间" require="true" name="startTime">
<@ComboBox td="false" binding="lessonPlan.startHour" style="width:71px;" list=hourList
listKey="name" listValue="value" layout="false" error="false"/>
    <label style="margin:0px 2px;">时</label>
<@ComboBox td="false" binding="lessonPlan.startMinute" style="width:71px;"
list=minuteList listKey="name" listValue="value" layout="false" error="false"/>
    <label style="margin:0px 2px;">分</label>
</@LabelField>
<@ComboBox td="false" label="时长" binding="lessonPlan.timeLength" require="true"
list=lessonTimeLengthList listKey="name" listValue="value"/>
<@ComboBox td="false" label="频率" binding="lessonPlan.frequency" require="true"
list=lessonFrequencyList listKey="name" listValue="value"/>
<@ComboBox td="false" label="教室" binding="lessonPlan.classroomId"
list=classroomList listKey="name" listValue="id"/>
    <div style="text-align:center;">
    <@Button label="保 存" iconCls="icon-save">
        <#if lessonPlan??>
            athene.form.get('lp-edit-${number}').update();
            <#else >
                athene.form.get('lp-edit-${number}').save();
        </#if>
    </@Button>
    <@Button label="关 闭" iconCls="icon-close">
        athene.window.get('${number}').close();
    </@Button>
    </div>
</div>
<script type="text/javascript">
    athene.form.create({
        id:"lp-edit-${number}",
        binding:"${number}",
        saveUrl:"lessonPlanSave",
        updateUrl:"lessonPlanSave",
        items:[
            {id:"day-of-week-${number}",type:"ComboBox",label:"周期"},
            {id:"start-hour-${number}",type:"ComboBox",label:"开始小时"},
            {id:"start-minute-${number}",type:"ComboBox",label:"开始分钟"},
            {id:"time-length-${number}",type:"ComboBox",label:"时长"},
            {id:"classroom-id-${number}",type:"ComboBox",label:"教室"},
            {id:"frequency-${number}",type:"ComboBox",label:"频率"}
        ],
    <#if lessonPlan??>
        onUpdate:function(row, d) {
            athene.window.get(${number}).onClose = function() {
                var grid = athene.grid.get("lp-list-${openerNumber}");
                grid && grid.replaceRow(${index}, d.data);
            };
            athene.window.get(${number}).close();
        },
    </#if>
        onSave:function(row, d) {
            athene.window.get(${number}).onClose = function() {
                var grid = athene.grid.get("lp-list-${openerNumber}");
                grid && grid.addRow(d.data);
            };
            athene.window.get(${number}).close();
        }
    });
</script>
