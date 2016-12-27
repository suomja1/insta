package insta.test;

import insta.serv.KayttajaService;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("prod")
public class InstaApplicationTest {
    @Autowired
    private WebApplicationContext webAppContext;
    @Autowired
    private KayttajaService kayttajaService;
    private MockMvc mockMvc;
    
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }
    
    @Test
    public void testNoTest() {
        assertTrue(true);
    }
    
//    @Test
//    public void testViewLoginPage() throws Exception {
//        MvcResult res = mockMvc.perform(get("/login"))
//                .andExpect(status().isOk()).andReturn();
//        
//        assertEquals("login", res.getModelAndView().getViewName());
//    }
    
//    @Test
//    public void testCreateUserAndLogin() throws Exception {
//        String nimi = "kayttaja";
//        String ssana = "salasana";
//        
//        MvcResult res = mockMvc.perform(post("/login")
//                .param("kayttajanimi", nimi)
//                .param("salasana", ssana))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/login")).andReturn();
//        
//        assertFalse(kayttajaService.hae(nimi) == null);
//    }
    
//    @Test
//    public void testLogin() throws Exception {
//        MvcResult res = mockMvc.perform(post("/login")
//                .param("username", "taavetti99")
//                .param("password", "taavetti99"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/home")).andReturn();
//    }
}