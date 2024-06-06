package music;

public class BST<T extends Comparable<T>>  implements Comparable<BST<T>>{
    Node<T> root;

    public void insert(T key) {
        Node<T> newNode = new Node<>(key);
        if (root == null) {
            root = newNode;
        } else {
         Node<T> current = root;
            while (true) {
                if (key.compareTo(current.data) < 0) {
                    if (current.left == null) {
                        current.left = newNode;
                        break;
                    }
                    current = current.left;
                } else {
                    if (current.right == null) {
                        current.right = newNode;
                        break;
                    }
                    current = current.right;
                }
            }
        }
    }

    public Node<T> getRoot() {
        return root;
    }

    public void LNR(Node<T> n) {
        if (n != null) {
            LNR(n.left);
            System.out.print(n.data + " ");
            LNR(n.right);
        }
    }

    public void LRN(Node<T> n) {
        if (n != null) {
            LRN(n.left);
            LRN(n.right);
            System.out.print(n.data + " ");
        }
    }

    public Node<T> find(T key) {
        Node<T> current = root;
        while (current != null) {
            int cmp = key.compareTo(current.data);
            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    public Node<T> Minimum() {
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    public Node<T> Maximum() {
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current;
    }
    public void delete(T key) {
    root = deleteNode(root, key);
}

private Node<T> deleteNode(Node<T> root, T key) {
    if (root == null) {
        return root;
    }

   
    int cmp = key.compareTo(root.data);
    if (cmp < 0) {
        root.left = deleteNode(root.left, key);
    } else if (cmp > 0) {
        root.right = deleteNode(root.right, key);
    } else {
        if (root.left == null) {
            return root.right;
        } else if (root.right == null) {
            return root.left;
        }

        root.data = minValue(root.right);

        root.right = deleteNode(root.right, root.data);
    }

    return root;
}

    private T minValue(Node<T> root) {
    T minValue = root.data;
    while (root.left != null) {
        minValue = root.left.data;
        root = root.left;
    }
    return minValue;
}
    public ArrayList<T> inOrderTraversal() {
        ArrayList<T> result = new ArrayList<>();
        inOrderTraversal(root, result);
        return result;
    }

    private void inOrderTraversal(Node<T> root, ArrayList<T> result) {
        if (root != null) {
            inOrderTraversal(root.left, result);
            result.add(root.data);
            inOrderTraversal(root.right, result);
        }
    }
    
    @Override
    public int compareTo(BST<T> otherBST) {
        T minValueThis = this.Minimum().data;
        T minValueOther = otherBST.Minimum().data;
        return minValueThis.compareTo(minValueOther);
    }
    
}
