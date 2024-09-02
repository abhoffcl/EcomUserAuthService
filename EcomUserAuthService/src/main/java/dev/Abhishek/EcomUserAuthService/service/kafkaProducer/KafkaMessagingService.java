package dev.Abhishek.EcomUserAuthService.service.kafkaProducer;

import dev.Abhishek.EcomUserAuthService.dto.SignupRequestDto;

public interface KafkaMessagingService {
    public void send(SignupRequestDto signupRequestDto);
}
