package br.com.cosmodev.sgp_api_dto_exceptions.service;

import br.com.cosmodev.sgp_api_dto_exceptions.model.UsuarioSistema;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

// Classe responsável pela lógica de criação e autenticação de tokens a partir de um UsuarioSistema.

@Service
public class JwtService {

    // Busca o token secret lá no applicationproprerties, para nao deixar a chave aqui exposta no codigo
    @Value("${api.security.token.secret}")
    private String secret;

    // Busca o tempo de vida do token lá no applicationproperties
    @Value("${api.security.token.expiration}")
    private Long expiration;


    // Cria um token
    public String gerarToken (UsuarioSistema usuario) {

        return Jwts.builder().subject(usuario.getEmail())
                .claim("role", usuario.getRole().name()) // role
                .issuedAt(new Date()) // data de criação
                .expiration(new Date(System.currentTimeMillis() + expiration)) // data de expiração
                .signWith(getSigninKey()) // valida o token com a chave da API
                .compact(); // transforma em String
    }

    // Extrai o email de um token
    public String extrairEmail(String token) {

        return extrairClaims(token).getSubject();

    }

    // Verifica se um token é valido, se o email do token coincide com o e-mail do UsuarioSistema no banco, e se o token
    // está expirado
    public boolean tokenValido(String token, UserDetails userdetails) {

        String email = extrairEmail(token);
        return email.equals(userdetails.getUsername()) && !tokenExpirado(token);

    }

    // Verifica se o token está expirado, testando se o instante de expiração dele já passou
    private boolean tokenExpirado(String token) {

        return extrairClaims(token).getExpiration().before(new Date());
        
    }


    // Esse metodo recebe o token, usa a chave da API para verificar se ele é válido com .verifyWith(getSigninKey())
    // e exrai todas as informações do token no formato de Claims
    private Claims extrairClaims(String token) {

        return Jwts.parser().verifyWith(getSigninKey()).build().parseSignedClaims(token).getPayload();

    }

    // Convertendo a chave da API de String para SecretKey, que é um formato reconhecido pelo SpringSecurity
    private SecretKey getSigninKey() {

        byte[] keyBytes = Decoders.BASE64.decode(Base64.getEncoder().encodeToString(secret.getBytes()));

        return Keys.hmacShaKeyFor(keyBytes);

    }

}
