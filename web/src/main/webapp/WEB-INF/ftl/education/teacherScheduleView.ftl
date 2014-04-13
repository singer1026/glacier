<@ExistError/>
<div class="fm-div">
    <table class="table-statistics">
        <thead>
        <tr>
            <th class="schedule-td"></th>
            <th class="schedule-head">星期一</th>
            <th class="schedule-head">星期二</th>
            <th class="schedule-head">星期三</th>
            <th class="schedule-head">星期四</th>
            <th class="schedule-head">星期五</th>
            <th class="schedule-head">星期六</th>
            <th class="schedule-head">星期日</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="schedule-time">上午</td>
        <#list AMList as dayList>
            <td class="schedule-td">
                <table cellpadding="0" cellspacing="0" class="schedule-inner-table">
                    <tbody>
                        <#list dayList as lessonPlan>
                        <tr class="<#if lessonPlan_index%2==0>schedule-odd<#else>schedule-even</#if>">
                            <td>
                                <div class="pl2">
                                    <strong>${lessonPlan.subjectName}</strong>
                                    <span>[${lessonPlan.type}]</span>
                                    <#if lessonPlan.classroomName??>
                                        <span>[${lessonPlan.classroomName}]</span>
                                    </#if>
                                </div>
                                <div>${lessonPlan.startTimeDisplay}-${lessonPlan.endTimeDisplay}
                                    &nbsp;${lessonPlan.timeLength}分钟
                                </div>
                                <#list lessonPlan.lessonStudentList as student>
                                    <div class="pl2">${student.studentName}</div>
                                </#list>
                            </td>
                        </tr>
                        </#list>
                    </tbody>
                </table>
            </td>
        </#list>
        </tr>
        <tr>
            <td class="schedule-time">下午</td>
        <#list PMList as dayList>
            <td class="schedule-td">
                <table cellpadding="0" cellspacing="0" class="schedule-inner-table">
                    <tbody>
                        <#list dayList as lessonPlan>
                        <tr class="<#if lessonPlan_index%2==0>schedule-odd<#else>schedule-even</#if>">
                            <td>
                                <div class="pl2">
                                    <strong>${lessonPlan.subjectName}</strong>
                                    <span>[${lessonPlan.type}]</span>
                                    <#if lessonPlan.classroomName??>
                                        <span>[${lessonPlan.classroomName}]</span>
                                    </#if>
                                </div>
                                <div>${lessonPlan.startTimeDisplay}-${lessonPlan.endTimeDisplay}
                                    &nbsp;${lessonPlan.timeLength}分钟
                                </div>
                                <#list lessonPlan.lessonStudentList as student>
                                    <div class="pl2">${student.studentName}</div>
                                </#list>
                            </td>
                        </tr>
                        </#list>
                    </tbody>
                </table>
            </td>
        </#list>
        </tr>
        </tbody>
    </table>
    <div style="margin-top: 8px;text-align: center;">
    <@Button label="导出功课表Excel" iconCls="icon-excel">
        page.teacherScheduleExcel(${id});
    </@Button>
    </div>
</div>