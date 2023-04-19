package controllers;

import DAO.ReplyDAO;
import DTO.MemberDTO;
import DTO.ReplyDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("*.reply")
public class ReplyController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getRequestURI();
        System.out.println(command);
        try {
            // 댓글 작성
            if (command.equals("/insert.reply")) {
                MemberDTO loginID = (MemberDTO)request.getSession().getAttribute("loginKey");
                String writer = loginID.getId();
                String contents = request.getParameter("replyContents");
                long parentId = Long.parseLong(request.getParameter("replyContentId"));
                int result = ReplyDAO.getInstance().replyComment(new ReplyDTO(0, writer, contents, null, parentId));
                response.sendRedirect("/viewTarget.board?id="+parentId);
            // 댓글 수정
            } else if (command.equals("/modifyReply.reply")) {
                long id = Long.parseLong(request.getParameter("modifyReplyId"));
                String writer = request.getParameter("modifyWriter");
                String contents = request.getParameter("modifyReplyContents");
                long parentId = Long.parseLong(request.getParameter("modifyReplyContentId"));
                int result = ReplyDAO.getInstance().modifyReply(new ReplyDTO(id, writer, contents, null, 0));
                response.sendRedirect("/viewTarget.board?id="+parentId);
            // 댓글 삭제
            } else if (command.equals("/deleteReply.reply")) {
                long id = Long.parseLong(request.getParameter("deleteReplyId"));
                long returnId = Long.parseLong(request.getParameter("returnId"));
                System.out.println(id + returnId + "");
                int result = ReplyDAO.getInstance().deleteReply(new ReplyDTO(id, "", "", null, 0));
                response.sendRedirect("/viewTarget.board?id="+returnId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
