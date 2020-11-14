package ro.decision.maker.decision

import io.vertx.core.AbstractVerticle
import io.vertx.core.AsyncResult
import io.vertx.core.Promise
import ro.decision.maker.decision.web.WebVerticle

class MainVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>) {
    vertx.deployVerticle(WebVerticle()) { handleWebVerticleDeployment(it, startPromise) }
  }

  private fun handleWebVerticleDeployment(webVerticleDeployment: AsyncResult<String>, startPromise: Promise<Void>): Unit = if (webVerticleDeployment.succeeded()) {
    println("web verticle deployment succeeded")
    startPromise.complete()
  } else {
    println("web verticle deployment failed")
    startPromise.fail(webVerticleDeployment.cause())
  }
}
