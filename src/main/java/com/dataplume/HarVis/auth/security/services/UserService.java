package com.dataplume.HarVis.auth.security.services;

import com.dataplume.HarVis.auth.enums.ERole;
import com.dataplume.HarVis.auth.models.User;
import com.dataplume.HarVis.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    PasswordEncoder encoder;

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
        Optional oldVersionUser = userRepository.findById(user.getId());

        // Check if user exist
        if(oldVersionUser.isEmpty())
            throw new ValidationException("user "+user.getId()+" is not exist", "400");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long id = ((User)oldVersionUser.get()).getId();
        Collection roles = auth.getAuthorities();
        // Check if current user has only user role and it's user
        if(auth.getAuthorities().size() == 1 && auth.getAuthorities().contains(ERole.ROLE_USER)) {
            // If normal user is not the owner of the account he is not autorized
            if (id != user.getId())
                throw new ValidationException("Not authorized user", "400");
            // Normal user can't edit roles
            user.setRoles(((User) oldVersionUser.get()).getRoles());
        }

        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);

    }
}
