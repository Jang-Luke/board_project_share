<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
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
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <title>Document</title>
    <style>
        .font-size-title {
            font-size: 16pt;
            font-weight: bold;
            text-align: center;
        }

        .board-content-body {
            min-height: 400px;
        }

        .borderless {
            border: none;
        }

        .border-solid-1 {
            border: 1px solid black;
        }

        .word-break-all {
            word-break: break-all;
        }

        .hidden {
            display: none;
        }

        .replyBody {
            word-wrap: break-word;
        }
    </style>
</head>
<body>
<form action="/modify.board?id=${requestScope.targetContent.id}" method="post">
    <input type="text" name="modifyTitle" id="modifyTitle" class="hidden">
    <input type="text" name="modifyContent" id="modifyContent" class="hidden">
    <div class="container border-solid-1">
        <div class="row">
            <div class="col-12 border-solid-1">
                <p class="font-size-title" id="titleView">${requestScope.targetContent.title}</p>
            </div>
        </div>
        <div class="row">
            <div class="col-4 border-solid-1">
                <span>작성자 : ${requestScope.targetContent.writer}</span>
            </div>
            <div class="col-8 d-flex justify-content-end border-solid-1">
                <span style="margin-right: 5px">작성일 : ${requestScope.targetContent.writeDate} | </span>
                <span>조회수 : ${requestScope.targetContent.viewCount}</span>
            </div>
            <div class="col-12 board-content-body word-break-all"
                 id="contentView">${requestScope.targetContent.contents}</div>
        </div>
        <div class="row boardFooter">
            <div class="col-12 d-flex justify-content-end border-solid-1">
                <C:if test="${requestScope.targetContent.writer==sessionScope.loginKey.id}">
                    <button type="button" class="btn btn-outline-warning" id="modifyButton">수정하기</button>
                    <button type="submit" class="btn btn-outline-warning hidden" id="confirmModify">수정하기</button>
                    <a href="/delete.board?id=${requestScope.targetContent.id}">
                        <button type="button" class="btn btn-outline-danger" id="deleteButton">삭제하기</button>
                    </a>
                    <button type="button" class="btn btn-light hidden" id="cancelModify">수정취소</button>
                </C:if>
                <%--            TODO: 왔던 페이지로 돌아가는 기능으로 수정--%>
                <button type="button" id="return" class="btn btn-outline-secondary">돌아가기</button>
            </div>
        </div>
    </div>
</form>
<hr>
<br>
<C:forEach var="i" items="${requestScope.targetReplies}">
    <form action="/modifyReply.reply" method="post">
        <input type="text" name="modifyReplyId" class="replyId hidden" value="${i.id}">
        <input type="text" name="modifyReplyWriter" class="hidden" value="${i.writer}">
        <input type="text" name="modifyReplyContents" class="modifyReplyValue hidden" value="${i.contents}">
        <input type="text" name="modifyReplyContentId" value="${requestScope.targetContent.id}" class="hidden">
        <div class="container">
            <div class="row">
                <div class="col-12">
                    <p class="d-flex justify-content-between">
                        <em style="border-bottom: 1px dotted gray;">${i.writer}</em>
                        <span>| ${i.writeDate}</span>
                    </p>
                </div>
                <div class="col-12">
                    <div class="replyBody">${i.contents}</div>
                </div>
                <C:if test="${i.writer==sessionScope.loginKey.id}">
                    <div class="col-12 d-flex justify-content-end">
                        <button type="button" class="btn btn-outline-warning modifyReply">수정</button>
                        <button type="button" class="btn btn-outline-danger deleteReply">삭제</button>
                        <button type="button" class="btn btn-outline-dark modifyReplyConfirm hidden">확인</button>
                        <button type="button" class="btn btn-outline-secondary modifyReplyCancel hidden">취소</button>
                    </div>
                </C:if>
            </div>
        </div>
    </form>
    <hr>
</C:forEach>
<br>
<form action="/insert.reply" method="post">
    <input type="text" name="replyContentId" id="replyContentId" value="${requestScope.targetContent.id}"
           class="hidden">
    <div class="container">
        <div class="row">
            <div class="col-10">
                <textarea name="replyContents" id="replyContents" class="form-control"></textarea>
            </div>
            <div class="col-2 d-flex">
                <button type="button" id="replyButton" class="btn btn-outline-primary align-self-center">
                    등록하기
                </button>
            </div>
        </div>
    </div>
</form>

<script>
    $('#modifyButton').on('click', function () {
        $('#deleteButton').addClass('hidden');
        $('#modifyButton').addClass('hidden');
        $('#confirmModify').removeClass('hidden');
        $('#cancelModify').removeClass('hidden').on('click', () => location.reload());
        $('#titleView').prop('contenteditable', true);
        $('#contentView').prop('contenteditable', true);
    })
    $('form').on('submit', function () {
        $('#modifyTitle').val($('#titleView').text());
        $('#modifyContent').val($('#contentView').text());
    });
    $('#return').on('click', function () {
        location.href = "/select.board?currentPage=1";
    })
    $('#replyButton').on('click', function () {
        if ($('#replyContents').val() !== "") {
            $(this).closest('form').submit();
        } else {
            Swal.fire({
                icon: 'error',
                title: '빈 값을 입력할 수 없습니다.',
                showConfirmButton: false,
                timer: 1500
            })
        }
    })
    const toggleModifyButton = function () {
        const replyBody = $(this).closest('.row').find('.replyBody');
        if (replyBody.prop('contenteditable') === true) {
            replyBody.prop('contenteditable', false);
        } else {
            replyBody.prop('contenteditable', true);
        }
        $(this).closest('div').children().toggleClass('hidden');
    }
    $('.modifyReply').on('click', toggleModifyButton)
    $('.modifyReplyCancel').on('click', toggleModifyButton)
    $('.modifyReplyConfirm').on('click', function () {
        if ($(this).closest('form').find('.replyBody').html() !== '') {
            $(this).closest('form').find('.modifyReplyValue').val($(this).closest('form').find('.replyBody').html());
            $(this).closest('form').submit();
        } else {
            Swal.fire({
                icon: 'error',
                title: '빈 값을 입력할 수 없습니다.',
                showConfirmButton: false,
                timer: 1500
            })
        }
    })
    $('.deleteReply').on('click', function(){
        const deleteId = $(this).closest('form').find('.replyId').val();
        const returnId = $('#replyContentId').val();
        location.href = "/deleteReply.reply?deleteReplyId=" + deleteId + "&returnId=" + returnId;
    })
</script>
</body>
</html>