<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: imjeongho
  Date: 11/27/23
  Time: 4:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="cafe" type="com.example.cafe.domain.Cafe" scope="request"/>
<jsp:useBean id="btnStart" type="java.lang.Integer" scope="request"/>
<jsp:useBean id="btnEnd" type="java.lang.Integer" scope="request"/>
<jsp:useBean id="currentPage" type="java.lang.Integer" scope="request"/>
<jsp:useBean id="cafeBoards" type="java.util.List" scope="request"/>
<jsp:useBean id="totalBoardCount" type="java.lang.Integer" scope="request"/>

<html>
<head>
    <title>Cafe Detail</title>
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
        padding: 10px 30px;
        text-align: center;
    }

    #box {
        width: 100px;
        height: 100px;
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
            <td>
                <div id="box">
                    <img id="cafeImg" src="/image/${cafe.cafeImg}" alt="img"/>
                </div>
            </td>
            <td colspan="3">
                <h3>${cafe.title}</h3>
            </td>
        </tr>
        <tr>
            <td colspan="4">
                <h6>게시글 ${totalBoardCount}개</h6>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <button id="cafe-info-btn" class="btn btn-success">카페소개</button>
            </td>
            <c:choose>
                <c:when test="${crewCheck}">
                    <td>
                        <button id="cafe-write-board-btn" class="btn btn-primary">글쓰기</button>
                    </td>
                    <td>
                        <button id="remove-join-cafe-btn" class="btn btn-danger">탈퇴하기</button>
                    </td>
                </c:when>
                <c:when test="${adminCheck}">
                    <td>
                        <button id="show-my-crew-btn" class="btn btn-primary">멤버목록</button>
                    </td>
                    <td colspan="2">
                        <button id="remove-cafe-btn" class="btn btn-danger">카페삭제</button>
                    </td>
                </c:when>
                <c:otherwise>
                    <td colspan="2">
                        <button id="join-cafe-btn" class="btn btn-primary">가입하기</button>
                    </td>
                </c:otherwise>
            </c:choose>
        </tr>
        <tr id="cafe-info" style="display: none">
            <td colspan="4">
                <label>
                    <textarea cols="55" rows="15" disabled="disabled">${cafe.introduce}</textarea>
                </label>
            </td>
        </tr>
        <tr>
            <td>게시글 번호</td>
            <td>제목</td>
            <td>작성일</td>
            <td>조횟수</td>
        </tr>
        <c:if test="${cafeBoards.size() gt 0}">
            <c:forEach var="board" items="${cafeBoards}">
                <tr>
                    <td>${board.id}</td>
                    <td>
                        <a href="<c:url value="/board-detail?boardId=${board.id}"/>">${board.title}</a>
                    </td>
                    <td>${board.writeDate}</td>
                    <td>${board.viewCount}</td>
                </tr>
            </c:forEach>
            <tr>
                <td>
                    <button onclick="window.location.href=window.location.href='<c:url
                            value="/cafe-detail?cafeId=${cafe.id}&minusBtn=true"/>'" class="btn btn-primary"><
                    </button>
                </td>
                <td colspan="2">
                    <c:forEach var="i" begin="${btnStart}" end="${btnEnd - 1}">
                        <c:choose>
                            <c:when test="${currentPage eq i + 1}">
                                <a href="<c:url value="/cafe-detail?cafeId=${cafe.id}&page=${i + 1}"/>"
                                   style="font-weight: bold">${i + 1}</a>
                            </c:when>
                            <c:otherwise>
                                <a href="<c:url value="/cafe-detail?cafeId=${cafe.id}&page=${i + 1}"/>">${i + 1}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </td>
                <td>
                    <button onclick="window.location.href='<c:url
                            value="/cafe-detail?cafeId=${cafe.id}&plusBtn=true"/>'" class="btn btn-primary">>
                    </button>
                </td>
            </tr>
        </c:if>
    </table>
</section>
<script src="<c:url value="/webjars/bootstrap/5.2.3/js/bootstrap.min.js"/>" rel="stylesheet"></script>
</body>
<script>
    $(document).ready(function () {
        // 카페 소개 버튼
        $("#cafe-info-btn").on("click", function () {
            $("#cafe-info").toggle()
        });

        // 글쓰기 버튼
        $("#cafe-write-board-btn").on("click", function () {
            window.location.href = '<c:url value="/join-cafe-write-board?cafeId=${cafe.id}"/>';
        });

        // 탈퇴하기 버튼
        $("#remove-join-cafe-btn").on("click", function () {
            window.location.href = '<c:url value="/remove-join-cafe?cafeId=${cafe.id}"/>';
        });

        // 가입하기 버튼
        $("#join-cafe-btn").on("click", function () {
            window.location.href = '<c:url value="/cafe-join?cafeId=${cafe.id}"/>';
        });

        // 내 카페 회원목록 버튼
        $("#show-my-crew-btn").on("click", function () {
            window.location.href = '<c:url value="/show-my-member?cafeId=${cafe.id}"/>';
        });

        // 카페 삭제 버튼
        $("#remove-cafe-btn").on("click", function () {
            window.location.href = '<c:url value="/delete-cafe?cafeId=${cafe.id}"/>';
        });
    });
</script>
</html>