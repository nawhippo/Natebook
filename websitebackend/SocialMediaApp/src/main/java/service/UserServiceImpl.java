package service;

import models.User;
import repository.UserRepository;

import java.util.List;


//intermediary between model and database (repository)
public class UserServiceImpl implements UserService {

    UserRepository repository;
    @Override
    public List<User> findByFirstname(String firstname) {
        return repository.findByFirstname(firstname);
    }

    @Override
    public List<User> findByLastname(String lastname) {
        return repository.findByLastname(lastname);
    }

    @Override
    public List<User> findByFirstNameAndLastName(String firstname, String lastname) {
        return repository.FindByFirstNameAndLastName(firstname, lastname);
    }

    @Override
    public User saveUser(User user) {
        repository.save(user);
        return user;
    }
}
