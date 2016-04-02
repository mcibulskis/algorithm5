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
}
