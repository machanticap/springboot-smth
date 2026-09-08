package com.example.ticketanalyzer.model;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tickets")
public class Ticket {

	@Id
	private String id;

	private String customerId;
	private String subject;
	private String body;
	private Instant submittedAt;
	private String channel;
	private TicketStatus status;

	public Ticket() {
	}

	public Ticket(String customerId, String subject, String body, String channel) {
		UUID.fromString(customerId);
		this.customerId = customerId;
		this.subject = subject;
		this.body = body;
		this.channel = channel;
		this.submittedAt = Instant.now();
		this.status = TicketStatus.NEW;
	}

	public String getId() {
		return id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}

	public Instant getSubmittedAt() {
		return submittedAt;
	}

	public String getChannel() {
		return channel;
	}

	public TicketStatus getStatus() {
		return status;
	}

	public void setStatus(TicketStatus status) {
		this.status = status;
	}
}
