package com.noackexpected.algorithm5.cluster

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
class JPClustering(numNearestNeighborsToExamine: Int = 20, numRequiredCommonNeighbors: Int = 15) {

  def cluster(neighborInformation: NeighborInformation): Set[Cluster] = {
    if (neighborInformation== null || neighborInformation.isEmpty) Set()
    else Set(neighborInformation.items)
  }

  def isCloseNeighbors(item1: (ItemID, NeighborList), item2: (ItemID, NeighborList)): Boolean = {
    def itemID1 = item1._1
    def itemID2 = item2._1
    def neighborsToExamine1 = item1._2.slice(0, numNearestNeighborsToExamine)
    def neighborsToExamine2 = item2._2.slice(0, numNearestNeighborsToExamine)

    itemsAreNeighborsOfEachOther(itemID1, neighborsToExamine1, itemID2, neighborsToExamine2) && itemsHaveSufficientNeighborsInCommon(neighborsToExamine1, neighborsToExamine2, numRequiredCommonNeighbors - 1)
  }

  private def itemsAreNeighborsOfEachOther(itemID1: ItemID, neighborsToExamine1: NeighborList, itemID2: ItemID, neighborsToExamine2: NeighborList): Boolean = {
    neighborsToExamine2.contains(itemID1) && neighborsToExamine1.contains(itemID2)
  }

  private def itemsHaveSufficientNeighborsInCommon(neighborsToExamine1: NeighborList, neighborsToExamine2: NeighborList, threshold: Int): Boolean = {
    (neighborsToExamine1.intersect(neighborsToExamine2).size >= threshold)
  }
}
