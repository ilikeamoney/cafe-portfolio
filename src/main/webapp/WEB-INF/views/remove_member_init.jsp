<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: imjeongho
  Date: 11/30/23
  Time: 12:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="cafeId" scope="request" type="java.lang.Integer"/>
<html>
<head>
    <title>Remove Member Init</title>
</head>
<script>
  window.location.href = '<c:url value="/show-my-member?cafeId=${cafeId}"/>';
</script>
</html>
