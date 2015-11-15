from UnionFind import UnionFind
import random

class GraphGenerator:
    def __init__(self, nodes, density,
                 graphFilePath, outputFilePath, 
                 leftProb = 1 / 3.0, rightProb = 1 / 3.0):
        self.__nodes = nodes
        self.__density = density
        self.__graphFilePath = graphFilePath
        self.__outputFilePath = outputFilePath
        
        self.__leftProb = leftProb
        self.__crossProb = 1 - leftProb - rightProb
        self.__rightProb = rightProb

        self.__UF = UnionFind(nodes)
        self.__graph = [[] for i in range(nodes)]

    def generateTree(self):
        trees = [i for i in range(self.__nodes)]

        while(len(trees) > 1):
            node1 = node2 = -1

            if(random.random() < 0.005):
                random.shuffle(trees)
            
            choice = random.random()
            if(choice <= self.__leftProb):
                node1 = trees.pop(0)
                node2 = trees.pop(0)

                self.__UF.union(node1, node2)
                
                trees[0:0] = [self.__UF.find(node1)]
            elif(choice <= self.__leftProb + self.__crossProb):
                node1 = trees.pop(0)
                node2 = trees.pop()

                self.__UF.union(node1, node2)

                if(random.random() <= 0.5):
                    trees[0:0] = [self.__UF.find(node1)]
                else:
                    trees.append(self.__UF.find(node1))
            else:
                node1 = trees.pop()
                node2 = trees.pop()

                self.__UF.union(node1, node2)

                trees.append(self.__UF.find(node1))

            weight = random.randint(1, 100)

            if(len(self.__graph[node1]) > 0 and random.random() >= 1.0 / len(self.__graph[node1])):
                rand = random.randint(1, len(self.__graph[node1]) >> 1) - 1
                node1 = self.__graph[node1][rand << 1]

            if(len(self.__graph[node2]) > 0 and random.random() >= 1.0 / len(self.__graph[node2])):
                rand = random.randint(1, len(self.__graph[node2]) >> 1) - 1
                node2 = self.__graph[node2][rand << 1]
            
            self.__graph[node1].extend([node2, weight])
            self.__graph[node2].extend([node1, weight])

    def writeGraph(self, mode = True):
        if(mode):
            outputFile = open(self.__graphFilePath, 'w')
        else:
            outputFile = open(self.__outputFilePath, 'w')
        
        for i in range(self.__nodes):
            outputFile.writelines(str(i) + '\n')
        outputFile.writelines('#\n')
        
        for i in range(self.__nodes):
            for j in range(0, len(self.__graph[i]), 2):
                if(self.__graph[i][j] <= i):
                    continue
                
                outputFile.writelines(str(i) + ' ')
                outputFile.writelines(str(self.__graph[i][j]) + ' ')
                outputFile.writelines(str(self.__graph[i][j + 1]) + '\n')
                
        outputFile.close()

    def generateGraph(self):
        density = self.__density

        mark = [-1] * self.__nodes
        for i in range(self.__nodes):
            if(len(self.__graph[i]) >= 2 * density):
                continue

            count = len(self.__graph[i])
            for j in range(0, len(self.__graph[i]), 2):
                mark[self.__graph[i][j]] = 1

            for j in range(i + 1, self.__nodes):
                if(mark[j] == -1):
                    weight = random.randint(101, 200)
                    self.__graph[i].extend([j, weight])
                    self.__graph[j].extend([i, weight])

                if(len(self.__graph[i]) >= 2 * density):
                    break

            for j in range(0, count, 2):
                mark[self.__graph[i][j]] = -1

if __name__ == '__main__':
    import sys
    nodes = int(sys.argv[1])
    density = int(sys.argv[2])
    graphFilePath = sys.argv[3]
    outputFilePath = sys.argv[4]
    if(len(sys.argv) >= 6):
        lp = float(sys.argv[5])
        rp = float(sys.argv[6])

    gg = GraphGenerator(nodes, density, graphFilePath, outputFilePath)
    gg.generateTree()
    gg.writeGraph(False)
    gg.generateGraph()
    gg.writeGraph()
