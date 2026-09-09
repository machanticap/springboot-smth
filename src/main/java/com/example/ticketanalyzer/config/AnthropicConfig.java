package com.example.ticketanalyzer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;

@Configuration
public class AnthropicConfig {

	private static final Logger log = LoggerFactory.getLogger(AnthropicConfig.class);

	@Bean
	public AnthropicClient anthropicClient(@Value("${anthropic.api.key}") String apiKey) {
		log.info("Anthropic API key resolved: prefix={}, length={}",
			apiKey.substring(0, Math.min(12, apiKey.length())), apiKey.length());
		return AnthropicOkHttpClient.builder()
			.apiKey(apiKey)
			.build();
	}
}
