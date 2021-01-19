package com.mobile.api.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mobile.api.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
	User findByUsername(String username);
}
