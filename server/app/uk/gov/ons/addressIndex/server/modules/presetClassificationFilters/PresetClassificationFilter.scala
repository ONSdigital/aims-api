package uk.gov.ons.addressIndex.server.modules.presetClassificationFilters

import nl.gn0s1s.requests.searches.queries.Query

trait PresetClassificationFilter {

  val queryFilter: Seq[Query]

}
