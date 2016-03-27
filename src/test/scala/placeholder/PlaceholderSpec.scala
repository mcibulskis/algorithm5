package placeholder

import org.scalatest._

class PlaceholderSpec extends FlatSpec with Matchers {
  "Placeholder" should "act as a placeholder within git to ensure directory structures are persisted" in {
    def placeholder = new Placeholder()

    placeholder.something() should be (true)
  }
}

