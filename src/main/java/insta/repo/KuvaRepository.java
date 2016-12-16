package insta.repo;

import insta.dom.Kayttaja;
import insta.dom.Kuva;
import insta.dom.Tunniste;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KuvaRepository extends JpaRepository<Kuva, Long> {
    public List<Kuva> findAllByKayttaja(Kayttaja kayttaja);
    public List<Kuva> findAllByTunnisteet(Tunniste tunniste);
}