package insta.dom;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Kuva extends AbstractPersistable<Long> {
    private byte[] sisalto;
    private String kuvateksti;
    @ManyToOne
    private Kayttaja kayttaja;
    @OneToMany(mappedBy = "kuva")
    private List<Kommentti> kommentit;
    @ManyToMany
    private List<Tunniste> tunnisteet;

    public byte[] getSisalto() {
        return this.sisalto;
    }

    public void setSisalto(byte[] sisalto) {
        this.sisalto = sisalto;
    }

    public Kayttaja getKayttaja() {
        return kayttaja;
    }

    public void setKayttaja(Kayttaja kayttaja) {
        this.kayttaja = kayttaja;
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

    public List<Tunniste> getTunnisteet() {
        if (this.tunnisteet == null) {
            this.tunnisteet = new ArrayList<>();
        }

        return this.tunnisteet;
    }

    public void setTunnisteet(List<Tunniste> tunnisteet) {
        this.tunnisteet = tunnisteet;
    }

    public String getKuvateksti() {
        return kuvateksti;
    }

    public void setKuvateksti(String kuvateksti) {
        this.kuvateksti = kuvateksti;
    }
}