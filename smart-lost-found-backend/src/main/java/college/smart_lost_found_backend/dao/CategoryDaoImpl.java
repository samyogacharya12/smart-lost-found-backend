package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.model.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@Slf4j
public class CategoryDaoImpl implements CategoryDao {

    private final JdbcTemplate jdbcTemplate;


    public CategoryDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Category> categoryMapper = (ResultSet rs, int rowNum) -> {
        Category category = new Category();
        category.setCategoryId(rs.getLong("category_id"));
        category.setCategoryName(rs.getString("category_name"));
        category.setDescription(rs.getString("description"));
        category.setCreatedDate(rs.getTimestamp("created_at").toLocalDateTime());
        return category;
    };

    @Override
    public int save(Category category) {
        log.info("save category");
        String sql = "INSERT INTO CATEGORIES (category_name,description,created_at) VALUES (?,?,?)";
        return jdbcTemplate.update(sql,
                category.getCategoryName(),
                category.getDescription(),
                category.getCreatedDate());
    }

    @Override
    public Optional<Category> findById(Long categoryId) {
        log.info("findById categoryId");
        String sql = "SELECT * FROM CATEGORIES WHERE category_id=?";
        List<Category> categories = jdbcTemplate
                .query(sql, new Object[]{categoryId}, categoryMapper);
        return categories.stream().findFirst();
    }

    @Override
    public List<Category> findAll() {
        log.info("findAll for category");
        String sql = "SELECT * FROM CATEGORIES";
        return jdbcTemplate.query(sql, categoryMapper);
    }

    @Override
    public int update(Category category) {
        log.info("update category");
        String sql = """
                UPDATE CATEGORIES
                SET category_name=?, description=?, created_at=?
                WHERE category_id=?    \s
               \s""";
        return jdbcTemplate.update(sql, category.getCategoryName(),
                category.getDescription(),
                category.getCreatedDate(),
                category.getCategoryId());
    }
}
