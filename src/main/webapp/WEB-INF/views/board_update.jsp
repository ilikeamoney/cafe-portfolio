<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: imjeongho
  Date: 11/29/23
  Time: 12:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="board" scope="request" type="com.example.cafe.domain.Board"/>
<html>
<head>
    <title>Board Update</title>
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
    text-align: center;
  }

  #comment-con {
    font-family: Arial, sans-serif;
    justify-content: center;
    height: 100%;
    width: 100%;
    margin: 30px 0 0;
  }

  #comment-table {
    margin: 0 auto;
  }

  #comment-table tr td {
    text-align: center;
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
          <input id="board-title" name="board-title" type="text" value="${board.title}" placeholder="수정 제목을 입력해주세요." class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default">
        <hr>
      </td>
    </tr>
    <tr id="update-title-valid-tag" style="display: none">
      <td>
        <p id="update-title-valid-msg"></p>
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <textarea id="board-content"  name="board-content" rows="15" cols="45" placeholder="수정 내용을 입력하세요." class="form-control" aria-label="With textarea">${board.content}</textarea>
      </td>
    </tr>
    <tr id="update-content-valid-tag" style="display: none">
      <td>
        <p id="update-content-valid-msg"></p>
      </td>
    </tr>
    <tr>
      <td>
        <button id="update-btn" class="btn btn-success">수정</button>
      </td>
    </tr>
  </table>
</section>
<script src="<c:url value="/webjars/bootstrap/5.2.3/js/bootstrap.min.js"/>" rel="stylesheet"></script>
</body>
<script>
  $(document).ready(function () {
    $("#board-title").on("input", function () {
      if ($("#board-title").val()) {
        $("#update-title-valid-tag").hide();
      }
    });

    $("#board-content").on("input", function () {
      if ($("#board-content").val()) {
        $("#update-content-valid-tag").hide();
      }
    });

    $("#update-btn").on("click", function () {
      if (!$("#board-title").val()) {
        $("#update-title-valid-tag").show();
        $("#update-title-valid-msg").text("제목을 입력해 주세요.").css("color", "red");
      }

      if (!$("#board-content").val()) {
        $("#update-content-valid-tag").show();
        $("#update-content-valid-msg").text("내용을 입력해 주세요.").css("color", "red");
      }

      if ($("#board-title").val() && $("#board-content").val()) {
        let query = {
          title: $("#board-title").val(),
          content: $("#board-content").val(),
          boardId: ${board.id},
          cafeId: ${board.cafeId}
        }

        $.ajax({
          url: "/board-update-pro",
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
        })

      }
    });
  });
</script>
</html>
