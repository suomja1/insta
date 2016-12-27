package insta.test;

import insta.serv.KayttajaService;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InstaApplicationTest {
    @Autowired
    private WebApplicationContext webAppContext;
    @Autowired
    private KayttajaService kayttajaService;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testViewLoginPage() throws Exception {
        MvcResult res = mockMvc.perform(get("/login"))
                .andExpect(status().isOk()).andReturn();

        assertEquals("login", res.getModelAndView().getViewName());
    }

    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(formLogin()
                .user("eioleolemassa")
                .password("salasana"))
                .andExpect(unauthenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));

        mockMvc.perform(formLogin()
                .user("taavetti99")
                .password("taavetti99"))
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    public void testCreateUserAndLogin() throws Exception {
        String username = "kayttaja";
        String password = "salasana";

            mockMvc.perform(post("/create")
                .with(csrf())
                .param("kayttajanimi", username)
                .param("salasana", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        
        assertTrue(kayttajaService.hae(username) != null);
        
        mockMvc.perform(formLogin()
                .user(username)
                .password(password))
                .andExpect(authenticated());
    }
    
    @Test
    public void testViewHome() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
        
        MvcResult res = mockMvc.perform(get("/home")
                .with(user("taavetti99").password("taavetti99")))
                .andExpect(status().isOk()).andReturn();

        assertEquals("index", res.getModelAndView().getViewName());
        
        
    }
}
