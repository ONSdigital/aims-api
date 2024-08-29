package uk.gov.ons.addressIndex.server.model.dao

import nl.gn0s1s.ElasticClient

/**
  * Provides access to Elastic client
  */

trait ElasticClientProvider {

  /**
    * Defines a getter for Elastic client
    *
    * @return
    */
  def client: ElasticClient

  /**
    * Defines a getter for Elastic client lite
    * Currently used for GCP deployments. Internally the API gateway determines the cluster to use.
    *
    * @return
    */
  def clientFullmatch: ElasticClient

  /**
    * Defines a getter for Elastic limited access Census client
    * Available on network only if key matches
    *
    * @return
    */
  def clientSpecialCensus: ElasticClient
}
