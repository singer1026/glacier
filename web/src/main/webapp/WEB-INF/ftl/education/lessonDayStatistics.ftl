<@ExistError/>
<div class="fm-div">
    <table style="margin:10px auto;" class="table-statistics">
        <thead>
        <tr>
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
        <#list dayStatistics as dayList>
            <td class="schedule-td">
                <table cellpadding="0" cellspacing="0" class="schedule-inner-table">
                    <tbody>
                        <#list dayList as teacherLesson>
                        <tr class="<#if teacherLesson_index%2==0>schedule-odd<#else>schedule-even</#if>">
                            <td>
                                <div style="padding-left:2px;width: 165px;">
                                    <span class="inline" style="width: 40px;font-weight: bold;">
                                    ${teacherLesson.teacherName!}
                                    </span>
                                    <#if (teacherLesson.count>=5)>
                                        <span style="width:${teacherLesson.count*12}px;font-size:10px;"
                                              class="strip-default strip-orange">${teacherLesson.count}</span>
                                        <#else>
                                            <span style="width:${teacherLesson.count*12}px;font-size:10px;"
                                                  class="strip-default strip-green">${teacherLesson.count}</span>
                                    </#if>
                                </div>
                                <div style="padding-left:2px;">
                                    <span>${teacherLesson.subjectName!}</span>
                                    <span>${teacherLesson.startTime!}-${teacherLesson.endTime!}</span>
                                </div>
                            </td>
                        </tr>
                        </#list>
                    </tbody>
                </table>
            </td>
        </#list>
        </tr>
        <tr style="line-height:20px;">
        <#list dayTotalStatistics as dayTotal>
            <td class="schedule-td" style="background-color:white;color:#ff5500">
                <div style="padding-left:2px;">
                    <span><strong>${dayTotal.teacherCount}</strong>位老师</span>
                    <span><strong>${dayTotal.lessonCount}</strong>节课</span>
                </div>
                <div style="padding-left:2px;">
                    <#list dayTotal.subjectNames as subjectName>
                        <span>${subjectName}</span>
                        <#if (subjectName_index+1)%4==0>
                            <br/>
                        </#if>
                    </#list>
                </div>
            </td>
        </#list>
        </tr>
        <tr style="line-height:20px;">
            <td class="schedule-td" style="background-color:white;color:#ff5500" colspan="7">
                <div style="padding-left:2px;">
                    <span><strong>${totalStatistics.lessonCount}</strong>节课</span>
                    <span><strong>${totalStatistics.subjectCount}</strong>个科目</span>
                <#list totalStatistics.subjectNames as subjectName>
                    <span>${subjectName}</span>
                </#list>
                </div>
                <div style="padding-left:2px;">
                    <span><strong>${totalStatistics.teacherCount}</strong>位老师</span>
                <#list totalStatistics.teacherNames as teacherName>
                    <span>${teacherName}</span>
                </#list>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>