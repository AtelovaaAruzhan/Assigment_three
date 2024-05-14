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

