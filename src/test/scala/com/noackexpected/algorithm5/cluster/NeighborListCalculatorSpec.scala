package com.noackexpected.algorithm5.cluster

import org.scalatest.{Matchers, FlatSpec}

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

    target.calculate("A") should be(List("C", "B", "D", "E"))
  }
}
