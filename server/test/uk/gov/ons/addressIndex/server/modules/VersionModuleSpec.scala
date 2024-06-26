package uk.gov.ons.addressIndex.server.modules

import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.{ElasticClient, ElasticNodeEndpoint, ElasticProperties}
import com.sksamuel.elastic4s.testkit._
import org.scalatest._
import matchers._
import org.scalatest.wordspec.AnyWordSpec
import org.testcontainers.elasticsearch.ElasticsearchContainer
import uk.gov.ons.addressIndex.model.config.AddressIndexConfig
import uk.gov.ons.addressIndex.server.model.dao.ElasticClientProvider

import scala.util.Try

class VersionModuleSpec extends AnyWordSpec with should.Matchers with SearchMatchers with ClientProvider with ElasticSugar {

  val testConfig = new AddressIndexConfigModule

  val container = new ElasticsearchContainer()
  container.setDockerImageName("docker.elastic.co/elasticsearch/elasticsearch-oss:7.3.1")
  container.start()
  val containerHost = container.getHttpHostAddress()
  val host =  containerHost.split(":").headOption.getOrElse("localhost")
  val port =  Try(containerHost.split(":").lastOption.getOrElse("9200").toInt).getOrElse(9200)

  val elEndpoint: ElasticNodeEndpoint = new ElasticNodeEndpoint("http",host,port,None)
  val eProps: ElasticProperties = new ElasticProperties(endpoints = Seq(elEndpoint))

  val client: ElasticClient = new ElasticClient(JavaClient(eProps))
  val testClient = client.copy()

  //  injections
  val elasticClientProvider: ElasticClientProvider = new ElasticClientProvider {
    override def client: ElasticClient = testClient
    /* Not currently used in tests as it doesn't look like you can have two test ES instances */
    override def clientFullmatch: ElasticClient = testClient
    override def clientSpecialCensus: ElasticClient = testClient
  }

  val invalidConfig: ConfigModule = new ConfigModule {
    override def config: AddressIndexConfig = testConfig.config.copy(
      elasticSearch = testConfig.config.elasticSearch.copy(
        indexes = testConfig.config.elasticSearch.indexes.copy(hybridIndex = "invalid")
      )
    )
  }

  val hybridIndex1 = "hybrid_33_202020"
  val hybridIndex2 = "hybrid_34_202020"
  val hybridIndex3 = "hybrid_105_202020"
  val hybridAlias: String = testConfig.config.elasticSearch.indexes.hybridIndex + "_current"
  val firstAlias: String = testConfig.config.elasticSearch.indexes.hybridIndex + "_33"
  val secondAlias: String = testConfig.config.elasticSearch.indexes.hybridIndex + "_34"
  val thirdAlias: String = testConfig.config.elasticSearch.indexes.hybridIndex + "_105"

  def blockUntilCountLocal(expected: Long, index: String): Unit = {
    blockUntil(s"Expected count of $expected") { () =>
      val result = testClient.execute {
        search(index).matchAllQuery().size(0)
      }.await
      expected <= result.toOption.getOrElse(null).totalHits
    }
  }


  testClient.execute(
    bulk(
      indexInto(hybridIndex1) id "11" fields ("name" -> "test1"),
      indexInto(hybridIndex2) id "12" fields ("name" -> "test2"),
      indexInto(hybridIndex3) id "13" fields ("name" -> "test3")
    )
  ).await

  blockUntilCountLocal(1, hybridIndex1)
  blockUntilCountLocal(1, hybridIndex2)
  blockUntilCountLocal(1, hybridIndex3)

  testClient.execute {
    addAlias(hybridAlias, hybridIndex2)
  }.await

  testClient.execute {
       addAlias(firstAlias, hybridIndex1)
  }.await

  testClient.execute {
    addAlias(thirdAlias, hybridIndex3)
  }.await

  "Version module" should {

    "extract all epoch versions from a correct alias->index" in {
      // Given
      val versionModule = new AddressIndexVersionModule(testConfig, elasticClientProvider)
      val expected = List("33","34","105")

      // When
      val result = versionModule.epochList
      // allow epochs to be in any order
      val success = (result.contains("33") && result.contains("34") && result.contains("105"))

      // Then
      success shouldBe true
    }

    "extract default epoch version from a correct alias->index" in {
      // Given
      val versionModule = new AddressIndexVersionModule(testConfig, elasticClientProvider)
      val expected = "34"

      // When
      val result = versionModule.dataVersion

      // Then
      result shouldBe expected
    }

    "not extract epoch version from an incorrect alias" in {
      // Given
      val versionModule = new AddressIndexVersionModule(invalidConfig, elasticClientProvider)
      val expected = "NA"

      // When
      val result = versionModule.dataVersion

      // Then
      result shouldBe expected
    }

    "extract api version from file in resources" in {
      // Given
      val versionModule = new AddressIndexVersionModule(testConfig, elasticClientProvider)
      val expected = "test"

      // When
      val result = versionModule.apiVersion

      // Then
      result shouldBe expected
    }

  }


}

