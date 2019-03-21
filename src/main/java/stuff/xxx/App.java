package stuff.xxx;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.servlet.ServletInputStream;
import org.apache.commons.io.IOUtils;
import org.checkerframework.common.reflection.qual.GetMethod;
import spark.Spark;

public class App {

  public static void main(String[] args) {

    Spark.port(8080);
    
    Spark.get("/", (request, response) -> "Hello world " + new Date());
    
    Spark.get("/health", (request, response) -> "ok");
    
    Spark.post("/", (request, response) -> {
      
      String data = null;
      
      try { 
        data = IOUtils.toString(request.raw().getInputStream(), StandardCharsets.UTF_8);
        System.out.println(data);
      }
      catch(Exception e) {
        System.out.println("Failure reading inputstream : " + e.getMessage());
      }
      
      return "Server says : " + data;
      
    });
  }
}
