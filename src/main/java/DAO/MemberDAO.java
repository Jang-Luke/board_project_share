package DAO;

import DTO.MemberDTO;
import commons.MyDataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class MemberDAO {
    private final BasicDataSource basicDataSource;
    private static MemberDAO instance = null;
    public synchronized static MemberDAO getInstance(){
        if (instance == null) {
            instance = new MemberDAO(MyDataSource.getBasicDataSource("/Users/luke/Documents/GitHub/test/workspace_back/board_project/src/main/resources/DB_properties.properties"));
        }
        return instance;
    }

    private MemberDAO(BasicDataSource basicDataSource) {
        this.basicDataSource = basicDataSource;
    }

    public int insertMember(MemberDTO memberDTO) throws Exception{
        String sql = "INSERT INTO MEMBERS VALUES(?, ?, ?, ?, ?, ?, ?, ?, DEFAULT)";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setString(1, memberDTO.getId());
            preparedStatement.setString(2, memberDTO.getPw());
            preparedStatement.setString(3, memberDTO.getName());
            preparedStatement.setString(4, memberDTO.getPhone());
            preparedStatement.setString(5, memberDTO.getEmail());
            preparedStatement.setString(6, memberDTO.getZipcode());
            preparedStatement.setString(7, memberDTO.getAddress1());
            preparedStatement.setString(8, memberDTO.getAddress2());
            int result = preparedStatement.executeUpdate();
            connection.commit();
            return result;
        }
    }
    public int deleteMember(MemberDTO memberDTO) throws Exception {
        String sql = "DELETE FROM MEMBERS WHERE ID = ?";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setString(1, memberDTO.getId());
            int result = preparedStatement.executeUpdate();
            connection.commit();
            return result;
        }
    }
    public MemberDTO searchById(String id) throws Exception {
        String sql = "SELECT * FROM MEMBERS WHERE ID = ?";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setString(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                return getAccount(resultSet);
            }
        }
    }
    public MemberDTO doLogin(MemberDTO memberDTO) throws Exception{
        String sql = "SELECT * FROM MEMBERS WHERE ID = ? AND PW = ?";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setString(1, memberDTO.getId());
            preparedStatement.setString(2, memberDTO.getPw());
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                return getAccount(resultSet);
            }
        }
    }

    private MemberDTO getAccount(ResultSet resultSet) throws Exception {
        if (resultSet.next()) {
            String id = resultSet.getString("ID");
            String pw = resultSet.getString("PW");
            String name = resultSet.getString("NAME");
            String phone = resultSet.getString("PHONE");
            String email = resultSet.getString("EMAIL");
            String zipcode = resultSet.getString("ZIPCODE");
            String address1 = resultSet.getString("ADDRESS_1");
            String address2 = resultSet.getString("ADDRESS_2");
            Timestamp join_date = resultSet.getTimestamp("JOIN_DATE");
            return new MemberDTO(id, pw, name, phone, email, zipcode, address1, address2, join_date);
        } else {
            MemberDTO notExist = new MemberDTO();
            notExist.setId("1");
            return notExist;
        }
    }
    public boolean is_id_duplicate(String id) throws Exception{
        String sql = "SELECT * FROM MEMBERS WHERE ID = ?";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setString(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                return resultSet.next();
            }
        }
    }

    public int modifyMember(MemberDTO memberDTO) throws Exception{
        String sql = "UPDATE MEMBERS SET NAME = ?, PHONE = ?, EMAIL = ?, ZIPCODE = ?, ADDRESS_1 = ?, ADDRESS_2 = ? WHERE ID = ?";
        try(Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setString(1, memberDTO.getName());
            preparedStatement.setString(2, memberDTO.getPhone());
            preparedStatement.setString(3, memberDTO.getEmail());
            preparedStatement.setString(4, memberDTO.getZipcode());
            preparedStatement.setString(5, memberDTO.getAddress1());
            preparedStatement.setString(6, memberDTO.getAddress2());
            preparedStatement.setString(7, memberDTO.getId());
            int result = preparedStatement.executeUpdate();
            connection.commit();
            return result;
        }
    }
}
