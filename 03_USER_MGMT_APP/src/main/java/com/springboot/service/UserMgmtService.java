package com.springboot.service;

import java.util.List;

import com.springboot.bindings.ActivateAccount;
import com.springboot.bindings.Login;
import com.springboot.bindings.User;

public interface UserMgmtService {
	public boolean saveUser(User user);

	public boolean activateUserAcc(ActivateAccount activateAcc);

	public List<User> getAllUsers();

	public User getUserById(Integer userId);

	public boolean deleteUserById(Integer userId);

	public boolean changeAccountStatus(Integer userId, String accStatus);

	public String login(Login login);

	public String forgotPwd(String email);

}
