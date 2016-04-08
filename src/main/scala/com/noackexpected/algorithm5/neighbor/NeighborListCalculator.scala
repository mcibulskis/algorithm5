package com.noackexpected.algorithm5.neighbor

import com.noackexpected.algorithm5.distance.DistanceInformation
import com.noackexpected.algorithm5.item.ItemID

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
object NeighborListCalculator {
  val DefaultNumNeighbors = 20
}

class NeighborListCalculator(distanceInformation: DistanceInformation, numNeighbors: Int = NeighborListCalculator.DefaultNumNeighbors) {

  def calculate(forItemID: ItemID): NeighborList = {
    distanceInformation.findAll(forItemID).toSeq.sortBy(_._3).slice(0,numNeighbors).map({
      case (from, to, _) if from == forItemID => to
      case (from, _, _) => from
    }).toList
  }

  def calculateAll(neighborInformation: NeighborInformation): NeighborInformation = {
    rCalculateAll(neighborInformation, distanceInformation.items.toList)
  }

  def rCalculateAll(neighborInformation: NeighborInformation, itemsToProcess: List[ItemID]): NeighborInformation = {
    itemsToProcess match {
      case item :: tail => rCalculateAll(neighborInformation + (item, calculate(item)), tail)
      case _ => neighborInformation
    }
  }
}
