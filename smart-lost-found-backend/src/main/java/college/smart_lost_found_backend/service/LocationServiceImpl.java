package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dao.LacationDao;
import college.smart_lost_found_backend.dto.LocationDto;
import college.smart_lost_found_backend.mapper.LocationMapper;
import college.smart_lost_found_backend.model.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService {

    private final LacationDao locationDao;

    private final LocationMapper locationMapper;

    public LocationServiceImpl(LacationDao locationDao, LocationMapper locationMapper){
     this.locationDao=locationDao;
     this.locationMapper=locationMapper;
    }


    @Override
    public LocationDto save(LocationDto locationDto) {
        log.info("Saving location");

        Location location = locationMapper.toEntity(locationDto);
        locationDao.save(location);

        return LocationMapper.toDto(location);
    }

    @Override
    public LocationDto findById(Long locationId) {
        log.info("Finding location by id: {}", locationId);

        return locationDao.findById(locationId)
                .map(LocationMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Location not found"));
    }

    @Override
    public List<LocationDto> findAll() {
        log.info("Fetching all locations");

        return locationDao.findAll()
                .stream()
                .map(LocationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public LocationDto update(LocationDto locationDto) {
        log.info("Updating location id: {}", locationDto.getLocationId());

        Location location = LocationMapper.toEntity(locationDto);
        locationDao.update(location);

        return LocationMapper.toDto(location);
    }

    @Override
    public void deleteById(Long locationId) {
        log.info("Deleting location id: {}", locationId);

        locationDao.deleteById(locationId);
    }
}
