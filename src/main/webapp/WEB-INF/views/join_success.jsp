<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: imjeongho
  Date: 11/23/23
  Time: 4:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Success Page</title>
    <link href="<c:url value="/webjars/bootstrap/5.2.3/css/bootstrap.min.css"/>" rel="stylesheet">
</head>
<style>
    body {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 100vh; /* 화면 높이에 맞추기 위한 설정 */
        margin: 0;
        font-family: Arial, sans-serif;
    }

    table {
        margin: 0 auto;
    }

    table tr td {
        text-align: center;
        width: 400px;
        height: 60px;
    }
</style>
<body>
<table>
    <tr>
        <td>
            <h1>회원가입을 축하합니다!</h1>
        </td>
    </tr>
    <tr>
        <td>
            <button type="button" id="success-home-btn" class="btn btn-success" onclick="window.location.href='<c:url value="/main"/>'">메인페이지 이동</button>
        </td>
    </tr>
</table>
<script src="<c:url value="/webjars/bootstrap/5.2.3/js/bootstrap.min.js"/>" rel="stylesheet"></script>
</body>
</html>
