package DAO;

import DTO.BoardDTO;
import DTO.ReplyDTO;
import commons.MyDataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReplyDAO {
    private final BasicDataSource basicDataSource;
    private static ReplyDAO instance = null;

    public synchronized static ReplyDAO getInstance() {
        if (instance == null) {
            instance = new ReplyDAO(MyDataSource.getBasicDataSource("/Users/luke/Documents/GitHub/test/workspace_back/board_project/src/main/resources/DB_properties.properties"));
        }
        return instance;
    }

    private ReplyDAO(BasicDataSource basicDataSource) {
        this.basicDataSource = basicDataSource;
    }

    public int replyComment(ReplyDTO replyDTO) throws Exception {
        String sql = "INSERT INTO REPLY VALUES(0, ?, ?, DEFAULT, ?, DEFAULT)";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setString(1, replyDTO.getWriter());
            preparedStatement.setString(2, replyDTO.getContents());
            preparedStatement.setLong(3, replyDTO.getParentId());
            int result = preparedStatement.executeUpdate();
            connection.commit();
            return result;
        }
    }

    public int modifyReply(ReplyDTO replyDTO) throws Exception {
        String sql = "UPDATE REPLY SET CONTENTS = ? WHERE ID = ?";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setString(1, replyDTO.getContents());
            preparedStatement.setLong(2, replyDTO.getId());
            int result = preparedStatement.executeUpdate();
            connection.commit();
            return result;
        }
    }

    public int deleteReply(ReplyDTO replyDTO) throws Exception {
        String sql = "DELETE FROM REPLY WHERE ID = ?";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setLong(1, replyDTO.getId());
            int result = preparedStatement.executeUpdate();
            connection.commit();
            return result;
        }
    }

    public List<ReplyDTO> findReplyByParentId(BoardDTO boardDTO) throws Exception {
        return findAllReply()
                .stream()
                .filter((targetReply) -> {
                    return targetReply.getParentId() == boardDTO.getId();
                }).collect(Collectors.toList());
    }

    private List<ReplyDTO> findAllReply() throws Exception {
        String sql = "SELECT * FROM REPLY";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                List<ReplyDTO> replies = new ArrayList<>();
                while (resultSet.next()) {
                    replies.add(getReplies(resultSet));
                }
                return replies;
            }
        }
    }
    private ReplyDTO getReplies(ResultSet resultSet) throws Exception {
        long id = resultSet.getLong("ID");
        String writer = resultSet.getString("WRITER");
        String contents = resultSet.getString("CONTENTS");
        Timestamp writeDate = resultSet.getTimestamp("WRITE_DATE");
        long parentId = resultSet.getLong("PARENT_ID");
        int replyLikeCount = resultSet.getInt("REPLY_LIKE_COUNT");
        return new ReplyDTO(id, writer, contents, writeDate, parentId, replyLikeCount);
    }

    public void hitReplyCount(ReplyDTO replyDTO) throws Exception {
        String sql = "UPDATE REPLY SET REPLY_LIKE_COUNT = REPLY_LIKE_COUNT + 1 WHERE ID = ?";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setLong(1, replyDTO.getId());
            preparedStatement.executeUpdate();
        }
    }
}
