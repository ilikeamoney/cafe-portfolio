<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Main</title>
</head>
<body>
<c:choose>
    <c:when test="${memLog ne null}">
        <jsp:include page="login_main.jsp"/>
    </c:when>
    <c:otherwise>
        <jsp:include page="basic_main.jsp"/>
    </c:otherwise>
</c:choose>
</body>
</html>
