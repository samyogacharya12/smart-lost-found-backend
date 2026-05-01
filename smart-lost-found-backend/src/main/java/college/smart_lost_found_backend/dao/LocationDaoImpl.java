package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.model.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@Slf4j
public class LocationDaoImpl implements LacationDao{

    private final JdbcTemplate jdbcTemplate;


    public LocationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Location> locationRowMapper = (rs, rowNum) -> {
        Location location = new Location();
        location.setLocationId(rs.getLong("location_id"));
        location.setLocationName(rs.getString("location_name"));
        location.setLocationDescription(rs.getString("description"));

        if (rs.getTimestamp("created_at") != null) {
            location.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        return location;
    };

    @Override
    public int save(Location location) {
        String sql = """
                INSERT INTO locations (location_name, description)
                VALUES (?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                location.getLocationName(),
                location.getLocationDescription()
        );
    }

    @Override
    public Optional<Location> findById(Long locationId) {
        String sql = """
                SELECT location_id, location_name, description, created_at
                FROM locations
                WHERE location_id = ?
                """;

        List<Location> locations = jdbcTemplate.query(sql, locationRowMapper, locationId);

        return locations.stream().findFirst();
    }

    @Override
    public List<Location> findAll() {
        String sql = """
                SELECT location_id, location_name, description, created_at
                FROM locations
                ORDER BY created_at DESC
                """;

        return jdbcTemplate.query(sql, locationRowMapper);
    }

    @Override
    public int update(Location location) {
        String sql = """
                UPDATE locations
                SET location_name = ?, description = ?
                WHERE location_id = ?
                """;

        return jdbcTemplate.update(
                sql,
                location.getLocationName(),
                location.getLocationDescription(),
                location.getLocationId()
        );
    }

    @Override
    public int deleteById(Long locationId) {
        String sql = """
                DELETE FROM locations
                WHERE location_id = ?
                """;

        return jdbcTemplate.update(sql, locationId);
    }
}
