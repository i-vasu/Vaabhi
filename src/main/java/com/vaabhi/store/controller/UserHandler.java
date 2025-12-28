package com.vaabhi.store.controller;

import com.vaabhi.store.model.User;
import com.vaabhi.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserHandler {

    @Autowired
    private final UserRepository repo;
    public UserHandler(UserRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    private List<User> getUsers(){
        return repo.findAll();
    }

    @PostMapping
    private User addUser(@RequestBody User user){
        return repo.save(user);
    }
}
