package com.example.ticketanalyzer.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ticketanalyzer.model.TicketAnalysis;

public interface TicketAnalysisRepository extends MongoRepository<TicketAnalysis, String> {

	List<TicketAnalysis> findByTicketIdOrderByAnalyzedAtDesc(String ticketId);
}
