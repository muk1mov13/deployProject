
package com.example.backend.repository;

import com.example.backend.entity.CustomerCategory;
import com.example.backend.entity.Territory;
import com.example.backend.projection.ClientProjection;
import com.example.backend.projection.TerritoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

import com.example.backend.entity.Territory;
import com.example.backend.projection.TerritoryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TerritoryRepository extends JpaRepository<Territory, UUID>, JpaSpecificationExecutor<Territory> {

    Page<Territory> findAllByNameContainsIgnoreCaseOrRegionContainsIgnoreCaseOrderByCreatedAtDesc(String name, String region, Pageable pageable);

    @Query(value = """
select t.id as value, t.name as label from Territory  t order by t.name
""")
    List<TerritoryProjection> findAllTerritoriesForClient();

    @Query(value = "SELECT * FROM territory ", nativeQuery = true)
    Page<Territory> findAllTerritoriesForBot(Pageable pageable);

    @Query(value = "SELECT * FROM territory " +
            "WHERE (:active is null or active = :active) " +
            "AND (lower(name) LIKE lower(concat('%', :search, '%')) " +
            "OR lower(region) LIKE lower(concat('%', :search, '%'))) ORDER BY created_at DESC",
            nativeQuery = true)
    Page<Territory> findAllByActiveAndNameContainsIgnoreCaseOrRegionContainsIgnoreCase(
            @Param("active") Boolean active,
            @Param("search") String search,
            Pageable pageable
    );

    @Query(value = "select * from territory where name=:name", nativeQuery = true)
    Optional<Territory> findByName(String name);
}

