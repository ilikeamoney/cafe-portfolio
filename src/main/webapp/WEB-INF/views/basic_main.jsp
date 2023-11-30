<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: imjeongho
  Date: 11/23/23
  Time: 12:19 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Basic Main</title>
    <link href="<c:url value="/webjars/bootstrap/5.2.3/css/bootstrap.min.css"/>" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
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
<div id="log-con">
    <table class="table-primary">
        <tr>
            <td>
                <h2>WellCome To My Web</h2>
            </td>
        </tr>
        <tr>
            <td>
                <button id="login-btn" type="button" class="btn btn-primary">로그인</button>
            </td>
        </tr>
        <tr>
            <td>
                <button id="join-btn" type="button" class="btn btn-secondary">회원가입</button>
            </td>
        </tr>
    </table>
</div>
<script src="<c:url value="/webjars/bootstrap/5.2.3/js/bootstrap.min.js"/>" rel="stylesheet"></script>
</body>
</html>
<script>
    $(document).ready(function () {
        $("#login-btn").on("click", function () {
            window.location.href = '<c:url value="/login"/>';
        });

        $("#join-btn").on("click", function () {
            window.location.href = '<c:url value="/join"/>';
        });
    });
</script>
