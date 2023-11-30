<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: imjeongho
  Date: 11/27/23
  Time: 2:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="currentPage" scope="session" type="java.lang.Integer"/>
<jsp:useBean id="cafeId" scope="request" type="java.lang.Integer"/>
<html>
<head>
    <title>Button Init</title>
</head>
<script>
  window.location.href = '<c:url value="/cafe-detail?cafeId=${cafeId}&page=${currentPage}"/>';
</script>
</html>
