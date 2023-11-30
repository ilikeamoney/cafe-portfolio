<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>Show Join Cafe</title>
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
        <c:choose>
            <c:when test="${cafeList ne null}">
                <tr>
                    <td colspan="5">
                        <h3>${member.name}님이 가입하신 ${cafeSize}개 카페목록</h3>
                    </td>
                </tr>
                <tr>
                    <td>번호</td>
                    <td>이미지</td>
                    <td>제목</td>
                    <td>장르</td>
                    <td>창설일</td>
                </tr>
                <c:forEach var="cafe" items="${cafeList}">
                    <tr>
                        <td>${cafe.id}</td>
                        <td>
                            <div id="box">
                                <img id="cafeImg" src="<c:url value="/image/${cafe.cafeImg}"/>" alt="img">
                            </div>
                        </td>
                        <td>
                            <a href="<c:url value="/cafe-detail?cafeId=${cafe.id}"/>">${cafe.title}</a>
                        </td>
                        <td>${cafe.genre}</td>
                        <td>${cafe.createDate}</td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td>
                        <h2>${member.name}님</h2>
                        <h2>좋아하는 장르의 카페에 가입하여 의견을 나눠보세요 !</h2>
                    </td>
                </tr>
                <tr>
                    <td>
                        <button onclick="window.location.href='<c:url value="/show-all-cafe"/>'"
                                class="btn btn-success">카페 찾아보기
                        </button>
                    </td>
                </tr>
            </c:otherwise>
        </c:choose>
    </table>
</section>
<script src="<c:url value="/webjars/bootstrap/5.2.3/js/bootstrap.min.js"/>" rel="stylesheet"></script>
</body>
</html>