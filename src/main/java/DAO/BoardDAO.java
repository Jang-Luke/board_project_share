package DAO;

import DTO.BoardDTO;
import commons.MyDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import statics.Settings;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO {
    private final BasicDataSource basicDataSource;
    private static BoardDAO instance = null;

    public synchronized static BoardDAO getInstance() {
        if (instance == null) {
            instance = new BoardDAO(MyDataSource.getBasicDataSource("/Users/luke/Documents/GitHub/test/workspace_back/board_project/src/main/resources/DB_properties.properties"));
        }
        return instance;
    }

    private BoardDAO(BasicDataSource basicDataSource) {
        this.basicDataSource = basicDataSource;
    }

    public int insertContent(BoardDTO boardDTO) throws Exception {
        String sql = "INSERT INTO BOARD VALUES (0, ?, ?, ?, DEFAULT, DEFAULT)";
        try (Connection connection = basicDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setString(1, boardDTO.getWriter());
            preparedStatement.setString(2, boardDTO.getTitle());
            preparedStatement.setString(3, boardDTO.getContents());
            int result = preparedStatement.executeUpdate();
            connection.commit();
            return result;
        }
    }

    public int deleteContent(BoardDTO targetContent) throws Exception {
        String sql = "DELETE FROM BOARD WHERE ID = ?";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setLong(1, targetContent.getId());
            int result = preparedStatement.executeUpdate();
            connection.commit();
            return result;
        }
    }

    public List<BoardDTO> findAll() throws Exception {
        String sql = "SELECT * FROM BOARD ORDER BY ID DESC";
        try (Connection connection = basicDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            try (ResultSet resultSet = preparedStatement.executeQuery();) {
                List<BoardDTO> contentsList = new ArrayList<>();
                while (resultSet.next()) {
                    contentsList.add(getContentsContainer(resultSet));
                }
                return contentsList;
            }
        }
    }

    // 1. SELECT * FROM BOARD ORDER BY ID DESC
    public List<BoardDTO> findAll(int start, int end) throws Exception {
        String sql = "SELECT * FROM (SELECT BOARD.*, ROW_NUMBER() OVER(ORDER BY ID DESC) \"NO\" FROM BOARD) NUM WHERE NO BETWEEN ? AND ?";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setInt(1, start);
            preparedStatement.setInt(2, end);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                List<BoardDTO> contentsList = new ArrayList<>();
                while(resultSet.next()){
                    contentsList.add(getContentsContainer(resultSet));
                }
                return contentsList;
            }
        }
    }

    public int getSearchContentsCount(String bound, String query) throws Exception {
        String sql = "SELECT * FROM (SELECT BOARD.*, ROW_NUMBER() OVER(ORDER BY ID DESC) \"NO\" FROM BOARD WHERE " + bound + " LIKE ?) NUM";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setString(1, "%"+query+"%");
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                int searchContentsCount = 0;
                while(resultSet.next()){
                    if (resultSet.isLast()) {
                        searchContentsCount = resultSet.getRow();
                    }
                }
                return searchContentsCount;
            }
        }
    }
    public List<BoardDTO> searchContents(String bound, String query, int start, int end) throws Exception {
        String sql = "SELECT * FROM (SELECT BOARD.*, ROW_NUMBER() OVER(ORDER BY ID DESC) \"NO\" FROM BOARD WHERE " + bound + " LIKE ?) NUM WHERE NO BETWEEN ? AND ?";

        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setString(1, "%"+query+"%");
            preparedStatement.setInt(2, start);
            preparedStatement.setInt(3, end);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                List<BoardDTO> searchResult = new ArrayList<>();
                while(resultSet.next()){
                    searchResult.add(getContentsContainer(resultSet));
                }
                return searchResult;
            }
        }
    }

    public int updateContent(BoardDTO targetContent) throws Exception {
        String sql = "UPDATE BOARD SET TITLE = ?, CONTENTS = ? WHERE ID = ?";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setString(1, targetContent.getTitle());
            preparedStatement.setString(2, targetContent.getContents());
            preparedStatement.setLong(3, targetContent.getId());
            int result = preparedStatement.executeUpdate();
            connection.commit();
            return result;
        }
    }

    public BoardDTO searchById(long id) throws Exception {
        String sql = "SELECT * FROM BOARD WHERE ID = ?";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery();) {
                if (resultSet.next()) {
                    return getContentsContainer(resultSet);
                } else {
                    throw new Exception("해당하는 게시글이 없습니다.");
                }
            }
        }
    }
    public BoardDTO viewTargetContent(BoardDTO targetContent) throws Exception {
        String sql = "UPDATE BOARD SET VIEW_COUNT = ? WHERE ID = ?";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setInt(1, (targetContent.getViewCount()+1));
            preparedStatement.setLong(2, targetContent.getId());
            preparedStatement.executeUpdate();
            connection.commit();
            return searchById(targetContent.getId());
        }
    }

    private int getRecordCount() throws Exception {
        String sql = "SELECT COUNT(*) FROM BOARD";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();){
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    private BoardDTO getContentsContainer(ResultSet resultSet) throws Exception { //
        long id = resultSet.getLong("ID");
        String writer = resultSet.getString("WRITER");
        String title = resultSet.getString("TITLE");
        String contents = resultSet.getString("CONTENTS");
        int view_count = resultSet.getInt("view_count");
        Timestamp write_date = resultSet.getTimestamp("WRITE_DATE");
        return new BoardDTO(id, writer, title, contents, view_count, write_date);
    }

    public List<List<String>> getPageNavi(int currentPage, boolean isSearch, int targetSize) throws Exception {
        // 네비게이터를 만들기 위해 필요한 초기 정보
        int recordTotalCount;
        if (!isSearch) {
            recordTotalCount = findAll().size(); // 1. 전체 글의 개수
        } else {
            recordTotalCount = targetSize == 0 ? 1 : targetSize;
        }
        // Settings.BOARD_RECORD_COUNT_PER_PAGE // 2. 페이지 당 보여줄 글의 개수
        // Settings.BOARD_NAVI_COUNT_PER_PAGE // 3. 페이지 당 보여줄 네비게이터의 개수

        int pageTotalCount = (int)Math.ceil((double)recordTotalCount/Settings.BOARD_RECORD_COUNT_PER_PAGE);
        // 4. 1번과 2번 항목에 의해 총 페이지의 개수가 정해짐. 나머지가 발생하면 페이지 + 1

        if (currentPage < 1) {
            currentPage = 1;
        } else if (currentPage > pageTotalCount) {
            currentPage = pageTotalCount;
        }

        int startNavi = (currentPage-1) / Settings.BOARD_NAVI_COUNT_PER_PAGE * Settings.BOARD_NAVI_COUNT_PER_PAGE + 1;
        int endNavi = startNavi+(Settings.BOARD_NAVI_COUNT_PER_PAGE-1);

        if (endNavi > pageTotalCount) {
            endNavi = pageTotalCount;
        }

        boolean needPrev = true;
        boolean needNext = true;

        if (startNavi == 1) {
            needPrev = false;
        }
        if (endNavi==pageTotalCount) {
            needNext = false;
        }
        List<String> naviPrevNext = new ArrayList<>();
        if (needPrev) {
            naviPrevNext.add("<< ");
        }
        if (needNext) {
            naviPrevNext.add(">>");
        }

        List<String> naviNumbers = new ArrayList<>();
        for (int i = startNavi; i <= endNavi; i++) {
            naviNumbers.add(String.valueOf(i));
        }
        List<List<String>> navigators = new ArrayList<>();
        navigators.add(naviPrevNext);
        navigators.add(naviNumbers);
        return navigators;
    }

}
