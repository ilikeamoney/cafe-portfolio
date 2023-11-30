<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: imjeongho
  Date: 11/24/23
  Time: 10:36 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Cafe Form</title>
    <link href="<c:url value="/webjars/bootstrap/5.2.3/css/bootstrap.min.css"/>" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
</head>
<style>
    body {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 110vh; /* 화면 높이에 맞추기 위한 설정 */
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
        padding: 3px;
    }
</style>
<body>
<form id="cafe_data" enctype="multipart/form-data">
    <table>
        <tr>
            <td colspan="2">
                <h3>카페 생성</h3>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <span class="input-group-text" id="inputGroup-sizing-default">타이틀</span>
                <input id="title" name="title" type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default">
            </td>
        </tr>
        <tr id="title-valid-tag" style="display: none">
            <td colspan="2">
                <p id="title-valid-msg"></p>
            </td>
        </tr>
        <tr>
            <td style="width: 10px;">
                <label class="input-group-text" for="img-file">이미지</label>
            </td>
            <td>
                <input id="img-file" name="imgFile" type="file" class="form-control">
            </td>
        </tr>
        <tr id="imgFile-valid-tag" style="display: none">
            <td colspan="2">
                <p id="imgFile-valid-msg"></p>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <label class="input-group-text" for="genre">장르</label>
                <select id="genre" name="genre" class="form-select">
                    <option value="">장르선택</option>
                    <option value="게임">게임</option>
                    <option value="독서">독서</option>
                    <option value="음악">음악</option>
                    <option value="영화">영화</option>
                    <option value="여행">여행</option>
                    <option value="IT">IT</option>
                    <option value="건강">건강</option>
                    <option value="패션">패션</option>
                    <option value="동물">동물</option>
                    <option value="미술">미술</option>
                    <option value="음식">음식</option>
                    <option value="역사">역사</option>
                    <option value="스포츠">스포츠</option>
                    <option value="자동차">자동차</option>
                    <option value="취미">취미</option>
                    <option value="학문">학문</option>
                    <option value="언어">언어</option>
                    <option value="사진">사진</option>
                    <option value="경제">경제</option>
                    <option value="정치">정치</option>
                </select>
            </td>
        </tr>
        <tr id="genre-valid-tag" style="display: none">
            <td colspan="2">
                <p id="genre-valid-msg"></p>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <span class="input-group-text">소개</span>
                <textarea id="introduce" name="introduce" rows="12" class="form-control" aria-label="With textarea"></textarea>
            </td>
        </tr>
        <tr id="introduce-valid-tag" style="display: none">
            <td colspan="2">
                <p id="introduce-valid-msg"></p>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <button id="back-btn" type="button" class="btn btn-secondary">뒤로가기</button>
                <button id="create-btn" type="button" class="btn btn-success">카페생성</button>
            </td>
        </tr>
    </table>
</form>
<script src="<c:url value="/webjars/bootstrap/5.2.3/js/bootstrap.min.js"/>" rel="stylesheet"></script>
</body>
<script>
    $(document).ready(function () {

        $("#title").on("input", function () {
            if ($("#title").val()) {
                $("#title-valid-tag").hide();
            }
        });

        $("#img-file").on("input", function () {
            if ($("#img-file").val()) {
                $("#imgFile-valid-tag").hide();
            }
        });

        $("#genre").on("input", function () {
            if ($("#genre").val()) {
                $("#genre-valid-tag").hide();
            }
        });

        $("#introduce").on("input", function () {
            if ($("#introduce").val()) {
                $("#introduce-valid-tag").hide();
            }
        });


        $("#back-btn").on("click", function() {
            window.location.href = '<c:url value="/"/>';
        })

        $("#create-btn").on("click", function () {
            let count = 0;
            let formData = new FormData($("#cafe_data")[0]);

            let title = formData.get("title");
            let imgFileName = formData.get("imgFile").name;
            let genre = formData.get("genre")
            let introduce = formData.get("introduce")


            if (title === "") {
                count += 1;
                $("#title-valid-tag").show();
                $("#title-valid-msg").text("카페 제목을 입력하세요.").css("color", "red");
            }
            if (imgFileName === "") {
                count += 1;
                $("#imgFile-valid-tag").show();
                $("#imgFile-valid-msg").text("카페 이미지를 넣어주세요.").css("color", "red");
            }
            if (genre === "") {
                count += 1;
                $("#genre-valid-tag").show();
                $("#genre-valid-msg").text("카페 장르를 선택하세요.").css("color", "red");
            }
            if (introduce === "") {
                count += 1;
                $("#introduce-valid-tag").show();
                $("#introduce-valid-msg").text("카페 소개글을 작성하세요").css("color", "red");
            }

            if (count === 0) {
                $.ajax({
                    url: "/send-my-cafe-data",
                    type: "post",
                    data: formData,
                    contentType: false,
                    processData: false,
                    success: function () {
                        window.location.href = '<c:url value="/"/>';
                    }
                });
            }
        });

    });
</script>
</html>
