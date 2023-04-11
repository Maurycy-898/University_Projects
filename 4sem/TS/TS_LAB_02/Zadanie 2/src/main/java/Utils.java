
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import java.util.ArrayList;
import java.util.List;


public class Utils {

  private final int[][] matrixN;
  private final int[][] matrixC;
  private final double maxDelay;
  private final double failProbability;
  private final ArrayList<DefaultWeightedEdge> edgesToRemove = new ArrayList<>();


  public Utils(int[][] matrixN, int[][] matrixC, double maxDelay, double failProbability){
    this.matrixN = matrixN;
    this.matrixC = matrixC;
    this.maxDelay = maxDelay;
    this.failProbability = failProbability;
  }


  void printMatrix(int[][] matrix){
    for(int i=0; i<10; i++){
      for(int j=0; j<10; j++) {
        if(matrix[i][j] < 10 ){
          System.out.print("0"+matrix[i][j] + " ");
        } else {
          System.out.print(matrix[i][j] + " ");
        }
      }
      System.out.println();
    }
  }


  SimpleWeightedGraph<Integer, DefaultWeightedEdge> createGraph(){
    SimpleWeightedGraph<Integer, DefaultWeightedEdge> tmpGraph = new SimpleWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);

    tmpGraph.addVertex(0);
    tmpGraph.addVertex(1);
    tmpGraph.addVertex(2);
    tmpGraph.addVertex(3);
    tmpGraph.addVertex(4);
    tmpGraph.addVertex(5);
    tmpGraph.addVertex(6);
    tmpGraph.addVertex(7);
    tmpGraph.addVertex(8);
    tmpGraph.addVertex(9);

    tmpGraph.addEdge(0, 1);
    tmpGraph.addEdge(0, 2);
    tmpGraph.addEdge(0, 3);
    tmpGraph.addEdge(1, 3);
    tmpGraph.addEdge(1, 4);
    tmpGraph.addEdge(2, 5);
    tmpGraph.addEdge(2, 6);
    tmpGraph.addEdge(3, 5);
    tmpGraph.addEdge(3, 6);
    tmpGraph.addEdge(3, 7);
    tmpGraph.addEdge(4, 6);
    tmpGraph.addEdge(4, 7);
    tmpGraph.addEdge(5, 8);
    tmpGraph.addEdge(6, 8);
    tmpGraph.addEdge(6, 9);
    tmpGraph.addEdge(7, 9);
    tmpGraph.addEdge(8, 9);

    return tmpGraph;
  }

  double calcDelay(int[][] matrixN, int[][] matrixC, int[][] matrixA, Graph<Integer, DefaultWeightedEdge> graph) {

    double delay;
    double sum = 0;
    double sum2 = 0;

    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        sum += matrixN[i][j];
      }
    }

    for (DefaultWeightedEdge de : graph.edgeSet()) {
      float temp = matrixA[graph.getEdgeSource(de)][graph.getEdgeTarget(de)] / (float)
              ((matrixC[graph.getEdgeSource(de)][graph.getEdgeTarget(de)] / 1500)
                      - matrixA[graph.getEdgeSource(de)][graph.getEdgeTarget(de)]);

      if(temp < 0){
        sum2 += temp;
      }

    }
    delay = (1/sum) * sum2;
    return delay;
  }


  int[][] createMatrixA(int[][] matrixN, Graph<Integer, DefaultWeightedEdge> graph) {

    int[][] matrixA = new int[10][10];

    DijkstraShortestPath dijkstraGraph = new DijkstraShortestPath(graph);

    for(int i=0; i<10; i++){
      for(int j=i; j<10; j++){

        GraphPath shortestPath = dijkstraGraph.getPath(i,j);
        List<Integer> shortestPathList = shortestPath.getVertexList();

        for(int k=0; k<shortestPath.getLength()-1; k++){

          int lastVertex = (int) shortestPath.getEndVertex();
          int firstVertex = (int) shortestPath.getStartVertex();

          int val = matrixN[firstVertex][lastVertex];

          matrixA[shortestPathList.get(k)][shortestPathList.get(k+1)] += val;
          matrixA[shortestPathList.get(k+1)][shortestPathList.get(k)] += val;
        }
      }
    }
    return matrixA;
  }


  Boolean graphReliability(SimpleWeightedGraph graph){

      int[][] matrixA = createMatrixA(matrixN,graph);
      double delay = calcDelay(matrixN,matrixC,matrixA,graph);
      if(delay>0 && delay<maxDelay){
        return true;
      }
    return false;
  }

  boolean isCohesive(SimpleWeightedGraph graph) {
    return new ConnectivityInspector<>(graph).isGraphConnected();
  }

}
