package co.develhope.crudTestDemo.Controllers;

import co.develhope.crudTestDemo.Entities.User;
import co.develhope.crudTestDemo.Repositories.UserRepository;
import co.develhope.crudTestDemo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("")
    public @ResponseBody User create(@RequestBody User user){
        return userRepository.save(user);

    }

    @GetMapping("/")
    public @ResponseBody List<User> getList(){
        return userRepository.findAll();

    }

    @GetMapping("/{id}")
    public @ResponseBody User getSingle(@PathVariable long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }else {
            return null;
        }
    }

    @PutMapping("/{id}")
    public  @ResponseBody User update(@PathVariable long id, @RequestBody @NonNull User user){
        user.setId(id);
        return userRepository.save(user);

    }

    @PutMapping("/{id}/activation")
    public @ResponseBody User setUserActive(@PathVariable long id, @RequestParam("activated") boolean isActive){
        return userService.setUserActivationStatus(id, isActive);

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id){
        userRepository.deleteById(id);

    }
}
