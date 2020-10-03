package com.dataplume.HarVis.auth.security.services;

import com.dataplume.HarVis.auth.models.User;
import com.dataplume.HarVis.auth.repository.UserRepository;
import javassist.NotFoundException;
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
        throw new ValidationException("user not exist", "400");
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
