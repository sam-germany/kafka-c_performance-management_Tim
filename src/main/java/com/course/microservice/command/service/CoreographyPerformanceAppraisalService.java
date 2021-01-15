package com.course.microservice.command.service;

import com.course.microservice.broker.message.PerformanceAppraisalApprovedMessage;
import com.course.microservice.broker.publisher.PerformanceAppraisalApprovedPublisher;
import com.course.microservice.entity.PerformanceAppraisalStatus;
import com.course.microservice.repository.PerformanceAppraisalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CoreographyPerformanceAppraisalService {

	private static final Logger LOG = LoggerFactory.getLogger(CoreographyPerformanceAppraisalService.class);

	@Autowired
	private PerformanceAppraisalApprovedPublisher publisher;

	@Autowired
	private PerformanceAppraisalRepository repository;

	public void approveCompensatingPerformanceAppraisal(String appraisalId) {


		//
		var appraisal = repository.findById(UUID.fromString(appraisalId)).orElseThrow();

		if (appraisal.isFinalState()) {
			return;
		}

		LOG.debug("[Choreography-Compensating Saga] Approving performance appraisal");

		// 1.
repository.updatePerformanceAppraisalStatusById( PerformanceAppraisalStatus.APPROVAL_ON_PROGRESS.toString(),UUID.fromString(appraisalId) );

		// 2. publish 'appraisal approved message' to kafka
		var appraisalApprovedMessage = new PerformanceAppraisalApprovedMessage();
		appraisalApprovedMessage.setAppraisalId(appraisalId);
		appraisalApprovedMessage.setEmployeeId(appraisal.getEmployeeId());
		appraisalApprovedMessage.setGrade(appraisal.getGrade());
		appraisalApprovedMessage.setScore(appraisal.getScore());

		publisher.publishForCoreographyCompensatingSaga(appraisalApprovedMessage);
	}

	public void approvePerformanceAppraisal(String appraisalId) {

		//  here we are checking through the DB that record with this id is their or not
		var appraisal = repository.findById(UUID.fromString(appraisalId)).orElseThrow();

		if (appraisal.isFinalState()) {       // see class PerformanceAppraisal  their he create this method if it return true
			return;                           // then just return the call and do not execute the code further down
		}

		LOG.debug("[Choreography-Saga] Approving performance appraisal");

		// 1. here we are updating the Table performance_appraisal the "status" from  "NEW"  to  "APPROVAL_ON_PROGRESS"
     repository.updatePerformanceAppraisalStatusById(PerformanceAppraisalStatus.APPROVAL_ON_PROGRESS.toString(),
			                                                                                      UUID.fromString(appraisalId));

		// 2. publish 'appraisal approved message' to kafka
		var appraisalApprovedMessage = new PerformanceAppraisalApprovedMessage();
		appraisalApprovedMessage.setAppraisalId(appraisalId);
		appraisalApprovedMessage.setEmployeeId(appraisal.getEmployeeId());
		appraisalApprovedMessage.setGrade(appraisal.getGrade());
		appraisalApprovedMessage.setScore(appraisal.getScore());

		publisher.publishForCoreographySaga(appraisalApprovedMessage);
	}

	public void errorPerformanceAppraisal(String appraisalId) {
		// ...
		// Compensate previous transaction, e.g. update status, etc
		// ...

      repository.updatePerformanceAppraisalStatusById(PerformanceAppraisalStatus.APPROVAL_ERROR.toString(),
			                                                                                   UUID.fromString(appraisalId));
	}


	public void finalizePerformanceAppraisal(String appraisalId) {
		// ...
		// finalize appraisal
		// ...
      repository.updatePerformanceAppraisalStatusById(PerformanceAppraisalStatus.APPROVED.toString(),
			                                                                                   UUID.fromString(appraisalId));
	}

}
