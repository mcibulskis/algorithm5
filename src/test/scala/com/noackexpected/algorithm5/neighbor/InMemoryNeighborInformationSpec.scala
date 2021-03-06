package com.noackexpected.algorithm5.neighbor

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
class InMemoryNeighborInformationSpec extends FlatSpec with Matchers {

  "isEmpty" should "return true if the neighbors lists is empty" in {
    def target = new InMemoryNeighborInformation(Map())

    target.isEmpty should be (true)
  }

  it should "return false if the neighbors lists is not empty" in {
    def target = new InMemoryNeighborInformation(Map(("A", List("B"))))

    target.isEmpty should be (false)
  }


  "size" should "return 0 if there are no items for which there are neighbors lists" in {
    def target = new InMemoryNeighborInformation(Map())

    target.size should be (0)
  }

  it should "return the number of items for which there are neighbors lists" in {
    def target = new InMemoryNeighborInformation(Map(("A", List("B")), ("B", List("A"))))

    target.size should be (2)
  }


  "items" should "return an empty set if there are no items for which there are neighbors lists" in {
    def target = new InMemoryNeighborInformation(Map())

    target.items should be (Set())
  }

  it should "return the set of items for which there are neighbors lists" in {
    def target = new InMemoryNeighborInformation(Map(("A", List("B")), ("B", List("A"))))

    target.items should be (Set("A", "B"))
  }


  "neighborsOf()" should "return an empty list if no neighbor information is present for the specified item" in {
    def target = new InMemoryNeighborInformation(Map(("A", List("B"))))

    target.neighborsOf("NotPresent") should be (List())
  }

  it should "return the neighbors of the specified item ID" in {
    def target = new InMemoryNeighborInformation(Map(("A", List("B", "C")), ("B", List("A", "C"))))

    target.neighborsOf("B") should be (List("A", "C"))
  }



  "+" should "retain any neighbor information it already had" in {
    def target = new InMemoryNeighborInformation(Map(("A", List("B")), ("B", List("A"))))

    def neighborInformation = target + ("C", List("A"))
    neighborInformation.neighborsOf("A") should be (List("B"))
    neighborInformation.neighborsOf("B") should be (List("A"))
  }

  it should "return a new InMemoryNeighborInformation with the specified NeighborList included" in {
    def target = new InMemoryNeighborInformation(Map(("A", List("B")), ("B", List("A"))))

    def neighborInformation = target + ("C", List("A"))
    neighborInformation.neighborsOf("C") should be (List("A"))
  }
}
