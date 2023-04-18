<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery/dist/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/3.1.2/rollups/aes.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="https://cdn.jsdelivr.net/npm/@editorjs/editorjs@latest"></script>
    <script src="simple-image.js"></script>
    <link href="simple-image.css" rel="stylesheet"/>
    <title>Document</title>
    <style>
        .hidden {
            display: none;
        }
        .border-solid-1 {
            border: 1px solid black;
        }
        .codex-editor__redactor{
            padding-bottom: 50px !important;
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
            <input type="text" name="inWriter" id="inWriter" class="hidden" value="${sessionScope.loginKey.id}">
            <input type="text" name="inTitle" id="inTitle" class="form-control" placeholder="글 제목을 입력하세요">
            <input type="text" name="inContents" id="inContents" class="hidden">
        </div>
    </div>
    <div id="editorjs"></div>
    <button type="button" class="btn btn-outline-primary" id="save-button">저장하기</button>
    <button type="button" class="btn btn-outline-warning" id="reset-editor">초기화</button>
    <pre id="output"></pre>
    <%--        <div class="row">--%>
    <%--            <div class="col-12 board-content-body word-break-all" id="content">${requestScope.targetContent.contents}</div>--%>
    <%--        </div>--%>
    <div class="row boardFooter">
        <div class="col-12 d-flex justify-content-end border-solid-1">
            <a href="/select.board?currentPage=1">
                <button>목록으로</button>
            </a>
            <button id="doWrite">작성완료</button>
        </div>
    </div>
</div>
</form>
<script>
    const editor = new EditorJS({
        holderId: 'editorjs',
        inlineToolbar: true
    });
    const inContents = document.querySelector('#inContents');
    $('#doWrite').on('click', function () {
        editor.save().then(savedData => {
            savedData.blocks.forEach(function (e) {
                inContents.value += e.data.text + '<br>';
            })
            $(this).closest('form').submit();
        });
    });

    const resetEditor = document.querySelector('#reset-editor');
    resetEditor.addEventListener('click', () => {
        editor.clear();
    })


</script>
</body>
</html>
