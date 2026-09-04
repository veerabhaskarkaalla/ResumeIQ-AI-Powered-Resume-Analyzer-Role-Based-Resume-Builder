package com.resumeiq.dto;

public class OllamaGenerateRequest {

    private String model;

    private String prompt;

    private boolean stream;

    private Object format;

    private boolean think;

    private OllamaOptions options;


    public OllamaGenerateRequest() {
    }


    public OllamaGenerateRequest(
            String model,
            String prompt,
            boolean stream,
            Object format,
            boolean think,
            OllamaOptions options) {

        this.model = model;
        this.prompt = prompt;
        this.stream = stream;
        this.format = format;
        this.think = think;
        this.options = options;
    }


    public String getModel() {
        return model;
    }


    public void setModel(String model) {
        this.model = model;
    }


    public String getPrompt() {
        return prompt;
    }


    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }


    public boolean isStream() {
        return stream;
    }


    public void setStream(boolean stream) {
        this.stream = stream;
    }


    public Object getFormat() {
        return format;
    }


    public void setFormat(Object format) {
        this.format = format;
    }


    public boolean isThink() {
        return think;
    }


    public void setThink(boolean think) {
        this.think = think;
    }


    public OllamaOptions getOptions() {
        return options;
    }


    public void setOptions(
            OllamaOptions options) {

        this.options = options;
    }
}