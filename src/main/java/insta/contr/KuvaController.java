package insta.contr;

import insta.dom.Kayttaja;
import insta.dom.Kuva;
import insta.dom.Tunniste;
import insta.repo.KayttajaRepository;
import insta.repo.KuvaRepository;
import insta.repo.TunnisteRepository;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class KuvaController {
    @Autowired
    private KayttajaRepository kayttajaRepository;
    @Autowired
    private TunnisteRepository tunnisteRepository;
    @Autowired
    private KuvaRepository kuvaRepository;
        
    @RequestMapping(value = "/tag/{id}", method = RequestMethod.GET)
    public String tunnisteittain(Model model, @PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajanimi = auth.getName();
        Tunniste tunniste = tunnisteRepository.findOne(id);
        model.addAttribute("tkayttaja", kayttajaRepository.findByKayttajanimi(kayttajanimi));
        model.addAttribute("tunniste", tunniste);
        model.addAttribute("kuvat", kuvaRepository.findAllByTunnisteet(tunniste));
        return "pics";
    }
    
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public String kayttajittain(Model model, @PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajanimi = auth.getName();
        Kayttaja kayttaja = kayttajaRepository.findOne(id);
        model.addAttribute("tkayttaja", kayttajaRepository.findByKayttajanimi(kayttajanimi));
        model.addAttribute("kayttaja", kayttaja);
        model.addAttribute("kuvat", kuvaRepository.findAllByKayttaja(kayttaja));
        return "pics";
    }
    
    @RequestMapping(value = "pic/{id}/content", method = RequestMethod.GET, produces = "image/jpg")
    @ResponseBody
    public byte[] get(@PathVariable Long id) {
        return kuvaRepository.findOne(id).getSisalto();
    }
    
    @RequestMapping(value = "/home", method = RequestMethod.POST)
    @Transactional
    public String lisaa(@RequestParam("kuva") MultipartFile file,
            @RequestParam(required=false) String kuvateksti) throws IOException {
        if (file.getContentType().contains("image")) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String kayttajanimi = auth.getName();
            Kayttaja kayttaja = kayttajaRepository.findByKayttajanimi(kayttajanimi);
            
            Kuva kuva = new Kuva();
            kuva.setSisalto(file.getBytes());
            kuva.setKuvateksti(kuvateksti);
            kuva.setKayttaja(kayttaja);
            
            kayttaja.getKuvat().add(kuva);
            kuvaRepository.save(kuva);
        }
        
        return "redirect:/home";
    }
}