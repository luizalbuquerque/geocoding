package apigeomapservices.service.impl;

import apigeomapservices.entity.GeocodingEntity;
import apigeomapservices.exceptions.GeocodingException;
import apigeomapservices.repository.GeocodingQueryRepository;
import apigeomapservices.service.GeocodingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GeocodingServiceImpl implements GeocodingService {

    private static final Logger logger = LoggerFactory.getLogger(GeocodingServiceImpl.class);

    private GeocodingQueryRepository geocodingQueryRepository;
    private RestTemplate restTemplate;

    public GeocodingServiceImpl(GeocodingQueryRepository geocodingQueryRepository, RestTemplate restTemplate) {
        this.geocodingQueryRepository = geocodingQueryRepository;
        this.restTemplate = restTemplate;
    }

    @Value("${photon.api.url}")
    private String photonApiUrl;

    @Override
    public String geocode(double latitude ,double longitude) throws GeocodingException {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(photonApiUrl)
                    .queryParam("lat", latitude)
                    .queryParam("lon", longitude);

            String url = builder.toUriString();
            System.out.println(url);  // ou logger.info(url);


            String response = restTemplate.getForObject(builder.toUriString(), String.class);

            String fullAddress = extractFullAddressFromResponse(response);

            GeocodingEntity query = new GeocodingEntity();
            query.setLatitude(latitude);
            query.setLongitude(longitude);
            query.setFullAddress(fullAddress);
            query.setTimestamp(LocalDateTime.now());

            geocodingQueryRepository.save(query);

            return fullAddress;
        } catch (Exception e) {
            logger.error("Erro na geocodificação", e);
            throw new GeocodingException("Erro ao consultar a API de Geocodificação", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public List<GeocodingEntity> getGeocodingHistory() {
        return geocodingQueryRepository.findAll();
    }

    public String extractFullAddressFromResponse(String jsonResponse) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        if (rootNode.has("features")) {
            JsonNode featuresNode = rootNode.get("features");
            if (featuresNode.isArray() && featuresNode.size() > 0) {
                JsonNode firstFeatureNode = featuresNode.get(0);
                JsonNode propertiesNode = firstFeatureNode.get("properties");
                if (propertiesNode.has("name")) {
                    return propertiesNode.get("name").asText() + ", " +
                            propertiesNode.get("city").asText() + ", " +
                            propertiesNode.get("state").asText() + ", " +
                            propertiesNode.get("country").asText();
                }
            }
        }
        throw new Exception("Endereço não encontrado na resposta da API.");
    }
}
