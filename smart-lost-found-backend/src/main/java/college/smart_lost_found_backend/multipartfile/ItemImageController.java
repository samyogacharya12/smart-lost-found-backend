package college.smart_lost_found_backend.multipartfile;

import college.smart_lost_found_backend.dto.ClaimDto;
import college.smart_lost_found_backend.dto.ItemDto;
import college.smart_lost_found_backend.dto.ItemImageDto;
import college.smart_lost_found_backend.service.ItemImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/item")
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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemDto> saveItemWithImage(
             ItemDto itemDto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return ResponseEntity.ok(itemImageService.saveItemWithImage(itemDto, file));
    }

    @PostMapping(value = "/claim/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClaimDto> saveClaimWithImage(
            ClaimDto claimDto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return ResponseEntity.ok(itemImageService.saveClaimWithImage(claimDto, file));
    }


    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable Long imageId) {
        itemImageService.deleteImage(imageId);
        return ResponseEntity.ok("Image deleted successfully");
    }


    @GetMapping("/images/download/{itemId}/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long  imageId, @PathVariable Long  itemId) {
        try {
            byte[] imageBytes = itemImageService.downloadImage(imageId, itemId);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .body(imageBytes);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load image: " + e.getMessage());
        }
    }

    @GetMapping("/claim/download/{imageId}/{claimId}")
    public ResponseEntity<byte[]> geClaimtImage(@PathVariable Long  imageId, @PathVariable Long  claimId) {
        try {
            byte[] imageBytes = itemImageService.downloadImageByClaim(claimId,imageId);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .body(imageBytes);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load image: " + e.getMessage());
        }
    }


}
