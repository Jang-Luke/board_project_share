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

@WebServlet("/UpdateProp")
public class UpdateProp extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("modifyId");
        String name = request.getParameter("modifyName");
        String phone = request.getParameter("modifyPhone");
        String email = request.getParameter("modifyEmail");
        String zipcode = request.getParameter("modifyZipcode");
        String address1 = request.getParameter("modifyAddress1");
        String address2 = request.getParameter("modifyAddress2");

        MemberDTO modifiedValueContainer = new MemberDTO(id, "1", name, phone, email, zipcode, address1, address2, Timestamp.valueOf(LocalDateTime.now()));
        try {
            int result = MemberDAO.getInstance().modifyMember(modifiedValueContainer);
            MemberDTO newAccount = MemberDAO.getInstance().searchById(id);
            if (!newAccount.getId().equals("1")) {
                request.getSession().invalidate();
                request.getSession().setAttribute("loginKey", newAccount);
                response.sendRedirect("/index.jsp");
            } else {
                request.setAttribute("loginFailed", true);
                request.getRequestDispatcher("/index.jsp").forward(request,response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error.html");
        }
    }
}
