package servlets

import org.apache.commons.lang.StringEscapeUtils;

import java.util.Enumeration;

import java.io.BufferedReader
import player.Player
import org.json.JSONObject
import card.pokemon._

import java.lang.StringBuffer

import javax.servlet._
import javax.servlet.http.{HttpServlet,
  HttpServletRequest => HSReq, HttpServletResponse => HSResp}

/**
 * 
 */
class HandToActive extends HttpServlet {
  override def doPost(req : HSReq, resp : HSResp) = {
    resp.setContentType("application/json")
    
    
   /* val reader : BufferedReader = req.getReader
    val sb : StringBuffer = new StringBuffer()
    
    def read(reader : BufferedReader) {
      val str : String = reader.readLine()
      if (str == null) return
      sb.append(str)
      read(reader)
    }
    read(reader)*/
    
    val pNames : Enumeration[String] = req.getParameterNames()
    while (pNames.hasMoreElements()) {
      val pName = pNames.nextElement()
          System.out.println(pName + ": " + req.getParameter(pName))
    }
    
    

    val writer = resp.getWriter
    
    val p : Player = new Player()
    //p.setJsonValues(new JSONObject(sb.toString()))
    p.hand = List.fill(9)(new Rattata())

    writer.print(p.toJson().toString())
    writer.close()
  }
  
  def parseResponse(req: HSReq) : Player = {
    val p : Player = new Player()
    val p2 : Player = new Player()
    val players : Array[Player] = Array()
    val pNames : Enumeration[String] = req.getParameterNames()
    // Example: DECK[30][CURR_HP] -> 50
    while (pNames.hasMoreElements()) {
      val pName = pNames.nextElement()
      // we need to somehow parse this line...
      var player = players(pName.charAt(6) - 48)
    }
    return p
  }
  
}