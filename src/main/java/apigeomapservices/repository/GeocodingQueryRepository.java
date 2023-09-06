package apigeomapservices.repository;

import apigeomapservices.entity.GeocodingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeocodingQueryRepository extends JpaRepository<GeocodingEntity, Long> {
}

