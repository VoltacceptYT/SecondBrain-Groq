package me.sailex.secondbrain.llm.groq;

public class GroqChatMessage {

    public String role;
    public String content;

    public GroqChatMessage() {}

    public GroqChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}