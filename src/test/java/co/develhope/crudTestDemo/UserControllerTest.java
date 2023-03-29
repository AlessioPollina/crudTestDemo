package co.develhope.crudTestDemo;

import co.develhope.crudTestDemo.Controllers.UserController;
import co.develhope.crudTestDemo.Entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTest{

    @Autowired
    private UserController userController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;



    private User createAUser() throws Exception {

        User user = new User();
        user.setIsActive(true);
        user.setName("Lorenzo");
        user.setSurname("De francesco");
        user.setAge(30);
        return createAUser(user);
    }

    private User createAUser(User user) throws Exception {
        MvcResult result = createAUserRequest(user);
        User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        assertThat(userFromResponse).isNotNull();
        assertThat(userFromResponse.getId()).isNotNull();
        return userFromResponse;
    }

    private MvcResult createAUserRequest() throws Exception {
        User user = new User();
        user.setIsActive(true);
        user.setName("Lorenzo");
        user.setSurname("De francesco");
        user.setAge(30);
        return createAUserRequest(user);
    }

    private MvcResult createAUserRequest(User user) throws Exception {
        if (user == null) return null;
        String userJSON = objectMapper.writeValueAsString(user);
        return this.mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }



    private User getUserFromId(Long id)throws Exception {
        MvcResult result = this.mockMvc.perform(get("/user/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        try {
            String userJSON = result.getResponse().getContentAsString();
            User user = objectMapper.readValue(userJSON, User.class);
            assertThat(user).isNotNull();
            assertThat(user.getId()).isNotNull();
            return user;

        } catch (Exception e) {
            return null;
        }
    }

    @Test
    void userControllerLoads() {
        assertThat(userController).isNotNull();
    }




    @Test
    void createAUserTest() throws Exception {
        User userFromResponse = createAUser();
        assertThat(userFromResponse.getId()).isNotNull();
    }


    @Test
    void readUserList() throws Exception {
        createAUserRequest();
        MvcResult result = this.mockMvc.perform(get("/user/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<User> userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        System.out.println("User in Database are: " + userFromResponse.size());
        assertThat(userFromResponse.size()).isNotZero();
    }

    @Test
    void readSingleUser() throws Exception {
      User user = createAUser();
        User userFromResponse = getUserFromId(user.getId());
        assertThat(userFromResponse).isNotNull();
        assertThat(userFromResponse.getId()).isEqualTo(user.getId());
    }

    @Test
    void updateUser() throws Exception {
        User user = createAUser();
        String newName = "Giovanni";
        user.setName(newName);
        String userJSON = objectMapper.writeValueAsString(user);
        MvcResult result = this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //Check user from Put
        User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        assertThat(userFromResponse.getId()).isEqualTo(user.getId());
        assertThat(userFromResponse.getName()).isEqualTo(newName);


        //I get user with Get
        User userFromResponseGet = getUserFromId(user.getId());
        assertThat(userFromResponseGet.getId()).isEqualTo(user.getId());
        assertThat(userFromResponseGet.getName()).isEqualTo(newName);

    }

    @Test
    void deleteUser()throws Exception{
        User user = createAUser();
        assertThat(user.getId()).isNotNull();
        this.mockMvc.perform(delete("/user/" + user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        User userFromResponseGet = getUserFromId(user.getId());
        assertThat(userFromResponseGet).isNull();

    }


    @Test
    void activateUser()throws Exception{
        User user = createAUser();
        assertThat(user.getId()).isNotNull();

        MvcResult result = this.mockMvc.perform(put("/user/"+user.getId()+"/activation?activated=true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertThat(userFromResponse).isNotNull();
        assertThat(userFromResponse.getId()).isEqualTo(user.getId());
        assertThat(userFromResponse.getIsActive()).isEqualTo(true);

        User userFromResponseGet = getUserFromId(user.getId());

        assertThat(userFromResponseGet).isNotNull();
        assertThat(userFromResponseGet.getId()).isEqualTo(user.getId());
        assertThat(userFromResponseGet.getIsActive()).isEqualTo(true);

    }
}
