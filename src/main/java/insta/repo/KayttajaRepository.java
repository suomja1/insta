package insta.repo;

import insta.dom.Kayttaja;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KayttajaRepository extends JpaRepository<Kayttaja, Long> {
    public List<Kayttaja> findAllByOrderByKayttajanimi();
}
