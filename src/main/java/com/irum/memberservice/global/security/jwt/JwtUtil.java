package com.irum.memberservice.global.security.jwt;

import static com.irum.memberservice.global.constants.SecurityConstants.TOKEN_ROLE_NAME;

import com.irum.memberservice.domain.auth.dto.request.AccessTokenDto;
import com.irum.memberservice.domain.auth.dto.request.RefreshTokenDto;
import com.irum.memberservice.domain.member.domain.entity.enums.Role;
import com.irum.memberservice.global.infrastructure.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private PrivateKey accessTokenPrivateKey;
    private PublicKey accessTokenPublicKey;

    public AccessTokenDto generateAccessTokenDto(Long memberId, Role authority) {
        Date issuedAt = new Date();
        Date expiredAt =
                new Date(issuedAt.getTime() + jwtProperties.accessTokenExpirationMilliTime());
        String accessTokenValue = buildAccessToken(memberId, authority, issuedAt, expiredAt);
        return AccessTokenDto.of(memberId, authority, accessTokenValue);
    }

    public String generateAccessToken(Long memberId, Role authority) {
        Date issuedAt = new Date();
        Date expiredAt =
                new Date(issuedAt.getTime() + jwtProperties.accessTokenExpirationMilliTime());
        return buildAccessToken(memberId, authority, issuedAt, expiredAt);
    }

    public RefreshTokenDto generateRefreshTokenDto(Long memberId) {
        Date issuedAt = new Date();
        Date expiredAt =
                new Date(issuedAt.getTime() + jwtProperties.refreshTokenExpirationMilliTime());
        String refreshTokenValue = buildRefreshToken(memberId, issuedAt, expiredAt);
        return RefreshTokenDto.of(
                memberId, refreshTokenValue, jwtProperties.refreshTokenExpirationTime());
    }

    public String generateRefreshToken(Long memberId) {
        Date issuedAt = new Date();
        Date expiredAt =
                new Date(issuedAt.getTime() + jwtProperties.refreshTokenExpirationMilliTime());
        return buildRefreshToken(memberId, issuedAt, expiredAt);
    }

    public AccessTokenDto parseAccessToken(String accessTokenValue) throws ExpiredJwtException {
        try {
            Claims claims = parseAccessTokenClaims(accessTokenValue);

            return AccessTokenDto.of(
                    Long.parseLong(claims.getSubject()),
                    Role.valueOf(claims.get(TOKEN_ROLE_NAME, String.class)),
                    accessTokenValue);
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (Exception e) {
            return null;
        }
    }

    public RefreshTokenDto parseRefreshToken(String refreshTokenValue) throws ExpiredJwtException {
        try {
            Claims claims = parseRefreshTokenClaims(refreshTokenValue);

            return RefreshTokenDto.of(
                    Long.parseLong(claims.getSubject()),
                    refreshTokenValue,
                    jwtProperties.refreshTokenExpirationTime());
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (Exception e) {
            return null;
        }
    }

    public long getRemainingExpirationMillis(String tokenValue) {
        Claims claims = parseAccessTokenClaims(tokenValue);
        Date exp = claims.getExpiration();
        return Math.max(exp.getTime() - System.currentTimeMillis(), 0);
    }

    public long getRefreshTokenExpirationTime() {
        return jwtProperties.refreshTokenExpirationTime();
    }

    // Private 헬퍼 메서드들

    private Claims parseAccessTokenClaims(String token) {
        JwtParser parser =
                Jwts.parser()
                        .verifyWith(getAccessTokenPublicKey())
                        .requireIssuer(jwtProperties.issuer())
                        .build();

        return parser.parseSignedClaims(token).getPayload();
    }

    private Claims parseRefreshTokenClaims(String token) {
        JwtParser parser =
                Jwts.parser()
                        .verifyWith(getRefreshTokenKey())
                        .requireIssuer(jwtProperties.issuer())
                        .build();

        return parser.parseSignedClaims(token).getPayload();
    }

    private PrivateKey getAccessTokenPrivateKey() {
        if (accessTokenPrivateKey == null) {
            try {
                String privateKeyPEM =
                        jwtProperties
                                .accessTokenPrivateKey()
                                .replace("-----BEGIN PRIVATE KEY-----", "")
                                .replace("-----END PRIVATE KEY-----", "")
                                .replaceAll("\\s", "");

                byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                accessTokenPrivateKey = keyFactory.generatePrivate(keySpec);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load private key", e);
            }
        }
        return accessTokenPrivateKey;
    }

    private PublicKey getAccessTokenPublicKey() {
        if (accessTokenPublicKey == null) {
            try {
                String publicKeyPEM =
                        jwtProperties
                                .accessTokenPublicKey()
                                .replace("-----BEGIN PUBLIC KEY-----", "")
                                .replace("-----END PUBLIC KEY-----", "")
                                .replaceAll("\\s", "");

                byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                accessTokenPublicKey = keyFactory.generatePublic(keySpec);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load public key", e);
            }
        }
        return accessTokenPublicKey;
    }

    private SecretKey getRefreshTokenKey() {
        return Keys.hmacShaKeyFor(jwtProperties.refreshTokenSecret().getBytes());
    }

    private String buildAccessToken(Long memberId, Role authority, Date issuedAt, Date expiredAt) {
        return Jwts.builder()
                .issuer(jwtProperties.issuer())
                .subject(memberId.toString())
                .claim(TOKEN_ROLE_NAME, authority.name())
                .issuedAt(issuedAt)
                .expiration(expiredAt)
                .signWith(getAccessTokenPrivateKey(), Jwts.SIG.RS256)
                .compact();
    }

    private String buildRefreshToken(Long memberId, Date issuedAt, Date expiredAt) {
        return Jwts.builder()
                .issuer(jwtProperties.issuer())
                .subject(memberId.toString())
                .issuedAt(issuedAt)
                .expiration(expiredAt)
                .signWith(getRefreshTokenKey(), Jwts.SIG.HS512)
                .compact();
    }
}
