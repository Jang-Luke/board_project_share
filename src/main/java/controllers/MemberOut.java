package controllers;

import DAO.MemberDAO;
import DTO.MemberDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Member;

@WebServlet("/MemberOut")
public class MemberOut extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MemberDTO currentAccount = (MemberDTO)request.getSession().getAttribute("loginKey");
        try {
            int result = MemberDAO.getInstance().deleteMember(currentAccount);
            request.getSession().invalidate();
            response.sendRedirect("/index.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error.html");
        }
    }
}
