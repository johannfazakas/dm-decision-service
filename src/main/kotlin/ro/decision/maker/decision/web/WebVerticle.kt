package ro.decision.maker.decision.web

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext

class WebVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>) {
    val router = createRouter()

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(9020) { http ->
        if (http.succeeded()) {
          startPromise.complete()
          println("HTTP server started on port 8888")
        } else {
          startPromise.fail(http.cause());
        }
      }
  }

  private fun createRouter(): Router = Router.router(vertx).apply {
    get("/decisions")
      .handler { getDecisions(it) }
    get("/decisions/:id")
      .handler { getDecision(it) }
    post("/decisions")
      .handler { createDecision(it) }
    delete("/decisions/:id")
      .handler { deleteDecision(it) }
  }

  private fun getDecisions(routingContext: RoutingContext) {
    routingContext.response().end("here is your list of decisions")
  }

  private fun getDecision(routingContext: RoutingContext) {
    routingContext.response().end("here is the decision with id ${routingContext.pathParam("id")}")
  }

  private fun createDecision(routingContext: RoutingContext) {
    routingContext.response().end("create decision")
  }

  private fun deleteDecision(routingContext: RoutingContext) {
    routingContext.response().end("delete decision with id ${routingContext.pathParam("id")}")
  }
}
