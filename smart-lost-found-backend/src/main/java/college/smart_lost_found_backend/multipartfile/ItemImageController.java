package college.smart_lost_found_backend.multipartfile;

import college.smart_lost_found_backend.dto.ItemImageDto;
import college.smart_lost_found_backend.service.ItemImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemImageController {


    private final ItemImageService itemImageService;


    public ItemImageController(ItemImageService itemImageService) {
        this.itemImageService = itemImageService;
    }

    @PostMapping("/{itemId}/images")
    public ResponseEntity<ItemImageDto> uploadImage(
            @PathVariable Long itemId,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(itemImageService.uploadImage(itemId, file));
    }

    @GetMapping("/{itemId}/images")
    public ResponseEntity<List<ItemImageDto>> getImagesByItemId(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemImageService.getImagesByItemId(itemId));
    }

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable Long imageId) {
        itemImageService.deleteImage(imageId);
        return ResponseEntity.ok("Image deleted successfully");
    }


    @GetMapping("/images/download")
    public ResponseEntity<byte[]> getImage(@RequestParam String path) {
        try {
            Path filePath = Paths.get(path);

            byte[] imageBytes = Files.readAllBytes(filePath);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(imageBytes);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load image: " + e.getMessage());
        }
    }


}
