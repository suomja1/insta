package insta.dom;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Kommentti extends AbstractPersistable<Long> {
    @NotBlank
    private String sisalto;
    @ManyToOne
    private Kayttaja kayttaja;
    @ManyToOne
    private Kuva kuva;
    
    public String getSisalto() {
        return sisalto;
    }

    public void setSisalto(String sisalto) {
        this.sisalto = sisalto;
    }

    public Kayttaja getKayttaja() {
        return kayttaja;
    }

    public void setKayttaja(Kayttaja kayttaja) {
        this.kayttaja = kayttaja;
    }

    public Kuva getKuva() {
        return kuva;
    }

    public void setKuva(Kuva kuva) {
        this.kuva = kuva;
    }
}