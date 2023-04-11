package myprograms.lab06;

public class Tree <T extends Comparable<T>>{
    Node<T> root;

    public Tree() { root = null; }

    public void insert(T value) {
        //root = ins(value, root);
        if(containsElement(value)){return;}

        else {
            if (root == null)
                root = new Node(value);
            else {
                Node actual = root;
                Node parent = null;
                while (actual != null) {
                    parent = actual;
                    actual = (actual.value.compareTo(value) > 0) ? actual.left : actual.right;
                }
                if (parent.value.compareTo(value) > 0) {
                    parent.left = new Node(value);
                    parent.left.parent = parent;
                } else {
                    parent.right = new Node(value);
                    parent.right.parent = parent;
                }
            }
        }
    }


    /*private Node<T> ins(T value, Node<T> node) {
        if( node == null ) { return new Node<T>(value); }
        if( value.compareTo(node.value) < 0 ) { node.left = ins(value, node.left); }
        else if( value.compareTo(node.value) > 0) { node.right = ins(value, node.right);}

        return node;
    }*/

    public boolean containsElement(T value) { return containsElem(value,root); }
    private boolean containsElem(T value, Node<T> w) {
        if( w == null ) {return false;}
        if( value.compareTo(w.value) == 0 ) { return true;}
        if( value.compareTo(w.value) < 0) { return containsElem(value, w.left);}
        else { return containsElem(value, w.right);}
    }

    public Node<T> search(T value){
        Node actual = root;
        while(actual != null && actual.value.compareTo(value)!=0){
            if(actual.value.compareTo(value)>0){actual = actual.left;}
            else { actual = actual.right; }
        }
        if(actual == null)
            System.out.println("Value Not Found");
        return actual;
    }
    private Node<T> min(Node<T> node) {
        while(node.left != null)
            node = node.left;
        return node;
    }

   /* public void deleteElement(T value){ deleteElem( value, this.root ); }
    public void deleteElem(T value, Node<T> root) {
        Node<T> elemToDelete = search(value, root);

        if (elemToDelete.right == null && elemToDelete.left == null){
            elemToDelete = null;

            return;
        }
        else if (elemToDelete.left!=null && elemToDelete.right==null){
            elemToDelete = elemToDelete.left;
            return;
        }
        else if (elemToDelete.left==null && elemToDelete.right!=null){
            elemToDelete = elemToDelete.right;
            return;
        }
        else if(elemToDelete.left!=null && elemToDelete.right!=null){
            Node<T> succesor = min(elemToDelete.right);
            elemToDelete.value = succesor.value;
            deleteElem(succesor.value, elemToDelete.right);
        }
    }*/


    private Node<T> max(Node node) {
        while(node.right != null)
            node = node.right;
        return node;
    }
    private Node<T> successor(T value) {
        Node node = this.search(value);
        if(node.right != null) {
            node = node.right;
            while(node.left != null)
                node = node.left;
            return node;
        }
        else if(node.right == null && node != root && node != this.max(root)) {
            Node parent = node.parent;
            while(parent != root && (parent.value.compareTo(node.value)<0))
                parent = parent.parent;
            return parent;
        }
        else{
            System.out.println("succesor not found");
            return null;
        }
    }
    private Node<T> predecessor(T value) {
        Node node = this.search(value);
        if(node.left != null) {
            node = node.left;
            while(node.right != null)
                node = node.right;
            return node;
        }
        else if(node.left == null && node != root  && node != this.min(root)) {
            Node parent = node.parent;
            while(parent != root && (parent.value.compareTo(node.value)>0))
                parent = parent.parent;
            return parent;
        }
        else{
            System.out.println("Not Found Predecessor");
            return null;
        }
    }
    public void deleteElement(T value){ deleteElem(value);}
    public Node<T> deleteElem(T value) {
        Node node = this.search(value);
        Node parent = node.parent;
        Node tmp;
        if(node.left != null && node.right != null) {
            tmp = this.deleteElem(this.successor(value).value);
            tmp.left = node.left;
            if(tmp.left != null)
                tmp.left.parent = tmp;
            tmp.right = node.right;
            if(tmp.right != null)
                tmp.right.parent = tmp;
        }
        else
            tmp = (node.left != null) ? node.left : node.right;
        if(tmp != null)
            node.parent = parent;
        if(parent == null)
            root = tmp;
        else if(parent.left == node)
            parent.left = tmp;
        else
            parent.right = tmp;

        return node;
    }



    public String toString() { return toS(root); }
    private String toS(Node<T> w) {
        if( w!=null ) { return "("+w.value+":"+toS(w.left)+":"+toS(w.right)+")"; }
        return "()";
    }
}
