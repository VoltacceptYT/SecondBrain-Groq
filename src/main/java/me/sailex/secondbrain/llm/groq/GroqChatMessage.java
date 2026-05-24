package me.sailex.secondbrain.llm.groq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroqChatMessage {
	
	private String role;
	private String content;

}