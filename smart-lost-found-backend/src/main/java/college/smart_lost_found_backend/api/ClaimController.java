package college.smart_lost_found_backend.api;

import college.smart_lost_found_backend.dto.ClaimDto;
import college.smart_lost_found_backend.dto.RestResponse;
import college.smart_lost_found_backend.service.ClaimService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
@Slf4j
public class ClaimController {

    private final ClaimService claimService;


    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }



    @PostMapping
    public ResponseEntity<ClaimDto> save(@RequestBody ClaimDto claimDto) {
        log.info("Creating claim");
        return ResponseEntity.ok(claimService.save(claimDto));
    }

    @GetMapping("/{claimId}")
    public ResponseEntity<ClaimDto> findById(@PathVariable Long claimId) {
        return ResponseEntity.ok(claimService.findById(claimId));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ClaimDto>> findAll() {
        return ResponseEntity.ok(claimService.findAll());
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<ClaimDto>> findByItemId(@PathVariable Long itemId) {
        return ResponseEntity.ok(claimService.findByItemId(itemId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ClaimDto>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(claimService.findByUserId(userId));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{claimId}/approve")
    public ResponseEntity<RestResponse> approveClaim(@PathVariable Long claimId) {
         claimService.approveClaim(claimId);
        RestResponse restResponse=new RestResponse();
        restResponse.setMessage("Claim approved and item marked as RETURNED");
        return ResponseEntity.ok(restResponse);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{claimId}/reject")
    public ResponseEntity<RestResponse> rejectClaim(@PathVariable Long claimId) {
        claimService.rejectClaim(claimId);
        RestResponse restResponse=new RestResponse();
        restResponse.setMessage("Claim rejected and item marked as OPEN");
        return ResponseEntity.ok(restResponse);
    }


}
