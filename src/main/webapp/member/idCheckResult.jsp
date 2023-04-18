<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Id Check Result</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.min.js"></script>
    <script src='https://cdn.jsdelivr.net/npm/jquery/dist/jquery.min.js'></script>
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-12 d-flex justify-content-center">
                <C:choose>
                    <C:when test="${requestScope.duplicateCheck}">
                        <h1>중복된 아이디입니다.</h1>
            </div>
            <div class="col-12 d-flex justify-content-center">
                        <input class="btn btn-secondary check w-25" id="duplicate" type="button" value="확인">
                    </C:when>
                    <C:otherwise>
                        <h1>사용 가능한 아이디입니다.</h1>
            </div>
            <div class="col-12 d-flex justify-content-center">
                        <input class="btn btn-success check w-25" id="validate" type="button" value="확인">
                    </C:otherwise>
                </C:choose>
            </div>
        </div>
    </div>
    <script>
        $('.check').on('click', function() {
            if ($(this).attr('id') == 'validate') {
                $('#duplicationCheck', opener.document).prop('disabled', true)
                    .text('사용가능')
                    .removeClass('btn-outline-primary')
                    .addClass('btn-black');
                $('#inPw', opener.document).focus();
            }
            window.close();
        })
    </script>
</body>
</html>
