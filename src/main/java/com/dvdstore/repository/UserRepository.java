package com.dvdstore.repository;

import com.dvdstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
	 User findByEmail(String email);

	@Query("SELECT u FROM User u where u.takenDiscList IS NOT EMPTY")
	List<User> findDiscsHolders();
}
