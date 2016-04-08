package com.noackexpected.algorithm5.cluster

import com.noackexpected.algorithm5.item.ItemID
import com.noackexpected.algorithm5.neighbor.{NeighborList, NeighborInformation}

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
object JPClustering {
  val DefaultNumNearestNeighborsToExamine = 20
  val DefaultNumRequiredCommonNeighbors = 15
}

class JPClustering(numNearestNeighborsToExamine: Int = JPClustering.DefaultNumNearestNeighborsToExamine,
                   numRequiredCommonNeighbors: Int = JPClustering.DefaultNumRequiredCommonNeighbors) {

  def cluster(neighborInformation: NeighborInformation): Set[Cluster] = {
    rCluster(neighborInformation, neighborInformation.items, Set[Cluster]())
  }

  private def rCluster(neighborInformation: NeighborInformation, itemsToProcess: Set[ItemID], currentClusters: Set[Cluster]): Set[Cluster] = {
    if (itemsToProcess.isEmpty) {
      currentClusters
    } else {
      rCluster(neighborInformation, itemsToProcess.tail, updateClusters(currentClusters, itemsToProcess.head, neighborInformation))
    }
  }

  private def updateClusters(currentClusters: Set[Cluster], currentItem: ItemID, neighborInformation: NeighborInformation): Set[Cluster] = {
    def singleton = Set(currentItem)
    def belongsToClusters = currentClusters.filter(cluster => belongsToCluster(currentItem, cluster, neighborInformation))
    def doesNotBelongToClusters = currentClusters.diff(belongsToClusters)
    def mergedCluster = belongsToClusters.foldLeft(singleton)((aggregate, cluster) => aggregate ++ cluster)
    Set(mergedCluster) ++ doesNotBelongToClusters
  }

  private def belongsToCluster(item: ItemID, cluster: Cluster, neighborInformation: NeighborInformation): Boolean = {
    def itemWithNeighborInfo = (item, neighborInformation.neighborsOf(item))
    cluster.foldLeft(false)((aggregate, itemToCompare) => {
      aggregate || isCloseNeighbors(itemWithNeighborInfo, (itemToCompare, neighborInformation.neighborsOf(itemToCompare)))
    })
  }

  def isCloseNeighbors(item1: (ItemID, NeighborList), item2: (ItemID, NeighborList)): Boolean = {
    def itemID1 = item1._1
    def itemID2 = item2._1
    def neighborsToExamine1 = item1._2.slice(0, numNearestNeighborsToExamine)
    def neighborsToExamine2 = item2._2.slice(0, numNearestNeighborsToExamine)

    itemsAreNeighborsOfEachOther(itemID1, neighborsToExamine1, itemID2, neighborsToExamine2) &&
      itemsHaveSufficientNeighborsInCommon(neighborsToExamine1, neighborsToExamine2, numRequiredCommonNeighbors - 1)
  }

  private def itemsAreNeighborsOfEachOther(itemID1: ItemID, neighborsToExamine1: NeighborList, itemID2: ItemID, neighborsToExamine2: NeighborList): Boolean = {
    neighborsToExamine2.contains(itemID1) && neighborsToExamine1.contains(itemID2)
  }

  private def itemsHaveSufficientNeighborsInCommon(neighborsToExamine1: NeighborList, neighborsToExamine2: NeighborList, threshold: Int): Boolean = {
    neighborsToExamine1.intersect(neighborsToExamine2).size >= threshold
  }
}
