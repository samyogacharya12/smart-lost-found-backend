package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.model.Location;

import java.util.List;
import java.util.Optional;

public interface LacationDao {

    int save(Location location);

    Optional<Location> findById(Long locationId);

    List<Location> findAll();

    int update(Location location);

    int deleteById(Long locationId);

}
