package com.example.digitalwallet.repository;

import com.example.digitalwallet.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Authority, Long> {

}