package com.cuping.cupingbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Cafe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Owner ownerId;
    private String cafeAddress;
    private String cafePhoneNumber;
    private String cafeName;
    private String x;
    private String y;
    private boolean cafePermit;

}
