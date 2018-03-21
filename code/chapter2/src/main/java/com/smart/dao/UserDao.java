package com.smart.dao;

import com.smart.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

//通过spring注解定义一个DAO
@Repository
public class UserDao {
    private JdbcTemplate jdbcTemplate;

    //根据用户名查询用户的SQL语句
    private final static String MATCH_COUNT_SQL = "SELECT count(*) FROM " +
            " t_user WHERE user_name=? and password=? ";

    private final static String UPDATE_LOGIN_INFO_SQL = "UPDATE t_user SET " +
            " last_visit=?, last_ip=?, credits=? WHERE user_id=?";

    //自动注入JdbcTemplate的Bean
    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getMatchCount(String userName, String password) {
        return jdbcTemplate.queryForObject(MATCH_COUNT_SQL, new Object[]{userName, password}, Integer.class);
    }

    public User findUserByUserName(final String userName) {
        String sqlStr = " SELECT user_id,user_name,credits "
                + " FROM t_user WHERE user_name =? ";
        final User user = new User();
        jdbcTemplate.query(sqlStr, new Object[] { userName },
                //匿名类方式实现的回调函数
                new RowCallbackHandler() {
                    public void processRow(ResultSet rs) throws SQLException {
                        user.setUserId(rs.getInt("user_id"));
                        user.setUserName(userName);
                        user.setCredits(rs.getInt("credits"));
                    }
                });
        return user;
    }

    public void updateLoginInfo(User user) {
        jdbcTemplate.update(UPDATE_LOGIN_INFO_SQL, new Object[] { user.getLastVisit(),
                user.getLastIp(),user.getCredits(),user.getUserId()});
    }

}
