/*
 *    Adwin.java
 *    Copyright (C) 2008 UPC-Barcelona Tech, Catalonia
 *    @author Albert Bifet
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package algorithms.NOHAR.ADWIN;

import moa.core.SizeOf;

public class Adwin { //extends Estimator {

    private double mdbldelta = .002; //.1;
    private int mintTime = 0;
    private int mintMinimWindow = 10; //10
    private int mintClock = 50;//50 Hertz
    private double mdblWidth = 0; // Mean of Width = mdblWidth/Number of items

    //BUCKET
    public static final int MAXBUCKETS = 5;
    private int lastBucketRow = 0;
    private double TOTAL = 0;
    private double VARIANCE = 0;
    private int WIDTH = 0;
    private int BucketNumber = 0;

    private int Detect = 0;
    private int numberDetections = 0;
    private int DetectTwice = 0;
    private boolean blnBucketDeleted = false;
    private int BucketNumberMAX = 0;
    private int mintMinWinLength = 5;

    private List listRowBuckets;

    public Adwin() {
        initBuckets();
    }

    public Adwin(double d) {
        initBuckets();
        mdbldelta = d;
    }

    public Adwin(double d, int BOPsize, int hertz) {
        initBuckets();
        mdbldelta = d;
        mintMinimWindow = BOPsize;
        mintClock = hertz;
    }

    public boolean setInput(double intEntrada) {
        boolean blnChange = false;
        boolean blnExit = false;
        ListItem cursor;
        mintTime++;

        //1,2)Increment window in one element
        insertElement(intEntrada);
        blnBucketDeleted = false;
        //3)Reduce  window
        if (mintTime % mintClock == 0 && WIDTH > mintMinimWindow) {
            boolean blnReduceWidth = true; // Diference

            while (blnReduceWidth) // Diference
            {
                blnReduceWidth = false; // Diference
                blnExit = false;
                int n0 = 0;
                int n1 = WIDTH;
                double u0 = 0;
                double u1 = getTotal();
                double v0 = 0;
                double v1 = VARIANCE;
                double n2 = 0;
                double u2 = 0;

                cursor = listRowBuckets.tail();
                int i = lastBucketRow;
                do {
                    for (int k = 0; k <= (cursor.bucketSizeRow - 1); k++) {
                        n2 = bucketSize(i);
                        u2 = cursor.Total(k);
                        if (n0 > 0) {
                            v0 += cursor.Variance(k) + (double) n0 * n2 * (u0 / n0 - u2 / n2) * (u0 / n0 - u2 / n2) / (n0 + n2);
                        }
                        if (n1 > 0) {
                            v1 -= cursor.Variance(k) + (double) n1 * n2 * (u1 / n1 - u2 / n2) * (u1 / n1 - u2 / n2) / (n1 + n2);
                        }

                        n0 += bucketSize(i);
                        n1 -= bucketSize(i);
                        u0 += cursor.Total(k);
                        u1 -= cursor.Total(k);

                        if (i == 0 && k == cursor.bucketSizeRow - 1) {
                            blnExit = true;
                            break;
                        }
                        double absvalue = (double) (u0 / n0) - (u1 / n1);       //n1<WIDTH-mintMinWinLength-1
                        if ((n1 > mintMinWinLength + 1 && n0 > mintMinWinLength + 1)
                                && // Diference NEGATIVE
                                //if(
                                blnCutexpression(n0, n1, u0, u1, v0, v1, absvalue, mdbldelta)) {
                            blnBucketDeleted = true;
                            Detect = mintTime;

                            if (Detect == 0) {
                                Detect = mintTime;
                                //blnFirst=true;
                                //blnWarning=true;
                            } else if (DetectTwice == 0) {
                                DetectTwice = mintTime;
                                //blnDetect=true;
                            }
                            blnReduceWidth = true; // Diference
                            blnChange = true;
                            if (getWidth() > 0) { //Reduce width of the window
                                //while (n0>0)  // Diference NEGATIVE
                                n0 -= deleteElement();
                                blnExit = true;
                                break;
                            }
                        } //End if
                    }//Next k
                    cursor = cursor.previous();
                    i--;
                } while (((!blnExit && cursor != null)));
            }//End While // Diference
        }//End if

        mdblWidth += getWidth();
        if (blnChange) {
            numberDetections++;
        }
        return blnChange;
    }

    /*
    * PRIVATE METHODS
     */
    private void initBuckets() {
        //Init buckets
        listRowBuckets = new List();
        lastBucketRow = 0;
        TOTAL = 0;
        VARIANCE = 0;
        WIDTH = 0;
        BucketNumber = 0;
    }

    private void insertElement(double Value) {
        WIDTH++;
        insertElementBucket(0, Value, listRowBuckets.head());
        double incVariance = 0;
        if (WIDTH > 1) {
            incVariance = (WIDTH - 1) * (Value - TOTAL / (WIDTH - 1)) * (Value - TOTAL / (WIDTH - 1)) / WIDTH;
        }
        VARIANCE += incVariance;
        TOTAL += Value;
        compressBuckets();
    }

    private void insertElementBucket(double Variance, double Value, ListItem Node) {
        //Insert new bucket
        Node.insertBucket(Value, Variance);
        BucketNumber++;
        if (BucketNumber > BucketNumberMAX) {
            BucketNumberMAX = BucketNumber;
        }
    }

    private int deleteElement() {
        //LIST
        //Update statistics
        ListItem Node;
        Node = listRowBuckets.tail();
        int n1 = bucketSize(lastBucketRow);
        WIDTH -= n1;
        TOTAL -= Node.Total(0);
        double u1 = Node.Total(0) / n1;
        double incVariance = Node.Variance(0) + n1 * WIDTH * (u1 - TOTAL / WIDTH) * (u1 - TOTAL / WIDTH) / (n1 + WIDTH);
        VARIANCE -= incVariance;

        //Delete Bucket
        Node.RemoveBucket();
        BucketNumber--;
        if (Node.bucketSizeRow == 0) {
            listRowBuckets.removeFromTail();
            lastBucketRow--;
        }
        return n1;
    }

    private int bucketSize(int Row) {
        return (int) Math.pow(2, Row);
    }

    private void compressBuckets() {
        //Traverse the list of buckets in increasing order
        int n1, n2;
        double u2, u1, incVariance;
        ListItem cursor;
        ListItem nextNode;
        cursor = listRowBuckets.head();
        int i = 0;
        do {
            //Find the number of buckets in a row
            int k = cursor.bucketSizeRow;
            //If the row is full, merge buckets
            if (k == MAXBUCKETS + 1) {
                nextNode = cursor.next();
                if (nextNode == null) {
                    listRowBuckets.addToTail();
                    nextNode = cursor.next();
                    lastBucketRow++;
                }
                n1 = bucketSize(i);
                n2 = bucketSize(i);
                u1 = cursor.Total(0) / n1;
                u2 = cursor.Total(1) / n2;
                incVariance = n1 * n2 * (u1 - u2) * (u1 - u2) / (n1 + n2);

                nextNode.insertBucket(cursor.Total(0) + cursor.Total(1), cursor.Variance(0) + cursor.Variance(1) + incVariance);
                BucketNumber++;
                cursor.compressBucketsRow(2);
                if (nextNode.bucketSizeRow <= MAXBUCKETS) {
                    break;
                }
            } else {
                break;
            }
            cursor = cursor.next();
            i++;
        } while (cursor != null);
    }

    private boolean blnCutexpression(int n0, int n1, double u0, double u1, double v0, double v1, double absvalue, double delta) {
        int n = getWidth();
        double dd = Math.log(2 * Math.log(n) / delta);     // -- ull perque el ln n va al numerador.
        // Formula Gener 2008
        double v = getVariance();
        double m = ((double) 1 / ((n0 - mintMinWinLength + 1))) + ((double) 1 / ((n1 - mintMinWinLength + 1)));
        double epsilon = Math.sqrt(2 * m * v * dd) + (double) 2 / 3 * dd * m;

        return (Math.abs(absvalue) > epsilon);
    }

    /*
    * GET METHODS
     */
    public String getEstimatorInfo() {
        return "ADWIN;;";
    }

    public boolean getChange() {
        return blnBucketDeleted;
    }

    public void resetChange() {
        blnBucketDeleted = false;
    }

    public int getBucketsUsed() {
        return BucketNumberMAX;
    }

    public int getWidth() {
        return WIDTH;
    }

    public void setClock(int intClock) {
        mintClock = intClock;
    }

    public int getClock() {
        return mintClock;
    }

    public boolean getWarning() {
        return false;
    }

    public boolean getDetect() {
        return (Detect == mintTime);
    }

    public int getNumberDetections() {
        return numberDetections;
    }

    public double getTotal() {
        return TOTAL;
    }

    public double getEstimation() {
        return TOTAL / WIDTH;
    }

    public double getVariance() {
        return VARIANCE / WIDTH;
    }

    public double getWidthT() {
        return mdblWidth;
    }

    public int measureByteSize() {
        int size = (int) SizeOf.sizeOf(this);
        size += listRowBuckets.measureByteSize();
        return size;
    }
}
