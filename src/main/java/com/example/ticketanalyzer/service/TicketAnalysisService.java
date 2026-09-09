package com.example.ticketanalyzer.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.anthropic.client.AnthropicClient;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.StructuredMessageCreateParams;
import com.example.ticketanalyzer.model.Ticket;
import com.example.ticketanalyzer.model.TicketAnalysis;
import com.example.ticketanalyzer.repository.TicketAnalysisRepository;

@Service
public class TicketAnalysisService {

	private static final Logger log = LoggerFactory.getLogger(TicketAnalysisService.class);
	private static final String MODEL = "claude-opus-5";

	private final AnthropicClient anthropicClient;
	private final TicketAnalysisRepository ticketAnalysisRepository;
	private final String systemPrompt;

	public TicketAnalysisService(AnthropicClient anthropicClient, TicketAnalysisRepository ticketAnalysisRepository) {
		this.anthropicClient = anthropicClient;
		this.ticketAnalysisRepository = ticketAnalysisRepository;
		this.systemPrompt = loadSystemPrompt();
	}

	public TicketAnalysis analyze(Ticket ticket) {
		StructuredMessageCreateParams<TicketAnalysisResult> params = MessageCreateParams.builder()
			.model(MODEL)
			.maxTokens(16000L)
			.system(systemPrompt)
			.outputConfig(TicketAnalysisResult.class)
			.addUserMessage("Subject: " + ticket.getSubject() + "\n\nBody: " + ticket.getBody())
			.build();

		var response = anthropicClient.messages().create(params);
		log.info("Claude analysis response for ticket {}: {}", ticket.getId(), response);

		TicketAnalysisResult result = response.content().stream()
			.flatMap(block -> block.text().stream())
			.findFirst()
			.map(typed -> typed.text())
			.orElseThrow(() -> new IllegalStateException(
				"Claude did not return a structured analysis for ticket " + ticket.getId()));

		TicketAnalysis analysis = new TicketAnalysis(
			ticket.getId(),
			result.summary(),
			result.category(),
			result.severity(),
			result.confidence(),
			MODEL
		);
		return ticketAnalysisRepository.save(analysis);
	}

	private static String loadSystemPrompt() {
		try {
			return new ClassPathResource("prompts/ticket_analysis_prompt.md")
				.getContentAsString(StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new UncheckedIOException("Failed to load ticket analysis prompt", e);
		}
	}
}
