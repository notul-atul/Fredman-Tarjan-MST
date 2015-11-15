/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author atul
 */
public class GraphReader {

    private final String END_ID = "#";

    private int nodeCount;
    private int edgeCount;

    private boolean forrestMode;

    private HashMap<String, Integer> encoderTable;
    private String[] decoderTable;

    private LinkedList<Float>[] graph;

    BufferedReader infile;
    BufferedWriter outfile;

    GraphReader(String inputFilePath, String outputFilePath,
            boolean forrestMode) throws IOException {

        this.forrestMode = forrestMode;

        this.outfile = new BufferedWriter(new FileWriter(outputFilePath));
        this.infile = new BufferedReader(new FileReader(inputFilePath));

        this.encoderTable = new HashMap<>();
        
        this.nodeCount = 0;
        this.edgeCount = 0;

        {
            String input = "";

            try {
                input = this.infile.readLine();
                while (input.compareTo(END_ID) != 0) {
                    this.encoderTable.put(input, this.nodeCount++);
                    input = this.infile.readLine();
                }
            } catch (Exception e) {
                System.err.print("Bad input file.");
            }

            this.graph = new LinkedList[this.nodeCount];
            this.decoderTable = new String[this.nodeCount];

            for (int i = 0; i < this.graph.length; i++) {
                this.graph[i] = new LinkedList<>();
                this.decoderTable[i] = new String();
            }

            for (Map.Entry<String, Integer> entry : this.encoderTable.entrySet()) {
                this.decoderTable[entry.getValue()] = entry.getKey();
            }
        }

        {
            while (true) {
                
                String inread = infile.readLine();
                if(inread == null){
                    break;
                }
                inread = inread.trim();

                if (inread.compareTo("") == 0) {
                    break;
                }

                String[] values = inread.split(" ");

                String nodeId1, nodeId2;
                float weight;

                nodeId1 = values[0];
                nodeId2 = values[1];
                weight = Float.parseFloat(values[2]);

                this.edgeCount++;

                int node1 = this.encoderTable.get(nodeId1);
                int node2 = this.encoderTable.get(nodeId2);

                this.graph[node1].add((float) node2);
                this.graph[node1].add(weight);

                this.graph[node2].add((float) node1);
                this.graph[node2].add(weight);

                if (this.forrestMode) {
                    this.graph[node1].add((float) node1);
                    this.graph[node1].add((float) node2);

                    this.graph[node2].add((float) node2);
                    this.graph[node2].add((float) node1);
                }
            }
        }
        this.infile.close();
    }

    public LinkedList<Float>[] getSimplifiedGraph() {
        return this.graph;
    }

    public void writeSymbolicGraph(LinkedList<Float>[] oGraph) throws IOException {
        for (int i = 0; i < this.nodeCount; i++) {
            this.outfile.write(this.decoderTable[i]);
            this.outfile.newLine();
        }

        this.outfile.write("#");
        this.outfile.newLine();

        for (int i = 0; i < this.nodeCount; i++) {
            String node1Id = this.decoderTable[i];

            for (Iterator iter = oGraph[i].listIterator(0); iter.hasNext();) {
                int node2ID = (int) ((float)iter.next());
                float weight = (float) iter.next();

                if (i < node2ID) {
                    this.outfile.write(node1Id + " " + this.decoderTable[node2ID]
                            + " " + weight);
                    this.outfile.newLine();
                }
            }
        }
        this.outfile.flush();
        this.outfile.close();
    }

    public int getNodeCount() {
        return this.nodeCount;
    }

    public int getEdgeCount() {
        return this.edgeCount;
    }
};
