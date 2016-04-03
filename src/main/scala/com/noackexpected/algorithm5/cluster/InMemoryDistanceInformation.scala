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
class InMemoryDistanceInformation(distances: Set[Distance]) extends DistanceInformation {

  override def find(fromItem: ItemID, toItem: ItemID): Double = {
    distances.find((distance: Distance) => directionAgnosticMatchesFromTo((fromItem, toItem), (distance._1, distance._2))).getOrElse((fromItem, toItem, 1.0))._3
  }

  private def directionAgnosticMatchesFromTo(target: (ItemID, ItemID), current: (ItemID, ItemID)): Boolean = {
    matchesFromTo(target, current) || matchesFromTo(target, (current._2, current._1))
  }

  private def matchesFromTo(target: (ItemID, ItemID), current: (ItemID, ItemID)): Boolean = {
    (target._1 == current._1) && (target._2 == current._2)
  }
}
