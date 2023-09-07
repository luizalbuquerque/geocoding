package apigeomapservices.service;

import apigeomapservices.dto.GeocodingDTO;
import apigeomapservices.entity.GeocodingEntity;
import apigeomapservices.exceptions.GeocodingException;

import java.util.List;

public interface GeocodingService {
    String geocode(double latitude, double longitude) throws GeocodingException;
    List<GeocodingEntity> getGeocodingHistory();
}