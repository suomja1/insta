package insta.dom;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Tunniste extends AbstractPersistable<Long> {
    private String nimi;
    @ManyToMany(mappedBy = "tunnisteet")
    private List<Kuva> kuvat;

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public List<Kuva> getKuvat() {
        if (this.kuvat == null) {
            this.kuvat = new ArrayList<>();
        }
        
        return this.kuvat;
    }

    public void setKuvat(List<Kuva> kuvat) {
        this.kuvat = kuvat;
    }
}