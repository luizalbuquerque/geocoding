package apigeomapservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeocodingDTO {

    private Double latitude;
    private Double longitude;
    private String fullAddress;
    private LocalDateTime timestamp;
    private String apiResponseJson;
    private String title;
    private String description;
    private String status;
}
