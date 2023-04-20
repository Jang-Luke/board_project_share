package DAO;

import DTO.BoardDTO;
import DTO.ReplyDTO;
import commons.MyDataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public int insertReply(ReplyDTO replyDTO) throws Exception {
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
        String sql = "SELECT R.*, (SELECT COUNT(*) FROM REPLY_LIKE_LIST WHERE REPLY_ID = R.ID) AS LIKE_COUNT FROM REPLY R";
//        String sql = "SELECT * FROM REPLY";
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
        int likeCount = resultSet.getInt("LIKE_COUNT");
        return new ReplyDTO(id, writer, contents, writeDate, parentId, likeCount);
    }

    public boolean hitReplyLike(ReplyDTO replyDTO, String memberId) throws Exception {
        String sql = "";
        boolean result = false;
        if (isUserHitLikeBefore(replyDTO, memberId)) {
            deleteMemberIdLikeList(replyDTO.getId(), memberId);
            result = false;
        } else {
            insertMemberIdLikeList(replyDTO, memberId);
            result = true;
        }
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setLong(1, replyDTO.getId());
            preparedStatement.executeUpdate();
            connection.commit();
            return result;
        }
    }
    public boolean isUserHitLikeBefore(ReplyDTO replyDTO, String memberId) throws Exception {
        String sql = "SELECT R.ID, RL.MEMBER_ID, R.PARENT_ID FROM REPLY R RIGHT OUTER JOIN REPLY_LIKE_LIST RL ON R.ID = RL.REPLY_ID WHERE ID = ? AND MEMBER_ID = ? AND PARENT_ID = ?";
//        String sql = "SELECT * FROM REPLY_LIKE_LIST WHERE MEMBER_ID = ? AND REPLY_ID = ?";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setLong(1, replyDTO.getId());
            preparedStatement.setString(2, memberId);
            preparedStatement.setLong(3, replyDTO.getParentId());
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                return resultSet.next();
            }
        }
    }
    private void insertMemberIdLikeList(ReplyDTO replyDTO, String memberId) throws Exception {
        String sql = "INSERT INTO REPLY_LIKE_LIST VALUES(0, ?, ?)";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setLong(1, replyDTO.getId());
            preparedStatement.setString(2, memberId);
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }
    private void deleteMemberIdLikeList(long replyId, String memberId) throws Exception {
        String sql = "DELETE FROM REPLY_LIKE_LIST WHERE REPLY_ID = ? AND MEMBER_ID = ?";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setLong(1, replyId);
            preparedStatement.setString(2, memberId);
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }
}
