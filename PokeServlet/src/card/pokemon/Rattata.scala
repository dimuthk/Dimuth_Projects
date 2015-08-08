package card.pokemon

import org.json.JSONObject

import json.Identifier

/**
 * @author dimuth
 */
class Rattata extends PokemonCard(
    "Rattata",
    "Rattata-Base-Set-61.jpg",
    30){
  
  override def getIdentifier() = Identifier.RATTATA
  
}