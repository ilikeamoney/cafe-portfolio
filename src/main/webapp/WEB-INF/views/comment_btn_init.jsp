<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: imjeongho
  Date: 11/30/23
  Time: 10:58 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="boardId" scope="request" type="java.lang.Integer"/>
<html>
<head>
    <title>Title</title>
</head>
<script>
    window.location.href = '<c:url value="/board-detail?boardId=${boardId}&page=${currentPage}"/>';
</script>
</html>
