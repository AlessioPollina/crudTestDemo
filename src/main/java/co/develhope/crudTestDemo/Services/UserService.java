package co.develhope.crudTestDemo.Services;

import co.develhope.crudTestDemo.Entities.User;
import co.develhope.crudTestDemo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User setUserActivationStatus(Long userId, Boolean isActive){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return null;
        user.get().setIsActive(isActive);
        return userRepository.save(user.get());

    }
}
