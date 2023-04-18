<%--
  Created by IntelliJ IDEA.
  User: luke
  Date: 2023/04/07
  Time: 11:47 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.min.js"></script>
    <script src='https://cdn.jsdelivr.net/npm/jquery/dist/jquery.min.js'></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <title>Main</title>
    <style>
        body {
            background-color: #1BBC9B
        }

        .background-color-green {
            background-color: #a0dbb7
        }

        .border-radius-10 {
            border-radius: 10px;
        }

        .font-size-bg {
            font-size: 16pt;
            font-weight: bold;
        }

        .hidden {
            display: none;
        }
        .myPageText{
            outline: none;
            background: none;
        }
    </style>
</head>
<body>
<div class="container headContainer background-color-green border-radius-10">
    <div class="row">
        <div class="col-12">
            <p class="font-size-bg">환영합니다 ${sessionScope.loginKey.name}님 !</p>
            <p>ID : ${sessionScope.loginKey.id}</p>
            <p>PHONE : ${sessionScope.loginKey.phone}</p>
            <p>EMAIL : ${sessionScope.loginKey.email}</p>
            <p>ZIPCODE : ${sessionScope.loginKey.zipcode}</p>
            <p>ADDRESS : ${sessionScope.loginKey.address1} - ${sessionScope.loginKey.address2}</p>
            <p>JOIN_DATE : ${sessionScope.loginKey.join_date}</p>
        </div>
    </div>
</div>
<form action="/updateAccount.member" method="post" id="modifyForm">
    <table align="center">
        <tr>
            <td>ID</td>
            <td>
                <input type="text" id="modifyId" name="modifyId" class="myPageText" value="${sessionScope.loginKey.id}" readonly>
                <input type="text" id="modifyPw" name="modifyPw" class="hidden" value="${sessionScope.loginKey.pw}" readonly>
            </td>
        </tr>
        <tr>
            <td>Name</td>
            <td><input type="text" id="modifyName" name="modifyName" class="myPageText" value="${sessionScope.loginKey.name}" readonly>
            </td>
        </tr>
        <tr>
            <td>Phone</td>
            <td><input type="text" id="modifyPhone" name="modifyPhone" class="myPageText" value="${sessionScope.loginKey.phone}"
                       readonly></td>
        </tr>
        <tr>
            <td>E-mail</td>
            <td><input type="text" id="modifyEmail" name="modifyEmail" class="myPageText" value="${sessionScope.loginKey.email}"
                       readonly></td>
        </tr>
        <tr>
            <td>Zip-code</td>
            <td><input type="text" id="modifyZipcode" name="modifyZipcode" class="myPageText" value="${sessionScope.loginKey.zipcode}"
                       readonly>
                <button type="button" id="zipcodeSearch" class="hidden">우편번호 찾기</button></td>
        </tr>
        <tr>
            <td>Address1</td>
            <td><input type="text" id="modifyAddress1" name="modifyAddress1" class="myPageText" value="${sessionScope.loginKey.address1}"
                       readonly></td>
        </tr>
        <tr>
            <td>Address2</td>
            <td><input type="text" id="modifyAddress2" name="modifyAddress2" class="myPageText" value="${sessionScope.loginKey.address2}"
                       readonly></td>
        </tr>
        <tr>
            <td>Join_date</td>
            <td>${sessionScope.loginKey.join_date}</td>
        </tr>
    </table>
</form>
<div class="container-fluid bodyContainer">
    <div class="row">
        <div class="col-12 d-flex justify-content-center">
            <button type="button" id="return">돌아가기</button>
            <button type="button" id="modify">정보수정</button>
            <button type="button" id="modifyConfirm" class="hidden">수정확인</button>
            <button type="button" id="cancel" class="hidden">수정취소</button>
        </div>
    </div>
</div>
<script>
    const nameRegex = /^[가-힣]+$/;
    const phoneRegex = /^01\d([-\s.\/]?\d{4})([-\s.\/]?\d{4})$/;
    const emailRegex = /\w{4,16}@\w+\.com$/;

    const checkNameValidation = () => {
        const name = document.querySelector('#modifyName').value;
        return nameRegex.test(name);
    }
    const checkPhoneValidation = () => {
        const phone = document.querySelector('#modifyPhone').value;
        return phoneRegex.test(phone);
    }
    const checkEmailValidation = () => {
        const email = document.querySelector('#modifyEmail').value;
        return emailRegex.test(email);
    }
    const is_all_arguments_validate = function(){
        return checkNameValidation() && checkPhoneValidation() && checkEmailValidation();
    }
    function execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function (data) {
                var roadAddr = data.roadAddress;
                document.getElementById('modifyZipcode').value = data.zonecode;
                document.getElementById("modifyAddress1").value = roadAddr;
            }
        }).open();
    };

    document.querySelector('#return').addEventListener('click', () => location.href = '/index.jsp');
    $('#modify').on('click', function () {
        $('input').removeAttr('readonly');
        $('#modifyId').prop('readonly', true);
        $('button').toggleClass('hidden');
    });
    $('#cancel').on('click', function()  {
        location.reload()
    });
    $('#modifyConfirm').on('click', function (){
        if (nameRegex.test(
            $('#modifyName').val())
        ) {
            $('#modifyForm').submit();
        } else {
            Swal.fire({
                title: '입력된 값을 다시 확인해주세요.',
                icon: 'error',
                showConfirmButton: false,
                timer: 1500
            });
        }
    });
    $('#zipcodeSearch').on('click', execDaumPostcode);


</script>
</body>
</html>
