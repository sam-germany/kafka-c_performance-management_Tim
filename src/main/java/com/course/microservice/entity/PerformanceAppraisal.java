package com.course.microservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class PerformanceAppraisal {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "BINARY(16)")      // if we use "BINARY(16)"   for a  UUID  column value then we will not get
	private UUID appraisalId;                     // any error

	@JsonIgnore                        // this property will not be transfered by the json while serialization and deserialization
	@CreationTimestamp                 // it will take the date time from the virtual machine and set to this property ,when first time
	@Column(nullable = false)                                   // the object is created then only it will set this value, if again it is updated
	private LocalDateTime createdDateTime;                    // then it will not modify this value

	@Column(nullable = false, length = 30)
	private String employeeId;

	@Column(nullable = false, length = 30)
	private String grade;

	@JsonIgnore
	@UpdateTimestamp                       // it will take the current time from the virtual machine and put to this property only when the
	@Column(nullable = false)                          // this object is updated, it will be null for the first time
	private LocalDateTime lastUpdatedDateTime;

	@Column(nullable = false)
	private int score;

	@Column(nullable = false, length = 30)
	private String status;

	@JsonIgnore
	public boolean isFinalState() {
		return
getStatus().equals(PerformanceAppraisalStatus.APPROVED.toString())	|| getStatus().equals(PerformanceAppraisalStatus.APPROVAL_ERROR.toString());
	}



	public UUID getAppraisalId() {
		return appraisalId;
	}

	public LocalDateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public String getGrade() {
		return grade;
	}

	public LocalDateTime getLastUpdatedDateTime() {
		return lastUpdatedDateTime;
	}

	public int getScore() {
		return score;
	}

	public String getStatus() {
		return status;
	}



	public void setAppraisalId(UUID appraisalId) {
		this.appraisalId = appraisalId;
	}

	public void setCreatedDateTime(LocalDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public void setLastUpdatedDateTime(LocalDateTime lastUpdatedDateTime) {
		this.lastUpdatedDateTime = lastUpdatedDateTime;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "PerformanceAppraisal [appraisalId=" + appraisalId + ", createdDateTime=" + createdDateTime
				+ ", employeeId=" + employeeId + ", grade=" + grade + ", lastUpdatedDateTime=" + lastUpdatedDateTime
				+ ", score=" + score + ", status=" + status + "]";
	}

}
