package insta.dom;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Kayttaja extends AbstractPersistable<Long> {
    @NotBlank
    @Length(min = 4, max = 30)
    private String kayttajanimi;
    @NotBlank
    @Length(min = 6)
    private String salasana;
    @OneToMany(mappedBy = "kayttaja")
    private List<Kuva> kuvat;
    @OneToMany(mappedBy = "kayttaja")
    private List<Kommentti> kommentit;
    
    public String getKayttajanimi() {
        return kayttajanimi;
    }

    public void setKayttajanimi(String kayttajanimi) {
        this.kayttajanimi = kayttajanimi;
    }

    public String getSalasana() {
        return salasana;
    }

    public void setSalasana(String salasana) {
        this.salasana = salasana;
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

    public List<Kommentti> getKommentit() {
        if (this.kommentit == null) {
            this.kommentit = new ArrayList<>();
        }
        
        return this.kommentit;
    }

    public void setKommentit(List<Kommentti> kommentit) {
        this.kommentit = kommentit;
    }
}