
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author atul
 */
public class FredmanTarjanMST {

    private boolean isMST_Calculated;

    private LinkedList<Float>[] graph;
    private LinkedList<Float>[] MST;

    private GraphReader graphReader;

    FredmanTarjanMST(String inputGraphFilePath, String outputFilePath)
            throws IOException {
        this.isMST_Calculated = false;

        this.graphReader = new GraphReader(inputGraphFilePath, outputFilePath,
                true);
        this.graph = this.graphReader.getSimplifiedGraph();

        this.MST = new LinkedList[this.graphReader.getNodeCount()];
        for (int i = 0; i < this.graphReader.getNodeCount(); i++) {
            this.MST[i] = new LinkedList<>();
        }
    }

    private void runPrimsMST(int heapSizeCapacity, LinkedList<Float>[] forrest,
            int treeCount) {
        LinkedList<Float>[] tempMST = new LinkedList[treeCount];
        for (int i = 0; i < treeCount; i++) {
            tempMST[i] = new LinkedList<>();
        }

        int[] treeLabel = new int[treeCount];
        for (int i = 0; i < treeCount; i++) {
            treeLabel[i] = -1;
        }

        for (int i = 0; i < treeCount; i++) {
            if (tempMST[i].size() == 0) {
                try {
                    treeLabel[i] = i;

                    FibonacciHeap heap = new FibonacciHeap(heapSizeCapacity);
                    boolean isJoinedToOther = false;

                    for (Iterator iter = forrest[i].listIterator();
                            iter.hasNext();) {
                        int val1 = (int) ((float) iter.next());
                        float val2 = (float) iter.next();
                        int val3 = (int) ((float) iter.next());
                        int val4 = (int) ((float) iter.next());

                        FBHeapNode node = new FBHeapNode();
                        node.forrestNode1 = i;
                        node.forrestNode2 = val1;
                        node.value = val2;
                        node.graphNode1 = val3;
                        node.graphNode2 = val4;
                        node.degree = 0;

                        node.childHead = node.childTail = null;
                        node.next = node.previous = null;
                        node.parent = null;

                        heap.insert(node, node, node, 1);
                    }

                    while (heap.getSize() > 0) {
                        FBHeapNode node = heap.min();
                        heap.removeMin();

                        int temp = 0;
                        if (node.forrestNode1 != i
                                && tempMST[node.forrestNode1].size() == 0) {
                            temp = node.forrestNode1;
                        } else if (node.forrestNode2 != i
                                && tempMST[node.forrestNode2].size() == 0) {
                            temp = node.forrestNode2;
                        } else if (treeLabel[node.forrestNode1] != i) {
                            if (!isJoinedToOther) {
                                temp = node.forrestNode1;
                                isJoinedToOther = true;
                            } else {
                                heap.destroyHeap();
                                throw new Exception();
                            }
                        } else if (treeLabel[node.forrestNode2] != i) {
                            if (!isJoinedToOther) {
                                temp = node.forrestNode1;
                                isJoinedToOther = true;
                            } else {
                                heap.destroyHeap();
                                throw new Exception();
                            }
                        } else {
                            continue;
                        }

                        tempMST[node.forrestNode1].add((float) node.forrestNode2);
                        tempMST[node.forrestNode2].add((float) node.forrestNode1);

                        treeLabel[node.forrestNode1] = i;
                        treeLabel[node.forrestNode2] = i;

                        this.MST[node.graphNode1].add((float) node.graphNode2);
                        this.MST[node.graphNode1].add(node.value);

                        this.MST[node.graphNode2].add((float) node.graphNode1);
                        this.MST[node.graphNode2].add((float) node.value);

                        for (Iterator iter = forrest[temp].listIterator();
                                iter.hasNext();) {
                            int val1 = (int) ((float) iter.next());
                            float val2 = (float) iter.next();
                            int val3 = (int) ((float) iter.next());
                            int val4 = (int) ((float) iter.next());

                            if (tempMST[val1].size() != 0
                                    && treeLabel[val1] == i) {
                            } else {
                                FBHeapNode node2 = new FBHeapNode();
                                node2.degree = 0;
                                node2.forrestNode1 = temp;
                                node2.forrestNode2 = val1;
                                node2.value = val2;
                                node2.graphNode1 = val3;
                                node2.graphNode2 = val4;

                                node2.childHead = node2.childTail = null;
                                node2.next = node2.previous = null;
                                node2.parent = null;

                                heap.insert(node2, node2, node2, 1);
                            }
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }

    private int labelTrees(int[] labels) {
        for (int i = 0; i < this.graphReader.getNodeCount(); i++) {
            labels[i] = -1;
        }

        int treeIDCount = 0;
        for (int i = 0; i < this.graphReader.getNodeCount(); i++) {
            if (labels[i] == -1) {
                LinkedList<Integer> BFSData = new LinkedList<>();
                BFSData.add(i);

                while (BFSData.size() > 0) {
                    int node = BFSData.pop();

                    if (labels[node] == -1) {
                        labels[node] = treeIDCount;

                        for (Iterator iter = this.MST[node].listIterator();
                                iter.hasNext();) {
                            int val1 = (int) ((float) iter.next());
                            float val2 = (float) iter.next();

                            if (labels[val1] == -1) {
                                BFSData.add(val1);
                            }
                        }
                    }
                }
                treeIDCount++;
            }
        }
        return treeIDCount;
    }

    private LinkedList<Float>[] calculateForrest(int[] labels, int treeCount) {
        LinkedList<Integer>[] treeID = new LinkedList[treeCount];
        for (int i = 0; i < treeCount; i++) {
            treeID[i] = new LinkedList<>();
        }

        for (int i = 0; i < this.graphReader.getNodeCount(); i++) {
            treeID[labels[i]].add(i);
        }

        LinkedList<Float>[] graph = new LinkedList[treeCount];
        for (int i = 0; i < treeCount; i++) {
            graph[i] = new LinkedList<>();
        }

        for (int i = 0; i < treeCount; i++) {

            for (Iterator iter1 = treeID[i].listIterator();
                    iter1.hasNext();) {

                int val11 = (int) iter1.next();
                for (Iterator iter2 = this.graph[val11].listIterator();
                        iter2.hasNext();) {

                    int val1 = (int) ((float) iter2.next());
                    float val2 = (float) iter2.next();

                    int lval1 = labels[val1];

                    if (lval1 == i) {
                    } else {
                        Iterator it;
                        float temp = -1;
                        if (graph[lval1].size() != 0) {
                            it = graph[lval1].descendingIterator();
                            it.next();
                            it.next();
                            it.next();
                            temp = (float) it.next();
                        }

                        if (graph[lval1].size() == 0 || temp != i) {
                            graph[lval1].add((float) i);
                            graph[lval1].add(val2);
                            graph[lval1].add((float) val1);
                            graph[lval1].add((float) val11);
                        } else {
                            Iterator it2 = graph[lval1].descendingIterator();
                            it2.next();
                            it2.next();
                            float v = (float) it2.next();
                            if (v > val2) {
                                graph[lval1].pollLast();
                                graph[lval1].pollLast();
                                graph[lval1].pollLast();
                                graph[lval1].pollLast();

                                graph[lval1].add((float) i);
                                graph[lval1].add(val2);
                                graph[lval1].add((float) val1);
                                graph[lval1].add((float) val11);
                            }
                        }
                    }
                }
            }
        }
        return graph;
    }

    public void runFredmanTarjanMSTAlgorithm() {
        this.isMST_Calculated = true;

        int m = this.graphReader.getEdgeCount();
        int n = this.graphReader.getNodeCount();

        double ratio = (double) m / n;

        int heapSizeCapacity = (int) (ratio >= 30 ? m : Math.pow(2, ratio));

        int[] labels = new int[n];
        for (int i = 0; i < n; i++) {
            labels[i] = i;
        }
        int treeIDCount = n;

        this.runPrimsMST(heapSizeCapacity, this.graph, treeIDCount);
        
        LinkedList<Float>[] forrest;

        while (true) {
            treeIDCount = this.labelTrees(labels);

            if (treeIDCount <= 1) {
                break;
            }

            forrest = this.calculateForrest(labels, n);

            heapSizeCapacity = (int) (heapSizeCapacity >= 30 ? m
                    : Math.pow(2, (double) heapSizeCapacity));

            this.runPrimsMST(heapSizeCapacity, forrest, treeIDCount);
        }
    }

    public LinkedList<Float>[] getMST() {
        if (!this.isMST_Calculated) {
            this.runFredmanTarjanMSTAlgorithm();
        }

        return this.MST;
    }

    public void printMST() throws IOException {
        if (!this.isMST_Calculated) {
            this.runFredmanTarjanMSTAlgorithm();
        }

        this.graphReader.writeSymbolicGraph(this.MST);
    }
};
