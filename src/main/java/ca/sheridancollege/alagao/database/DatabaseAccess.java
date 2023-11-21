package ca.sheridancollege.alagao.database;

import ca.sheridancollege.alagao.beans.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DatabaseAccess {
    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder passenc;

    public User findUserAccount(String email) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String query = "SELECT * FROM sec_user WHERE email = :email";
        params.addValue("email", email);
        return jdbc.queryForObject(query, params, new BeanPropertyRowMapper<>(User.class));
    }

    public List<String> getRolesById(Long userID) {
        MapSqlParameterSource mapsqlp = new MapSqlParameterSource();

        String query = "SELECT sec_role.roleName "
                + "FROM user_role, sec_role "
                + "WHERE user_role.roleId = sec_role.roleId "
                + "AND userID = :userID";

        mapsqlp.addValue("userID", userID);
        return jdbc.queryForList(query, mapsqlp, String.class);
    }

    public void addUser(String email, String passsword) {
        MapSqlParameterSource mapsql = new MapSqlParameterSource();

        String query = "INSERT INTO sec_user (email, encryptedPassword,enabled) "
                + "VALUES (:email, :encryptedPassword, 1)";

        mapsql.addValue("email", email);
        mapsql.addValue("encryptedPassword", passenc.encode(passsword));

        jdbc.update(query, mapsql);
    }

    public void addRole(Long userId, Long roleId) {
        MapSqlParameterSource mapsql = new MapSqlParameterSource();

        String query = "INSERT INTO user_role (userID, roleId) "
                + "VALUES (:userID, :roleId)";

        mapsql.addValue("userID", userId);
        mapsql.addValue("roleId", roleId);

        jdbc.update(query, mapsql);
    }
    public void updateUserBalance(String email, double newBalance) {
        String query = "UPDATE sec_user SET balance = :balance WHERE email = :email";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("balance", newBalance);
        params.addValue("email", email);
        jdbc.update(query, params);
    }
}