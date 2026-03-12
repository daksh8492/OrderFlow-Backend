package com.orderflow.dto;

import com.orderflow.entity.user.User;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private UUID userId;
    private String name;
    private String code;
    private User.FieldOfWork fieldOfWork;
    private UUID warehouseId;
    private String address;
    private String city;
    private String contactNumber;
    private String contactTelephone;
    private String contactEmail;
    private BigDecimal salary;
    private boolean active;
    private Instant joinedAt;
}
