package com.george.springsecurity.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Entity
@Table(name = "partners")
@Data
public class PartnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String clientId;
    private String clientName;
    private String clientSecret;
    private String scopes;
    private String grandTypes;
    private String authenticationMethods;
    private String redirectUri;
    private String redirectUriLogout;

}
