package stuff.xxx;

import static org.junit.Assert.fail;
import java.util.stream.IntStream;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.rocksdb.RocksDBException;

public class DatabaseTest {

  private Database database = Database.getInstance();
  
  @Test
  public void testCRUD() throws RocksDBException {
    
    String key = "1";
    String value = "hello";
    
    database.put(key, value);
    Assert.assertTrue(database.exists(key));
    
    String data = database.get(key);
    Assert.assertEquals(value, data);
    
    database.remove(key);;
    Assert.assertFalse(database.exists(key));
    
  }
  
  @Test
  public void testBombardment() throws RocksDBException {
    
    int totalInserts = 100;
    
    IntStream.range(0, totalInserts).forEach(i -> {
      
      String key = Integer.toString(i);
      
      try {
        
        database.put(key, key);
        Assert.assertTrue(database.exists(key));
      }
      catch(Exception e) {
        fail(e.getMessage());
      }
    });
    
    Assert.assertEquals(totalInserts, database.getAll().size());
    Assert.assertEquals("55", Integer.toString(55));
  }
}
