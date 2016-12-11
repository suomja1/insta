package insta.repo;

import insta.dom.Kommentti;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KommenttiRepository extends JpaRepository<Kommentti, Long> {
}