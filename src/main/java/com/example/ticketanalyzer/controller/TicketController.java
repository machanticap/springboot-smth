package com.example.ticketanalyzer.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.ticketanalyzer.model.Ticket;
import com.example.ticketanalyzer.model.TicketAnalysis;
import com.example.ticketanalyzer.model.TicketStatus;
import com.example.ticketanalyzer.repository.TicketRepository;
import com.example.ticketanalyzer.service.TicketAnalysisService;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

	private static final String UUID_REGEX =
		"^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
	private static final String CHANNEL_REGEX = "email|web_form|chat";

	private final TicketRepository ticketRepository;
	private final TicketAnalysisService ticketAnalysisService;

	public TicketController(TicketRepository ticketRepository, TicketAnalysisService ticketAnalysisService) {
		this.ticketRepository = ticketRepository;
		this.ticketAnalysisService = ticketAnalysisService;
	}

	@PostMapping
	public ResponseEntity<Ticket> createTicket(@Valid @RequestBody CreateTicketRequest request) {
		Ticket ticket = new Ticket(
			request.customerId(),
			request.subject(),
			request.body(),
			request.channel()
		);
		Ticket saved = ticketRepository.save(ticket);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@PostMapping("/{id}/analyze")
	public ResponseEntity<TicketAnalysis> analyzeTicket(@PathVariable String id) {
		Ticket ticket = ticketRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found: " + id));

		try {
			TicketAnalysis analysis = ticketAnalysisService.analyze(ticket);
			ticket.setStatus(TicketStatus.PROCESSED);
			ticketRepository.save(ticket);
			return ResponseEntity.ok(analysis);
		} catch (RuntimeException e) {
			ticket.setStatus(TicketStatus.ERROR);
			ticketRepository.save(ticket);
			throw e;
		}
	}

	record CreateTicketRequest(
		@NotBlank @Pattern(regexp = UUID_REGEX, message = "customerId must be a valid UUID") String customerId,
		@NotBlank String subject,
		@NotBlank String body,
		@NotBlank @Pattern(regexp = CHANNEL_REGEX, message = "channel must be one of: email, web_form, chat") String channel
	) {
	}
}
