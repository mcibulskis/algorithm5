package com.noackexpected.algorithm5.neighbor

import com.noackexpected.algorithm5.distance.InMemoryDistanceInformation
import com.noackexpected.algorithm5.item.ItemID
import org.scalatest.{FlatSpec, Matchers}

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
class NeighborListCalculatorSpec extends FlatSpec with Matchers {

  "calculate()" should "return an empty neighbor list for an item if there is no distance information" in {
    def target = new NeighborListCalculator(new InMemoryDistanceInformation(Set(("A", "B", 0.25))))

    target.calculate("C") should be (List())
  }

  it should "include neighbors of the target when the target is in the from aspect of the distance information" in {
    def target = new NeighborListCalculator(new InMemoryDistanceInformation(Set(("A", "B", 0.25))))

    target.calculate("A") should be (List("B"))
  }

  it should "include neighbors of the target when the target is in the to aspect of the distance information" in {
    def target = new NeighborListCalculator(new InMemoryDistanceInformation(Set(("B", "A", 0.25))))

    target.calculate("A") should be (List("B"))
  }

  it should "sort neighbors from closest (smallest distance) to farthest (largest distance)" in {
    def target = new NeighborListCalculator(new InMemoryDistanceInformation(Set(("A", "B", 0.25), ("A", "E", 0.75), ("C", "A", 0.125), ("D", "A", 0.5))))

    target.calculate("A") should be (List("C", "B", "D", "E"))
  }

  it should "not include neighbors of items that are not the target item" in {
    def target = new NeighborListCalculator(new InMemoryDistanceInformation(Set(("B", "C", 0.25), ("A", "B", 0.125))))

    target.calculate("A") should be (List("B"))
  }

  it should "only include the closest n neighbors" in {
    def target = new NeighborListCalculator(new InMemoryDistanceInformation(Set(("A", "B", 0.25), ("A", "E", 0.75), ("C", "A", 0.125), ("D", "A", 0.5))), 3)

    target.calculate("A") should be (List("C", "B", "D"))
  }



  "calculateAll" should "return an empty neighbor information if there is no distance information" in {
    def target = new NeighborListCalculator(new InMemoryDistanceInformation(Set()))

    target.calculateAll(new InMemoryNeighborInformation(Map())).isEmpty should be (true)
  }

  it should "include neighbor information for all items for which we have distance information" in {
    def target = new NeighborListCalculator(new InMemoryDistanceInformation(Set(("A", "B", 0.25), ("A", "E", 0.75), ("C", "A", 0.125), ("D", "A", 0.5))))

    target.calculateAll(new InMemoryNeighborInformation(Map())).items should be (Set("A", "B", "C", "D", "E"))
  }

  it should "invoke calculate() for each item for which we have distance information" in {
    def target = new NeighborListCalculator(new InMemoryDistanceInformation(Set(("B", "C", 0.25), ("A", "B", 0.125)))) {
      override def calculate(forItemID: ItemID): NeighborList = List(s"invoked calculate(${forItemID})")
    }

    def neighborInformation = target.calculateAll(new InMemoryNeighborInformation(Map()))
    neighborInformation.neighborsOf("A") should be (List("invoked calculate(A)"))
    neighborInformation.neighborsOf("B") should be (List("invoked calculate(B)"))
    neighborInformation.neighborsOf("C") should be (List("invoked calculate(C)"))
  }

  it should "return an instance of the type of NeighborInformation that was provided" in {
    class StubNeighborInformation(neighborLists: InMemoryNeighborInformation.NeighborLists) extends InMemoryNeighborInformation(neighborLists) {
      override def +(itemNeighbors: (ItemID, NeighborList)): NeighborInformation = {
        new StubNeighborInformation(neighborLists + itemNeighbors)
      }
    }
    def target = new NeighborListCalculator(new InMemoryDistanceInformation(Set(("A", "B", 0.25), ("A", "E", 0.75), ("C", "A", 0.125), ("D", "A", 0.5))))

    target.calculateAll(new StubNeighborInformation(Map())).isInstanceOf[StubNeighborInformation] should be (true)
  }
}
