package ro.decision.maker.decision

import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.AbstractVerticle
import io.vertx.core.AsyncResult
import io.vertx.core.Promise
import io.vertx.core.json.JsonObject

class MainVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>) {

    val configRetriever: ConfigRetriever = getConfigRetriever()
    configRetriever.getConfig { configHandler -> handleConfig(configHandler, startPromise) }
  }

  private fun handleConfig(configHandler: AsyncResult<JsonObject>, startPromise: Promise<Void>) {
    if (configHandler.succeeded()) {
      val config = configHandler.result()
      vertx
        .createHttpServer()
        .requestHandler { req ->
          req.response()
            .putHeader("content-type", "text/plain")
            .end(config.getString("message"))
        }
        .listen(config.getInteger("port")) { http ->
          if (http.succeeded()) {
            startPromise.complete()
            println("HTTP server started on port 8888")
          } else {
            startPromise.fail(http.cause());
          }
        }
    } else {
      startPromise.fail(configHandler.cause())
    }
  }

  private fun getConfigRetriever(): ConfigRetriever {
    val configStoreOptions: ConfigStoreOptions = ConfigStoreOptions()
      .setType("file")
      .setConfig(JsonObject(mapOf("path" to "application.json")))
    val configRetrieverOptions: ConfigRetrieverOptions = ConfigRetrieverOptions().addStore(configStoreOptions)
    return ConfigRetriever.create(vertx, configRetrieverOptions)
  }
}
