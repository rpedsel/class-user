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

/**
 * @author Josh Long
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class EClassRestControllerTest {


    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private String firstName = "myfirstname";

    private String lastName = "bdussault";

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

        this.user = userRepository.save(new User(firstName, lastName, "email@test.com"));
        this.eclassList.add(eclassRepository.save(new EClass(user, "A class by "+lastName)));
        this.eclassList.add(eclassRepository.save(new EClass(user, "B class by "+lastName)));
    }

    @Test
    public void userNotFound() throws Exception {
        mockMvc.perform(get("/user/apple/creator"))
                .andExpect(status().isNotFound());
    }

    // @Test
    // public void userNotFound() throws Exception {
    //     mockMvc.perform(post("/george/bookmarks/")
    //             .content(this.json(new Bookmark(null, null, null)))
    //             .contentType(contentType))
    //             .andExpect(status().isNotFound());
    // }

    // @Test
    // public void readSingleBookmark() throws Exception {
    //     mockMvc.perform(get("/bookmarks/" + lastName + "/" + this.bookmarkList.get(0).getId()))
    //             .andExpect(status().isOk())
    //             .andExpect(content().contentType(contentType))
    //             .andExpect(jsonPath("$.id", is(this.bookmarkList.get(0).getId().intValue())))
    //             .andExpect(jsonPath("$.uri", is("http://bookmark.com/1/" + lastName)))
    //             .andExpect(jsonPath("$.description", is("A description")));
    // }

    // @Test
    // public void readBookmarks() throws Exception {
    //     mockMvc.perform(get("/bookmarks/" + lastName))
    //             .andDo(print())
    //             .andExpect(status().isOk())
    //             .andExpect(content().contentType(contentType))
    //             .andExpect(jsonPath("$", hasSize(2)))
    //             .andExpect(jsonPath("$[0].id", is(this.bookmarkList.get(0).getId().intValue())))
    //             .andExpect(jsonPath("$[0].uri", is("http://bookmark.com/1/" + lastName)))
    //             .andExpect(jsonPath("$[0].description", is("A description")))
    //             .andExpect(jsonPath("$[1].id", is(this.bookmarkList.get(1).getId().intValue())))
    //             .andExpect(jsonPath("$[1].uri", is("http://bookmark.com/2/" + lastName)))
    //             .andExpect(jsonPath("$[1].description", is("A description")));
    // }

    // @Test
    // public void createBookmark() throws Exception {
    //     String bookmarkJson = json(new Bookmark(
    //             this.user, "http://spring.io", "a bookmark to the best resource for Spring news and information"));

    //     this.mockMvc.perform(post("/bookmarks/" + lastName)
    //             .contentType(contentType)
    //             .content(bookmarkJson))
    //             .andExpect(status().isCreated());
    // }

    // protected String json(Object o) throws IOException {
    //     MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
    //     this.mappingJackson2HttpMessageConverter.write(
    //             o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
    //     return mockHttpOutputMessage.getBodyAsString();
    // }
}
