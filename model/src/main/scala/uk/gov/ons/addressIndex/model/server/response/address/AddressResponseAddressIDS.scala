package uk.gov.ons.addressIndex.model.server.response.address

import play.api.libs.json.{Format, Json}
import uk.gov.ons.addressIndex.model.db.index.HybridAddress

/**
  * Contains address information retrieved in ES (PAF or NAG)
  *
  * @param addressEntryId             uprn equivalent
  * @param confidenceScore  score from elastic search / Hopper / Sigmoid formula
  *
  */
case class AddressResponseAddressIDS(addressEntryId: String,
                                     confidenceScore: Double
                                 )

object AddressResponseAddressIDS {
  implicit lazy val addressResponseAddressIDSFormat: Format[AddressResponseAddressIDS] = Json.format[AddressResponseAddressIDS]

  /**
    * Transforms hybrid object returned by ES into an Address that will be in the json response
    *
    * @param other HybridAddress from ES
    * @return
    */
  def fromHybridAddress(other: HybridAddress): AddressResponseAddressIDS = {

    AddressResponseAddressIDS(
      addressEntryId = other.addressEntryId,
      confidenceScore = 100D,
    )
  }
}




