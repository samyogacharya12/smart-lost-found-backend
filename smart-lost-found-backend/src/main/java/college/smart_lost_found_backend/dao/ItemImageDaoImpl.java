package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.model.ItemImage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemImageDaoImpl implements ItemImageDao {


    private final JdbcTemplate jdbcTemplate;


    public ItemImageDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ItemImage> rowMapper = (rs, rowNum) -> {
        ItemImage image = new ItemImage();
        image.setId(rs.getLong("id"));
        image.setItemId(rs.getLong("item_id"));
        image.setImageUrl(rs.getString("image_url"));

        if (rs.getTimestamp("uploaded_at") != null) {
            image.setUploadedAt(rs.getTimestamp("uploaded_at").toLocalDateTime());
        }

        return image;
    };

    @Override
    public int save(Long itemId, String imageUrl) {
        String sql = """
                INSERT INTO item_images (item_id, image_url)
                VALUES (?, ?)
                """;

        return jdbcTemplate.update(sql, itemId, imageUrl);
    }

    @Override
    public List<ItemImage> findByItemId(Long itemId) {
        String sql = """
                SELECT id, item_id, image_url, uploaded_at
                FROM item_images
                WHERE item_id = ?
                ORDER BY uploaded_at DESC
                """;

        return jdbcTemplate.query(sql, rowMapper, itemId);
    }

    @Override
    public int deleteById(Long imageId) {
        String sql = "DELETE FROM item_images WHERE image_id = ?";
        return jdbcTemplate.update(sql, imageId);
    }
}
