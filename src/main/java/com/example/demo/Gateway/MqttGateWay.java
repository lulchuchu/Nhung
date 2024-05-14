package com.example.demo.Gateway;

import org.springframework.integration.annotation.MessagingGateway;

import org.springframework.integration.mqtt.support.MqttHeaders;

import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateWay {
    void sendToMqtt(String data, @Header(MqttHeaders.TOPIC) String topic);
}