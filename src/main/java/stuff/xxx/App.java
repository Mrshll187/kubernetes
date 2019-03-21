package stuff.xxx;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.servlet.ServletInputStream;
import org.apache.commons.io.IOUtils;
import spark.Spark;

public class App {

  public static void main(String[] args) {

    Spark.port(8080);
    Spark.get("/", (request, response) -> "Hello world " + new Date());
    Spark.get("/health", (request, response) -> "ok");
    Spark.post("/", (request, response) -> {
      
      String data = IOUtils.toString(request.raw().getInputStream(), StandardCharsets.UTF_8);
      System.out.println(data);
      
      return "Server says : " + data;
    });
  }
}
