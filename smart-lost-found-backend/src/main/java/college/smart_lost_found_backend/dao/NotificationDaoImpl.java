package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.model.Notification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NotificationDaoImpl implements NotificationDao {

    private final JdbcTemplate jdbcTemplate;

    public NotificationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private final RowMapper<Notification> notificationRowMapper = (rs, rowNum) -> {
        Notification notification = new Notification();

        notification.setId(rs.getLong("id"));
        notification.setUserId(rs.getLong("user_id"));
        notification.setTitle(rs.getString("title"));
        notification.setMessage(rs.getString("message"));
        notification.setRead(rs.getBoolean("is_read"));

        if (rs.getTimestamp("created_at") != null) {
            notification.setCreated(rs.getTimestamp("created_at").toLocalDateTime());
        }

        return notification;
    };



    @Override
    public int save(Notification notification) {
        String sql = """
                INSERT INTO notifications (user_id, title, message, is_read)
                VALUES (?, ?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                notification.getUserId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.isRead()
        );
    }

    @Override
    public Optional<Notification> findById(Long notificationId) {
        String sql = """
                SELECT id, user_id, title, message, is_read, created_at
                FROM notifications
                WHERE notification_id = ?
                """;

        List<Notification> notifications =
                jdbcTemplate.query(sql, notificationRowMapper, notificationId);

        return notifications.stream().findFirst();
    }

    @Override
    public List<Notification> findByUserId(Long userId) {
        String sql = """
                SELECT id, user_id, title, message, is_read, created_at
                FROM notifications
                WHERE user_id = ?
                ORDER BY created_at DESC
                """;

        return jdbcTemplate.query(sql, notificationRowMapper, userId);
    }

    @Override
    public List<Notification> findUnreadByUserId(Long userId) {
        String sql = """
                SELECT id, user_id, title, message, is_read, created_at
                FROM notifications
                WHERE user_id = ?
                AND is_read = false
                ORDER BY created_at DESC
                """;

        return jdbcTemplate.query(sql, notificationRowMapper, userId);
    }

    @Override
    public int markAsRead(Long notificationId) {
        String sql = """
                UPDATE notifications
                SET is_read = true
                WHERE id = ?
                """;

        return jdbcTemplate.update(sql, notificationId);
    }

    @Override
    public int markAllAsRead(Long userId) {
        String sql = """
                UPDATE notifications
                SET is_read = true
                WHERE user_id = ?
                """;

        return jdbcTemplate.update(sql, userId);
    }

    @Override
    public int deleteById(Long notificationId) {
        String sql = """
                DELETE FROM notifications
                WHERE id = ?
                """;

        return jdbcTemplate.update(sql, notificationId);
    }
}
