<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: imjeongho
  Date: 11/28/23
  Time: 2:33 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cafe Remove Pro</title>
</head>
<script>
    window.location.href='<c:url value="/cafe-detail?cafeId=${cafeId}"/>'
</script>
</html>
