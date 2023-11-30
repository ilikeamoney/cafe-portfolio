<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="btnStart" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="btnEnd" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="cafeList" scope="request" type="java.util.List"/>
<jsp:useBean id="currentPage" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="totalCafeCount" scope="request" type="java.lang.Integer"/>
<%--
  Created by IntelliJ IDEA.
  User: imjeongho
  Date: 11/24/23
  Time: 5:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Show All Cafe</title>
    <link href="<c:url value="/webjars/bootstrap/5.2.3/css/bootstrap.min.css"/>" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
</head>
<style>
    #content {
        font-family: Arial, sans-serif;
        justify-content: center;
        height: 100%;
        width: 100%;
        margin: 0;
    }

    #my-cafe-content {
        margin: 0 auto;
    }

    #my-cafe-content tr td {
        padding: 5px 15px;
        text-align: center;
    }

    #box {
        width: 70px;
        height: 70px;
        border-radius: 70%;
        overflow: hidden;
    }

    #cafeImg {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }
</style>
<body>
<jsp:include page="login_main.jsp"/>
<section id="content">
    <table id="my-cafe-content">
        <tr>
            <td colspan="6">
                <h3>현재 ${totalCafeCount}개의 카페가 활동중 입니다.</h3>
            </td>
        </tr>
        <tr>
            <td>번호</td>
            <td>이미지</td>
            <td colspan="2">제목</td>
            <td>장르</td>
            <td>창설일</td>
        </tr>
        <c:if test="${cafeList ne null}">
            <c:forEach var="cafe" items="${cafeList}">
                <tr>
                    <td>${cafe.id}</td>
                    <td>
                        <div id="box">
                            <img id="cafeImg" src="<c:url value="/image/${cafe.cafeImg}"/>" alt="img">
                        </div>
                    </td>
                    <td colspan="2">
                        <a href="<c:url value="/cafe-detail?cafeId=${cafe.id}"/>">${cafe.title}</a>
                    </td>
                    <td>${cafe.genre}</td>
                    <td>${cafe.createDate}</td>
                </tr>
            </c:forEach>
            <tr>
                <td colspan="2">
                    <button onclick="window.location.href=window.location.href='<c:url value="/show-all-cafe?minusBtn=true"/>'" class="btn btn-primary"><</button>
                </td>
                <td colspan="3">
                    <c:forEach var="i" begin="${btnStart}" end="${btnEnd - 1}">
                        <c:choose>
                            <c:when test="${currentPage eq i + 1}">
                                <a href="<c:url value="/show-all-cafe?page=${i + 1}"/>" style="font-weight: bold">${i + 1}</a>
                            </c:when>
                            <c:otherwise>
                                <a href="<c:url value="/show-all-cafe?page=${i + 1}"/>">${i + 1}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </td>
                <td>
                    <button onclick="window.location.href='<c:url value="/show-all-cafe?plusBtn=true"/>'" class="btn btn-primary">></button>
                </td>
            </tr>
        </c:if>
    </table>
</section>
<script src="<c:url value="/webjars/bootstrap/5.2.3/js/bootstrap.min.js"/>" rel="stylesheet"></script>
</body>
<script>
    $(document).ready(function () {

    });
</script>
</html>