package com.peachkite.db.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.peachkite.db.domain.User;

public interface UserRepository extends MongoRepository<User,String>{
	
	public User findByEmail(String email);
	
}
