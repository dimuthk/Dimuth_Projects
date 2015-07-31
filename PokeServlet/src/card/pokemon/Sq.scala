package card.pokemon

import card.Card
import org.json.JSONObject
import json.JSONSchema


import json.Jsonable

class Sq(val maxHp : Int) extends Card("Sq", "I") {
  def makeJson() = "HERES A STRING"
  
  var currHp : Int = 5
  
  override def setJsonValues(json : JSONObject) {
    /*currHp = json.get(JSONSchema.CURR_HP.toString()) match {
      case hp : Integer => hp
      case _ => throw new ClassCastException()
    }*/
  }

  override def toJsonImpl() = new JSONObject()
      .put(JSONSchema.DISPLAY_NAME.toString(), displayName)
      .put(JSONSchema.MAX_HP.toString(), maxHp)
      .put(JSONSchema.CURR_HP.toString(), 3)
      
      def getIdentifier() = null
  
}