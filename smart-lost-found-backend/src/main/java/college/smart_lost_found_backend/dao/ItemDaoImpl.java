package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.enumconstant.ItemStatus;
import college.smart_lost_found_backend.enumconstant.ItemType;
import college.smart_lost_found_backend.model.Item;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
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

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {

            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, item.getUserId());
            ps.setLong(2, item.getCategoryId());
            ps.setLong(3, item.getLocationId());
            ps.setString(4, item.getTitle());
            ps.setString(5, item.getDescription());
            ps.setString(6, item.getItemType().name());
            ps.setString(7, item.getStatus().name());
            ps.setObject(8, item.getDateLostOrFound());

            return ps;

        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public int updateStatusUsingProcedure(Long itemId, ItemStatus status) {
        String sql = "CALL update_item_status(?, ?)";

        return jdbcTemplate.update(
                sql,
                itemId,
                status.name()
        );    }

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
    public List<Item> findByItemTypeAndUserId(String itemType, Long userId) {
        String sql = "SELECT * FROM items WHERE item_type = ? and user_id=? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, itemRowMapper, itemType, userId);
    }

    @Override
    public List<Item> searchItems(String itemName, Long locationId, String itemType) {
        String sql = """
                    SELECT
                        i.item_id,
                        i.title,
                        i.user_id,
                        u.username,
                        im.id,
                        i.description,
                        i.item_type,
                        i.status,
                        i.date_lost_or_found,
                        l.location_name
                    FROM items i
                    INNER JOIN locations l ON l.location_id = i.location_id
                    INNER JOIN item_images im on im.item_id = i.item_id
                    INNER JOIN users u ON u.user_id = i.user_id
                    WHERE (? IS NULL OR i.title LIKE ?)
                    AND (? IS NULL OR l.location_id = ?)
                    AND (? IS NULL OR i.item_type = ?)
                """;
        String likeSearchName=null;
        if(Objects.nonNull(itemName)){
            likeSearchName = itemName + "%";
        }
        return jdbcTemplate.query(
                sql,
                new Object[]{
                        likeSearchName,
                        likeSearchName,
                        locationId,
                        locationId,
                        itemType,
                        itemType
                },
                new int[] {
                        Types.VARCHAR,
                        Types.VARCHAR,
                        Types.BIGINT,
                        Types.BIGINT,
                        Types.VARCHAR,
                        Types.VARCHAR
                },
                (rs, rowNum) -> {
                    Item item = new Item();

                    item.setItemId(rs.getLong("item_id"));
                    item.setTitle(rs.getString("title"));
                    item.setUserId(rs.getLong("user_id"));
                    item.setUserName(rs.getString("username"));
                    item.setImageId(rs.getLong("id"));
                    item.setDescription(rs.getString("description"));
                    item.setItemType(Enum.valueOf(ItemType.class, rs.getString("item_type")));
                    item.setStatus(Enum.valueOf(ItemStatus.class, rs.getString("status")));
                    if(Objects.nonNull(rs.getString("date_lost_or_found"))){
                        item.setDateLostOrFound(
                                rs.getDate("date_lost_or_found").toLocalDate()
                        );
                    }
                    item.setLocationName(rs.getString("location_name"));
                    return item;
                }
        );
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
    public int updateStatus(Long itemId, ItemStatus status) {
        String sql = "UPDATE items SET status = ? WHERE item_id = ?";

        return jdbcTemplate.update(sql, status.toString(), itemId);
    }

    @Override
    public int deleteById(Long itemId) {
        String sql = "DELETE FROM items WHERE item_id = ?";

        return jdbcTemplate.update(sql, itemId);
    }
}
