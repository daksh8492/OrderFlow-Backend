package com.orderflow.entity.product;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID itemId;
    private String name;
    @Enumerated(EnumType.STRING)
    private InwardSource sourceType;
    @Enumerated(EnumType.STRING)
    private ItemCategory category;
    @Enumerated(EnumType.STRING)
    private ItemStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Variant> variants = new ArrayList<>();

    @PrePersist
    void onCreate(){
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    void onUpdate(){
        updatedAt = Instant.now();
    }

    public enum ItemStatus{
        DRAFT, ACTIVE, INACTIVE, DISCONTINUED;
    }

    public enum ItemCategory{
        ELECTRONICS, APPARELS, HOME_APPLIANCES, CONSUMER_GOODS;
    }

    public enum InwardSource{
        MANUFACTURED, PURCHASED
    }
}

