package com.noackexpected.algorithm5.cluster

import org.scalatest._

/**
 * Copyright 2016 Michael J. Cibulskis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class JPClusteringSpec extends FlatSpec with Matchers {
  def singleClusterNeighborInfo = new InMemoryNeighborInformation(
    Map(
      ("A", List("B", "C", "D", "E")),
      ("B", List("A", "C", "D", "E")),
      ("C", List("A", "B", "D", "E")),
      ("D", List("A", "B", "C", "E")),
      ("E", List("A", "B", "C", "D"))
    ))

  "JPClustering.cluster()" should "create no clusters when there is no neighbor information" in {
    def target = new JPClustering

    target.cluster(new InMemoryNeighborInformation(Map())).size should be (0)
  }

  it should "create no clusters when provided a null neighbor information is provided" in {
    def target = new JPClustering

    target.cluster(null).size should be (0)
  }

  it should "create a single cluster when all items are each others' neighbors and are within the threshold of minimum number of common neighbors" in {
    def target = new JPClustering

    def clusters = target.cluster(singleClusterNeighborInfo)
    clusters.size should be (1)
  }

  it should "create a cluster containing all items when all items are each others' neighbors and are within the threshold of minimum number of common neighbors" in {
    def target = new JPClustering

    def clusters = target.cluster(singleClusterNeighborInfo)
    def cluster = clusters.head
    cluster.size should be(5)
    singleClusterNeighborInfo.items.foreach(itemID => cluster should contain (itemID))
  }

  ignore should "exclude items from a cluster when the threshold of minimum number of common neighbors is not met" in {
    def target = new JPClustering(3, 3)

    def clusters = target.cluster(singleClusterNeighborInfo)
    def cluster = clusters.head
    cluster.size should be(4)
    cluster should not contain("E")
  }


  "JPClustering.isCloseNeighbors()" should "return true when both items are in each others' neighbors lists" in {
    def target = new JPClustering
    def item1 = ("A", List("B"))
    def item2 = ("B", List("A"))

    target.isCloseNeighbors(item1, item2) should be (true)
  }

  it should "return false when item1 is not in item2's neighbors list" in {
    def target = new JPClustering
    def item1 = ("A", List("B"))
    def item2 = ("B", List("C"))

    target.isCloseNeighbors(item1, item2) should be (false)
  }
}
