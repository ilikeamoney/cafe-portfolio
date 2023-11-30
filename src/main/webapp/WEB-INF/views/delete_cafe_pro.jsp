<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: imjeongho
  Date: 11/30/23
  Time: 2:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="memId" scope="request" type="java.lang.String"/>
<jsp:useBean id="memPw" scope="request" type="java.lang.String"/>
<html>
<head>
    <title>Delete Cafe Pro</title>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
</head>
<script>
    $(document).ready(function () {
        let query = {
            memberId: '${memId}',
            memberPw: '${memPw}'
        }

        $.ajax({
            url: "/loginPro",
            type: "post",
            data: query,
            success: function (data) {
                let startTag = '<p>';
                let endTag = '</p>'

                let startIdx = data.indexOf(startTag) + startTag.length;
                let endIdx = data.indexOf(endTag, startIdx);

                let memId = parseInt(data.substring(startIdx, endIdx));

                if (memId > 0) {
                    window.location.href = '<c:url value="/main"/>';
                }
            }
        });
    });
</script>
</html>
