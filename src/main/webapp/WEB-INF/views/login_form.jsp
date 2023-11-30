<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: imjeongho
  Date: 11/23/23
  Time: 2:29 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login Form</title>
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
        width: 120px;
        height: 60px;
    }
</style>
<body>
<table>
    <tr>
        <td colspan="3">
            <h2>로그인</h2>
        </td>
    </tr>
    <tr>
        <td>
            <h5>아이디</h5>
        </td>
        <td>
            <label>
                <input type="text" name="id" id="id">
            </label>
        </td>
    </tr>
    <tr id="id-valid-tag" style="display: none;">
        <td colspan="3" style="height: 5px">
            <p id="id-valid-msg"></p>
        </td>
    </tr>
    <tr>
        <td>
            <h5>비밀번호</h5>
        </td>
        <td>
            <label>
                <input type="password" name="pw" id="pw">
            </label>
        </td>
    </tr>
    <tr id="pw-valid-tag" style="display: none">
        <td colspan="3" style="height: 5px">
            <p id="pw-valid-msg"></p>
        </td>
    </tr>
    <tr id="login-result-tag" style="display: none">
        <td colspan="3" style="height: 5px">
            <p id="login-result-msg"></p>
        </td>
    </tr>
    <tr>
        <td>
            <button id="login-back-btn" class="btn btn-secondary">뒤로가기</button>
        </td>
        <td>
            <button id="login-btn" class="btn btn-primary">로그인</button>
        </td>
    </tr>
</table>
<script src="<c:url value="/webjars/bootstrap/5.2.3/js/bootstrap.min.js"/>" rel="stylesheet"></script>
</body>
<script>
    $(document).ready(function () {
        $("#login-back-btn").on("click", function () {
            window.location.href = '<c:url value="/main"/>';
        });

        $("#id").on("input", function () {
            $("#id-valid-tag").hide();
        });

        $("#pw").on("input", function () {
            $("#pw-valid-tag").hide();
        });

        $("#login-btn").on("click", function () {
            if (!$("#id").val()) {
                $("#id-valid-tag").show();
                $("#id-valid-msg").text("아이디를 입력하세요.").css("color", "red");
            }

            if (!$("#pw").val()) {
                $("#pw-valid-tag").show();
                $("#pw-valid-msg").text("비밀번호를 입력하세요.").css("color", "red");
            }

            if ($("#id").val() !== null && $("#pw").val() !== null) {
                var query = {
                    memberId: $("#id").val(),
                    memberPw: $("#pw").val(),
                }

                    $.ajax({
                        url: "/loginPro",
                        type: "post",
                        data: query,
                        success: function (data) {
                            var startTag = '<p>';
                            var len = startTag.length;
                            var endTag = '</p>';

                            var startIdx = data.indexOf(startTag) + len;
                            var endIdx = data.indexOf(endTag, startIdx)

                            var id = parseInt(data.substring(startIdx, endIdx));

                            if (!isNaN(id)) {
                                window.location.href = '<c:url value="/main"/>';
                            } else {
                                $("#login-result-tag").show();
                                $("#login-result-msg").text("아이디 또는 비밀번호가 다릅니다.").css("color", "red");
                                $("#id").text("");
                                $("#pw").text("");
                            }
                        },
                    })
            }
        });
    });
</script>
</html>
