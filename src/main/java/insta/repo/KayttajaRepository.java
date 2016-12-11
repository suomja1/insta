package insta.repo;

import insta.dom.Kayttaja;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KayttajaRepository extends JpaRepository<Kayttaja, Long> {
}
