package insta.repo;

import insta.dom.Kuva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KuvaRepository extends JpaRepository<Kuva, Long> {
}