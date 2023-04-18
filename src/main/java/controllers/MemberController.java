package controllers;

import DAO.MemberDAO;
import DTO.MemberDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@WebServlet("*.member")
public class MemberController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getRequestURI();
        try {
            // 회원가입
            if (command.equals("/createAccount.member")) {
                MemberDTO requestMember = getRequestMemberContainer(request, "in");
                MemberDAO memberDAO = MemberDAO.getInstance();
                int result = memberDAO.insertMember(requestMember);
                response.sendRedirect("/index.jsp?state=a_j");
            // 회원탈퇴
            } else if (command.equals("/deleteAccount.member")) {
                MemberDTO currentAccount = (MemberDTO) request.getSession().getAttribute("loginKey");
                int result = MemberDAO.getInstance().deleteMember(currentAccount);
                response.sendRedirect("/logout.member");
            // 정보수정
            } else if (command.equals("/updateAccount.member")) {
                MemberDTO requestMember = getRequestMemberContainer(request, "modify");
                int result = MemberDAO.getInstance().modifyMember(requestMember);
                MemberDTO newAccount = MemberDAO.getInstance().searchById(requestMember.getId());
                request.getSession().removeAttribute("loginKey");
                request.getSession().setAttribute("loginKey", newAccount);
                response.sendRedirect("/index.jsp");
            // 마이페이지로
            } else if (command.equals("/toMyPage.member")) {
                response.sendRedirect("/member/myPage.jsp");
            // 로그인
            } else if (command.equals("/tryLogin.member")) {
                String id = request.getParameter("mainId");
                String pw = request.getParameter("mainPw");
                MemberDAO memberDAO = MemberDAO.getInstance();
                MemberDTO result = memberDAO.doLogin(new MemberDTO(id, pw, "", "", "", "", "", "", null));
                if (!result.getId().equals("1")) {
                    request.getSession().setAttribute("loginKey", result);
                    response.sendRedirect("/index.jsp");
                } else {
                    request.setAttribute("loginFailed", "fail");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                }
            // 로그아웃
            } else if (command.equals("/logout.member")) {
                request.getSession().invalidate();
                response.sendRedirect("/index.jsp");
            // 아이디 중복 체크
            } else if (command.equals("/idDuplicationCheck.member")) {
                String id = request.getParameter("inId");
                MemberDAO memberDAO = MemberDAO.getInstance();
                boolean result = true;
                result = memberDAO.is_id_duplicate(id);
                request.setAttribute("duplicateCheck", result);
                request.getRequestDispatcher("/member/idCheckResult.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error.html");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        doGet(request, response);
    }
    private MemberDTO getRequestMemberContainer(HttpServletRequest request, String actionType) {
        String id = request.getParameter(actionType + "Id");
        String pw = request.getParameter(actionType + "Pw");
        String name = request.getParameter(actionType + "Name");
        String phone = request.getParameter(actionType + "Phone")
                .chars()
                .filter(character -> 48 <= character && character <= 57)
                .mapToObj(element -> (char) element)
                .map(Object::toString)
                .collect(Collectors.joining());
        StringBuilder stringBuilder = new StringBuilder(phone.substring(0, 3));
        phone = stringBuilder.append("-")
                .append(phone.substring(3, 7))
                .append("-")
                .append(phone.substring(7))
                .toString();
        String email = request.getParameter(actionType + "Email");
        String zipcode = request.getParameter(actionType + "Zipcode");
        String address1 = request.getParameter(actionType + "Address1");
        String address2 = request.getParameter(actionType + "Address2");
        return new MemberDTO(id, pw, name, phone, email, zipcode, address1, address2, null);
    }
}
