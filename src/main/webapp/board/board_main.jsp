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
        .btn{
            align-self: center;
            min-width: 50px;
            padding: 6px;
            font-size: 10pt;
        }

        .form-select{
            max-width: 100px;
            font-size: 10pt;
        }
        .form-control{
            font-size: 10pt;
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
            <C:choose>
                <C:when test="${empty requestScope.searchQuery}">
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
                </C:when>
                <C:otherwise>
                    <C:if test="${not empty requestScope.navigatorPrevNext && requestScope.navigatorPrevNextLength > 0 && requestScope.navigatorPrevNext[0] == '<< '}">
                        <span class="navi"><a href="/select.board?currentPage=${requestScope.navigatorNum[0]-1}&searchBound=${requestScope.searchBound}&searchQuery=${requestScope.searchQuery}">${requestScope.navigatorPrevNext[0]}</a></span>
                    </C:if>
                    <C:forEach var="navi" items="${requestScope.navigatorNum}">
                        <span class="navi"><a href="/select.board?currentPage=${navi}&searchBound=${requestScope.searchBound}&searchQuery=${requestScope.searchQuery}">${navi}</a></span>
                    </C:forEach>
                    <C:choose>
                        <C:when test="${not empty requestScope.navigatorPrevNext && requestScope.navigatorPrevNextLength > 0 && requestScope.navigatorPrevNext[0] == '>>'}">
                            <span class="navi"><a href="/select.board?currentPage=${requestScope.navigatorLast+1}&searchBound=${requestScope.searchBound}&searchQuery=${requestScope.searchQuery}">${requestScope.navigatorPrevNext[0]}</a></span>
                        </C:when>
                        <C:when test="${not empty requestScope.navigatorPrevNext && requestScope.navigatorPrevNextLength > 1 && requestScope.navigatorPrevNext[1] == '>>'}">
                            <span class="navi"><a href="/select.board?currentPage=${requestScope.navigatorLast+1}&searchBound=${requestScope.searchBound}&searchQuery=${requestScope.searchQuery}">${requestScope.navigatorPrevNext[1]}</a></span>
                        </C:when>
                    </C:choose>
                </C:otherwise>
            </C:choose>
        </div>
        <div class="col-10">
            <form action="/select.board" id="searchForm" class="d-flex">
                <input type="hidden" name="currentPage" value="1">
                <select name="searchBound" id="searchBound" class="form-select">
                    <option value="TITLE">제목</option>
                    <option value="CONTENTS">내용</option>
                    <option value="WRITER">작성자</option>
                </select>
                <input type="text" name="searchQuery" id="searchQuery" class="form-control" placeholder="검색할 내용을 입력해주세요.">
                <button class="btn btn-dark">검색</button>
            </form>
        </div>
        <div class="col-2 d-flex justify-content-end">
            <a href="/board/writeForm2.jsp">
                <button class="btn btn-outline-primary">작성하기</button>
            </a>
            <a href="/index.jsp">
                <button class="btn btn-light">메인으로</button>
            </a>
        </div>
    </div>
</div>
<script>
    $('#searchForm').on('submit', (event, string) => {
        const searchQuery = $('#searchQuery').val();
        if (searchQuery === '') {
            event.preventDefault();
            Swal.fire({
                icon: 'error',
                title: '검색할 단어를 입력해주세요.',
                showConfirmButton: false,
                timer: 800
            });
            return false;
        } else if (searchQuery === '금지어') {
            event.preventDefault();
            Swal.fire({
                icon: 'error',
                title: string + '은 검색할 수 없는 단어입니다.',
                showConfirmButton: false,
                timer: 800
            })
        }
    })
</script>
</body>
</html>