package insta.contr;

import insta.dom.Kayttaja;
import insta.dom.Kommentti;
import insta.dom.Kuva;
import insta.dom.Tunniste;
import insta.repo.KayttajaRepository;
import insta.repo.KommenttiRepository;
import insta.repo.KuvaRepository;
import insta.repo.TunnisteRepository;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
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
    private KommenttiRepository kommenttiRepository;

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
    @Transactional
    public String kommentoi(@PathVariable Long id, @RequestParam String kommentti) {
        Kayttaja ka = kayttajaRepository.findByKayttajanimi(SecurityContextHolder.getContext().getAuthentication().getName());
        Kuva ku = kuvaRepository.findOne(id);

        Kommentti ko = new Kommentti();
        ko.setSisalto(kommentti);
        ko.setKayttaja(ka);
        ko.setKuva(ku);
        kommenttiRepository.save(ko);

        ka.getKommentit().add(ko);
        ku.getKommentit().add(ko);

        return "redirect:/pic/" + id;
    }

    @RequestMapping(value = "/home", method = RequestMethod.POST)
    @Transactional
    public String lisaaKuva(@RequestParam("kuva") MultipartFile file,
            @RequestParam(required = false) String kuvateksti,
            @RequestParam String tunnisteet) {
        if (file.getContentType().contains("image")) {
            byte[] sisalto;
            try {
                sisalto = pienenna(file.getBytes());
            } catch (IOException ex) {
                return "redirect:/home";
            }

            Kayttaja kayttaja = kayttajaRepository.findByKayttajanimi(SecurityContextHolder.getContext().getAuthentication().getName());

            Kuva kuva = new Kuva();
            kuva.setSisalto(sisalto);
            kuva.setKuvateksti(kuvateksti);
            kuva.setKayttaja(kayttaja);

            String[] osat = tunnisteet.split(",");
            for (String osat1 : osat) {
                String nimi = osat1.trim().toLowerCase();
                if (!nimi.isEmpty()) {
                    Tunniste tunniste = tunnisteRepository.findByNimi(nimi);

                    if (tunniste == null) {
                        tunniste = new Tunniste();
                        tunniste.setNimi(nimi);
                        tunnisteRepository.save(tunniste);
                    }

                    tunniste.getKuvat().add(kuva);
                    kuva.getTunnisteet().add(tunniste);
                }
            }

            kayttaja.getKuvat().add(kuva);
            kuvaRepository.save(kuva);
        }

        return "redirect:/home";
    }

    private byte[] pienenna(byte[] sisalto) throws IOException {
        BufferedImage thumb;

        thumb = Scalr.resize(ImageIO.read(new ByteArrayInputStream(sisalto)),
                Scalr.Method.QUALITY,
                Scalr.Mode.FIT_TO_WIDTH,
                512, 512, Scalr.OP_ANTIALIAS);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(thumb, "png", out);
        return out.toByteArray();
    }
}