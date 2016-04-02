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
class InMemoryNeighborInformationSpec extends FlatSpec with Matchers {

  "isEmpty()" should "return true if the neighbors lists is empty" in {
    def target = new InMemoryNeighborInformation(Map())

    target.isEmpty should be (true)
  }

  it should "return false if the neighbors lists is not empty" in {
    def target = new InMemoryNeighborInformation(Map(("A", List("B"))))

    target.isEmpty should be (false)
  }

  it should "return true if the neighbors lists is null" in {
    def target = new InMemoryNeighborInformation(null)

    target.isEmpty should be (true)
  }


  "size" should "return 0 if there are no items for which there are neighbors lists" in {
    def target = new InMemoryNeighborInformation(Map())

    target.size should be (0)
  }

  it should "return 0 if the neighbor lists is null" in {
    def target = new InMemoryNeighborInformation(null)

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

  it should "return an empty set if the neighbors lists is null" in {
    def target = new InMemoryNeighborInformation(null)

    target.items should be (Set())
  }

  it should "return the set of items for which there are neighbors lists" in {
    def target = new InMemoryNeighborInformation(Map(("A", List("B")), ("B", List("A"))))

    target.items should be (Set("A", "B"))
  }
}
