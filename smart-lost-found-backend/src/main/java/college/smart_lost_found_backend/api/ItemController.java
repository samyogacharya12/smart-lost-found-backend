package college.smart_lost_found_backend.api;

import college.smart_lost_found_backend.dto.ItemDto;
import college.smart_lost_found_backend.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/items")
public class ItemController {


    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemDto> save(@RequestBody ItemDto itemDto) {
        log.info("Saving item");
        return ResponseEntity.ok(itemService.save(itemDto));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> findById(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.findById(itemId));
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> findAll() {
        return ResponseEntity.ok(itemService.findAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ItemDto>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(itemService.findByUserId(userId));
    }

    @GetMapping("/type/{itemType}")
    public ResponseEntity<List<ItemDto>> findByItemType(@PathVariable String itemType) {
        return ResponseEntity.ok(itemService.findByItemType(itemType));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto
    ) {
        return ResponseEntity.ok(itemService.update(itemId, itemDto));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{itemId}/status")
    public ResponseEntity<ItemDto> updateStatus(
            @PathVariable Long itemId,
            @RequestParam String status
    ) {
        ItemDto itemDto = itemService.updateStatus(itemId, status);
        return ResponseEntity.ok(itemDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteById(@PathVariable Long itemId) {
        itemService.deleteById(itemId);
        return ResponseEntity.ok("Item deleted successfully");
    }


}
