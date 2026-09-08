package com.example.ticketanalyzer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ticketanalyzer.model.Ticket;
import com.example.ticketanalyzer.repository.TicketRepository;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

	private final TicketRepository ticketRepository;

	public TicketController(TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
	}

	@PostMapping
	public ResponseEntity<Ticket> createTicket(@RequestBody CreateTicketRequest request) {
		Ticket ticket = new Ticket(
			request.customerId(),
			request.subject(),
			request.body(),
			request.channel()
		);
		Ticket saved = ticketRepository.save(ticket);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	record CreateTicketRequest(String customerId, String subject, String body, String channel) {
	}
}
