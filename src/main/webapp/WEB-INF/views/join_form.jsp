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
    <title>Join Form</title>
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
            <h2>회원가입</h2>
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
        <td>
            <button id="id-duplication-valid-btn" class="btn btn-dark">중복확인</button>
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
    <tr>
        <td>
            <h5>비밀번호 확인</h5>
        </td>
        <td>
            <label>
                <input type="password" id="pw-valid">
            </label>
        </td>
    </tr>
    <tr>
        <td>
            <h5>이름</h5>
        </td>
        <td>
            <label>
                <input type="text" name="name" id="name">
            </label>
        </td>
    </tr>
    <tr id="name-valid-tag" style="display: none">
        <td colspan="3" style="height: 5px">
            <p id="name-valid-msg"></p>
        </td>
    </tr>
    <tr>
        <td>
            <button id="join-back-btn" class="btn btn-secondary">뒤로가기</button>
        </td>
        <td>
            <button id="join-btn" class="btn btn-primary">가입하기</button>
        </td>
    </tr>
</table>
<script src="<c:url value="/webjars/bootstrap/5.2.3/js/bootstrap.min.js"/>" rel="stylesheet"></script>
</body>
<script>
    $(document).ready(function () {
        var idDuplicationCheck = false;

        $("#join-back-btn").on("click", function () {
            window.location.href = '<c:url value="/main"/>';
        });

        $("#id").on("input", function () {
            if ($("#id").val()) {
                $("#id-valid-tag").hide();
            }
        });

        $("#pw").on("input", function () {
            if ($("#pw").val()) {
                $("#pw-valid-tag").hide();
            }
        });

        $("#name").on("input", function () {
            if ($("#name").val()) {
                $("#name-valid-tag").hide();
            }
        });

        $("#pw-valid").on("input", function () {
            var pw = $("#pw").val();
            var pwValid = $("#pw-valid").val();

            $("#pw-valid-tag").show();
            if (pw === pwValid) {
                $("#pw-valid-msg").text("비밀번호가 일치합니다.").css("color", "green");
            } else {
                $("#pw-valid-msg").text("비밀번호가 일치 하지않습니다.").css("color", "red");
            }


        });

        $("#id-duplication-valid-btn").on("click", function () {
            if ($("#id").val()) {
                var query = {
                    id: $("#id").val(),
                }

                $.ajax({
                    url: "/duplicationCheck",
                    type: "post",
                    data: query,
                    success: function (data) {
                        var startTag = '<p>';
                        var endTag = '</p>';
                        var len = startTag.length;

                        var startIdx = data.indexOf(startTag) + len;
                        var endIdx = data.indexOf(endTag, startIdx);

                        var val = parseInt(data.substring(startIdx, endIdx));

                        if (val > 0) {
                            alert("중복되는 아이디 입니다.");
                        } else {
                            idDuplicationCheck = true;
                            alert("사용 가능한 아이디 입니다.");
                            $("#id-valid-tag").hide();
                        }
                    },
                });

            } else {
                $("#id-valid-tag").show();
                $("#id-valid-msg").text("아이디를 입력하세요.").css("color", "red");
            }
        });

        $("#join-btn").on("click", function () {
            var count = 0;

            if (!$("#id").val()) {
                count += 1;
                $("#id-valid-tag").show();
                $("#id-valid-msg").text("아이디를 입력하세요.").css("color", "red");
            }

            if (!idDuplicationCheck) {
                count += 1;
                $("#id-valid-tag").show();
                $("#id-valid-msg").text("중복확인을 눌러주새요.").css("color", "red");
            }
            if (!$("#pw").val()) {
                count += 1;
                $("#pw-valid-tag").show();
                $("#pw-valid-msg").text("비밀번호를 입력하세요.").css("color", "red");
            }
            if ($("#pw").val() !== $("#pw-valid").val()) {
                count += 1;
                $("#pw-valid-tag").show();
                $("#pw-valid-msg").text("비밀번호가 일치하지 않습니다.").css("color", "red");
            }
            if (!$("#name").val()) {
                count += 1;
                $("#name-valid-tag").show();
                $("#name-valid-msg").text("성함을 입력하세요.").css("color", "red");
            }

            if (count === 0) {
                var query = {
                    memberId: $("#id").val(),
                    memberPw: $("#pw").val(),
                    name: $("#name").val(),
                }

                $.ajax({
                    url: "/joinPro",
                    type: "post",
                    data: query,
                    success: function (data) {
                        window.location.href = '<c:url value="/success"/>';
                    }
                });
            }
        });

    });
</script>
</html>
