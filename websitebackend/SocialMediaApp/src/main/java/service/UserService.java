package service;

import models.User;

import java.util.List;

public interface UserService {
    List<User> findByFirstname(String firstname);
    List<User> findByLastname(String firstname);
    List<User> findByFirstNameAndLastName(String firstname, String lastname);
    User saveUser (User user);

}
