package college.smart_lost_found_backend.service;

import college.smart_lost_found_backend.dto.LocationDto;

import java.util.List;

public interface LocationService {

    LocationDto save(LocationDto locationDto);

    LocationDto findById(Long locationId);

    List<LocationDto> findAll();

    LocationDto update(LocationDto locationDto);

    void deleteById(Long locationId);
}
