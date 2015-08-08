package card

import json.Identifier
import org.json.JSONObject

/**
 * Used to represent a "nocard" in json. State objects on the server-side will
 * simply process no_cards as None, and add a placeholder to represent the None
 * in json.
 */
class Placeholder extends Card("PlaceHolder", "NO_IMG") {
  
  override def toJsonImpl() = new JSONObject()
  
  override def setJsonValues(json : JSONObject) = {}
  
  override def getIdentifier() = Identifier.PLACEHOLDER
  
}