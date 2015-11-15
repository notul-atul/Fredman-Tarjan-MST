/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author atul
 */
class FibonacciHeap {

    private FBHeapNode rootListHead;
    private FBHeapNode rootListTail;

    private FBHeapNode minNode;

    private int size;
    private int capacity;

    FibonacciHeap(int capacity) {
        this.rootListHead = null;
        this.rootListTail = null;

        this.minNode = null;

        this.size = 0;
        this.capacity = capacity;
    }

    public int getSize() {
        return this.size;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void destroyHeap() throws Exception {
        while (this.size > 0) {
            FBHeapNode minNode = this.min();
            this.removeMin();
        }
    }

    private FBHeapNode link(FBHeapNode node1, FBHeapNode node2) throws Exception {
        if (node1 == null && node2 == null) {
            return null;
        } else {
            if (node1 == null || node2 == null) {
                throw new Exception("One node is null");
            } else if (node1.degree != node2.degree) {
                throw new Exception("Degree of nodes should be same");
            }

            if (node1.value > node2.value) {
                FBHeapNode temp = node1;
                node1 = node2;
                node2 = temp;
            }

            if (node1.childHead == null) {
                node1.childHead = node1.childTail = node2;
                node2.previous = null;
            } else {
                node1.childTail.next = node2;
                node2.previous = node1.childTail;
                node1.childTail = node2;
            }
            node2.parent = node1;
            node2.next = null;

            node1.degree++;
        }
        return node1;
    }

    public void insert(FBHeapNode rootListHead, FBHeapNode rootListTail,
            FBHeapNode minNode, int size) throws Exception {
        if (rootListHead == null && rootListTail == null) {
            return;
        } else if (rootListHead == null) {
            throw new Exception("head is null but tail is not");
        } else if (rootListTail == null) {
            throw new Exception("tail is null but head is not");
        }

        if (this.rootListHead == null) {
            if (size > this.capacity) {
                this.destroyHeap();

                throw new Exception("Capacity limit violated");
            }

            this.rootListHead = rootListHead;
            this.rootListTail = rootListTail;

            this.minNode = minNode;

            this.size = size;
        } else {
            if (size + this.size > this.capacity) {
                throw new Exception("Capacity limit violated");
            }

            this.rootListTail.next = rootListHead;
            rootListHead.previous = this.rootListTail;

            if (minNode.value < this.minNode.value) {
                this.minNode = minNode;
            }

            this.size += size;

            this.rootListTail = rootListTail;
        }
    }

    private void consolidate() throws Exception {
        if (this.size <= 1) {
            this.minNode = this.rootListHead;
            return;
        }

        int tempSize = (int) (2 * (Math.log10(this.size) / Math.log10(2)));

        FBHeapNode tempArray[] = new FBHeapNode[tempSize];
        for (int i = 0; i < tempSize; i++) {
            tempArray[i] = null;
        }

        int maxDegree = -1;

        for (FBHeapNode iter = this.rootListHead; iter != null;) {
            int degree = iter.degree;
            if (tempArray[degree] == null) {
                tempArray[degree] = iter;

                maxDegree = (maxDegree < degree) ? degree : maxDegree;

                iter = iter.next;
            } else {
                FBHeapNode tempNode = iter;
                iter = iter.next;

                while (tempArray[degree] != null) {
                    tempNode = this.link(tempNode, tempArray[degree]);

                    tempArray[degree] = null;

                    degree++;
                }
                tempArray[degree] = tempNode;

                maxDegree = (maxDegree < degree) ? degree : maxDegree;
            }
        }

        this.rootListHead = null;
        this.rootListTail = null;

        FBHeapNode tempMinNode = tempArray[maxDegree];
        for (int i = 0; i <= maxDegree; i++) {
            if (tempArray[i] != null) {
                if (this.rootListHead == null) {
                    this.rootListHead = this.rootListTail = tempArray[i];
                    tempArray[i].previous = tempArray[i].next = null;
                } else {
                    this.rootListTail.next = tempArray[i];
                    tempArray[i].previous = this.rootListTail;
                    tempArray[i].next = null;
                    this.rootListTail = tempArray[i];
                }

                if (tempMinNode.value > tempArray[i].value) {
                    tempMinNode = tempArray[i];
                }
            }
        }

        this.minNode = tempMinNode;
    }

    public FBHeapNode min() {
        return this.minNode;
    }

    public void removeMin() throws Exception {
        if (this.minNode == null) {
            return;
        } else if (this.minNode.previous == null && this.minNode.next == null) {
            if (this.minNode.childHead == null) {
                this.rootListHead = null;
                this.rootListTail = null;

                this.minNode = null;
            } else {
                this.rootListHead = this.minNode.childHead;
                this.rootListTail = this.minNode.childTail;

                this.minNode.childHead = null;
                this.minNode.childTail = null;

                this.minNode = this.rootListHead;
            }
        } else if (this.minNode.previous == null) {
            this.insert(this.minNode.childHead, this.minNode.childTail,
                    this.minNode, 0);

            this.rootListHead = this.rootListHead.next;
            this.rootListHead.previous = null;

            FBHeapNode temp = this.minNode;
            this.minNode = this.minNode.next;

            temp.next = null;
            temp.childHead = null;
            temp.childTail = null;
        } else {
            this.insert(this.minNode.childHead, this.minNode.childTail,
                    this.minNode, 0);

            this.minNode.previous.next = this.minNode.next;
            if (this.minNode.next != null) {
                this.minNode.next.previous = this.minNode.previous;
            } else {
                this.rootListTail = this.minNode.previous;
            }

            this.minNode.childHead = null;
            this.minNode.childTail = null;

            this.minNode.previous = null;
            this.minNode.next = null;
        }
        this.size = (this.size - 1 > 0) ? this.size - 1 : 0;

        this.consolidate();
    }
};
