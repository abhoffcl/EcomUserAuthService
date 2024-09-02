package dev.Abhishek.EcomUserAuthService.service.kafkaProducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.Abhishek.EcomUserAuthService.config.KafkaProducer;
import dev.Abhishek.EcomUserAuthService.dto.WelcomeUserDto;
import dev.Abhishek.EcomUserAuthService.dto.SignupRequestDto;
import dev.Abhishek.EcomUserAuthService.exception.KafkaMessagingException;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessagingServiceImpl implements KafkaMessagingService {
    private KafkaProducer kafkaProducer;
    private ObjectMapper objectMapper;

    public KafkaMessagingServiceImpl(KafkaProducer kafkaProducer, ObjectMapper objectMapper) {
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
    }

    public void send(SignupRequestDto signupRequestDto)  {
        WelcomeUserDto userDto = new WelcomeUserDto();
        userDto.setName(signupRequestDto.getName());
        userDto.setEmail(signupRequestDto.getEmail());
        userDto.setFrom("admin@ecom.com");
        userDto.setSubject("Welcome to Ecom!");
        userDto.setBody("Thank you for registering with us.  We're excited to have you on board. ");
    try{
        String message = objectMapper.writeValueAsString(userDto);
        kafkaProducer.send("sendEmail", message);
        } catch (JsonProcessingException e) {
            throw new KafkaMessagingException("Failed to process JSON for Kafka message");
        } catch (RuntimeException e) {
            throw new KafkaMessagingException("Failed to send Kafka message");
        }
    }
}
