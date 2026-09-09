package com.example.ticketanalyzer.service;

import com.example.ticketanalyzer.model.Category;
import com.example.ticketanalyzer.model.Severity;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record TicketAnalysisResult(
	@JsonPropertyDescription("Brief plain-language summary of the customer's issue")
	String summary,

	@JsonPropertyDescription("Category chosen from the fixed taxonomy")
	Category category,

	@JsonPropertyDescription("Severity chosen from the fixed taxonomy")
	Severity severity,

	@JsonPropertyDescription("Optional self-reported confidence between 0 and 1")
	Double confidence
) {
}
