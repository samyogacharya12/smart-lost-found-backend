package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setUserId(rs.getLong("user_id"));
        user.setFullName(rs.getString("full_name"));
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
                INSERT INTO users (full_name, email, password, phone_number, role)
                VALUES (?, ?, ?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhoneNumber(),
                user.getRole()
        );
    }

    @Override
    public Optional<User> findById(Long userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        List<User> users = jdbcTemplate.query(sql, userRowMapper, userId);

        return users.stream().findFirst();
    }

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
                SET full_name = ?, phone_number = ?, role = ?
                WHERE user_id = ?
                """;

        return jdbcTemplate.update(
                sql,
                user.getFullName(),
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
}
