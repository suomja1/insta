package insta.serv;

import insta.dom.Kayttaja;
import insta.repo.KayttajaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class KayttajaService {
    @Autowired
    private KayttajaRepository kayttajaRepository;
    
    public Kayttaja tamaKayttaja() {
        return this.kayttajaRepository.findByKayttajanimi(SecurityContextHolder.getContext().getAuthentication().getName());
    }
    
    public Kayttaja hae(Long id) {
        return this.kayttajaRepository.findOne(id);
    }
    
    public Kayttaja hae(String nimi) {
        return this.kayttajaRepository.findByKayttajanimi(nimi);
    }
    
    public void tallenna(Kayttaja kayttaja) {
        kayttajaRepository.save(kayttaja);
    }
    
    public List<Kayttaja> haeKaikkiNimittain() {
        return kayttajaRepository.findAllByOrderByKayttajanimi();
    }
}