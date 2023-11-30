<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%--
  Created by IntelliJ IDEA.
  User: imjeongho
  Date: 11/30/23
  Time: 11:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="boardId" scope="request" type="java.lang.Integer"/>
<html>
<head>
    <title>Delete Comment Init</title>
</head>
<script>
  window.location.href = '<c:url value="/board-detail?boardId=${boardId}&page=1"/>';
</script>
</html>
