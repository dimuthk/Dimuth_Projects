package json

import card.Placeholder
import card.pokemon.PokemonCard
import card.pokemon.Machop

import player.Player

import org.json.JSONObject

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

class JsonableTest extends FunSuite {
  
  test("Test pokemon") {
    val machop = new Machop()
    machop.currHp -= 20
    val machopRT : Machop = new Machop()
    machopRT.setJsonValues(new JSONObject(machop.toJson().toString()))
    assert(machopRT.currHp == machopRT.maxHp - 20)
  }
  
  test("Test player active card") {
    val player = new Player()
    player.active = Some(new Machop())
    val playerRT : Player = new Player()
    playerRT.setJsonValues(new JSONObject(player.toJson().toString()))
    assert(playerRT.active.get.equals(new Machop()))
    playerRT.bench.foreach { x => assert(x == None) }
    playerRT.prizes.foreach { x => assert(x == None) }
    assert(playerRT.garbage.isEmpty)
    assert(playerRT.hand.isEmpty)
    assert(playerRT.deck.isEmpty)
  }
  
  test("Test player bench") {
    val player = new Player()
    val filter = (i : Int) => i ==2 || i == 4
    player.bench = (for(i <- 0 to 4) yield if (filter(i)) Some(new Machop()) else None).toArray
    val playerRT : Player = new Player()
    playerRT.setJsonValues(new JSONObject(player.toJson().toString()))
    assert(playerRT.active.isEmpty)
    for(i <- 0 to 4) {
      val card : Option[PokemonCard] = playerRT.bench(i)
      assert(if (filter(i)) card.get.equals(new Machop()) else card.isEmpty)
    }
    playerRT.prizes.foreach { x => assert(x == None) }
    assert(playerRT.garbage.isEmpty)
    assert(playerRT.hand.isEmpty)
    assert(playerRT.deck.isEmpty)
  }
  
}