<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: imjeongho
  Date: 11/23/23
  Time: 12:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login Main</title>
    <link href="<c:url value="/webjars/bootstrap/5.2.3/css/bootstrap.min.css"/>" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
</head>
<style>
    header {
        font-family: Arial, sans-serif;
        display: flex;
        align-items: center;
        justify-content: center;
        height: 10vh;
        margin: 0;
    }

    #nav-bar {
        padding: 10px;
        color: white;
    }

    #nav-bar button {
        margin-right: 10px;
    }
</style>
<body>
<header id="header">
    <div id="nav-bar">
        <table>
            <tr>
                <td>
                    <button id="view-all-cafe-btn" type="button" class="btn btn-dark">전체 카페</button>
                </td>
                <td>
                    <button id="my-join-cafe-btn" type="button" class="btn btn-dark">가입한 카페</button>
                </td>
                <c:choose>
                    <c:when test="${cafeId ne null}">
                        <td>
                            <button id="my-cafe-btn" type="button" class="btn btn-dark">나의 카페</button>
                        </td>
                    </c:when>
                    <c:otherwise>
                        <td>
                            <button id="create-cafe-btn" type="button" class="btn btn-success">카페 생성</button>
                        </td>
                    </c:otherwise>
                </c:choose>
                <td>
                    <button id="logout-btn" type="button" class="btn btn-danger">로그아웃</button>
                </td>
            </tr>
        </table>
    </div>
    <div>
        <table>
            <tr>
                <td></td>
            </tr>
        </table>
    </div>
</header>
<script src="<c:url value="/webjars/bootstrap/5.2.3/js/bootstrap.min.js"/>" rel="stylesheet"></script>
</body>
<script>
    $(document).ready(function () {
        $("#logout-btn").on("click", function () {
            window.location.href = '<c:url value="/logout"/>';
        });

        // 카페 생성
        $("#create-cafe-btn").on("click", function () {
            window.location.href = '<c:url value="/create-cafe-form"/>';
        });

        $("#view-all-cafe-btn").on("click", function () {
            window.location.href = '<c:url value="/show-all-cafe"/>';
        });

        $("#my-join-cafe-btn").on("click", function () {
            window.location.href = '<c:url value="/show-join-cafe"/>';
        });

        $("#my-cafe-btn").on("click", function () {
            window.location.href = '<c:url value="/show-my-cafe"/>';
        });
    });
</script>
</html>
