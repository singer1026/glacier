<#include "head.ftl"/>
<body>
<script type="text/javascript">
    $.cookie("_glacier_", null, { path: '/' });
    window.location.href = "${basePath}login";
</script>
</body>
</html>