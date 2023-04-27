package controllers;

import DAO.BoardDAO;
import DAO.ReplyDAO;
import DTO.BoardDTO;
import DTO.MemberDTO;
import DTO.ReplyDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import statics.Settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("*.board")
public class BoardController extends HttpServlet {
    private String XSSCheck(String text) {
        return text.replaceAll("<script>", "&lt;script>");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getRequestURI();
        System.out.println(command);
        try {
            // 게시글 작성
            if (command.equals("/insert.board")) {
                String writer = request.getParameter("inWriter");
                String title = request.getParameter("inTitle");
                title.replaceAll("<script>", "&lt;script>");
                String contents = request.getParameter("inContents");
                contents.replaceAll("<script>", "&lt;script>");
                BoardDTO newContents = new BoardDTO(0, writer, title, contents, 0, null);
                int result = BoardDAO.getInstance().insertContent(newContents);
                response.sendRedirect("/select.board?currentPage=1");
            // 게시글 출력
            } else if (command.startsWith("/select.board")) { //items
                BoardDAO boardDAO = BoardDAO.getInstance();
                int currentPage = Integer.parseInt(request.getParameter("currentPage"));

                int start = currentPage * Settings.BOARD_RECORD_COUNT_PER_PAGE - (Settings.BOARD_RECORD_COUNT_PER_PAGE-1);
                int end = currentPage * Settings.BOARD_RECORD_COUNT_PER_PAGE;

                String searchBound = request.getParameter("searchBound");
                String searchQuery = request.getParameter("searchQuery");
                List<BoardDTO> contentsList = new ArrayList<>();
                boolean isSearch = false;
                int targetSize = 0;
                if (searchBound == null && searchQuery == null) {
                    contentsList = boardDAO.findAll(start, end);
                } else {
                    request.setAttribute("searchBound", searchBound);
                    request.setAttribute("searchQuery", searchQuery);
                    contentsList = boardDAO.searchContents(searchBound, searchQuery, start, end);
                    isSearch = true;
                    targetSize = boardDAO.getSearchContentsCount(searchBound, searchQuery);
                }

                List<List<String>> boardNavigator = boardDAO.getPageNavi(currentPage, isSearch, targetSize);
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

                MemberDTO loginKey = (MemberDTO)request.getSession().getAttribute("loginKey");
                List<String> likedLog = targetReplies.stream()
                        .map((reply) -> {
                            try {
                                return String.valueOf(ReplyDAO.getInstance().isUserHitLikeBefore(reply, loginKey.getId()));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }).collect(Collectors.toList());
//                boolean isLiked = ReplyDAO.getInstance().isUserHitLikeBefore(, loginKey.getId());
                request.setAttribute("liked", likedLog);

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
