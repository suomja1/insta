package insta.contr;

import insta.dom.Kayttaja;
import insta.repo.KayttajaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/create")
public class KayttajaController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private KayttajaRepository kayttajaRepository;
    
    @RequestMapping(method = RequestMethod.GET)
    public String view(Model model) {
        return "create";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestParam String kayttajanimi, @RequestParam String salasana) {
        Kayttaja kayttaja = new Kayttaja();
        kayttaja.setKayttajanimi(kayttajanimi);
        kayttaja.setSalasana(passwordEncoder.encode(salasana));
        kayttaja = kayttajaRepository.save(kayttaja);
        return "redirect:/login";
    }
}