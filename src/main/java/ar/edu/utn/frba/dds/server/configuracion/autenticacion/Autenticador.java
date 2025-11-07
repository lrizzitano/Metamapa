package ar.edu.utn.frba.dds.server.configuracion.autenticacion;

import ar.edu.utn.frba.dds.server.configuracion.AppKeys;
import ar.edu.utn.frba.dds.server.exceptions.SesionInvalidaException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.javalin.http.Context;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static ar.edu.utn.frba.dds.server.configuracion.autorizacion.Rol.USUARIO;


public class Autenticador {

  private final Algorithm algoritmo;

  public Autenticador(Algorithm algoritmo) {

    this.algoritmo = algoritmo;
  }

  public DecodedJWT verificarFirma(Context ctx) {

    var token = ctx.header("authorization");

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
      throw new SesionInvalidaException("La informacion de la sesión es invalida", exception);
    }
  }

  public String crearSesion(/*informacion de usuario*/){
    try {

      return JWT.create()
          .withIssuer("metamapa")
          .withExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
          .withIssuedAt(Instant.now())
          .withClaim(AppKeys.ROL, "XXX")
          .sign(algoritmo);

    } catch (JWTCreationException exception){
      throw new SesionInvalidaException("Ocurrio un error al generar la sesión", exception);
    }
  }
}
