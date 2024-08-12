package dev.Abhishek.EcomUserAuthService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
public class Token extends BaseModel{
    @OneToOne
    @JoinColumn(name="userId")
    private User user;
    @Column(columnDefinition = "TEXT")
    private String value;
    private Instant expireAt;
    private boolean deleted;
}
