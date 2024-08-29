package uk.gov.ons.addressIndex.server.modules.presetClassificationFilters

import nl.gn0s1s.requests.searches.queries.PrefixQuery

object CommercialClassificationFilter extends PresetClassificationFilter {

  override val queryFilter = Seq(PrefixQuery("classificationCode", "C"))

}
