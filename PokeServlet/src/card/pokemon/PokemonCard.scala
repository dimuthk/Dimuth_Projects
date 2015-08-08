package card.pokemon

import org.json.JSONObject
import card.Card
import json.Identifier

abstract class PokemonCard(
    displayName : String,
    imgName : String,
    val maxHp : Int) extends Card(displayName, imgName) {

  var currHp : Int = maxHp
  
  override def setJsonValues(json : JSONObject) {
    currHp = json.get(Identifier.CURR_HP.toString()) match {
      case hp : Integer => hp
      case _ => throw new ClassCastException()
    }
  }

  override def toJsonImpl() = new JSONObject()
      .put(Identifier.DISPLAY_NAME, displayName)
      .put(Identifier.MAX_HP, maxHp)
      .put(Identifier.CURR_HP, currHp)
      .put(Identifier.IMG_NAME, imgName)

  
  override def equals(o : Any) = o match {
    case other : PokemonCard => currHp == other.currHp
    case _ => false
  }
  
}