# Fredman-Tarjan-MST
Test data generation process:
Constructor:
	args:
		nodes: number of nodes in graph
		density: aprroximate avergae degree of node
		
Method:
	generateTree():
		1. Intially it constructs list of trees with single node. Numbered 1...#nodes
		2. Randomly choose two tree either from begining or end or one from begining and 		    one from end. Join them , by randomly choosing one node from each of them, to 		    form a single tree. Give a name to new tree. Edge weight id b/w 1-100.
		3. Repeat process 1 and 2 until a single tree is left

	generateGraph():
		1. Assumes that tree is gnerated with method generateTree()
		2. traverse each node, numbered from 1..#nodes, in tree. If node have degree less 		    than density then try to choose nodes with number greater than current node to 		    make degree of node = density. But it doesnot gurantee it. Weight assigned in this 		    process is b/w 101-200
	WriteGraph():
		prints the graph in file with format specified in question.


FibonacciHeap Data Structure:
	node field: FBHeapNode
		int graphNode1; // one end of edge in original tree
    		int graphNode2;// another end of edge in original tree

	    	int forrestNode1;// one end of edge in the forrest with tree as nodes. Trees are disjoint
		int forrestNode2;// another end of edge in the forrest.

		float value;// weight of edge

		int degree;// degree of node in the Fibonacci tree.
		
		FBHeapNode parent;// node's parent in Fibonacci tree
		FBHeapNode next;//next node pointer in list
		FBHeapNode previous;// previous node pointer in list
		FBHeapNode childHead;// pointer to the first node in child list
		FBHeapNode childTail;// pointer to the last node in child list
	Class field:
		private FBHeapNode rootListHead;// points to first node. Root list is not circular
		private FBHeapNode rootListTail; //  points to last node
		private FBHeapNode minNode;
		private int size;
		private int capacity;
	Constructor(capacity):
		sets rootListHead, rootListTail and minNode = null
		size = 0
		this.capacity = capacity
	Class methods:
		link(FBHeapNode node1, FBHeapNode node2):
			links FibonacciTree pointed by node1 and node2 as specified in Cormen book
		consolidate():
			runs the linking process of root list. Procedure is similar as specified in 				cormen book.
		insert(FBHeapNode rootlistHead, FBHeapNode rootListTail, FBHeapNode 				minNode, int size):
			insert another fibonacci heap with rootListHead, rootListTail, minNode and 			heapSize. If size exceeds capacity it throws an error. It simply connects 				current rootListTail to given rootListHead.
		RemoveMin():
			It joins child of min node tree with rootList and remove min node from tree. 			After that it runs considate operation. And re-computes min node.
		

Fredman&TarjanMST:
	private LinkedList<Float>[] graph;
			graph[i] contains edges going from it with weight. graph[i][2*m] contains 			other end of edge in forrest. graph[i][2*m+1] contains its weight. graph[i]			[2*m+2] contains one end of end in original tree. graph[i][2*m+3] contains 			other end of edge in original graph.
    		private LinkedList<Float>[] MST;
			reference MST computed  by Fredman&Tarjan algo. Semantics same as 				graph.
		
		Constructor():
			args: input graph file path, output file path
			initializes graph by reading from file.
			Creates empty MST.
		Class methods:
		    void	runPrimsMST(int heapCapacity, LinkedList<Float>[] forrest, int treeCount):
				1. starts from a node in forrest. Runs prims algo with it with limited 				    heapSize.
				2. choose another node in forrest with is not in any tree compute so far
				3. Starts prims algo with it and stops either when heapSize exceeds 				    capacity or process terminates as in prims case or when second 					    edge is found whic connects it to some other tree computed in 					    previous steps.
				4. Repeat 2 and 3 unless 2 can't find any such node.
				5. While running prims on forrest it keeps adding computed edge to 				    MST
		   int labelTrees(labels[]):
				It runs BFS on the MST computed so far to find diconnected 					components. It numbers the nodes in the original graph with the 					disconnected component to which it belongs. Returns number of such 				disconnected components.
		  LinkedList<Float>[] calculateForrest(int[] labels, int treeCount):
				Treats disconnected component as a single node and edge b/w nodes 				are the 	min weight edge that connects any of the node in that 					component to 	another one.
				It checks all the edges on a connected component and keeps noting the 				min edges that connects them to other components.
		runFredmanTarjanMSTAlgorithm():
				1. intializes heapCapacity with 2^(m/n), labels with node numbers and 				    forrest with intial graph
				2. calls runPrimsMST(heapCapcity, forrest, nodes)
				3. relabel nodes
				4. recompute forrest
				5. heapCapacity = min(2^heapCapacity, #edges)
				6. repeat 2-5 until only connected component is left.
				Return MST.

