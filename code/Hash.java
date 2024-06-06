
package music;


public class Hash<K extends Comparable<K>, V> {
    private static final int TABLE_SIZE = 100;

    private ArrayList<Pair<K, V>>[] table;

    public ArrayList<Pair<K, V>>[] getTable() {

        return table;
    }

    public Hash() {
        table = (ArrayList<Pair<K, V>>[]) new ArrayList[TABLE_SIZE];
        for (int i = 0; i < TABLE_SIZE; i++) {
            table[i] = new ArrayList<>();
        }
    }

    private int hashFunction(K key) {
    int hash = key.hashCode();
    return (hash < 0 ? -hash : hash) % TABLE_SIZE;
}

    public void put(K key, V value) {
        int index = hashFunction(key);
        table[index].add(new Pair<>(key, value));
    }

    public V get(K key) throws Exception {
        int index = hashFunction(key);
        ArrayList<Pair<K, V>> bucket = table[index];
        for (int i = 0; i < bucket.Length(); i++) {
            Pair<K, V> pair = bucket.get(i);
            if (pair.getKey().equals(key)) {
                return pair.getValue();
            }
        }
        return null; 
    }

    public boolean containsKey(K key) throws Exception {
        int index = hashFunction(key);
        ArrayList<Pair<K, V>> bucket = table[index];
        for (int i = 0; i < bucket.Length(); i++) {
            Pair<K, V> pair = bucket.get(i);
            if (pair.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

   public boolean remove(K key) throws Exception {
    int index = hashFunction(key);
    ArrayList<Pair<K, V>> bucket = table[index];
    
    for (int i = 0; i < bucket.Length(); i++) {
        Pair<K, V> pair = bucket.get(i);
        if (pair.getKey().equals(key)) {
            bucket.Remove(pair); 
            return true;
        }
    }

    return false; 
}
    
}
