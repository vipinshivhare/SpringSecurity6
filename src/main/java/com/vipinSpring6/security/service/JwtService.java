package com.vipinSpring6.security.service;

import com.vipinSpring6.security.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
// payload data means Claims

    private String secretKey = null;

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts
                .builder()
                .claims()
                .add(claims)
                .subject(user.getUsername())
                .issuer("vipin")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ 60*10*1000))
                .and()
                .signWith(generateKey() )
                .compact();
    }

    private SecretKey generateKey(){
        byte[] decode = Decoders.BASE64.decode(getSecretKey());
        return Keys.hmacShaKeyFor(decode);

    }

    public String getSecretKey(){
        return secretKey="674e40a187cbb526669ce8683342d36f49b462e047b5e5a0db940b3b7a1892ef4bca0daa";
    }


    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);// yaha token means userName hai and yha claims se subject milega
    }


    private <T> T extractClaims(String token, Function<Claims,T> claimsResolver) {
        Claims claims = extractClaims(token);// here will get all claims
        return claimsResolver.apply(claims);

    }

    private Claims extractClaims(String token) {// this return claims || get claims from the token
        return Jwts
                .parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public boolean isTokenValid(String token, UserDetails userDetails) {// we need to get the username from the token
        // and match it from the userdetails and also check that the token is expired or not  usernames

        final String userName = extractUserName(token);// will get the userName from this

        return (userName.equals(userDetails.getUsername()) && !isTokenExpire(token));// checking ussername is same and checking the token is valid or not
    }

    private boolean isTokenExpire(String token) {// for check expire need to extract the expiration time
        return extractExpiration(token).before(new Date());// get expiratioon date and time
    }

    private Date extractExpiration(String token) {// if expiration date&time is more than its time so its expired
        return extractClaims(token, Claims::getExpiration);
    }
}
