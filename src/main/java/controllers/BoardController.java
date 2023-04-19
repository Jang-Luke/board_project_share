package controllers;

import DAO.BoardDAO;
import DAO.ReplyDAO;
import DTO.BoardDTO;
import DTO.ReplyDTO;
import statics.Settings;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("*.board")
public class BoardController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getRequestURI();
        System.out.println(command);
        try {
            // 게시글 작성
            if (command.equals("/insert.board")) {
                String writer = request.getParameter("inWriter");
                String title = request.getParameter("inTitle");
                String contents = request.getParameter("inContents");
                BoardDTO newContents = new BoardDTO(0, writer, title, contents, 0, null);
                int result = BoardDAO.getInstance().insertContent(newContents);
                response.sendRedirect("/select.board?currentPage=1");
            // 게시글 전체(리스트) 출력
            } else if (command.startsWith("/select.board")) { //items
                BoardDAO boardDAO = BoardDAO.getInstance();
                int currentPage = Integer.parseInt(request.getParameter("currentPage"));

                int start = currentPage * Settings.BOARD_RECORD_COUNT_PER_PAGE - (Settings.BOARD_RECORD_COUNT_PER_PAGE-1);
                int end = currentPage * Settings.BOARD_RECORD_COUNT_PER_PAGE;

                List<BoardDTO> contentsList = boardDAO.findAll(start, end);
                List<List<String>> boardNavigator = boardDAO.getPageNavi(currentPage);
                request.setAttribute("contentsList", contentsList);
                request.setAttribute("length", contentsList.size());
                request.setAttribute("navigatorNum", boardNavigator.get(1));
                request.setAttribute("navigatorLast", boardNavigator.get(1).get(boardNavigator.get(1).size()-1));
                request.setAttribute("navigatorPrevNext", boardNavigator.get(0));
                request.setAttribute("navigatorPrevNextLength", boardNavigator.get(0).size());

                request.getRequestDispatcher("/board/board_main.jsp").forward(request, response);
            // 게시글 내용 출력
            } else if (command.startsWith("/viewTarget.board")) {
                BoardDTO targetContent = getTarget(request);
                targetContent = BoardDAO.getInstance().viewTargetContent(targetContent);
                List<ReplyDTO> targetReplies = ReplyDAO.getInstance().findReplyByParentId(targetContent);
                request.setAttribute("targetContent", targetContent);
                request.setAttribute("targetReplies", targetReplies);
                request.getRequestDispatcher("/board/content_view.jsp").forward(request,response);
            // 게시글 삭제
            } else if (command.startsWith("/delete.board")) {
                BoardDTO targetContent = getTarget(request);
                int result = BoardDAO.getInstance().deleteContent(targetContent);
                response.sendRedirect("/select.board?currentPage=1");
            // 게시글 삭제
            } else if (command.startsWith("/modify.board")) {
                long id = Long.parseLong(request.getParameter("id"));
                String title = request.getParameter("modifyTitle");
                String content = request.getParameter("modifyContent");
                int result = BoardDAO.getInstance().updateContent(new BoardDTO(id, null, title, content, 0, null));
                response.sendRedirect("/viewTarget.board?id="+id);

            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/error.html");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    private BoardDTO getTarget(HttpServletRequest request) throws Exception {
        long id = Long.parseLong(request.getParameter("id"));
        return BoardDAO.getInstance().searchById(id);
    }
}
