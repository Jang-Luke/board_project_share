<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.js"></script>
    <title>Document</title>
    <style>
        .hidden {
            display: none;
        }

        .border-solid-1 {
            border: 1px solid black;
        }
        .font-size-title {
            font-size: 16pt;
            font-weight: bold;
            text-align: center;
        }
        .btn{
            align-self: center;
            min-width: 50px;
            padding: 6px;
            font-size: 10pt;
        }
    </style>
</head>
<body>
<form action="/insert.board" method="post">

    <div class="container border-solid-1">
        <div class="row">
            <div class="col-12 border-solid-1">
                <p class="font-size-title">게시글 작성하기</p>
            </div>
            <div class="col-12 border-solid-1">
                <input type="hidden" name="inWriter" id="inWriter" class="hidden" value="${sessionScope.loginKey.id}">
                <input type="text" name="inTitle" id="inTitle" class="form-control" placeholder="글 제목을 입력하세요">
                <%--            <input type="text" name="inContents" id="inContents" class="hidden">--%>
            </div>
        </div>
        <div class="row boardbody">
            <div class="col-12">
                <textarea name="inContents" id="summernote"></textarea>
            </div>
        </div>
        <%--        <div class="row">--%>
        <%--            <div class="col-12 board-content-body word-break-all" id="content">${requestScope.targetContent.contents}</div>--%>
        <%--        </div>--%>
        <div class="row boardFooter">
            <div class="col-12 d-flex justify-content-end border-solid-1">
                <a href="/select.board?currentPage=1">
                    <button type="button" class="btn btn-outline-secondary">돌아가기</button>
                </a>
                <button type="button" id="doWrite" class="btn btn-outline-primary">작성완료</button>
            </div>
        </div>
    </div>
</form>
<script>
    $(document).ready(function() {
        $('#summernote').summernote({
            height: 300,                 // 에디터 높이
            minHeight: null,             // 최소 높이
            maxHeight: null,             // 최대 높이
            focus: true,                  // 에디터 로딩후 포커스를 맞출지 여부
            lang: "ko-KR",					// 한글 설정
            placeholder: '최대 2048자까지 쓸 수 있습니다'	//placeholder 설정
        });
    });
    const inContents = document.querySelector('#inContents');
    $('#doWrite').on('click', function () {
        if ($('.note-editable').find('p').text() !== '' && $('#inTitle').val() !== '') {
            $(this).closest('form').submit();
        } else {
            Swal.fire({
                icon: 'error',
                title: '빈 값을 입력할 수 없습니다.',
                showConfirmButton: false,
                timer: 1500
            })
        }
    });

</script>
</body>
</html>
