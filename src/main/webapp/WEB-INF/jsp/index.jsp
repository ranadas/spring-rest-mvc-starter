<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" rel="stylesheet">
    <title>title of index</title>
</head>
<body>
<div class="container">
    <h4>${title}</h4>

    <spring:message code="message.index" arguments="${message}" htmlEscape="true" />
</div>

</body>
</html>
