package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.enumconstant.ImageType;
import college.smart_lost_found_backend.model.SystemImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class SystemDaoImpl implements SystemDao {


    private final JdbcTemplate jdbcTemplate;


    public SystemDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(SystemImage systemImage) {
        String sql = """
                    INSERT INTO system_images
                    (
                        image_name,
                        image_path,
                        image_type
                    )
                    VALUES
                    (
                        ?, ?, ?
                    )
                """;

        return jdbcTemplate.update(
                sql,
                systemImage.getImageName(),
                systemImage.getImagePath(),
                systemImage.getImageType().name()
        );
    }

    @Override
    public List<SystemImage> findAll() {
        String sql = """
                    SELECT
                        image_id,
                        image_name,
                        image_path,
                        image_type
                    FROM system_images
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {

                    SystemImage image = new SystemImage();

                    image.setImageId(
                            rs.getLong("image_id")
                    );

                    image.setImageName(
                            rs.getString("image_name")
                    );

                    image.setImagePath(
                            rs.getString("image_path")
                    );

                    image.setImageType(
                            Enum.valueOf(
                                    ImageType.class,
                                    rs.getString("image_type")
                            )
                    );

                    return image;
                }
        );
    }

    @Override
    public SystemImage findById(Long id) {
        String sql = """
                    SELECT
                        image_id,
                        image_name,
                        image_path,
                        image_type
                    FROM system_images
                    WHERE image_id = ?
                """;

        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{
                        id
                },
                (rs, rowNum) -> {

                    SystemImage image = new SystemImage();

                    image.setImageId(
                            rs.getLong("image_id")
                    );

                    image.setImageName(
                            rs.getString("image_name")
                    );

                    image.setImagePath(
                            rs.getString("image_path")
                    );

                    image.setImageType(
                            Enum.valueOf(
                                    ImageType.class,
                                    rs.getString("image_type")
                            )
                    );

                    return image;
                }
        );
    }

    @Override
    public Optional<SystemImage> findByImageName(String imageName) {
        String sql = """
                    SELECT
                        image_id,
                        image_name,
                        image_path,
                        image_type
                    FROM system_images
                    WHERE image_name = ?
                """;

        List<SystemImage> images = jdbcTemplate.query(
                sql,
                new Object[]{imageName},
                (rs, rowNum) -> {

                    SystemImage image = new SystemImage();

                    image.setImageId(
                            rs.getLong("image_id")
                    );

                    image.setImageName(
                            rs.getString("image_name")
                    );

                    image.setImagePath(
                            rs.getString("image_path")
                    );

                    image.setImageType(
                            Enum.valueOf(
                                    ImageType.class,
                                    rs.getString("image_type")
                            )
                    );

                    return image;
                }
        );

        return images.stream().findFirst();
    }

    @Override
    public List<SystemImage> findByImageType(ImageType imageType) {
        String sql = """
                    SELECT
                        image_id,
                        image_name,
                        image_path,
                        image_type
                    FROM system_images
                    WHERE image_type = ?
                """;

        return jdbcTemplate.query(
                sql,
                new Object[]{
                        imageType.name()
                },
                (rs, rowNum) -> {

                    SystemImage image = new SystemImage();

                    image.setImageId(
                            rs.getLong("image_id")
                    );

                    image.setImageName(
                            rs.getString("image_name")
                    );

                    image.setImagePath(
                            rs.getString("image_path")
                    );

                    image.setImageType(
                            Enum.valueOf(
                                    ImageType.class,
                                    rs.getString("image_type")
                            )
                    );

                    return image;
                }
        );
    }

    @Override
    public int delete(Long imageId) {
        String sql = """
                    DELETE FROM system_images
                    WHERE image_id = ?
                """;

        return jdbcTemplate.update(
                sql,
                imageId
        );
    }
}
