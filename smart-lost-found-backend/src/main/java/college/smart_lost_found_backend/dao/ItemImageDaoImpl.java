package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.model.ItemImage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
        image.setPath(rs.getString("file_path"));

        if (rs.getTimestamp("uploaded_at") != null) {
            image.setUploadedAt(rs.getTimestamp("uploaded_at").toLocalDateTime());
        }

        return image;
    };

    @Override
    public int save(Long itemId, String imageUrl) {
        String sql = """
                INSERT INTO item_images (item_id, file_path)
                VALUES (?, ?)
                """;

        return jdbcTemplate.update(sql, itemId, imageUrl);
    }

    @Override
    public Optional<ItemImage> getImageByIdAndItemId(Long imageId, Long itemId) {
        String sql = """
                SELECT *
                FROM item_images
                WHERE id=? and item_id = ?
                """;
        List<ItemImage> itemImages= jdbcTemplate.query(sql, rowMapper,imageId ,itemId);
        return itemImages.stream().findFirst();
    }

    @Override
    public List<ItemImage> findByItemId(Long itemId) {
        String sql = """
                SELECT id, item_id, file_path, uploaded_at
                FROM item_images
                WHERE item_id = ?
                ORDER BY uploaded_at DESC
                """;
        return jdbcTemplate.query(sql, rowMapper, itemId);
    }

    @Override
    public int deleteById(Long imageId) {
        String sql = "DELETE FROM item_images WHERE id = ?";
        return jdbcTemplate.update(sql, imageId);
    }
}
