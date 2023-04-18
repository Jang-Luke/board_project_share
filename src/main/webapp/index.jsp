<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <script src="https://cdn.jsdelivr.net/npm/jquery/dist/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/3.1.2/rollups/aes.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap');

        * {
            font-family: 'Noto Sans KR', sans-serif;
        }

        body {
            background-color: #1BBC9B;
        }

        .my-container {
            margin: auto;
            width: 300px;
            background-color: #EEEFF1;
            border-radius: 5px;
            text-align: center;
            padding: 20px;
        }

        input {
            width: 100%;
            padding: 10px;
            box-sizing: border-box;
            border-radius: 5px;
            border: none;
        }

        .in {
            margin-bottom: 10px;
        }

        .my-btn {
            background-color: #1BBC9B;
            margin-bottom: 30px;
            color: white;
        }

        a {
            text-decoration: none;
            color: #9B9B9B;
            font-size: 12px;
        }
    </style>
</head>

<body>
<C:if test="${param.state=='a_j'}">
    <script>
        Swal.fire({
            title: '회원가입을 축하합니다.',
            icon: 'info',
            showConfirmButton: false,
            timer: 1500
        })
    </script>
</C:if>
<C:if test="${requestScope.loginFailed=='fail'}">
    <script>
        Swal.fire({
            title: '아이디 또는 비밀번호를 다시 확인해주세요.',
            icon: 'error',
            showConfirmButton: false,
            timer: 1500
        })
    </script>
</C:if>

<C:choose>
    <C:when test="${sessionScope.loginKey==null}">
        <div class="my-container">
            <form action="/tryLogin.member" method="post">
                <input type="text" id="mainId" name="mainId" placeholder="아이디" class="in">
                <input type="password" id="mainPw" name="mainPw" placeholder="비밀번호" class="in">
                <input type="button" class="my-btn" id="btn" value="로그인"><br>
                <input type="button" class="my-btn" id="toJoin" value="회원가입"><br>
                <input type="checkbox" name="" id="">ID 기억하기
            </form>
            <a href="#none">비밀번호를 잊어버리셨나요?</a>
        </div>
        <script>
            const id = document.querySelector('#mainId');
            const pw = document.querySelector('#mainPw');
            const loginBtn = $('#btn');
            $('#toJoin').on('click', function () {
                location.href = "/member/joinform.jsp";
            })
            const encryptionPw = function (pw) {
                var hash = CryptoJS.MD5(pw);
                var key = CryptoJS.enc.Utf8.parse(hash);
                var base64 = CryptoJS.enc.Base64.stringify(key);
                return base64;
            }
            const doSubmit = function () {
                pw.value = encryptionPw(pw.value);
                loginBtn.closest('form').submit();
            }
            loginBtn.on('click', doSubmit);
            id.addEventListener('keyup', (e) => {
                if (e.key === 'Enter') {
                    e.preventDefault();
                    $('#mainId').next().focus();
                }
            })
            pw.addEventListener('keyup', (e) => {
                if (e.key === 'Enter') {
                    doSubmit();
                }
            })
        </script>
    </C:when>
    <C:otherwise>
        <table align="center">
            <tr>
                <th colspan="4">
                        ${sessionScope.loginKey.id} 님 환영합니다.
                </th>
            </tr>
            <tr>
                <td>
                    <a href="/select.board?currentPage=1">
                        <button>게시판으로</button>
                    </a>
                </td>
                <td>
                    <a href="/toMyPage.member">
                        <button>마이페이지</button>
                    </a>
                </td>
                <td>
                    <a href="/logout.member">
                        <button id="logout">로그아웃</button>
                    </a>
                </td>
                <td>
                    <button type="button" id="memberOut">회원탈퇴</button>
                </td>
            </tr>
        </table>
        <script>
            $('#memberOut').on('click', function() {
                window.open("/member/confirmDelete.jsp", "_blank", "width=350px,height=250px");
            });
            var deleteMember = function() {
                location.href = "/deleteAccount.member";
            }
        </script>
    </C:otherwise>
</C:choose>

</body>
</html>