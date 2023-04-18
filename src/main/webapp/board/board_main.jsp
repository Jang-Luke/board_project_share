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
    <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/3.1.2/rollups/aes.js"></script>
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
        .borderless{
            border: none;
        }
        .border-solid-1{
            border: 1px solid black;
        }
        .text-eclipse{
            text-overflow: ellipsis;
        }
        .content-lists{
            min-width: 70px;
            border: none;
        }
        .navi{
            margin: 1px 3px 1px 3px;
        }
    </style>
</head>
<body>
<div class="container border-solid-1">
    <div class="row">
        <div class="col-12 border-solid-1">
            <p class="font-size-title">자유게시판</p>
        </div>
        <div class="col-1 border-solid-1">#</div>
        <div class="col-5 border-solid-1">제목</div>
        <div class="col-3 border-solid-1">작성자</div>
        <div class="col-2 border-solid-1">날짜</div>
        <div class="col-1 border-solid-1">조회</div>
    </div>
    <div class="row">
    <C:choose>
        <C:when test="${requestScope.length==0}">
            <div class="col-12 borderless board-content-body">표시할 내용이 없습니다.</div>
        </C:when>
        <C:otherwise>
            <C:forEach var="contents" items="${requestScope.contentsList}">
                <div class="col-1 borderless">${contents.id}</div>
                <div class="col-5 content-lists text-eclipse"><a href="/viewTarget.board?id=${contents.id}">${contents.title}</a></div>
                <div class="col-3 content-lists">${contents.writer}</div>
                <div class="col-2 content-lists">${contents.formattedWriteDate}</div>
                <div class="col-1 borderless">${contents.viewCount}</div>
            </C:forEach>
        </C:otherwise>
    </C:choose>
    </div>
    <div class="row boardFooter">
        <div class="col-12 d-flex justify-content-center">
            <C:if test="${not empty requestScope.navigatorPrevNext && requestScope.navigatorPrevNextLength > 0 && requestScope.navigatorPrevNext[0] == '<< '}">
                <span class="navi"><a href="/select.board?currentPage=${requestScope.navigatorNum[0]-1}">${requestScope.navigatorPrevNext[0]}</a></span>
            </C:if>
            <C:forEach var="navi" items="${requestScope.navigatorNum}">
                <span class="navi"><a href="/select.board?currentPage=${navi}">${navi}</a></span>
            </C:forEach>
            <C:choose>
                <C:when test="${not empty requestScope.navigatorPrevNext && requestScope.navigatorPrevNextLength > 0 && requestScope.navigatorPrevNext[0] == '>>'}">
                    <span class="navi"><a href="/select.board?currentPage=${requestScope.navigatorLast+1}">${requestScope.navigatorPrevNext[0]}</a></span>
                </C:when>
                <C:when test="${not empty requestScope.navigatorPrevNext && requestScope.navigatorPrevNextLength > 1 && requestScope.navigatorPrevNext[1] == '>>'}">
                    <span class="navi"><a href="/select.board?currentPage=${requestScope.navigatorLast+1}">${requestScope.navigatorPrevNext[1]}</a></span>
                </C:when>
            </C:choose>
        </div>
        <div class="col-12 d-flex justify-content-end border-solid-1">
            <a href="/board/writeForm2.jsp">
                <button class="btn btn-outline-primary">작성하기</button>
            </a>
            <a href="/board/writeForm2.jsp">
                <button class="btn btn-light">메인으로</button>
            </a>
        </div>
    </div>
</div>
</body>
</html>