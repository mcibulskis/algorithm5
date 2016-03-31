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
  class StubbedJPClustering(expectedClusters: Map[ItemID, ClusterID]) extends JPClustering {
    override def isCloseNeighbors(item1: (ItemID, NeighborList), item2: (ItemID, NeighborList)): Boolean = {
      expectedClusters.get(item1._1) == expectedClusters.get(item2._1)
    }
  }

  def fakeNeighborInformation(expectedClusters: Map[ItemID, ClusterID]) = {
    new InMemoryNeighborInformation(expectedClusters.keys.foldLeft(Map[ItemID, NeighborList]())((aggregate, itemID) => {aggregate ++ Map((itemID, List("Fake", "Neighbor", "List")))}))
  }

  "JPClustering.cluster()" should "create no clusters when there is no neighbor information" in {
    def target = new JPClustering

    target.cluster(new InMemoryNeighborInformation(Map())).size should be (0)
  }

  it should "create no clusters when provided a null neighbor information is provided" in {
    def target = new JPClustering

    target.cluster(null).size should be (0)
  }

  it should "create a singleton cluster when there is a single item" in {
    def expectedClusters = Map(("A", 1))
    def target = new StubbedJPClustering(expectedClusters)

    def clusters = target.cluster(fakeNeighborInformation(expectedClusters))
    clusters.size should be(1)
  }

  it should "create a singleton cluster containing the item when there is a single item" in {
    def expectedClusters = Map(("A", 1))
    def target = new StubbedJPClustering(expectedClusters)

    def clusters = target.cluster(fakeNeighborInformation(expectedClusters))
    def cluster = clusters.head
    cluster.size should be (1)
    cluster should contain("A")
  }

  it should "create singleton clusters for each singleton item when there are multiple singletons" in {
    def expectedClusters = Map(("A", 1), ("B", 2))
    def target = new StubbedJPClustering(expectedClusters)

    def clusters = target.cluster(fakeNeighborInformation(expectedClusters))
    clusters.size should be (2)
  }

  it should "create singleton clusters containing each singleton when there are multiple singletons" in {
    def expectedClusters = Map(("A", 1), ("B", 2))
    def target = new StubbedJPClustering(expectedClusters)

    def clusters = target.cluster(fakeNeighborInformation(expectedClusters))
    clusters.foreach(cluster => {
      cluster.size should be (1)
    })
    def singletonItems = clusters.foldLeft(List[ItemID]())((aggregate, cluster) => {aggregate ++ List(cluster.head)})
    singletonItems.size should be (2)
    singletonItems should contain ("A")
    singletonItems should contain ("B")
  }

//  ignore should "create a single cluster when all items are each others' neighbors and are within the threshold of minimum number of common neighbors" in {
//    def target = new JPClustering(4, 3)
//
//    def clusters = target.cluster(singleClusterNeighborInfo)
//    clusters.size should be (1)
//  }
//
//  ignore should "create a cluster containing all items when all items are each others' neighbors and are within the threshold of minimum number of common neighbors" in {
//    def target = new JPClustering(4, 3)
//
//    def clusters = target.cluster(singleClusterNeighborInfo)
//    def cluster = clusters.head
//    cluster.size should be(5)
//    singleClusterNeighborInfo.items.foreach(itemID => cluster should contain (itemID))
//  }
//
//  ignore should "exclude items from a cluster when the threshold of minimum number of common neighbors is not met" in {
//    def target = new JPClustering(3, 3)
//
//    def clusters = target.cluster(singleClusterNeighborInfo)
//    def cluster = clusters.head
//    cluster.size should be(4)
//    cluster should not contain("E")
//  }


  "JPClustering.isCloseNeighbors()" should "return true when both items are in each others' neighbors lists" in {
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
