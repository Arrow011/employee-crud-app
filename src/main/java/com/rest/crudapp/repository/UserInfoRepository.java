package com.rest.crudapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.crudapp.model.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo,Integer>{

	Optional<UserInfo> findByName(String name);
}
