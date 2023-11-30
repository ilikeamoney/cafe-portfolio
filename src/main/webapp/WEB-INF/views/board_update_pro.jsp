<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: imjeongho
  Date: 11/29/23
  Time: 12:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="boardId" scope="request" type="java.lang.Integer"/>
<html>
<head>
    <title>Board Update</title>
</head>
<body>
<p>${boardId}</p>
</body>
</html>
