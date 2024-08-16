package dev.Abhishek.EcomUserAuthService.service.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.Abhishek.EcomUserAuthService.entity.JwtObject;
import dev.Abhishek.EcomUserAuthService.entity.Role;
import dev.Abhishek.EcomUserAuthService.entity.Token;
import dev.Abhishek.EcomUserAuthService.entity.User;
import dev.Abhishek.EcomUserAuthService.dto.RoleResponseDto;
import dev.Abhishek.EcomUserAuthService.exceptions.JwtDeserializationException;
import dev.Abhishek.EcomUserAuthService.exceptions.JwtSerializationException;
import dev.Abhishek.EcomUserAuthService.repository.TokenRepository;
import dev.Abhishek.EcomUserAuthService.service.role.RoleService;
import dev.Abhishek.EcomUserAuthService.service.role.RoleServiceImpl;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Base64;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService{
    private RoleService roleService;
    private TokenRepository tokenRepository;
    private ObjectMapper objectMapper;


    public TokenServiceImpl(RoleService roleService, TokenRepository tokenRepository, ObjectMapper objectMapper) {
        this.roleService = roleService;
        this.tokenRepository = tokenRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Token createToken(User user)throws JwtSerializationException  {
        JwtObject jwtObject = new JwtObject();
        List<Role> roles =user.getRoles();
        List<RoleResponseDto>roleResponseDtos =!(roles==null || roles.isEmpty())?roles.
                stream().
                map(savedRole->((RoleServiceImpl)roleService).convertRoleEntityToRoleResponseDto(savedRole)).
                collect(Collectors.toList()) :  new ArrayList<>();
        jwtObject.setUserId(user.getId());
        jwtObject.setEmail(user.getEmail());
        jwtObject.setName(user.getName());
        jwtObject.setPhoneNumber(user.getPhoneNumber());
        jwtObject.setRoles(roleResponseDtos);
        jwtObject.setCreatedAt(Instant.now());
        jwtObject.setExpiryAt(Instant.now().plus(Duration.ofDays(30)));
        String tokenValue = serializeJwtObject(jwtObject);
        String encodedTokenValue = encodeToken(tokenValue);
        Token token = new Token();
        token.setUser(user);
        token.setValue(encodedTokenValue);
        token.setExpireAt(jwtObject.getExpiryAt());
        token.setDeleted(false);
        return tokenRepository.save(token);
    }
    @Override
    public JwtObject validateToken(String token) throws JwtDeserializationException  {
        Optional<Token> tokenOptional = tokenRepository.findByValueAndDeletedEqualsAndExpireAtGreaterThan(
                token, false, Instant.now());
        if (tokenOptional.isEmpty())
            return null;
        Token tokenEntity = tokenOptional.get();
        // decode token value and then deserialize the token value to JwtObject
        String decodedTokenValue = decodeToken(tokenEntity.getValue());
        JwtObject jwtObject = deserializeJwtObject(decodedTokenValue);
        return jwtObject;
    }

    @Override
    public JwtObject deserializeJwtObject(String tokenValue) throws JwtDeserializationException {
        try {
            return objectMapper.readValue(tokenValue, JwtObject.class);
        }catch(JsonProcessingException je){
            throw new JwtDeserializationException("Error deserializing JwtObject");
        }
    }

    @Override
    public String serializeJwtObject(JwtObject jwtObject)throws JwtSerializationException  {
        try {
            return objectMapper.writeValueAsString(jwtObject);
        }catch(JsonProcessingException je){
            throw new JwtSerializationException("Error serializing JwtObject");
        }
    }
    private String encodeToken(String tokenValue) {
        return Base64.getEncoder().encodeToString(tokenValue.getBytes(StandardCharsets.UTF_8));
    }
    private String decodeToken(String encodedToken) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedToken);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
