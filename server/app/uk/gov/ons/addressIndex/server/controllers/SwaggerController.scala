package uk.gov.ons.addressIndex.server.controllers

import play.api.mvc.ControllerComponents
import play.api.Configuration
import com.iheart.playSwagger.generator.{NamingConvention, SwaggerSpecGenerator}
import play.api.mvc._

import javax.inject.{Inject, Singleton}

@Singleton
class ApiSpecs @Inject()(cc: ControllerComponents, config: Configuration) extends AbstractController(cc) {
  implicit val cl: ClassLoader = getClass.getClassLoader

  val domainPackage = "uk.gov.ons.addressIndex.model.server.response"

  lazy val generator: SwaggerSpecGenerator = SwaggerSpecGenerator(namingConvention = NamingConvention.None, swaggerV3 = true,domainNameSpaces = domainPackage)

// it is possible to modify the swagger output and items can be drawn from config

  lazy val swagger: Action[AnyContent] = Action {
      generator.generate().fold(
      e => InternalServerError("Couldn't generate swagger."),
      s => Ok(s))
  }

  def specs: Action[AnyContent] = swagger
}
