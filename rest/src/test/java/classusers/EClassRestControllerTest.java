package classusers;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;
import org.junit.Test;

/**
 * author of spring boot REST tutorial (https://spring.io/guides/tutorials/bookmarks/)
 * @author Josh Long
 *
 * @author I-Hui Huang
 */
@FixMethodOrder
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class EClassRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private String firstName = "Testy";
    private String lastName = "Terriby";
    private String email = "testyter@example.com";

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private User user;

    private List<EClass> eclassList = new ArrayList<>();

    @Autowired
    private EClassRepository eclassRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.eclassRepository.deleteAllInBatch();
        this.userRepository.deleteAllInBatch();

        // add one user
        this.user = userRepository.save(new User(firstName, lastName, email));
        // add two eclasses, add user as student to first class
        EClass eclass1 = eclassRepository.save(new EClass(user, "Test Class I"));
        EClass eclass2 = eclassRepository.save(new EClass(user, "Test Class II"));
        eclass1.getStudents().add(user);
        eclassRepository.save(eclass1);
        this.eclassList.add(eclass1);
        this.eclassList.add(eclass2);
    }

    // test GET-1 normal
    @Test
    public void readCreatorEClassNormal() throws Exception {
        mockMvc.perform(get("/user/" + this.user.getId() + "/creator"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
                //.andExpect(jsonPath("$[0].id", is(this.eclassList.get(0).getId().intValue())))
                //.andExpect(jsonPath("$[0].classname", is("Test Class I")))
                //.andExpect(jsonPath("$[1].id", is(this.eclassList.get(1).getId().intValue())));
                //.andExpect(jsonPath("$[1].classname", is("Test Class II")));
    }
    
    // test GET-1 user not found
    @Test
    public void userNotFound() throws Exception {
        mockMvc.perform(get("/user/33/creator"))
                .andExpect(status().isNotFound());
    }

    // test GET-2 normal
    @Test
    public void readStudentEClassNormal() throws Exception {
        mockMvc.perform(get("/user/" + this.user.getId() + "/student"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[0].id", is(this.eclassList.get(0).getId().intValue())));
                //.andExpect(jsonPath("$[0].classname", is("Test Class I")));
    }

    // test GET-3 normal
    @Test
    public void readEClassStudentNormal() throws Exception {
        mockMvc.perform(get("/class/" + this.eclassList.get(0).getId() + "/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[0].id", is(this.user.getId().intValue())))
                .andExpect(jsonPath("$[0].firstname", is("Testy")))
                .andExpect(jsonPath("$[0].lastname", is("Terriby")))
                .andExpect(jsonPath("$[0].email", is("testyter@example.com")));
    }

    // test GET-3 class not found
    @Test
    public void classNotFound() throws Exception {
        mockMvc.perform(get("/class/1111/students"))
                .andExpect(status().isNotFound());
    }

    // test POST-1 normal
    @Test
    public void renameClassNormal() throws Exception {
        String renameClassJson = json(new EClass(null, "Renamed Class I"));
        this.mockMvc.perform(post("/class/" + this.eclassList.get(0).getId() + "/rename")
                .contentType(contentType)
                .content(renameClassJson))
                .andExpect(status().isOk());
    }

    // test POST-1 invalid input - (empty new classname)
    // 422 HttpStatus.UNPROCESSABLE_ENTITY
    @Test
    public void invalidInput() throws Exception {
        String renameClassJson = json(new EClass(null, ""));
        this.mockMvc.perform(post("/class/" + this.eclassList.get(0).getId() + "/rename")
                .contentType(contentType)
                .content(renameClassJson))
                .andExpect(status().isUnprocessableEntity());
    }

    // test POST-2 normal
    // add user to second class
    @Test
    public void addClassStudentNormal() throws Exception {
        String addClassStudentJson = "{\"id\":" + this.user.getId() + "}";
        this.mockMvc.perform(post("/class/" + this.eclassList.get(1).getId() + "/addstudent")
                .contentType(contentType)
                .content(addClassStudentJson))
                .andExpect(status().isOk());
    }

    // test POST-3 normal
    // update firstname of user
    @Test
    public void updateUserNormal() throws Exception {
        String updateUserJson = "{\"firstname\":\"Tilda\"}";
        this.mockMvc.perform(post("/user/" + this.user.getId() + "/update")
                .contentType(contentType)
                .content(updateUserJson))
                .andExpect(status().isOk());
        this.checkUpdateNormal();
    }
    // check if user is really updated and last name, email not affected
    public void checkUpdateNormal() throws Exception {
        mockMvc.perform(get("/user/" + this.user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.firstname", is("Tilda")))
                .andExpect(jsonPath("$.lastname", is("Terriby")))
                .andExpect(jsonPath("$.email", is("testyter@example.com")));
    }

    // test PUT-1 normal
    // create user 2
    @Test
    public void addUserNormal() throws Exception {
        String addUserJson = json(new User("Monica", "Mo", "monicamoo@example.com"));
        this.mockMvc.perform(put("/user/create")
                .contentType(contentType)
                .content(addUserJson))
                .andExpect(status().isCreated());
        this.checkAddUserNormal();
    }
    // check if user is really updated and last name, email not affected
    public void checkAddUserNormal() throws Exception {
        mockMvc.perform(get("/user/search/lastname/Mo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[0].firstname", is("Monica")))
                .andExpect(jsonPath("$[0].lastname", is("Mo")))
                .andExpect(jsonPath("$[0].email", is("monicamoo@example.com")));
    }

    // test PUT-2 normal
    // create class 3
    @Test
    public void addClassNormal() throws Exception {
        String addClassJson = "{\"classname\":\"The New Class\"}";;
        this.mockMvc.perform(put("/class/create/" + this.user.getId())
                .contentType(contentType)
                .content(addClassJson))
                .andExpect(status().isCreated());
        this.checkAddClassNormal();
    }
    // check if user is really updated and last name, email not affected
    public void checkAddClassNormal() throws Exception {
        mockMvc.perform(get("/class/search/classname/The New Class"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
                //.andExpect(jsonPath("$[0].classname", is("The New Class")));
    }
    

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
