package bsep.pkiapp.security.util;

import bsep.pkiapp.model.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


@Component
@Slf4j
public class TokenUtils {

    @Value("PKI")
    private String APP_NAME;


    @Value("somesecret")
    public String SECRET;


    @Value("1800000")
    private int EXPIRES_IN;


    @Value("Authorization")
    private String AUTH_HEADER;


    private static final String AUDIENCE_WEB = "web";


    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;


    /**
     * Funkcija za generisanje JWT tokena.
     *
     * @param username Korisničko ime korisnika kojem se token izdaje
     * @return JWT token
     */
    public String generateToken(String username, String role, List<String> authorities) {
        log.debug("GT U: [{}], R: [{}], AS: {}", username, role, authorities);
        Date exp_date = new Date(new Date().getTime() + EXPIRES_IN);
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(username)
                .claim("role", role)
                .claim("authorities", authorities)
                .setExpiration(exp_date)
                .setAudience(generateAudience())
                .setIssuedAt(new Date())
                .signWith(SIGNATURE_ALGORITHM, SECRET).compact();

    }

    /**
     * Funkcija za utvrđivanje tipa uređaja za koji se JWT kreira.
     *
     * @return Tip uređaja.
     */
    private String generateAudience() {


        return AUDIENCE_WEB;
    }

    /**
     * Funkcija generiše datum do kog je JWT token validan.
     *
     * @return Datum do kojeg je JWT validan.
     */
    private Date generateExpirationDate(Boolean isRefreshToken) {
        Date date;
        if (isRefreshToken.equals(true)) {
            date = new Date(new Date().getTime() + 3600000);
        } else {
            date = new Date(new Date().getTime() + EXPIRES_IN);
        }
        return date;
    }


    /**
     * Funkcija za preuzimanje JWT tokena iz zahteva.
     *
     * @param request HTTP zahtev koji klijent šalje.
     * @return JWT token ili null ukoliko se token ne nalazi u odgovarajućem
     * zaglavlju HTTP zahteva.
     */
    public String getToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);


        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    /**
     * Funkcija za preuzimanje vlasnika tokena (korisničko ime).
     *
     * @param token JWT token.
     * @return Korisničko ime iz tokena ili null ukoliko ne postoji.
     */
    public String getEmailFromToken(String token) {
        log.debug("GEFT");
        String username;

        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            username = claims.getSubject();
            log.debug("GEFT U: {}", username);
        } catch (ExpiredJwtException ex) {
            log.warn("GEFT TE");
            throw ex;
        } catch (Exception e) {
            log.warn("GEFT FAILED");
            e.printStackTrace();
            username = null;
        }

        return username;
    }

    /**
     * Funkcija za preuzimanje role vlasnika tokena.
     *
     * @param token JWT token.
     * @return Rola iz tokena ili null ukoliko ne postoji.
     */
    public String getRoleFromToken(String token) {
        log.warn("GRFT");
        String role;

        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            log.debug("GRFT U: {}", claims.getSubject());
            role = claims.get("role", String.class);
        } catch (ExpiredJwtException ex) {
            log.warn("GRFT TE");
            throw ex;
        } catch (Exception e) {
            log.warn("GRFT FAILED");
            e.printStackTrace();
            role = null;
        }

        return role;
    }

    /**
     * Funkcija za preuzimanje datuma kreiranja tokena.
     *
     * @param token JWT token.
     * @return Datum kada je token kreiran.
     */
    public Date getIssuedAtDateFromToken(String token) {
        Date issueAt;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            issueAt = claims.getIssuedAt();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            issueAt = null;
        }
        return issueAt;
    }

    /**
     * Funkcija za preuzimanje informacije o uređaju iz tokena.
     *
     * @param token JWT token.
     * @return Tip uredjaja.
     */
    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            audience = claims.getAudience();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    /**
     * Funkcija za preuzimanje datuma do kada token važi.
     *
     * @param token JWT token.
     * @return Datum do kojeg token važi.
     */
    public Date getExpirationDateFromToken(String token) {
        log.warn("GEDFT");
        Date expiration;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            log.warn("GEDFT U: {}", claims.getSubject());
            expiration = claims.getExpiration();
        } catch (ExpiredJwtException ex) {
            log.warn("GEDFT TE");
            throw ex;
        } catch (Exception e) {
            log.warn("GEDFT FAILED");
            e.printStackTrace();
            expiration = null;
        }

        return expiration;
    }

    /**
     * Funkcija za čitanje svih podataka iz JWT tokena
     *
     * @param token JWT token.
     * @return Podaci iz tokena.
     */
    private Claims getAllClaimsFromToken(String token) {
        log.warn("GACT");
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            log.warn("GACT TE");
            throw ex;
        } catch (Exception e) {
            log.warn("GACT FAILED");
            e.printStackTrace();
            claims = null;
        }


        return claims;
    }


    /**
     * Funkcija za validaciju JWT tokena.
     *
     * @param token       JWT token.
     * @param userDetails Informacije o korisniku koji je vlasnik JWT tokena.
     * @return Informacija da li je token validan ili ne.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        log.debug("VT U: {}", userDetails.getUsername());
        final String username = getEmailFromToken(token);
        return (username != null && username.equals(userDetails.getUsername()));
    }

    /**
     * Funkcija proverava da li je lozinka korisnika izmenjena nakon izdavanja
     * tokena.
     *
     * @param created           Datum kreiranja tokena.
     * @param lastPasswordReset Datum poslednje izmene lozinke.
     * @return Informacija da li je token kreiran pre poslednje izmene lozinke ili
     * ne.
     */
    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    /**
     * Funkcija za preuzimanje perioda važenja tokena.
     *
     * @return Period važenja tokena.
     */
    public int getExpiredIn() {
        return EXPIRES_IN;
    }

    /**
     * Funkcija za preuzimanje sadržaja AUTH_HEADER-a iz zahteva.
     *
     * @param request HTTP zahtev.
     * @return Sadrzaj iz AUTH_HEADER-a.
     */
    public String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }

    public Boolean isTokenExpired(String token) {
        Date date = getExpirationDateFromToken(token);
        return date.before(new Date());
    }
}