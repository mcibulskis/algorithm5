package com.noackexpected.algorithm5.distance

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
class InMemoryDistanceInformationSpec extends FlatSpec with Matchers {

  "find()" should "return 1.0 (maximally different) when no data is available for from/to item combination" in {
    def target = new InMemoryDistanceInformation(Set(("C", "D", 0.125)))

    target.find("A", "B") should be (1.0)
  }

  it should "return the actual distance when data is available for from/to combination" in {
    def target = new InMemoryDistanceInformation(Set(("C", "D", 0.125), ("A", "B", 0.25)))

    target.find("A", "B") should be (0.25)
  }

  it should "return the actual distance when data is available for to/from combination" in {
    def target = new InMemoryDistanceInformation(Set(("C", "D", 0.125), ("B", "A", 0.25)))

    target.find("A", "B") should be (0.25)
  }


  "findAll()" should "return an empty set when no data is available for the target item" in {
    def target = new InMemoryDistanceInformation(Set(("C", "D", 0.125)))

    target.findAll("A") should be (Set())
  }

  it should "return distance information with the target item as the from aspect of the distances" in {
    def target = new InMemoryDistanceInformation(Set(("A", "B", 0.25), ("A", "C", 0.75)))

    target.findAll("A") should be (Set(("A", "B", 0.25), ("A", "C", 0.75)))
  }

  it should "return distance information with the target item as the to aspect of the distances" in {
    def target = new InMemoryDistanceInformation(Set(("B", "A", 0.25), ("C", "A", 0.75)))

    target.findAll("A") should be (Set(("B", "A", 0.25), ("C", "A", 0.75)))
  }


  "items" should "return an empty set when no data is available" in {
    def target = new InMemoryDistanceInformation(Set())

    target.items should be (Set())
  }

  it should "include all of the items that are in the from aspect of the distances" in {
    def target = new InMemoryDistanceInformation(Set(("A", "B", 0.25), ("C", "D", 0.125)))

    target.items should contain ("A")
    target.items should contain ("C")
  }

  it should "include all of the items that are in the to aspect of the distances" in {
    def target = new InMemoryDistanceInformation(Set(("A", "B", 0.25), ("C", "D", 0.125)))

    target.items should contain ("B")
    target.items should contain ("D")
  }
}
