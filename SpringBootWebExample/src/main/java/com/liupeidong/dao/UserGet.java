package com.liupeidong.dao;

import com.liupeidong.model.User;

import java.util.List;

public interface UserGet {

    public List<User> getAllUsers();

    public User getUserByUserId(int _userId);

    public User getUserByUserLoginName(String _userLoginName);

    public User getUserByUserName(String _userName);

    public int delete(int _userId);

    public int update(User _user);

}
