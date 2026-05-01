package college.smart_lost_found_backend.api;

import college.smart_lost_found_backend.dto.LocationDto;
import college.smart_lost_found_backend.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/location")
@Slf4j
public class LocationController {

    private final LocationService locationService;


    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<LocationDto> save(@RequestBody LocationDto locationDto) {
        log.info("Saving location");
        return new ResponseEntity<>(locationService.save(locationDto), HttpStatus.CREATED);
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> findById(@PathVariable Long locationId) {
        log.info("Finding location by id: {}", locationId);
        return ResponseEntity.ok(locationService.findById(locationId));
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> findAll() {
        log.info("Fetching all locations");
        return ResponseEntity.ok(locationService.findAll());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> update(
            @PathVariable Long locationId,
            @RequestBody LocationDto locationDto
    ) {
        log.info("Updating location id: {}", locationId);
        locationDto.setLocationId(locationId);
        return ResponseEntity.ok(locationService.update(locationDto));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{locationId}")
    public ResponseEntity<String> deleteById(@PathVariable Long locationId) {
        log.info("Deleting location id: {}", locationId);
        locationService.deleteById(locationId);
        return ResponseEntity.ok("Location deleted successfully");
    }

}
