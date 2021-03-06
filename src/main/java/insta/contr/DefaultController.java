package insta.contr;

import insta.dom.Kayttaja;
import insta.dom.Kuva;
import insta.dom.Tunniste;
import insta.repo.TunnisteRepository;
import insta.serv.KayttajaService;
import insta.serv.KuvaService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DefaultController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private KayttajaService kayttajaService;
    @Autowired
    private TunnisteRepository tunnisteRepository;
    @Autowired
    private KuvaService kuvaService;

    @PostConstruct
    @Transactional
    public void init() {
        if (kayttajaService.hae("taavetti99") != null) {
            return;
        }

        try {
            Kayttaja kayttaja = new Kayttaja();
            kayttaja.setKayttajanimi("taavetti99");
            kayttaja.setSalasana(passwordEncoder.encode("taavetti99"));
            Kuva kuva = new Kuva();
            Path polku = Paths.get("src/main/resources/static/images/P6101609.JPG");
            kuva.setSisalto(Files.readAllBytes(polku));
            kuva.setKuvateksti("Mmhmm...");
            Tunniste tunniste = new Tunniste();
            tunniste.setNimi("arvostelemunillallinen");
            kayttaja.getKuvat().add(kuva);
            kuva.setKayttaja(kayttaja);
            kuva.getTunnisteet().add(tunniste);
            tunniste.getKuvat().add(kuva);
            tunnisteRepository.save(tunniste);
            kayttajaService.tallenna(kayttaja);
            kuvaService.tallenna(kuva);

            kayttaja = new Kayttaja();
            kayttaja.setKayttajanimi("_miukumauku_");
            kayttaja.setSalasana(passwordEncoder.encode("sssalasanass"));
            kuva = new Kuva();
            polku = Paths.get("src/main/resources/static/images/P4100070.JPG");
            kuva.setSisalto(Files.readAllBytes(polku));
            kuva.setKuvateksti("Raidalla on pitkät viikset.");
            tunniste = new Tunniste();
            tunniste.setNimi("kissakuvat");
            kayttaja.getKuvat().add(kuva);
            kuva.setKayttaja(kayttaja);
            kuva.getTunnisteet().add(tunniste);
            tunniste.getKuvat().add(kuva);
            tunnisteRepository.save(tunniste);
            kayttajaService.tallenna(kayttaja);
            kuvaService.tallenna(kuva);
        } catch (IOException ex) {
        }
    }

    @RequestMapping(value = "*", method = RequestMethod.GET)
    public String welcome() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/home";
        }

        return "redirect:/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(@ModelAttribute Kayttaja kayttaja) {
        return "login";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("tkayttaja", kayttajaService.tamaKayttaja());
        model.addAttribute("kayttajat", kayttajaService.haeKaikkiNimittain());
        model.addAttribute("tunnisteet", tunnisteRepository.findAllByOrderByNimi());
        return "index";
    }
}