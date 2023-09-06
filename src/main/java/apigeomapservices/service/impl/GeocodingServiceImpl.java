package apigeomapservices.service.impl;

import apigeomapservices.config.GoogleConstants;
import apigeomapservices.dto.GeocodingDTO;
import apigeomapservices.entity.GeocodingEntity;
import apigeomapservices.exceptions.GeocodingException;
import apigeomapservices.repository.GeocodingQueryRepository;
import apigeomapservices.service.GeocodingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GeocodingServiceImpl implements GeocodingService {

    private static final Logger logger = LoggerFactory.getLogger(GeocodingServiceImpl.class);

    private GeocodingQueryRepository geocodingQueryRepository;

    private RestTemplate restTemplate;

    @Value("${google.api.key}")
    private String googleApiKey;

    @Override
    public String geocode(GeocodingDTO geocodingDTO) throws GeocodingException {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GoogleConstants.GOOGLE_GEOCODING_API_URL)
                    .queryParam("latlng", geocodingDTO.getLatitude() + "," + geocodingDTO.getLongitude())
                    .queryParam("key", googleApiKey);

            String response = restTemplate.getForObject(builder.toUriString(), String.class);

            // Suponhamos que você tenha uma função para extrair o endereço completo da resposta
            String fullAddress = extractFullAddressFromResponse(response);

            // Agora armazene esse endereço na entidade antes de salvar no banco de dados
            GeocodingEntity query = new GeocodingEntity();
            query.setLatitude(geocodingDTO.getLatitude());
            query.setLongitude(geocodingDTO.getLongitude());
            query.setFullAddress(fullAddress);  // novo campo para armazenar o endereço
            query.setTimestamp(LocalDateTime.now());

            geocodingQueryRepository.save(query);

            return fullAddress; // ou 'response' dependendo do que você precisa
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

        if (rootNode.has("status") && rootNode.get("status").asText().equals("OK")) {
            JsonNode resultsNode = rootNode.get("results");
            if (resultsNode.isArray() && resultsNode.size() > 0) {
                JsonNode firstResultNode = resultsNode.get(0);
                if (firstResultNode.has("formatted_address")) {
                    return firstResultNode.get("formatted_address").asText();
                }
            }
        }
        throw new Exception("Endereço não encontrado na resposta da API.");
    }
}
