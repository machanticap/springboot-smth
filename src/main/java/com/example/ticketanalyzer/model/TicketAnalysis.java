package com.example.ticketanalyzer.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ticket_analysis")
public class TicketAnalysis {

	@Id
	private String id;

	private String ticketId;
	private String summary;
	private Category category;
	private Severity severity;
	private Double confidence;
	private Instant analyzedAt;
	private String modelVersion;

	public TicketAnalysis() {
	}

	public TicketAnalysis(String ticketId, String summary, Category category, Severity severity,
			Double confidence, String modelVersion) {
		this.ticketId = ticketId;
		this.summary = summary;
		this.category = category;
		this.severity = severity;
		this.confidence = confidence;
		this.modelVersion = modelVersion;
		this.analyzedAt = Instant.now();
	}

	public String getId() {
		return id;
	}

	public String getTicketId() {
		return ticketId;
	}

	public String getSummary() {
		return summary;
	}

	public Category getCategory() {
		return category;
	}

	public Severity getSeverity() {
		return severity;
	}

	public Double getConfidence() {
		return confidence;
	}

	public Instant getAnalyzedAt() {
		return analyzedAt;
	}

	public String getModelVersion() {
		return modelVersion;
	}
}
