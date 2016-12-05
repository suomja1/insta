package insta.dom;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    
    @NotBlank
    private String kokoNimi;
    
    @NotBlank
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date syntymaAika;

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

    public String getKokoNimi() {
        return kokoNimi;
    }

    public void setKokoNimi(String kokoNimi) {
        this.kokoNimi = kokoNimi;
    }

    public Date getSyntymaAika() {
        return syntymaAika;
    }

    public void setSyntymaAika(Date syntymaAika) {
        this.syntymaAika = syntymaAika;
    }
}