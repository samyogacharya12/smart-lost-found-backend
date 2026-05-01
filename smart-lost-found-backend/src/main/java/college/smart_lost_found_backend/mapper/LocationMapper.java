package college.smart_lost_found_backend.mapper;

import college.smart_lost_found_backend.dto.LocationDto;
import college.smart_lost_found_backend.model.Location;
import org.springframework.stereotype.Service;

@Service
public class LocationMapper {

    public static LocationDto toDto(Location location) {
        if (location == null) {
            return null;
        }

        return new LocationDto(
                location.getLocationId(),
                location.getLocationName(),
                location.getLocationDescription(),
                location.getCreatedAt()
        );
    }


    public static Location toEntity(LocationDto locationDto) {
        if (locationDto == null) {
            return null;
        }

        return new Location(
                locationDto.getLocationId(),
                locationDto.getLocationName(),
                locationDto.getLocationDescription(),
                locationDto.getCreatedAt()
        );
    }
}
