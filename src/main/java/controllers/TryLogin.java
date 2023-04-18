package controllers;

import DAO.MemberDAO;
import DTO.MemberDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/TryLogin")
public class TryLogin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        HTTP 의 특성 - Stateless
        HTTP 통신의 stateless 특성 때문에 한 번 로그인에 성공했었던 사실을 기억하지 못한다.
        이를 해결하기 위해 브라우저에서 cookie 라는 파일을 만들어 로그인 정보를 별도로 저장한다. (과거에 사용하던 방식)
        쿠키 하이재킹 (쿠키만 어떻게든 탈취하면 로그인 상태로 서버에 접속 가능 - 보안 취약) 문제로 로그인 시에는 이제 사용하지 않음
        쿠키의 대표적인 사용 예 -> 쇼핑몰(장바구니)
        로그인 프로세싱은 한 단계 더 업그레이드 되었음
        :>: 로그인에 성공할 시 서버 측에 보관소를 만들어서 특정 정보(session 키 값)를 보관함
        :>: session 키 값을 response 에 적재하여 브라우저로 전달
        :>: 전달받은 session 키 값을 브라우저에서는 파일이 아닌 램 속에 '변수'로 저장
        session 키 값을 어떻게든 탈취하기만 한다면 여전히 치명적이긴 하지만, 쿠키에 비해 그 난이도가 훨씬 높다.
        또한 session 키는 휘발성이 있어 브라우저가 닫히면 사라지게 되어 보안상으로 강화되었다.
         */
        String id = request.getParameter("mainId");
        String pw = request.getParameter("mainPw");
        MemberDAO memberDAO = MemberDAO.getInstance();
        try {
            MemberDTO result = memberDAO.doLogin(new MemberDTO(id, pw, "가", "010-0000-0000", "email.@email.com", "", "", "", null));
            if (!result.getId().equals("1")) {
//                request.setAttribute("matchedAccount", result);
                request.getSession().setAttribute("loginKey", result);
                System.out.println(result.getId());
//                request.getRequestDispatcher("/myPage.jsp").forward(request, response);
                response.sendRedirect("/index.jsp");
            } else{
                request.setAttribute("loginFailed", true);
                request.getRequestDispatcher("/index.jsp").forward(request,response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error.html");
        }
    }
}
