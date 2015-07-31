package card

import card.pokemon.Machop

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

/**
 * @author dimuth
 */
class ConstructorTest extends FunSuite {
  
  test("Example") {
    new Machop().toJson()
  }
  
}