package uk.gov.ons.addressIndex.model.server.response.uprn

import play.api.libs.json.{Format, Json}
import uk.gov.ons.addressIndex.model.server.response.address.AddressResponseAddressNonIDS

/**
  * Contains relevant information to the requested address
  *
  * @param addresses array of addresses
  */
case class AddressByMultiUprnResponse(addresses: Seq[AddressResponseAddressNonIDS],
                                      historical: Boolean,
                                      epoch: String,
                                      verbose: Boolean,
                                      pafdefault: Boolean)


object AddressByMultiUprnResponse {
  implicit lazy val addressByMultiUprnResponseFormat: Format[AddressByMultiUprnResponse] = Json.format[AddressByMultiUprnResponse]
}
