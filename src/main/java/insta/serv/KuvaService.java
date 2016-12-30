package insta.serv;

import insta.dom.Kayttaja;
import insta.dom.Kommentti;
import insta.dom.Kuva;
import insta.dom.Tunniste;
import insta.repo.KommenttiRepository;
import insta.repo.KuvaRepository;
import insta.repo.TunnisteRepository;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class KuvaService {
    @Autowired
    private TunnisteRepository tunnisteRepository;
    @Autowired
    private KuvaRepository kuvaRepository;
    @Autowired
    private KommenttiRepository kommenttiRepository;
    
    @Transactional
    public void kommentoi(Kayttaja kayttaja, Long kuva, String kommentti) {
        if (!kommentti.trim().isEmpty()) {
            Kuva ku = kuvaRepository.findOne(kuva);
            Kommentti ko = new Kommentti();
            ko.setSisalto(kommentti);
            ko.setKayttaja(kayttaja);
            ko.setKuva(ku);
            kommenttiRepository.save(ko);
            kayttaja.getKommentit().add(ko);
            ku.getKommentit().add(ko);
        }
    }
    
    @Transactional
    public void lisaaKuva(Kayttaja kayttaja, MultipartFile file, String kuvateksti, String tunnisteet) {
        if (file.getContentType().contains("image")) {
            byte[] sisalto;
            try {
                sisalto = pienenna(file.getBytes());
            } catch (IOException ex) {
                return;
            }
            
            Kuva kuva = new Kuva();
            kuva.setSisalto(sisalto);
            kuva.setKuvateksti(kuvateksti);
            kuva.setKayttaja(kayttaja);

            lisaaTunnisteet(tunnisteet, kuva);

            kayttaja.getKuvat().add(kuva);
            kuvaRepository.save(kuva);
        }
    }

    private void lisaaTunnisteet(String tunnisteet, Kuva kuva) {
        String[] osat = tunnisteet.split(",");
        for (String osat1 : osat) {
            String nimi = osat1.trim().toLowerCase();
            if (!nimi.isEmpty()) {
                lisaaTunniste(nimi, kuva);
            }
        }
    }

    private void lisaaTunniste(String nimi, Kuva kuva) {
        Tunniste tunniste = tunnisteRepository.findByNimi(nimi);
        
        if (tunniste == null) {
            tunniste = new Tunniste();
            tunniste.setNimi(nimi);
            tunnisteRepository.save(tunniste);
        }
        
        tunniste.getKuvat().add(kuva);
        kuva.getTunnisteet().add(tunniste);
    }
    
    private byte[] pienenna(byte[] sisalto) {
        try {
            BufferedImage thumb;
            
            thumb = Scalr.resize(ImageIO.read(new ByteArrayInputStream(sisalto)),
                    Scalr.Method.QUALITY,
                    Scalr.Mode.FIT_TO_WIDTH,
                    512, 512, Scalr.OP_ANTIALIAS);
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(thumb, "png", out);
            return out.toByteArray();
        } catch (Exception ex) {
            return sisalto; // ei kovin siisti ratkaisu, mutta helpottaa testausta...
        }
    }
    
    public List<Kuva> haeKaikki(Tunniste tunniste) {
        return this.kuvaRepository.findAllByTunnisteet(tunniste);
    }
    
    public List<Kuva> haeKaikki(Kayttaja kayttaja) {
        return this.kuvaRepository.findAllByKayttaja(kayttaja);
    }
    
    public List<Kuva> haeKaikki() {
        return this.kuvaRepository.findAll();
    }
    
    public Kuva hae(Long id) {
        return this.kuvaRepository.findOne(id);
    }
    
    public void tallenna(Kuva kuva) {
        kuvaRepository.save(kuva);
    }
}