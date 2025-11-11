package ar.edu.utn.frba.dds.server.configuracion.autenticacion;

import com.auth0.jwt.algorithms.Algorithm;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class FactoryAutenticador {

  public FactoryAutenticador() {}

  public Autenticador crearAutenticador() {
    try {
      KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
      generator.initialize(2048); // tamaño de clave razonable
      KeyPair keyPair = generator.generateKeyPair();

      var clavePrivada = (RSAPrivateKey) keyPair.getPrivate();
      var clavePublica = (RSAPublicKey) keyPair.getPublic();

      return new Autenticador(Algorithm.RSA256(clavePublica, clavePrivada));
    } catch (NoSuchAlgorithmException e) {
      // hoy en dia todas las compus tienen ese algoritmo (creo)
    }
    return null;
  }
}

