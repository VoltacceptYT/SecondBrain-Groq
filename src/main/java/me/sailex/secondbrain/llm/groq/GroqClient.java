package me.sailex.secondbrain.llm.groq;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.sailex.secondbrain.exception.LLMServiceException;
import me.sailex.secondbrain.history.Message;
import me.sailex.secondbrain.history.MessageConverter;
import me.sailex.secondbrain.llm.LLMClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GroqClient implements LLMClient {

	private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
	private final String groqApiKey;
	private final String groqModel;
	private final int timeout;
	private final HttpClient httpClient;
	private final ObjectMapper objectMapper;

	/**
	 * Constructor for GroqClient.
	 *
	 * @param model  the model name
	 * @param apiKey the API key
	 * @param timeout the timeout in seconds
	 */
	public GroqClient(String model, String apiKey, int timeout) {
		this.groqModel = model;
		this.groqApiKey = apiKey;
		this.timeout = timeout;
		this.httpClient = HttpClient.newBuilder()
				.connectTimeout(java.time.Duration.ofSeconds(timeout))
				.build();
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public Message chat(List<Message> messages) {
		try {
			GroqChatRequest chatRequest = new GroqChatRequest(
					groqModel,
					messages.stream()
						.map(MessageConverter::toGroqChatMessage)
						.toList(),
					0.7
			);

			String requestBody = objectMapper.writeValueAsString(chatRequest);

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(GROQ_API_URL))
					.header("Content-Type", "application/json")
					.header("Authorization", "Bearer " + groqApiKey)
					.POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
					.timeout(java.time.Duration.ofSeconds(timeout))
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200) {
				throw new LLMServiceException("Groq API returned status code: " + response.statusCode() + " - " + response.body());
			}

			GroqChatResponse chatResponse = objectMapper.readValue(response.body(), GroqChatResponse.class);

			if (chatResponse.getChoices() == null || chatResponse.getChoices().isEmpty()) {
				throw new LLMServiceException("No choices returned from Groq API");
			}

			GroqChatMessage responseMessage = chatResponse.getChoices().get(0).getMessage();
			return MessageConverter.toMessage(responseMessage);

		} catch (Exception e) {
			throw new LLMServiceException("Could not generate Response for prompt: " + 
					(messages.isEmpty() ? "empty" : messages.get(messages.size() - 1).getMessage()), e);
		}
	}

	@Override
	public void checkServiceIsReachable() {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://api.groq.com/openai/v1/models"))
					.header("Authorization", "Bearer " + groqApiKey)
					.GET()
					.timeout(java.time.Duration.ofSeconds(timeout))
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200) {
				throw new LLMServiceException("Groq API is not reachable. Status: " + response.statusCode());
			}
		} catch (Exception e) {
			throw new LLMServiceException("Failed to reach Groq API", e);
		}
	}

	@Override
	public void stopService() {
		// No resources to clean up for HTTP-based client
	}

}