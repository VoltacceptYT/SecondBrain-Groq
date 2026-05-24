package me.sailex.secondbrain.llm.groq;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.sailex.secondbrain.llm.groq.GroqChatMessage;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroqChatRequest {
	
	private String model;
	private List<ChatMessage> messages;
	
	@JsonProperty("temperature")
	private double temperature;

}