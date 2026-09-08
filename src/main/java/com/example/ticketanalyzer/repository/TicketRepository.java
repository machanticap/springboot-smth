package com.example.ticketanalyzer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ticketanalyzer.model.Ticket;

public interface TicketRepository extends MongoRepository<Ticket, String> {
}
