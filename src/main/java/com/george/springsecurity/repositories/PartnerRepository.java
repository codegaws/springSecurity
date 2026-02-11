package com.george.springsecurity.repositories;

import com.george.springsecurity.entities.PartnerEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface PartnerRepository extends CrudRepository<PartnerEntity, Long> {

    Optional<PartnerEntity> findByClientId(String clientId);
}
