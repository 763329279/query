<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>query</title>
</head>
<body>
<form action="/v1/indexQuery" method="get">
    快递公司：
     <select name="type" >
             
        <option value="" >请选择</option>
             
        <option value="jd" [#if type?? && type! == 'jd']selected[/#if]>京东</option>
             
        <option value="shunfeng" [#if type?? && type! == 'shunfeng']selected[/#if]>顺丰</option>
             
        <option value="wjkwl" [#if type?? && type! == 'wjkwl']selected[/#if]>万家康</option>
     </select>

    单号：<input type="text" name="postId" value="${postId!''}"/>

    手机号：<input type="text" name="phone" value="${phone!''}"  alt="顺丰输入手机后四位" />

    <input type="submit" value="提交"/>  
</form>
<br>
<#if error??>
    <label>${error}</label>
</#if>

<#if query??>
    状态:${query.stateName} <br>
    <#list query.data as data>
    　　${data.ftime ?string('yyyy-MM-dd hh:mm:ss')} : ${data.context} <br>
    </#list>
</#if>
</body>
</html>
