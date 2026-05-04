package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setUserId(rs.getLong("user_id"));
        user.setFirstName(rs.getString("first_name"));
        user.setMiddleName(rs.getString("middle_name"));
        user.setLastName((rs.getString("last_name")));
        user.setUsername((rs.getString("username")));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setRole(rs.getString("role"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return user;
    };

    @Override
    public int save(User user) {
        String sql = """
                INSERT INTO users (username,first_name, middle_name, last_name ,email, password, phone_number, role, email_verified, verification_token)
                VALUES (?, ?,?, ?, ?, ?, ?,?,?,?)
                """;

        return jdbcTemplate.update(
                sql,
                user.getUsername(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getEmailVerified(),
                user.getVerificationToken()
        );
    }

    @Override
    public Optional<User> findById(Long userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        List<User> users = jdbcTemplate.query(sql, userRowMapper, userId);

        return users.stream().findFirst();
    }

    @Override
    public Optional<User> findByName(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, username);
        return users.stream().findFirst();
    }

    @Override
    public String findEmailByUserId(Long userId) {
        String sql = "SELECT email FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, userId);    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        List<User> users = jdbcTemplate.query(sql, userRowMapper, email);

        return users.stream().findFirst();
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users ORDER BY created_at DESC";

        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public int update(User user) {
        String sql = """
                UPDATE users
                SET username = ?, first_name=?, middle_name=?, last_name=?, phone_number = ?, role = ?
                WHERE user_id = ?
                """;

        return jdbcTemplate.update(
                sql,
                user.getUsername(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getUserId()
        );
    }

    @Override
    public int deleteById(Long userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        return jdbcTemplate.update(sql, userId);
    }

    @Override
    public Optional<User> findByVerificationToken(String token) {
        String sql = "SELECT * FROM users WHERE verification_token = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, token);
        return users.stream().findFirst();
    }

    @Override
    public int verifyEmail(Long userId) {
        String sql = """
            UPDATE users
            SET email_verified = true,
                verification_token = null
            WHERE user_id = ?
            """;

        return jdbcTemplate.update(sql, userId);
    }
}
