package card.pokemon

import org.json.JSONObject

import json.Identifier

class Machop extends PokemonCard(
    "Machop",
    "Machop-Base-Set-52.jpg",
    50){
  
  override def getIdentifier() = Identifier.MACHOP
  
}