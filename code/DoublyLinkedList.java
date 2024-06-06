package music;

public class DoublyLinkedList<T extends Comparable<T>> {
    Node<T> head;

    public DoublyLinkedList() {
        head = null;
    }

    public Node<T> getHead() {
        return head;
    }

    public void InsertInOrder(T data) {           //Big O : N
        Node<T> T = head;
        Node<T> N = new Node<>(data);

        if (T == null || data.compareTo(T.data) < 0) {
            if (head != null) {
                T.prev = N;
            }
            N.next = T;
            head = N;
            return;
        }
        while (T.next != null && data.compareTo(T.data) >= 0) {
            T = T.next;
        }
        if (T.next == null) {
            N.prev = T;
            T.next = N;
        } else {
            N.next = T;
            N.prev = T.prev;
            T.prev.next = N;
            T.prev = N;
        }
    }

    public Node<T> Find(T value) {            //Big O : N
        Node<T> curr = head;
        while (curr != null) {
            if (curr.data.equals(value)) {
                return curr;
            }
            curr = curr.next;
        }
        return null;
    }

    public void Delete(T value) {
        Node<T> deleteNode = Find(value);
        if (deleteNode != null) {
            if (deleteNode.prev != null) {
                deleteNode.prev.next = deleteNode.next;
            } else {
                head = deleteNode.next;
            }
            if (deleteNode.next != null) {
                deleteNode.next.prev = deleteNode.prev;
            }
        }
    }

    public boolean contains(T value) {
        Node<T> curr = head;
        while (curr != null) {
            if (curr.data.equals(value)) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    @Override
    public String toString() {            //Big O : N
        String result = "";
        Node<T> curr = head;
        while (curr != null) {
            result += curr.data + ", ";
            curr = curr.next;
        }
        return result;
    }

    public void clearList() {             //BigO: 1
        head = null;
    }

    public boolean isEmpty()              //BigO: 1
    {
        return head == null;
    }

    public int length() {                  //BigO: N
        int count = 0;
        Node<T> curr = head;
        while (curr != null) {
            count++;
            curr = curr.next;
        }
        return count;
    }

    public void ReverseList() {                //BigO: M
        Node<T> curr = head;
        Node<T> temp = null;
        while (curr != null) {
            temp = curr.prev;
            curr.prev = curr.next;
            curr.next = temp;
            curr = curr.prev;
        }
        if (temp != null) {
            head = temp.prev;
        }
    }

    public T get(int index) {
        Node<T> current = head;
        int count = 0;
        while (current != null) {
            if (count == index) {
                return current.getData();
            }
            count++;
            current = current.getNext();
        }
        throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + length());
    }

    public ArrayList<T> searchByArtist(String artist) {
        ArrayList<T> result = new ArrayList<>();
        Node<T> current = head;
        while (current != null) {
            if (((Song) current.getData()).getArtistName().equalsIgnoreCase(artist)) {
                result.add(current.getData());
            }
            current = current.getNext();
        }
        return result;
    }

    public ArrayList<T> searchBySong(String songName) {
        ArrayList<T> result = new ArrayList<>();
        Node<T> current = head;
        while (current != null) {
            if (((Song) current.getData()).getSongName().equalsIgnoreCase(songName)) {
                result.add(current.getData());
            }
            current = current.getNext();
        }
        return result;
    }

    public ArrayList<T> searchByGenre(String genre) {
        ArrayList<T> result = new ArrayList<>();
        Node<T> current = head;
        while (current != null) {
            if (((Song) current.getData()).getGenre().equalsIgnoreCase(genre)) {
                result.add(current.getData());
            }
            current = current.getNext();
        }
        return result;
    }

    public ArrayList<T> searchAll() {
        ArrayList<T> result = new ArrayList<>();
        Node<T> current = head;
        while (current != null) {
            result.add(current.getData());
            current = current.getNext();
        }
        return result;
    }
}
