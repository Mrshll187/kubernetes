package stuff.xxx;

import java.lang.reflect.Method;
import java.util.Arrays;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class Test {

  public static void main(String...strings) throws Exception {
    
    JsonObject json = new JsonObject();
    
    Method method = Arrays
    .stream(Class.forName("stuff.xxx.Test").getMethods())
    .filter(m->m.getName().equalsIgnoreCase("addField"))
    .findFirst()
    .orElse(null);
    
    Object value = method.invoke(null, json, "hello", "hello-value");
    JsonObject updated = JsonObject.class.cast(value);
    
    System.out.println(updated.toString());
  }
  
  public static JsonObject addField(JsonObject json, String jsonPath, Object value) {
    
    DocumentContext context = JsonPath.parse(json.toString());
    JsonXPath xpath = new JsonXPath(jsonPath);
    
    String path = xpath.getPath();
    String key = xpath.getKey();
    
    context.put(path, key, value);
    
    String jsonString = context.jsonString();
    
    return new JsonParser().parse(jsonString).getAsJsonObject();
  }
  
  public static class JsonXPath {

    private String path;
    
    public JsonXPath(String jsonPath) {
      this.path = jsonPath;
    }
    
    public String getKey() {
      return Arrays
      .stream(path.split("\\."))
      .reduce((first, second) -> first)
      .get();
    }
    
    public String getPath() {   
      if(path.split("\\.").length==1)
        return "$";
      else
        return "$." + path.replace(getKey() + ".", "");
    }
  }
}
