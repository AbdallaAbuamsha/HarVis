package com.dataplume.HarVis.auth.security.services;

import com.dataplume.HarVis.auth.models.User;
import com.dataplume.HarVis.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) throws ValidationException {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent())
            return user.get();
        throw new ValidationException("user "+id+" is not exist", "400");
    }

    public void delete(Long id) throws ValidationException {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty())
            throw new ValidationException("user "+id+" is not exist", "400");
        userRepository.delete(user.get());
    }

    public User update(User user) throws ValidationException {
        if(userRepository.findById(user.getId()).isEmpty())
            throw new ValidationException("user "+user.getId()+" is not exist", "400");
        return userRepository.save(user);

    }
}
