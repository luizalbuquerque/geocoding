package apigeomapservices.controller;

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

    public GeocodingQueryController(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @PostMapping("/query")
    public ResponseEntity<String> geocodeLocation(@RequestParam double latitude, @RequestParam double longitude) {
        try {
            String fullAddress = geocodingService.geocode(latitude, longitude);
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
