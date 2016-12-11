package insta.contr;

import insta.dom.Kayttaja;
import insta.dom.Tunniste;
import insta.repo.KayttajaRepository;
import insta.repo.TunnisteRepository;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("*")
public class DefaultController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private KayttajaRepository kayttajaRepository;
    @Autowired
    private TunnisteRepository tunnisteRepository;
    
    @PostConstruct
    public void init() {
        if (kayttajaRepository.findByKayttajanimi("taavetti99") != null) {
            return;
        }
        
        Kayttaja kayttaja = new Kayttaja();
        kayttaja.setKayttajanimi("taavetti99");
        kayttaja.setSalasana(passwordEncoder.encode("taavetti99"));
        kayttaja = kayttajaRepository.save(kayttaja);
        
        kayttaja = new Kayttaja();
        kayttaja.setKayttajanimi("_miukumauku_");
        kayttaja.setSalasana(passwordEncoder.encode("sssalasanass"));
        kayttaja = kayttajaRepository.save(kayttaja);
        
        Tunniste tunniste = new Tunniste();
        tunniste.setNimi("kissakuvat");
        tunniste = tunnisteRepository.save(tunniste);
        
        tunniste = new Tunniste();
        tunniste.setNimi("arvostelemunillallinen");
        tunniste = tunnisteRepository.save(tunniste);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("kayttajat", kayttajaRepository.findAllByOrderByKayttajanimi());
        model.addAttribute("tunnisteet", tunnisteRepository.findAllByOrderByNimi());
        return "index";
    }
    
//    @RequestMapping(value = "/logout", method = RequestMethod.GET)
//    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        return "redirect:/login?logout";
//    }
}