package com.course.microservice.repository;

import com.course.microservice.entity.PerformanceAppraisal;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

@Repository
@RepositoryRestResource(collectionResourceRel = "appraisal", path = "appraisal")
public interface PerformanceAppraisalRepository extends PagingAndSortingRepository<PerformanceAppraisal, UUID> {

	@Modifying
	@Transactional
	@Query("UPDATE PerformanceAppraisal SET status = :status WHERE appraisal_id = :appraisalId")
	void updatePerformanceAppraisalStatusById(@Param("status") String status, @Param("appraisalId") UUID appraisalId);

}
/*
(video 27)
he use the url   http://localhost:8884/api/appraisal

as above he put only    "appraisal"    but in the .yml file he define    data:  rest: base-path: /api

so the   /api   comes from .yml file and at end   /api/appraisal

 */