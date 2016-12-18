package insta.contr;

import insta.dom.Kayttaja;
import insta.dom.Tunniste;
import insta.repo.KayttajaRepository;
import insta.repo.KommenttiRepository;
import insta.repo.KuvaRepository;
import insta.repo.TunnisteRepository;
import insta.serv.KuvaService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private KuvaService kuvaService;

    @RequestMapping(value = "/tag/{id}", method = RequestMethod.GET)
    public String tunnisteittain(Model model, @PathVariable Long id) {
        Tunniste tunniste = tunnisteRepository.findOne(id);
        model.addAttribute("tkayttaja", kayttajaRepository.findByKayttajanimi(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("tunniste", tunniste);
        model.addAttribute("kuvat", kuvaRepository.findAllByTunnisteet(tunniste));
        return "pics";
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public String kayttajittain(Model model, @PathVariable Long id) {
        Kayttaja kayttaja = kayttajaRepository.findOne(id);
        model.addAttribute("tkayttaja", kayttajaRepository.findByKayttajanimi(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("kayttaja", kayttaja);
        model.addAttribute("kuvat", kuvaRepository.findAllByKayttaja(kayttaja));
        return "pics";
    }

    @RequestMapping(value = "/pic/{id}", method = RequestMethod.GET)
    public String yksittain(Model model, @PathVariable Long id) {
        model.addAttribute("tkayttaja", kayttajaRepository.findByKayttajanimi(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("kuva", kuvaRepository.findOne(id));
        return "pic";
    }

    @RequestMapping(value = "pic/{id}/content", method = RequestMethod.GET, produces = "image/jpg")
    @ResponseBody
    public byte[] get(@PathVariable Long id) {
        return kuvaRepository.findOne(id).getSisalto();
    }

    @RequestMapping(value = "/pic/{id}", method = RequestMethod.POST)    
    public String kommentoi(@PathVariable Long id, @RequestParam String kommentti) {
        kuvaService.kommentoi(
                kayttajaRepository.findByKayttajanimi(SecurityContextHolder.getContext().getAuthentication().getName()),
                id,
                kommentti
        );
        return "redirect:/pic/" + id;
    }

    @RequestMapping(value = "/home", method = RequestMethod.POST)
    @Transactional
    public String lisaaKuva(@RequestParam("kuva") MultipartFile file,
            @RequestParam(required = false) String kuvateksti,
            @RequestParam(required = false) String tunnisteet) {
        kuvaService.lisaaKuva(
                kayttajaRepository.findByKayttajanimi(SecurityContextHolder.getContext().getAuthentication().getName()),
                file,
                kuvateksti,
                tunnisteet
        );
        return "redirect:/home";
    }
}