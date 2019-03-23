package stuff.xxx;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.logging.Logger;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;

public class Database {

  private static Logger logger = Logger.getLogger(Database.class.getName());
  
  private static Database instance;
  private static RocksDB db;
  
  public static synchronized Database getInstance() {
    
    if(instance == null)
      instance = new Database();
    
    return instance;
  }
  
  private Database() {
    
    RocksDB.loadLibrary();

    ClassLoader classLoader = getClass().getClassLoader();
    URL resource = classLoader.getResource("db");
    File file = new File(resource.getFile());
    
    try (@SuppressWarnings("resource")
    final Options options = new Options().setCreateIfMissing(true)) {
      
      db = RocksDB.open(options, file.getAbsolutePath());
    } 
    catch (RocksDBException e) {
      
      logger.severe(e.getMessage());
      System.exit(1);
    }
  }
  
  public void put(String key, String data) throws RocksDBException {
     db.put(key.getBytes(), data.getBytes());
  }
  
  public String get(String id) throws RocksDBException {
    
    byte[] data = db.get(id.getBytes());
    
    try {
      return new String(data);
    }
    catch(Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }
  
  public LinkedList<Tuple> getAll() throws RocksDBException {
    
    LinkedList<Tuple> list = new LinkedList<Tuple>();
    
    RocksIterator iter = db.newIterator();
    iter.seekToFirst();
    
    while (iter.isValid()) {
        
      String key = new String(iter.key(), StandardCharsets.UTF_8);  
      String value = new String(iter.value(), StandardCharsets.UTF_8);
        
      list.add(new Tuple(key, value));
      
      iter.next();
    }
    
    return list;
  }
  
  public boolean exists(String id) throws RocksDBException {
    return db.get(id.getBytes()) != null;
  }
  
  @SuppressWarnings("deprecation")
  public void remove(String id) throws RocksDBException {
    db.remove(id.getBytes());
  }
  
  public static class Tuple {
    
    public String key;
    public String value;
    
    public Tuple() {}
    
    public Tuple(String key, String value) {
      this.key = key;
      this.value = value;
    }
    
    public String getKey() {
      return key;
    }
    
    public void setKey(String key) {
      this.key = key;
    }
    
    public String getValue() {
      return value;
    }
    
    public void setValue(String value) {
      this.value = value;
    }
  }
}
