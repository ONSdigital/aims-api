package uk.gov.ons.addressIndex.server.modules

trait VersionModule {
  def apiVersion: String

  def dataVersion: String

  def termsAndConditions: String

  def epochList: List[String]

  def epochDates: Map[String,String]
}
