package co.develhope.crudTestDemo;

import co.develhope.crudTestDemo.Entities.User;
import co.develhope.crudTestDemo.Repositories.UserRepository;
import co.develhope.crudTestDemo.Services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void checkUserActivation()throws Exception{

        User user = new User();
        user.setIsActive(true);
        user.setName("Lorenzo");
        user.setSurname("De francesco");
        user.setAge(30);

        User userFromDB = userRepository.save(user);
        assertThat(userFromDB).isNotNull();
        assertThat(userFromDB.getId()).isNotNull();


        User userFromService = userService.setUserActivationStatus(user.getId(), true);
        assertThat(userFromService).isNotNull();
        assertThat(userFromService.getId()).isNotNull();
        assertThat(userFromService.getIsActive()).isTrue();

        User userFromFind = userRepository.findById(userFromDB.getId()).get();
        assertThat(userFromFind).isNotNull();
        assertThat(userFromFind.getId()).isNotNull();
        assertThat(userFromFind.getId()).isEqualTo(userFromDB.getId());
        assertThat(userFromFind.getIsActive()).isTrue();
    }
}
