package insta.repo;

import insta.dom.Tunniste;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TunnisteRepository extends JpaRepository<Tunniste, Long> {
    public List<Tunniste> findAllByOrderByNimi();
    public Tunniste findByNimi(String nimi);
}