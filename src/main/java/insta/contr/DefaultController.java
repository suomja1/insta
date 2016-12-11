package insta.contr;

import insta.repo.KayttajaRepository;
import insta.repo.TunnisteRepository;
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
    
    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("kayttajat", kayttajaRepository.findAllByOrderByKayttajanimi());
        model.addAttribute("tunnisteet", tunnisteRepository.findAllByOrderByNimi());
        return "index";
    }
}