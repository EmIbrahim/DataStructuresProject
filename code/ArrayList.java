package music;

public class ArrayList<T extends Comparable<T>>  {
    T[] arr;
    int currIndex;
    int arraysize = 5;
    
   ArrayList(){
        arr = (T[])new Comparable[5];
        currIndex = -1;
    }
    ArrayList(int size){
        arr = (T[])new Object[size];
        currIndex = -1;
    }
    @Override
    public String toString(){
    String str="";
    for(int i=0; i<arr.length;i++){
    str=str+arr[i]+"\n";}
    return str;
    }
    
    public boolean isEmpty(){
        return currIndex ==-1;
    }
    
    public void add(T a){
        if(currIndex >= arr.length-1){
            resize();
        }
        
            currIndex++;
            arr[currIndex] = a;
  
    }
    
    public void clear(){
        currIndex = -1;
        for(int i = currIndex+1; i<arr.length; i++){
        arr[i] = null;
    }
    }
    public int Length(){
        return currIndex+1;
    }
    
    public T get(int index) throws Exception{
        if(index>=0 && index <=currIndex){
            return arr[index];
        }
        else{
        throw new Exception("Index out of bounds");
    }
    }
    
    public int Find(T value){
       
        for(int i=0; i<=currIndex; i++){
            if(arr[i].compareTo(value) ==0){
                return i;
            }
        }
        return -1;
    }
    public void Update(T oldValue, T newValue) {
        int index = Find(oldValue);
        arr[index]  = newValue;
    }
    public void Remove(T value){
        int index = Find(value);
        arr[index] = null;
           
        }
    
    public void resize() {
    arraysize *= 1.3;
    T[] newarr = (T[]) new Comparable[arraysize];
    
    
    for (int i = 0; i < arr.length; i++) {
        newarr[i] = arr[i];
    };
    
    
    arr = newarr;
}

    public void addAll(ArrayList<T> list) throws Exception {
    for (int i = 0; i < list.Length(); i++) {
        add(list.get(i));
    }
}
}
