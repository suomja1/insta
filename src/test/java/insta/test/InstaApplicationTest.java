package insta.test;

import insta.dom.Kommentti;
import insta.dom.Kuva;
import insta.serv.KayttajaService;
import insta.serv.KuvaService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
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
    @Autowired
    private KuvaService kuvaService;
    private MockMvc mockMvc;
    
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build();
    }
    
    @Test
    public void testRoot() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
        
        mockMvc.perform(get("/")
                .with(user("taavetti99").password("taavetti99")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
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
        
        mockMvc.perform(formLogin()
                .user(username)
                .password(password))
                .andExpect(unauthenticated());
        
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
    public void testLogoutLoginLogout() throws Exception {
        mockMvc.perform(logout())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(unauthenticated());
        
        mockMvc.perform(formLogin()
                .user("taavetti99")
                .password("taavetti99"))
                .andExpect(authenticated());
        
        mockMvc.perform(logout())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(unauthenticated());
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

//    @Test
//    public void testAddImage() throws Exception {
//        MockMultipartFile multipartFile = new MockMultipartFile("file", "aarrggghh.gif", "image/gif", "aarrggghh".getBytes());
//        
//        mockMvc.perform(fileUpload("/home")
//                .file(multipartFile)
//                .with(csrf())
//                .with(user("taavetti99").password("taavetti99")))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/home"));
//    }
    @Test
    public void testViewPic() throws Exception {
        Kuva kuva = kuvaService.haeKaikki().get(0);
        
        mockMvc.perform(get("/pic/" + kuva.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
        
        MvcResult res = mockMvc.perform(get("/pic/" + kuva.getId())
                .with(user("taavetti99").password("taavetti99")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("kuva"))
                .andReturn();
        
        assertEquals("pic", res.getModelAndView().getViewName());
        
        Kuva i = (Kuva) res.getModelAndView().getModel().get("kuva");
        
        Assert.assertArrayEquals(kuva.getSisalto(), i.getSisalto());
    }
    
    @Test
    public void testComment() throws Exception {
        String kayttaja = "taavetti99";
        String teksti = UUID.randomUUID().toString();
        Kuva kuva = kuvaService.haeKaikki().get(0);
        
        mockMvc.perform(post("/pic/" + kuva.getId())
                .with(user(kayttaja).password("taavetti99"))
                .with(csrf())
                .param("kommentti", teksti))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pic/" + kuva.getId()));
        
        MvcResult res = mockMvc.perform(get("/pic/" + kuva.getId())
                .with(user(kayttaja).password("taavetti99")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("kuva"))
                .andReturn();
        
        Kuva i = (Kuva) res.getModelAndView().getModel().get("kuva");
        
        boolean t = false;
        
        for (Kommentti kommentti : i.getKommentit()) {
            if (kommentti.getSisalto().equals(teksti) && kommentti.getKayttaja().getKayttajanimi().equals(kayttaja)) {
                t = true;
            }
        }
        
        assertTrue(t);
    }
}