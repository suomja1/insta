package insta.contr;

import insta.dom.Kayttaja;
import insta.repo.KayttajaRepository;
import insta.repo.TunnisteRepository;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("*")
public class DefaultController {
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
        kayttaja.setSalasana("taavetti99");
        kayttaja = kayttajaRepository.save(kayttaja);
        
        kayttaja = new Kayttaja();
        kayttaja.setKayttajanimi("_miukumauku_");
        kayttaja.setSalasana("sssalasanass");
        kayttaja = kayttajaRepository.save(kayttaja);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("kayttajat", kayttajaRepository.findAllByOrderByKayttajanimi());
        model.addAttribute("tunnisteet", tunnisteRepository.findAllByOrderByNimi());
        return "index";
    }
}