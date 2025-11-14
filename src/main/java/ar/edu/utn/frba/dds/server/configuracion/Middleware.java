package ar.edu.utn.frba.dds.server.configuracion;

import ar.edu.utn.frba.dds.server.configuracion.autorizacion.Rol;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;


public class Middleware implements WithSimplePersistenceUnit {
  public Middleware() {}

  public void orquestarBefore(Context ctx) {
    entityManager().clear();
    DecodedJWT jwt = ctx.appData(AppKeys.AUTENTICADOR).verificarFirma(ctx);
    Map<String, Object> model = extraerClaimsDeRenderizado(jwt);
    this.definirAtributosDelContex(ctx, model, jwt);
  }

  private Map<String, Object> extraerClaimsDeRenderizado(DecodedJWT jwt) {
    Map<String, Object> model = new HashMap<>();

    if(jwt == null) {
      model.put(AppKeys.AUTENTICADO, false);
      model.put(AppKeys.ESADMIN, false);
    }  else {
      model.put(AppKeys.AUTENTICADO, true);
      model.put(AppKeys.ESADMIN, jwt.getClaim(AppKeys.ROL).asString().equals(Rol.ADMINISTRADOR.toString()));
    }

    return model;
  }

  private void definirAtributosDelContex(Context ctx, Map<String, Object> model, DecodedJWT jwt) {

    ctx.attribute(AppKeys.MODEL, model);
    if(jwt == null) {
      ctx.attribute(AppKeys.ROL, Rol.USUARIO);
    } else {
      ctx.attribute(AppKeys.ROL, jwt.getClaim(AppKeys.ROL).as(Rol.class));
    }

  }
}
