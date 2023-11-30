<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: imjeongho
  Date: 11/30/23
  Time: 11:47 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="members" scope="request" type="java.util.List"/>
<jsp:useBean id="cafe" scope="request" type="com.example.cafe.domain.Cafe"/>
<jsp:useBean id="admin" scope="request" type="com.example.cafe.domain.Member"/>
<html>
<head>
  <title>Show My Member</title>
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

  #my-cafe-member {
    margin: 0 auto;
  }

  #my-cafe-member tr td {
    padding: 10px 30px;
    text-align: center;
  }
</style>
<body>
<jsp:include page="login_main.jsp"/>
<section id="content">
  <table id="my-cafe-member">
    <tr>
      <td colspan="4">
        <h2>${admin.name}님 카페 회원목록<h3>
      </td>
    </tr>
    <tr>
      <td colspan="4">
        <h4>전체 회원 수는 ${members.size()}명 입니다.</h4>
      </td>
    </tr>
    <c:if test="${members.size() gt 0}">
      <tr>
        <td>
          <h5>번호</h5>
        </td>
        <td>
          <h5>이름</h5>
        </td>
        <td>
          <h5>My Web 가입일</h5>
        </td>
      </tr>
      <c:forEach var="member" items="${members}">
        <tr>
          <td>${member.id}</td>
          <td>${member.name}</td>
          <td>${member.joinDate}</td>
          <td>
            <button onclick="window.location.href='<c:url value="/remove-join-member?cafeId=${cafe.id}&memId=${member.id}"/>'" class="btn btn-danger">제명</button>
          </td>
        </tr>
      </c:forEach>
    </c:if>
  </table>
</section>
<script src="<c:url value="/webjars/bootstrap/5.2.3/js/bootstrap.min.js"/>" rel="stylesheet"></script>
</body>
</html>
