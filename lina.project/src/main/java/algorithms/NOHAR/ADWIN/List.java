/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.NOHAR.ADWIN;

import moa.core.SizeOf;

/**
 *
 * @author Wesllen Sousa
 */
public class List {

    protected int count;
    protected ListItem head;
    protected ListItem tail;

    public int measureByteSize() {
        int size = (int) SizeOf.sizeOf(this);
        size += count * head.measureByteSize();
        return size;
    }

    public List() {
//			post: initializes the list to be empty.
        clear();
        addToHead();
    }

    /* Interface Store Methods */
    public int size() {
        //	post: returns the number of elements in the list.
        return this.count;
    }

    public ListItem head() {
        //	post: returns the number of elements in the list.
        return this.head;
    }

    public ListItem tail() {
        //	post: returns the number of elements in the list.
        return this.tail;
    }

    public boolean isEmpty() {
        //	 post: returns the true iff store is empty.
        return (this.size() == 0);
    }

    public void clear() {
        //	 post: clears the list so that it contains no elements.
        this.head = null;
        this.tail = null;
        this.count = 0;
    }

    /* Interface List Methods */
    public void addToHead() {
        //	 pre: anObject is non-null
        //	 post: the object is added to the beginning of the list
        this.head = new ListItem(this.head, null);
        if (this.tail == null) {
            this.tail = this.head;
        }
        this.count++;
    }

    public void removeFromHead() {
        //		 pre: list is not empty
        //		 post: removes and returns first object from the list
//			ListItem temp;
//			temp = this.head;
        this.head = this.head.next();
        if (this.head != null) {
            this.head.setPrevious(null);
        } else {
            this.tail = null;
        }
        this.count--;
        //temp=null;
        return;
    }

    public void addToTail() {
//			pre: anObject is non-null
//			post: the object is added at the end of the list
        this.tail = new ListItem(null, this.tail);
        if (this.head == null) {
            this.head = this.tail;
        }
        this.count++;
    }

    public void removeFromTail() {
//			pre: list is not empty
//			post: the last object in the list is removed and returned
//			ListItem temp;
//			temp = this.tail;
        this.tail = this.tail.previous();
        if (this.tail == null) {
            this.head = null;
        } else {
            this.tail.setNext(null);
        }
        this.count--;
        //temp=null;
        return;
    }

}
