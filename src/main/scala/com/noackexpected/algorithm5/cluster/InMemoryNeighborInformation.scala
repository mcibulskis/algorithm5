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
class InMemoryNeighborInformation(neighborLists: NeighborLists) extends NeighborInformation {
  override def isEmpty: Boolean = neighborLists.isEmpty

  override def size: Int = neighborLists.size

  override def items: Set[ItemID] = neighborLists.keySet

  override def neighborsOf(itemID: ItemID): NeighborList = neighborLists.getOrElse(itemID, List())
}
