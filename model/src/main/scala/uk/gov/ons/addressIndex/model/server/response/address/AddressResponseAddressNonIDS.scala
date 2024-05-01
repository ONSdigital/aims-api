package uk.gov.ons.addressIndex.model.server.response.address

import play.api.libs.json.{Format, Json}

/**
  * Create the non-IDS (DAP and UI) using fields in the generic response
  *
  * @param uprn                     uprn of matched address
  * @param parentUprn               parent uprn of matched address
  * @param relatives                hierarchy information
  * @param crossRefs                links to other datasets
  * @param formattedAddress         cannonical address form
  * @param formattedAddressNag      cannonical address form
  * @param formattedAddressPaf      cannonical address form
  * @param welshFormattedAddressNag cannonical address form
  * @param welshFormattedAddressPaf cannonical address form
  * @param highlights               search engine "hit" data
  * @param paf                      optional, information from Paf index
  * @param nag                      optional, information from Nag index
  * @param geo                      lat lon and easting northing
  * @param classificationCode       ABP code e.g. RD02 = detached house
  * @param lpiLogicalStatus         ABP logical status of entry e.g. 8=historic
  * @param confidenceScore          bespoke confifence score 0-100
  * @param underlyingScore          score from elastic search
  *
  */
case class AddressResponseAddressNonIDS(uprn: String,
                                        parentUprn: String,
                                        relatives: Option[Seq[AddressResponseRelative]],
                                        crossRefs: Option[Seq[AddressResponseCrossRef]],
                                        formattedAddress: String,
                                        formattedAddressNag: String,
                                        formattedAddressPaf: String,
                                        welshFormattedAddressNag: String,
                                        welshFormattedAddressPaf: String,
                                        highlights: Option[AddressResponseHighlight],
                                        paf: Option[AddressResponsePaf],
                                        nag: Option[Seq[AddressResponseNag]],
                                        geo: Option[AddressResponseGeo],
                                        classificationCode: String,
                                        countryCode:String,
                                        lpiLogicalStatus: String,
                                        confidenceScore: Double,
                                        underlyingScore: Float
                                      )

object AddressResponseAddressNonIDS {
  implicit lazy val addressResponseAddressNonIDSFormat: Format[AddressResponseAddressNonIDS] = Json.format[AddressResponseAddressNonIDS]

  def addressesToNonIDS(normalAddresses: Seq[AddressResponseAddress]): Seq[AddressResponseAddressNonIDS] = {
    normalAddresses.map { address => transformToNonIDS(address) }
  }

  def transformToNonIDS(addressIn: AddressResponseAddress): AddressResponseAddressNonIDS = {
    AddressResponseAddressNonIDS.fromAddress(addressIn)
  }

  def fromAddress(addressIn: AddressResponseAddress): AddressResponseAddressNonIDS = {
    new AddressResponseAddressNonIDS(
      uprn = addressIn.uprn,
      parentUprn = addressIn.parentUprn,
      relatives = addressIn.relatives,
      crossRefs = addressIn.crossRefs,
      formattedAddress = addressIn.formattedAddress,
      formattedAddressNag = addressIn.formattedAddressNag,
      formattedAddressPaf = addressIn.formattedAddressPaf,
      welshFormattedAddressNag = addressIn.welshFormattedAddressNag,
      welshFormattedAddressPaf = addressIn.welshFormattedAddressPaf,
      highlights=  addressIn.highlights,
      paf = addressIn.paf,
      nag = addressIn.nag,
      geo = addressIn.geo,
      classificationCode = addressIn.classificationCode,
      countryCode = addressIn.countryCode,
      lpiLogicalStatus = addressIn.lpiLogicalStatus,
      confidenceScore = addressIn.confidenceScore,
      underlyingScore = addressIn.underlyingScore
    )
  }


}



