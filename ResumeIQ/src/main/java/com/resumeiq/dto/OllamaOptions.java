package com.resumeiq.dto;

public class OllamaOptions {

    private double temperature;

    private int num_predict;


    public OllamaOptions() {
    }


    public OllamaOptions(
            double temperature,
            int num_predict) {

        this.temperature = temperature;
        this.num_predict = num_predict;
    }


    public double getTemperature() {
        return temperature;
    }


    public void setTemperature(
            double temperature) {

        this.temperature = temperature;
    }


    public int getNum_predict() {
        return num_predict;
    }


    public void setNum_predict(
            int num_predict) {

        this.num_predict = num_predict;
    }
}