package stuff.xxx;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import org.apache.commons.io.IOUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import spark.Spark;
import stuff.xxx.Database.Tuple;

public class App {

  private static Database database = Database.getInstance();
  private static JsonParser parser = new JsonParser();
  private static Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
  
  public static void main(String[] args) {

    Spark.port(8080);
    Spark.get("/health", (request, response) -> "ok");
    
    Spark.get("/list", (request, response) -> {
      
      JsonObject json = new JsonObject();
      
      LinkedList<Tuple> list = database.getAll();
      list.forEach(t -> json.addProperty(t.getKey(), t.getValue()));
      
      return gson.toJson(json);
      
    });
    
    Spark.get("/:id", (request, response) -> {
      
      String id = request.params(":id");
      String entry = database.get(id);
      
      return entry;
      
    });
    
    Spark.delete("/:id", (request, response) -> {
      
      String id = request.params(":id");
      database.remove(id);
      
      return database.exists(id);
      
    });
    
    Spark.post("/", (request, response) -> {
      
      String data = IOUtils.toString(request.raw().getInputStream(), StandardCharsets.UTF_8);
      JsonObject json = parser.parse(data).getAsJsonObject();
      
      String key = json.get("id").getAsString();
      String value = json.get("data").getAsString();
      
      database.put(key, value);
      
      return database.exists(key);
      
    });
  }
}
