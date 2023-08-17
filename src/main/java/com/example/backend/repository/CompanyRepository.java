package com.example.backend.repository;

import com.example.backend.entity.Company;

import com.example.backend.projection.DashboardViewProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    @Query(value = "SELECT support_phone as support_phone, now() as current_date FROM company WHERE company.supervisor_id = :supervisorId", nativeQuery = true)
    Optional<DashboardViewProjection> getDashboardInfoBySuperVisorId(@Param("supervisorId") UUID supervisorId);

    @Query(value = "SELECT * FROM company " +
            "WHERE LOWER(CONCAT(region,' ',company_name,' ',support_phone,' ',email,' ',address)) LIKE CONCAT('%',LOWER(:search),'%')"
            ,nativeQuery = true)
    Page<Company> findAllByRegionContainsIgnoreCaseOrCompany_nameContainsIgnoreCaseOrSupport_phoneContainsIgnoreCaseOrEmailContainsIgnoreCaseOrAddressContainsIgnoreCase(String search, Pageable pageable);

}
