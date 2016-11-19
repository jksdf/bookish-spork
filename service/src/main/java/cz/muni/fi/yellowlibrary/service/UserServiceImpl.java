package cz.muni.fi.yellowlibrary.service;

import java.util.List;

import javax.inject.Inject;

import cz.muni.fi.pa165.yellowlibrary.backend.dao.UserDao;
import cz.muni.fi.pa165.yellowlibrary.backend.entity.User;

/**
 * @author Jozef Zivcic
 */
public class UserServiceImpl implements UserService {

  @Inject
  UserDao userDao;

  @Override
  public void create(User user) {
    userDao.createUser(user);
  }

  @Override
  public void update(User user) {
    userDao.updateUser(user);
  }

  @Override
  public void delete(User user) {
    userDao.deleteUser(user);
  }

  @Override
  public User findById(Long id) {
    return userDao.findById(id);
  }

  @Override
  public User findByLogin(String login) {
    return new User();
  }

  @Override
  public boolean isEmployee(User user) {
    return findById(user.getId()).isEmployee();
  }

  @Override
  public boolean isCustomer(User user) {
    return findById(user.getId()).isCustomer();
  }

  @Override
  public List<User> findAllUsers() {
    return userDao.findAllUsers();
  }
}
