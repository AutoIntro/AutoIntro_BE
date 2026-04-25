package com.capstone.autointro.domain.introduction.repository;

import com.capstone.autointro.domain.introduction.entity.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {

    List<UserRequest> findAllByUserIdOrderByIdDesc(Long userId);
}
