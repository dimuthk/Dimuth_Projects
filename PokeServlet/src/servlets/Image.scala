package servlets

import java.io.File
import javax.servlet._
import javax.servlet.http.{HttpServlet,
  HttpServletRequest => HSReq, HttpServletResponse => HSResp}

/**
 * @author dimuth
 */
class Image extends HttpServlet {
  
  override def doGet(req : HSReq, resp : HSResp) = {
    resp.setContentType("image/jpg")
    val content : ServletContext = getServletContext()
    val path : String = content.getRealPath("/WEB-INF/images/Machop-Base-Set-52.jpg")
    val file : File = new File(path)
    resp.setContentLength(file.length().intValue())
    val in : java.io.FileInputStream = new java.io.FileInputStream(file)
    val out : java.io.OutputStream = resp.getOutputStream()
    val buf : Array[Byte] = Array.fill(1024){0}
    def outputData() : Unit = {
      val cnt = in.read(buf)
      if (cnt < 0) return
      out.write(buf, 0, cnt)
      outputData()
    }
    outputData()
    
    out.close()
    in.close()
  }
}