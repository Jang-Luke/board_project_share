<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.min.js"></script>
    <script src='https://cdn.jsdelivr.net/npm/jquery/dist/jquery.min.js'></script>
    <title>confirm</title>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-12">
            <h2>정말로 탈퇴하시겠습니까?</h2>
        </div>
    </div>
    <div class="row">
        <div class="col-12">
            <div class="form-floating">
                <input id="deleteConfirmText" class="form-control form-control-lg" type="text" placeholder="계정탈퇴">
                <label for="deleteConfirmText" class="form-label">정말로 탈퇴하시려면 [계정탈퇴] 를 입력하세요.</label>
            </div>
        </div>
        <div class="col-12 d-flex justify-content-center">
            <button type="button" id="deleteConfirmButton" class="btn btn-outline-danger">확인</button>
        </div>
    </div>
</div>
<script>
    const deleteConfirmText = $('#deleteConfirmText');
    const deleteConfirmButton = $('#deleteConfirmButton');
    const confirmTextRegex = /^계정탈퇴$/;
    deleteConfirmText.on('focus', function () {
        deleteConfirmText.next().text('계정탈퇴');
    });
    deleteConfirmText.on('focusout', function () {
        deleteConfirmText.next().text('정말로 탈퇴하시려면 [계정탈퇴] 를 입력하세요.');
    });
    deleteConfirmButton.on('click', function () {
        if (confirmTextRegex.test(deleteConfirmText.val())) {
            opener.parent.deleteMember()
            console.log(1);
            window.close();
        }
    });
</script>
</body>
</html>
