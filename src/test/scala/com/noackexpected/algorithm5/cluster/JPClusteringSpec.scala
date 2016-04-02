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

  type ClusterID = Int
  class StubbedJPClustering(expectedClustering: Map[ItemID, ClusterID]) extends JPClustering {
    override def isCloseNeighbors(item1: (ItemID, NeighborList), item2: (ItemID, NeighborList)): Boolean = {
      expectedClustering.get(item1._1) == expectedClustering.get(item2._1)
    }
  }

  def fakeNeighborInformation(expectedClustering: Map[ItemID, ClusterID]) = {
    new InMemoryNeighborInformation(expectedClustering.keys.foldLeft(Map[ItemID, NeighborList]())((aggregate, itemID) => {aggregate ++ Map((itemID, List("Fake", "Neighbor", "List")))}))
  }

  "cluster()" should "create no clusters when there is no neighbor information" in {
    def target = new JPClustering

    target.cluster(new InMemoryNeighborInformation(Map())).size should be (0)
  }

  it should "create no clusters when provided a null neighbor information is provided" in {
    def target = new JPClustering

    target.cluster(null).size should be (0)
  }

  it should "create a singleton cluster when there is a single item" in {
    def expectedClustering = Map(("A", 1))
    def target = new StubbedJPClustering(expectedClustering)

    def clusters = target.cluster(fakeNeighborInformation(expectedClustering))
    clusters.size should be(1)
  }

  it should "create a singleton cluster containing the item when there is a single item" in {
    def expectedClustering = Map(("A", 1))
    def target = new StubbedJPClustering(expectedClustering)

    def clusters = target.cluster(fakeNeighborInformation(expectedClustering))
    def cluster = clusters.head
    cluster.size should be (1)
    cluster should contain("A")
  }

  it should "create singleton clusters for each singleton item when there are multiple singletons" in {
    def expectedClustering = Map(("A", 1), ("B", 2))
    def target = new StubbedJPClustering(expectedClustering)

    def clusters = target.cluster(fakeNeighborInformation(expectedClustering))
    clusters.size should be (2)
  }

  it should "create singleton clusters containing each singleton when there are multiple singletons" in {
    def expectedClustering = Map(("A", 1), ("B", 2))
    def target = new StubbedJPClustering(expectedClustering)

    def clusters = target.cluster(fakeNeighborInformation(expectedClustering))
    clusters.foreach(cluster => {
      cluster.size should be (1)
    })
    def singletonItems = clusters.foldLeft(List[ItemID]())((aggregate, cluster) => {aggregate ++ List(cluster.head)})
    singletonItems.size should be (2)
    singletonItems should contain ("A")
    singletonItems should contain ("B")
  }

  it should "create a single cluster with both items if they are both in each others' neighbors list and meet the threshold of minimum number of common neighbors" in {
    def expectedClustering = Map(("A", 1), ("B", 1))
    def target = new StubbedJPClustering(expectedClustering)

    def clusters = target.cluster(fakeNeighborInformation(expectedClustering))
    clusters.size should be (1)
    def cluster = clusters.head
    cluster.size should be (2)
    cluster should contain ("A")
    cluster should contain ("B")
  }

  it should "be able to create multiple multi-item clusters" in {
    def expectedClustering = Map(("A", 1), ("B", 2), ("C", 1), ("D", 3), ("E", 3), ("F", 1), ("G", 3), ("H", 2), ("J", 4))
    def target = new StubbedJPClustering(expectedClustering)

    def clusters = target.cluster(fakeNeighborInformation(expectedClustering))
    clusters.size should be (4)
    clusters.foreach(cluster => {
      if (cluster.contains("A")) {
        cluster.size should be (3)
        cluster should contain ("C")
        cluster should contain ("F")
      } else if (cluster.contains("B")) {
        cluster.size should be (2)
        cluster should contain ("H")
      } else if (cluster.contains("D")) {
        cluster.size should be(3)
        cluster should contain("E")
        cluster should contain("G")
      } else if (cluster.contains("J")) {
        cluster.size should be (1)
      } else {
        fail(s"Unexpected cluster configuration: ${cluster}")
      }
    })
  }

  it should "create chains where some items in a cluster may appear to be unrelated to others in the cluster" in {
    def target = new JPClustering(3, 2)
    def neighborList = new InMemoryNeighborInformation(Map(
      ("A", List("B", "C", "G", "H")),
      ("B", List("A", "C", "D", "J")),
      ("C", List("B", "D", "K", "L")),
      ("D", List("C", "K", "M", "N")),
      ("K", List("D", "M", "P", "Q"))  // K appears to be unrelated to A, but should chain into the same cluster
    ))

    def clusters = target.cluster(neighborList)
    clusters.size should be (1)
    def cluster = clusters.head
    cluster.size should be (5)
    cluster should contain ("A")
    cluster should contain ("B")
    cluster should contain ("C")
    cluster should contain ("D")
    cluster should contain ("K")
  }



  "isCloseNeighbors()" should "return true when both items are in each others' neighbors lists" in {
    def target = new JPClustering(1, 1)
    def item1 = ("A", List("B"))
    def item2 = ("B", List("A"))

    target.isCloseNeighbors(item1, item2) should be (true)
  }

  it should "return false when item1 is not in item2's neighbors list" in {
    def target = new JPClustering(1, 1)
    def item1 = ("A", List("B"))
    def item2 = ("B", List("C"))

    target.isCloseNeighbors(item1, item2) should be (false)
  }

  it should "return false when item2 is not in item1's neighbors list" in {
    def target = new JPClustering(1, 1)
    def item1 = ("A", List("C"))
    def item2 = ("B", List("A"))

    target.isCloseNeighbors(item1, item2) should be (false)
  }

  it should "return false if the minimum number of common neighbors is not met" in {
    def target = new JPClustering(2, 2)
    def item1 = ("A", List("B", "C"))
    def item2 = ("B", List("A", "D"))

    target.isCloseNeighbors(item1, item2) should be (false)
  }

  it should "return true if the minimum number of common neighbors is met" in {
    def target = new JPClustering(3, 2)
    def item1 = ("A", List("B", "D", "C"))
    def item2 = ("B", List("A", "E", "C"))

    target.isCloseNeighbors(item1, item2) should be (true)
  }

  it should "return false if the minimum number of common neighbors is not met within the window of neighbors to examine" in {
    def target = new JPClustering(3, 2)
    def item1 = ("A", List("B", "E", "D", "C"))
    def item2 = ("B", List("A", "G", "F", "C"))

    target.isCloseNeighbors(item1, item2) should be (false)
  }

  it should "return false when item2 is not within the window of neighbors to examine in item1's neighbors list" in {
    def target = new JPClustering(3, 2)
    def item1 = ("A", List("C", "E", "D", "B"))
    def item2 = ("B", List("A", "G", "C", "F"))

    target.isCloseNeighbors(item1, item2) should be (false)
  }

  it should "return false when item1 is not within the window of neighbors to examine in item2's neighbors list" in {
    def target = new JPClustering(3, 2)
    def item1 = ("A", List("B", "E", "C", "D"))
    def item2 = ("B", List("F", "G", "C", "A"))

    target.isCloseNeighbors(item1, item2) should be (false)
  }
}
