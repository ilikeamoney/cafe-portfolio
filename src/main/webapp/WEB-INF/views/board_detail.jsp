<%--
  Created by IntelliJ IDEA.
  User: imjeongho
  Date: 11/29/23
  Time: 9:22 AM
  To change this template use File | Settings | File Templates.
--%>
<jsp:useBean id="board" scope="request" type="com.example.cafe.domain.dto.BoardDto"/>
<jsp:useBean id="memberCheck" scope="request" type="java.lang.Boolean"/>
<jsp:useBean id="btnStart" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="btnEnd" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="currentPage" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="totalBoardCount" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="comments" scope="request" type="java.util.List"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Board Detail</title>
    <link href="<c:url value="/webjars/bootstrap/5.2.3/css/bootstrap.min.css"/>" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
</head>
<style>
    #content {
        font-family: Arial, sans-serif;
        justify-content: center;
        height: 80%;
        width: 100%;
        margin: 0;
    }

    #my-cafe-content {
        margin: 0 auto;
    }


    #my-cafe-content tr td {
        text-align: center;
    }

    #comment-con {
        font-family: Arial, sans-serif;
        justify-content: center;
        height: 50px;
        width: 100%;
        margin: 0;
    }

    #comment-table {
        margin: 0 auto;
    }

    #comment-table tr td {
        text-align: center;
    }

    #comments {
        font-family: Arial, sans-serif;
        justify-content: center;
        height: 50px;
        width: 100%;
        margin: 20px 0;
    }

    #comments-table {
        margin: 0 auto;
    }

    #comment-page-btn-con {
        font-family: Arial, sans-serif;
        justify-content: center;
        height: 40px;
        width: 100%;
        margin: 10px 0;
    }

    #comment-btn-table {
        margin: 0 auto;
    }

    .board-writer {
        font-weight: bolder;
    }

    .date-count span {
        font-size: 13px;
        color: dimgray;
        display: inline-block;
    }

    #delete-board a {
        color: dimgray;
    }
</style>
<body>
<jsp:include page="login_main.jsp"/>
<section id="content">
    <table id="my-cafe-content">
        <tr>
            <td colspan="2">
                <h1>${board.title}</h1>
                <hr>
                <span class="board-writer">작성자 ${board.name}</span>
                <div class="date-count">
                    <span class="date">${board.writeDate} | </span>
                    <span class="count">조회 ${board.viewCount}</span>
                    <c:if test="${memLog eq board.memId}">
                        <span id="delete-board">
                            <a href="<c:url value="/board-delete?boardId=${board.boardId}&cafeId=${board.cafeId}"/>">삭제</a>
                        </span>
                    </c:if>
                </div>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <textarea id="board-content" disabled="disabled" name="content" rows="15" cols="45" class="form-control"
                          aria-label="With textarea">${board.content}</textarea>
            </td>
        </tr>
        <tr>
            <c:choose>
                <c:when test="${memLog eq board.memId}">
                    <td>
                        <button class="board-detail-back-btn btn btn-secondary">뒤로가기</button>
                    </td>
                    <td>
                        <button id="board-edit-btn" class="btn btn-primary">수정하기</button>
                    </td>
                </c:when>
                <c:otherwise>
                    <td colspan="2">
                        <button class="board-detail-back-btn btn btn-secondary">뒤로가기</button>
                    </td>
                </c:otherwise>
            </c:choose>
        </tr>
    </table>
</section>
<c:if test="${memberCheck}">
    <section id="comment-con">
        <table id="comment-table">
            <tr>
                <td>
                    <button id="write-comment-btn" class="btn btn-dark">댓글 쓰기</button>
                </td>
            </tr>
            <tr class="comment-section" style="display: none">
                <td>
                    <input id="comment-title" name="comment-title" type="text" placeholder="댓글 제목을 입력해주세요."
                           class="form-control" aria-label="Sizing example input"
                           aria-describedby="inputGroup-sizing-default">
                </td>
            </tr>
            <tr class="comment-section" style="display: none">
                <td>
                <textarea id="comment-content" placeholder="댓글 내용을 입력해주세요." name="comment-content" rows="10" cols="30"
                          class="form-control" aria-label="With textarea"></textarea>
                </td>
            </tr>
            <tr class="comment-section" style="display: none">
                <td>
                    <button id="write-comment" class="btn btn-success" disabled>작성</button>
                </td>
            </tr>
        </table>
    </section>
