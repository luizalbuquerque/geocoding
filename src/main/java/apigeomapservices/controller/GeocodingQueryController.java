package apigeomapservices.controller;

import apigeomapservices.dto.GeocodingDTO;
import apigeomapservices.entity.GeocodingEntity;
import apigeomapservices.exceptions.GeocodingException;
import apigeomapservices.service.GeocodingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geocoding")
public class GeocodingQueryController {

    private GeocodingService geocodingService;

    @PostMapping("/query")
    public ResponseEntity<String> geocodeLocation(GeocodingDTO geocodingDTO) {
        try {
            String fullAddress = geocodingService.geocode(geocodingDTO);
            return ResponseEntity.ok(fullAddress);
        } catch (GeocodingException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<GeocodingEntity>> getGeocodingHistory() {
        List<GeocodingEntity> history = geocodingService.getGeocodingHistory();
        return ResponseEntity.ok(history);
    }
}
