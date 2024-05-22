public class MyHashTable<K, V>{
    public HashNode<K, V>[] getChainArray() {
        if (chainarray == null) {
            return new HashNode[0];
        }
        return chainarray;
    }

    public static class HashNode<K, V> {
        private K key;
        private V value;
        public HashNode<K, V> next;
        public HashNode(K key, V value){
            this.key=key;
            this.value=value;
        }
        @Override
        public String toString(){
            return "{" +key + " " + value+"}";
        }
    }
    private HashNode<K, V>[] chainarray;
    private int m=11;
    private int size;
    public MyHashTable(){
        chainarray=(HashNode<K,V>[]) new HashNode[m];
        size=0;
    }
    public MyHashTable(int m){
        this.m=m;
        chainarray=(HashNode<K,V>[]) new HashNode[m];
    }

    private int hash(K key){
        return (key.hashCode()& 0x7fffffff) %m; //using load factor
    }
    public void put(K key, V value){
        int index=hash(key);
        HashNode<K, V> newNode =new HashNode<>(key, value);
        if(chainarray[index] ==null){
            chainarray[index]=newNode;
        }
        else{
            HashNode<K, V> currentNode=chainarray[index];
            while(currentNode.next != null){
                if (currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode=currentNode.next;

            }
            currentNode.next=newNode;
        }
        size++;
    }
    public V get(K key){
        int index=hash(key);
        HashNode<K,V> currentNode=chainarray[index];
        while(currentNode.next!=null){
            if(currentNode.key.equals(key)){
                return currentNode.value;
            }
            currentNode=currentNode.next;
        }
        return null;
    }
    public V remove(K key){
        int index=hash(key);
        HashNode<K,V> currentNode=chainarray[index];
        HashNode<K, V> previous = null;
        while(currentNode.next!=null){
            if(currentNode.key.equals(key)){
                if(previous==null){
                    chainarray[index]=currentNode.next;
                }
                else{
                    previous.next=currentNode.next;
                }
                size--;
                return currentNode.value;

            }
            previous = currentNode;
            currentNode = currentNode.next;
        }
        return null;
    }
    public boolean contains(V value){
        for(HashNode<K, V> node: chainarray){
            HashNode<K, V> currentNode= node;
            while(currentNode!=null){
                if(currentNode.value.equals(value)){
                    return true;
                }
                currentNode=currentNode.next;
            }

        }
        return false;
    }
    public K getKey(V value){
        for(HashNode<K, V> node: chainarray){
            HashNode<K, V> currentNode= node;
            while(currentNode!=null){
                if(currentNode.value.equals(value)){
                    return currentNode.key;
                }
                currentNode=currentNode.next;
            }

        }
        return null;
    }

}
import java.util.*;

public class TestingK {
    private int id;
    public TestingK(int id){
        this.id=id;
    }
    public int getId(){
        return  id;
    }

    @Override
    public int hashCode(){
        return id;
    }
    @Override
    public boolean equals(Object anything){
        if(this == anything){
            return true;
        }
        if(this.getClass()!=anything.getClass()|| anything==null){
            return false;
        }
        TestingK key=(TestingK) anything;
        return id==key.getId();
    }


    public static void main(String[] args) {
        MyHashTable<TestingK, Student> table = new MyHashTable<>();
        Random random = new Random();

        for (int i = 0; i < 10000; i++) {
            TestingK key = new TestingK(random.nextInt(1000));
            Student value = new Student("Student " + i);
            table.put(key, value);
        }

        MyHashTable.HashNode<TestingK, Student>[] chainArray = table.getChainArray();
        for (int i = 0; i < chainArray.length; i++) {
            int count = 0;
            MyHashTable.HashNode<TestingK, Student> current = chainArray[i];
            while (current != null) {
                count++;
                current = current.next;
            }
            System.out.println("Bucket " + i + ": " + count + " elements");
        }

        BST<Integer, String> bst = new BST<>();

        for (int i = 0; i < 10000; i++) {
            int key = random.nextInt(1000);
            String value = "Value " + i;
            bst.put(key, value);
        }

        System.out.println("Size of BST: " + bst.size());

        for (Integer key : bst.iterator()) {
            System.out.println("Key is " + key + " and value is " + bst.get(key));
        }
    }



    static class Student {
        String name;

        public Student(String name) {
            this.name = name;
        }
    }
}

package org.example;
import java.util.ArrayList;

public class BST<K extends Comparable<K>, V> {
    private Node root;
    private int size;

    private class Node {
        private K key;
        private V val;
        private Node left, right;

        public Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    public void put(K key, V val) {
        root = put(root, key, val);
    }

    private Node put(Node x, K key, V val) {
        if (x == null) {
            size++;
            return new Node(key, val);
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = put(x.left, key, val);
        } else if (cmp > 0) {
            x.right = put(x.right, key, val);
        } else {
            x.val = val;
        }
        return x;
    }

    public V get(K key) {
        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) {
                x = x.left;
            } else if (cmp > 0) {
                x = x.right;
            } else {
                return x.val;
            }
        }
        return null;
    }

    public void delete(K key) {
        root = delete(root, key);
    }

    private Node delete(Node x, K key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = delete(x.left, key);
        } else if (cmp > 0) {
            x.right = delete(x.right, key);
        } else {
            if (x.right == null) return x.left;
            if (x.left == null) return x.right;
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        return x;
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        return x;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        return min(x.left);
    }

    public Iterable<K> iterator() {
        ArrayList<K> keys = new ArrayList<>();
        inorder(root, keys);
        return keys;
    }

    private void inorder(Node x, ArrayList<K> keys) {
        if (x == null) return;
        inorder(x.left, keys);
        keys.add(x.key);
        inorder(x.right, keys);
    }

    public int size() {
        return size;
    }
}