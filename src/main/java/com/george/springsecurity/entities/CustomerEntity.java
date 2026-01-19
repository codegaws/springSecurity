package com.george.springsecurity.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Table(name = "customers")
@Data
public class CustomerEntity implements Serializable {//todo objeto que viaja por la red es bueno que se implemente serializable

    @Id
    private BigInteger id;
    private String email;
    @Column(name = "pwd")
    private String password;
    @Column(name = "rol")
    private String role;


}
