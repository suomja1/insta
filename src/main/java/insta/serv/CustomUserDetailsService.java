package insta.serv;

import insta.dom.Kayttaja;
import insta.repo.KayttajaRepository;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private KayttajaRepository kayttajaRepository;

    @Override
    public UserDetails loadUserByUsername(String kayttajanimi) throws UsernameNotFoundException {
        Kayttaja kayttaja = kayttajaRepository.findByKayttajanimi(kayttajanimi);
        if (kayttaja == null) {
            throw new UsernameNotFoundException("Ei käyttäjää: " + kayttajanimi);
        }
        
        return new org.springframework.security.core.userdetails.User(
                kayttaja.getKayttajanimi(),
                kayttaja.getSalasana(),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority("USER")));
    }
}
