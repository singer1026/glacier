<head>
    <base href="${basePath}"/>
    <title>琴音汇钢琴音乐艺术中心</title>
<#if minScript??&&minScript>
    <link rel="stylesheet" type="text/css" media="all"
          href="${basePath}styles/src/athene-all.css?version=${scriptVersion}"/>
    <#else >
        <link rel="stylesheet" type="text/css" media="all" href="${basePath}styles/athene-all-import.css"/>
</#if>
    <script type="text/javascript" src="${basePath}scripts/jquery-1.7.min.js"></script>
<#if minScript??&&minScript>
    <script type="text/javascript" src="${basePath}scripts/athene-all.min.js?version=${scriptVersion}"></script>
    <#else >
        <script type="text/javascript" src="${basePath}scripts/src/jquery.cookie.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/jquery.json-2.3.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.core.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.extend.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.environment.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.dom.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.util.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.interaction.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.task.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.taskbar.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.dmenubar.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.window.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.windowmanager.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.desktop.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.dialog.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.grid.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.log.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.form.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.listfield.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.checkgroup.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.message.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/athene.page.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/biz/biz.teacher.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/biz/biz.student.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/biz/biz.lesson.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/biz/biz.expense.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/biz/biz.income.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/biz/biz.task.js"></script>
        <script type="text/javascript" src="${basePath}scripts/src/biz/biz.accountStatistics.js"></script>
</#if>
    <script type="text/javascript" src="${basePath}My97DatePicker/WdatePicker.js"></script>
</head>



