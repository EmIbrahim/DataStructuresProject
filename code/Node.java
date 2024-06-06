package music;


public class Node<T extends Comparable<T>> {
        T data;
        Node<T> next;
        Node<T> prev;
        Node<T> left; 
        Node<T> right;

        public Node(T data) {
            this.data = data;
        }
        
        public T getData(){
            return data;
        }
        public Node<T> getNext(){
            return next;
        }
        public Node<T> getPrev(){
            return prev;
        }
    }
