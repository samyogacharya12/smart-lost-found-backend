package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.enumconstant.ItemStatus;
import college.smart_lost_found_backend.enumconstant.ItemType;
import college.smart_lost_found_backend.model.Item;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ItemDaoImpl implements ItemDao {

    private final JdbcTemplate jdbcTemplate;


    public ItemDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private final RowMapper<Item> itemRowMapper = (rs, rowNum) -> {
        Item item = new Item();

        item.setItemId(rs.getLong("item_id"));
        item.setUserId(rs.getLong("user_id"));
        item.setCategoryId(rs.getLong("category_id"));
        item.setLocationId(rs.getObject("location_id", Long.class));
        item.setTitle(rs.getString("title"));
        item.setDescription(rs.getString("description"));
        item.setItemType(Enum.valueOf(ItemType.class,
                rs.getString("item_type")));
        item.setStatus(Enum.valueOf(ItemStatus.class, rs.getString("status")));

        if (rs.getDate("date_lost_or_found") != null) {
            item.setDateLostOrFound(rs.getDate("date_lost_or_found").toLocalDate());
        }

        if (rs.getTimestamp("created_at") != null) {
            item.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        if (rs.getTimestamp("updated_at") != null) {
            item.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }

        return item;
    };

    @Override
    public int save(Item item) {
        String sql = """
                INSERT INTO items
                (user_id, category_id, location_id, title, description, item_type, status, date_lost_or_found)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        return jdbcTemplate.update(sql, item.getUserId(), item.getCategoryId(), item.getLocationId(), item.getTitle(), item.getDescription(), item.getItemType().name(), item.getStatus().name(), item.getDateLostOrFound());
    }

    @Override
    public Optional<Item> findById(Long itemId) {
        String sql = "SELECT * FROM items WHERE item_id = ?";

        List<Item> items = jdbcTemplate.query(sql, itemRowMapper, itemId);

        return items.stream().findFirst();
    }

    @Override
    public List<Item> findAll() {
        String sql = "SELECT * FROM items ORDER BY created_at DESC";

        return jdbcTemplate.query(sql, itemRowMapper);
    }

    @Override
    public List<Item> findByUserId(Long userId) {
        String sql = "SELECT * FROM items WHERE user_id = ? ORDER BY created_at DESC";

        return jdbcTemplate.query(sql, itemRowMapper, userId);
    }

    @Override
    public List<Item> findByItemType(String itemType) {
        String sql = "SELECT * FROM items WHERE item_type = ? ORDER BY created_at DESC";

        return jdbcTemplate.query(sql, itemRowMapper, itemType);
    }

    @Override
    public int update(Item item) {
        String sql = """
                UPDATE items
                SET category_id = ?, location_id = ?, title = ?, description = ?,
                    item_type = ?, status = ?, date_lost_or_found = ?
                WHERE item_id = ?
                """;

        return jdbcTemplate.update(sql, item.getCategoryId(), item.getLocationId(), item.getTitle(), item.getDescription(), item.getItemType().name(), item.getStatus().name(), item.getDateLostOrFound(), item.getItemId());
    }

    @Override
    public int updateStatus(Long itemId, String status) {
        String sql = "UPDATE items SET status = ? WHERE item_id = ?";

        return jdbcTemplate.update(sql, status, itemId);
    }

    @Override
    public int deleteById(Long itemId) {
        String sql = "DELETE FROM items WHERE item_id = ?";

        return jdbcTemplate.update(sql, itemId);
    }
}
