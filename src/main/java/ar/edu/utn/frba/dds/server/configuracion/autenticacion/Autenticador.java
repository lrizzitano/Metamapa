package ar.edu.utn.frba.dds.server.configuracion.autenticacion;

import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.server.configuracion.AppKeys;
import ar.edu.utn.frba.dds.server.exceptions.SesionInvalidaException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.javalin.http.Context;
import io.javalin.http.Cookie;
import java.time.Instant;
import java.time.temporal.ChronoUnit;



public class Autenticador {

  private final Algorithm algoritmo;

  public Autenticador(Algorithm algoritmo) {

    this.algoritmo = algoritmo;
  }

  public DecodedJWT verificarFirma(Context ctx) {

    var token = ctx.cookie("token");

    if(token == null){
      return null;
    }

    try {
      JWTVerifier verifier = JWT.require(algoritmo)
          .withIssuer("metamapa")
          .acceptLeeway(3600)
          .build();

      return verifier.verify(token);

    } catch (JWTVerificationException exception){

      // para que al reiniciar el servidor, se "limpien" las cookies viejas
      ctx.cookie(this.crearCookie("",0));

      throw new SesionInvalidaException("La informacion de la sesión es invalida", exception);
    }
  }

  public void crearSesion(Usuario usuario, Context ctx) {
    try {
      var token = JWT.create()
          .withIssuer("metamapa")
          .withExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
          .withIssuedAt(Instant.now())
          .withClaim(AppKeys.ROL, usuario.getClass().getSimpleName().toUpperCase())
          .sign(algoritmo);

        ctx.cookie(this.crearCookie(token, 15 * 60));

    } catch (JWTCreationException exception) {
      throw new SesionInvalidaException("Ocurrió un error al generar la sesión", exception);
    }
  }

  private Cookie crearCookie(String token, int age) {
    Cookie cookie = new Cookie("token", token);
    cookie.setMaxAge(age);      // 15 minutos
    cookie.setPath("/");            // disponible en toda la app
    cookie.setHttpOnly(true);       // bloquea acceso desde JS
    cookie.setSecure(false);        // en true, la cookie solo se envia por protocolo https
    return cookie;
  }
}
