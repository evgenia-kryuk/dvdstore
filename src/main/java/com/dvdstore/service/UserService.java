package com.dvdstore.service;

import com.dvdstore.model.User;

import java.util.List;

public interface UserService {
	public User findUserByEmail(String email);
	public void saveUser(User user);
	public List<User> findDiscHolders();
}
