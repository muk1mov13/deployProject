package com.example.backend.repository;
import com.example.backend.entity.Client;
import com.example.backend.projection.ClientProjection;
import com.example.backend.projection.ClientsForMapProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    @Query(value = """
       SELECT c.id, c.active, c.latitude, c.longitude,  TO_CHAR(c.registration_date, 'yyyy-mm-dd') as registration_date, c.address, c.company_name, c.name, c.phone, c.tin, c.visiting_days as visiting_days, c.reference_point as reference_point, t.name as territory,t.id as territory_id, cc.name as category,cc.id as category_id
       FROM client c
                JOIN territory t ON t.id = c.territory_id
                JOIN customer_category cc ON cc.id = c.category_id
       WHERE
        (:active IS NULL OR c.active = :active )
        AND
         (:cities IS NULL OR t.id IN :cities)
         AND
          (:categories IS NULL OR cc.id IN :categories)
         AND
           (:tinStatus IS NULL OR (c.tin IS NOT NULL  AND :tinStatus = TRUE ) OR (c.tin IS NULL  AND :tinStatus = FALSE))
         AND
         (lower(CONCAT(c.name,' ',CAST(c.registration_date as varchar),' ',c.address,c.company_name,c.phone,c.tin)) LIKE '%'||lower(:search)||'%')
         ORDER BY c.registration_date DESC ;
""",nativeQuery = true)
    Page<ClientProjection> findClientsByFilter(
            @Param("active") Boolean active,
            @Param("cities") List<UUID> cities,
            @Param("tinStatus") Boolean tinStatus,
            @Param("categories") List<UUID> categories,
            @Param("search") String search, Pageable pageable);


    @Query(value = """
                 select c.id, c.latitude, c.longitude,c.name,c.address,c.active from client c ;
            """,nativeQuery = true)
    List<ClientsForMapProjection> findClientsForMap();
}
