<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: imjeongho
  Date: 11/27/23
  Time: 5:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="cafeId" scope="request" type="java.lang.Integer"/>
<html>
<head>
    <title>Write Board Form</title>
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
        padding: 5px 5px;
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
            <td colspan="2">
                <input id="title" name="title" type="text" placeholder="제목을 입력해주세요." class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default">
            </td>
        </tr>
        <tr id="title-valid-tag" style="display: none; height: 10px;">
            <td colspan="2">
                <p id="title-valid-msg"></p>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <textarea id="board-content" placeholder="내용을 입력해주세요." name="content" rows="15" cols="50" class="form-control" aria-label="With textarea"></textarea>
            </td>
        </tr>
        <tr id="content-valid-tag" style="display: none;">
            <td colspan="2">
                <p id="content-valid-msg"></p>
            </td>
        </tr>
        <tr>
            <td>
                <button id="write-board-back-btn" class="btn btn-secondary">뒤로가기</button>
            </td>
            <td>
                <button id="write-board-btn" class="btn btn-primary">작성하기</button>
            </td>
        </tr>
    </table>
</section>
</body>
<script src="<c:url value="/webjars/bootstrap/5.2.3/js/bootstrap.min.js"/>" rel="stylesheet"></script>
<script>
    $(document).ready(function () {
        $("#write-board-back-btn").on("click", function() {
            window.location.href = '<c:url value="/cafe-detail?cafeId=${cafeId}&page=${currentPage}"/>';
        });

        $("#title").on("input", function () {
            $("#title-valid-tag").hide();
        });

        $("#board-content").on("input", function () {
            $("#content-valid-tag").hide();
        });

        $("#write-board-btn").on("click", function () {
            let count = 0;

            if (!$("#title").val()) {
                $("#title-valid-tag").show();
                $("#title-valid-msg").text("제목을 입력하세요.").css("color", "red");
                count += 1;
            }

            if (!$("#board-content").val()) {
                $("#content-valid-tag").show();
                $("#content-valid-msg").text("내용을 입력하세요.").css("color", "red");
                count += 1;
            }

            if (count === 0) {
                let query = {
                    title: $("#title").val(),
                    content: $("#board-content").val(),
                    cafeId: ${cafeId},
                    memId: ${memLog}
                };

                $.ajax({
                    url: "/write-board-pro",
                    type: "post",
                    data: query,
                    success: function (data) {
                        let cafeIdTag = '<p class="cafeId">';
                        let currentPageTag = '<p class="currentPage">';
                        let endTag = '</p>';

                        let startIdx = data.indexOf(cafeIdTag) + cafeIdTag.length;
                        let endIdx = data.indexOf(endTag, startIdx);
                        let cafeId = parseInt(data.substring(startIdx, endIdx));

                        startIdx = data.indexOf(currentPageTag) + currentPageTag.length;
                        endIdx = data.indexOf(endTag, startIdx);
                        let page = parseInt(data.substring(startIdx, endIdx));

                        if (cafeId > 0 && 0 < page) {
                            window.location.href = "/cafe-detail?cafeId=" + cafeId + "&page=" + page;
                        }
                    },
                })
            }

        });

    });
</script>
</html>
