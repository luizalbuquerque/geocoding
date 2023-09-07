package apigeomapservices.service.impl;

import apigeomapservices.entity.GeocodingEntity;
import apigeomapservices.exceptions.GeocodingException;
import apigeomapservices.repository.GeocodingQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

class GeocodingServiceImplTest {

    @Mock
    private GeocodingQueryRepository geocodingQueryRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeocodingServiceImpl geocodingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(geocodingService, "photonApiUrl", "https://photon.komoot.io/reverse");
    }


    @Test
    void testGeocode() throws GeocodingException {
        double latitude = -28.486615;
        double longitude = -49.012439;
        String jsonResponse = "{\n" +
                "    \"features\": [\n" +
                "        {\n" +
                "            \"geometry\": {\n" +
                "                \"coordinates\": [\n" +
                "                    -49.0121012,\n" +
                "                    -28.4868724\n" +
                "                ],\n" +
                "                \"type\": \"Point\"\n" +
                "            },\n" +
                "            \"type\": \"Feature\",\n" +
                "            \"properties\": {\n" +
                "                \"osm_id\": 246008112,\n" +
                "                \"extent\": [\n" +
                "                    -49.0121012,\n" +
                "                    -28.4868478,\n" +
                "                    -49.0119144,\n" +
                "                    -28.4868724\n" +
                "                ],\n" +
                "                \"country\": \"Brasil\",\n" +
                "                \"city\": \"Tubarão\",\n" +
                "                \"countrycode\": \"BR\",\n" +
                "                \"postcode\": \"88701-611\",\n" +
                "                \"county\": \"Região Geográfica Intermediária de Criciúma\",\n" +
                "                \"type\": \"street\",\n" +
                "                \"osm_type\": \"W\",\n" +
                "                \"osm_key\": \"highway\",\n" +
                "                \"district\": \"Centro\",\n" +
                "                \"osm_value\": \"tertiary\",\n" +
                "                \"name\": \"Rua Altamiro Guimarães\",\n" +
                "                \"state\": \"Santa Catarina\"\n" +
                "            }\n" +
                "        }\n" +
                "    ],\n" +
                "    \"type\": \"FeatureCollection\"\n" +
                "}";

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);

        String result = geocodingService.geocode(latitude, longitude);

        // Validações. Por exemplo:
        assertNotNull(result);
        assertEquals("Rua Altamiro Guimarães, Tubarão, Santa Catarina, Brasil", result);

        // Verifique se o método de salvamento foi chamado
        verify(geocodingQueryRepository, times(1)).save(any(GeocodingEntity.class));
    }

    @Test
    void testGetGeocodingHistory() {
        // Simulando resposta do repositório
        GeocodingEntity mockEntity = new GeocodingEntity();
        mockEntity.setId(1L);
        mockEntity.setLatitude(-28.486615);
        mockEntity.setLongitude(-49.012439);
        mockEntity.setFullAddress("Rua Altamiro Guimarães, Tubarão, Santa Catarina, Brasil");
        mockEntity.setTimestamp(LocalDateTime.parse("2023-09-07T17:41:13.013212"));

        List<GeocodingEntity> mockResponse = Collections.singletonList(mockEntity);

        when(geocodingQueryRepository.findAll()).thenReturn(mockResponse);

        List<GeocodingEntity> response = geocodingService.getGeocodingHistory();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals(mockEntity, response.get(0));
    }


}
