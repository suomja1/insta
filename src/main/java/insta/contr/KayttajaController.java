package insta.contr;

import insta.dom.Kayttaja;
import insta.repo.KayttajaRepository;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KayttajaController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private KayttajaRepository kayttajaRepository;
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute Kayttaja kayttaja, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "login";
        }
        
        String salasana = kayttaja.getSalasana();
        kayttaja.setSalasana(passwordEncoder.encode(salasana));
        kayttajaRepository.save(kayttaja);
        
        return "redirect:/login";
    }
}