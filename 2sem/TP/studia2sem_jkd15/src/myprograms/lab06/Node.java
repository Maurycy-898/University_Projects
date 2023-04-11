package myprograms.lab06;

public class Node <T extends Comparable<T>>{
    T value;
    Node<T> parent;
    Node<T> right;
    Node<T> left;

    Node(T value) {
        this.value = value;
        left = null;
        right = null;
        parent = null;
    }

}
