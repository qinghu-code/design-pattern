package org.example;

public class Main {
    public static void main(String[] args) {
        Parent parent = new Son();
        System.out.println(parent.getNum());
    }
}

class Parent{
    public int get(){
        return 1;
    }

    public int getNum(){
        return this.get();
    }
}

class Son extends Parent{
    public int get(){
        return 2;
    }

}