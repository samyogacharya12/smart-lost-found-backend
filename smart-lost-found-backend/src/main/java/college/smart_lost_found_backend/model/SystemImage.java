package college.smart_lost_found_backend.model;

import college.smart_lost_found_backend.enumconstant.ImageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemImage {

    private Long imageId;

    private String imageName;


    private String imagePath;

    private ImageType imageType;
}
