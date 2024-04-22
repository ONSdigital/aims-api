package uk.gov.ons.addressIndex.model.server.response.rh

import play.api.libs.json.{Format, Json}
import uk.gov.ons.addressIndex.model.server.response.address.{AddressResponseError, AddressResponseStatus}

/**
  * Contains the reply for address by uprn request
  *
  * @param apiVersion  version of the API used for the response
  * @param dataVersion version of the address data used for the response
  * @param response    found content
  * @param status      response status / message
  * @param errors      encountered errors (or an empty list if there is no errors)
  */
case class AddressByRHUprnResponseContainer(apiVersion: String,
                                            dataVersion: String,
                                            response: AddressByRHUprnResponse,
                                            status: AddressResponseStatus,
                                            errors: Seq[AddressResponseError] = Seq.empty[AddressResponseError])


object AddressByRHUprnResponseContainer {
  implicit val addressByRHUprnResponseContainerFormat: Format[AddressByRHUprnResponseContainer] =
    Json.format[AddressByRHUprnResponseContainer]
}