</c:if>
<c:if test="${comments.size() gt 0}">
    <section id="comments">
        <table id="comments-table">
            <c:forEach var="comment" items="${comments}">
                <tr>
                    <td>
                        <h4>${comment.title}</h4>
                        <hr>
                        <span style="color: dimgray">
                            작성자 ${comment.name}
                            | ${comment.writeDate}
                        </span>
                        <c:if test="${memLog eq comment.memId}">
                            <span>
                                <a href="<c:url value="/delete-comment?commentId=${comment.id}&boardId=${board.boardId}"/>" style="color: dimgray; font-size: 15px">삭제</a>
                            </span>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td>
                        <textarea id="comment" disabled="disabled" name="comment" rows="5" cols="30" class="form-control"
                                  aria-label="With textarea">${comment.content}</textarea>
                        <br>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <div id="comment-page-btn-con">
            <table id="comment-btn-table">
                <tr>
                    <td>
                        <button onclick="window.location.href='<c:url value="/board-detail?boardId=${board.boardId}&minusBtn=true"/>'" class="btn btn-primary"><</button>
                    </td>
                    <td style="width: 200px; text-align: center" >
                        <c:forEach var="i" begin="${btnStart}" end="${btnEnd - 1}">
                            <c:choose>
                                <c:when test="${currentPage eq i + 1}">
                                    <a style="font-weight: bold" href="<c:url value="/board-detail?boardId=${board.boardId}&page=${i + 1}"/>">${i + 1}</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="<c:url value="/board-detail?boardId=${board.boardId}&page=${i + 1}"/>">${i + 1}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </td>
                    <td>
                        <button onclick="window.location.href='<c:url value="/board-detail?boardId=${board.boardId}&plusBtn=true"/>'" class="btn btn-primary">></button>
                    </td>
                </tr>
            </table>
        </div>
    </section>
</c:if>
<script src="<c:url value="/webjars/bootstrap/5.2.3/js/bootstrap.min.js"/>" rel="stylesheet"></script>
</body>
<script>
    $(document).ready(function () {
        let commentTitleCheck = false;
        let commentContentCheck = false;
        let changeHeight = 400;

        $(".board-detail-back-btn").on("click", function () {
            window.location.href = '<c:url value="/cafe-detail?cafeId=${board.cafeId}&page=${currentPage}"/>';
        });

        $("#board-edit-btn").on("click", function () {
            window.location.href = '<c:url value="/board-update?boardId=${board.boardId}"/>';
        });

        $("#write-comment-btn").on("click", function () {
            let currentHeight = $("#comment-con").height();

            if (currentHeight < changeHeight) {
                $("#comment-con").css("height", changeHeight + "px");
            } else {
                $("#comment-con").css("height", "50px");
            }

            $(".comment-section").toggle();
        });

        $("#write-comment").on("click", function () {
            let query = {
                memId: ${memLog},
                cafeId: ${board.cafeId},
                title: $("#comment-title").val(),
                content: $("#comment-content").val(),
                reg: ${board.boardId},
                reLevel: 2,
                reStep: 1,
            }


            $.ajax({
                url: "/write-comment",
                type: "post",
                data: query,
                success: function (data) {
                    let startTag = '<p>';
                    let endTag = '</p>';

                    let startIdx = data.indexOf(startTag) + startTag.length;
                    let endIdx = data.indexOf(endTag, startIdx);

                    let boardId = parseInt(data.substring(startIdx, endIdx));

                    if (boardId > 0) {
                        window.location.href = '/board-detail?boardId=' + boardId;
                    }
                },
            });
        });

        $("#comment-title, #comment-content").on("input", function () {
            commentTitleCheck = $("#comment-title").val().trim() !== "";
            commentContentCheck = $("#comment-content").val().trim() !== "";

            if (commentTitleCheck && commentContentCheck) {
                $("#write-comment").attr("disabled", false);
            } else {
                $("#write-comment").attr("disabled", true);
            }
        });
    });
</script>
</html>