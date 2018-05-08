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
public class ListItem {

//		protected Object data;
    protected ListItem next;
    protected ListItem previous;
    protected int bucketSizeRow = 0;
    protected int MAXBUCKETS = Adwin.MAXBUCKETS;
    protected double bucketTotal[] = new double[MAXBUCKETS + 1];
    protected double bucketVariance[] = new double[MAXBUCKETS + 1];

    public int measureByteSize() {
        int size = (int) SizeOf.sizeOf(this);
        return size;
    }

    public ListItem() {
//			post: initializes the node to be a tail node
//			containing the given value.
        this(null, null);
    }

    public void clear() {
        bucketSizeRow = 0;
        for (int k = 0; k <= MAXBUCKETS; k++) {
            clearBucket(k);
        }
    }

    private void clearBucket(int k) {
        setTotal(0, k);
        setVariance(0, k);
    }

    public ListItem(ListItem nextNode, ListItem previousNode) {
//			post: initializes the node to contain the given
//			object and link to the given next node.
        //this.data = element;
        this.next = nextNode;
        this.previous = previousNode;
        if (nextNode != null) {
            nextNode.previous = this;
        }
        if (previousNode != null) {
            previousNode.next = this;
        }
        clear();
    }

    public void insertBucket(double Value, double Variance) {
//			insert a Bucket at the end
        int k = bucketSizeRow;
        bucketSizeRow++;
        //Insert new bucket
        setTotal(Value, k);
        setVariance(Variance, k);
    }

    public void RemoveBucket() {
//			Removes the first Buvket
        compressBucketsRow(1);
    }

    public void compressBucketsRow(int NumberItemsDeleted) {
        //Delete first elements
        for (int k = NumberItemsDeleted; k <= MAXBUCKETS; k++) {
            bucketTotal[k - NumberItemsDeleted] = bucketTotal[k];
            bucketVariance[k - NumberItemsDeleted] = bucketVariance[k];
        }
        for (int k = 1; k <= NumberItemsDeleted; k++) {
            clearBucket(MAXBUCKETS - k + 1);
        }
        bucketSizeRow -= NumberItemsDeleted;
        //BucketNumber-=NumberItemsDeleted;
    }

    public ListItem previous() {
//			post: returns the previous node.
        return this.previous;
    }

    public void setPrevious(ListItem previous) {
//			post: sets the previous node to be the given node
        this.previous = previous;
    }

    public ListItem next() {
//			post: returns the next node.
        return this.next;
    }

    public void setNext(ListItem next) {
//			post: sets the next node to be the given node
        this.next = next;
    }

    public double Total(int k) {
//			post: returns the element in this node
        return bucketTotal[k];
    }

    public double Variance(int k) {
//			post: returns the element in this node
        return bucketVariance[k];
    }

    public void setTotal(double value, int k) {
//			post: sets the element in this node to the given
//			object.
        bucketTotal[k] = value;
    }

    public void setVariance(double value, int k) {
//			post: sets the element in this node to the given
//			object.
        bucketVariance[k] = value;
    }

}
