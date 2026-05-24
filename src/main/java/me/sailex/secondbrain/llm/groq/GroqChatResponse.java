package me.sailex.secondbrain.llm.groq;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroqChatResponse {
	
	private String id;
	private String object;
	private long created;
	private String model;
	private List<Choice> choices;
	private Usage usage;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Choice {
		private int index;
		private GroqChatMessage message;
		
		@JsonProperty("finish_reason")
		private String finishReason;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Usage {
		@JsonProperty("prompt_tokens")
		private int promptTokens;
		
		@JsonProperty("completion_tokens")
		private int completionTokens;
		
		@JsonProperty("total_tokens")
		private int totalTokens;
	}

}