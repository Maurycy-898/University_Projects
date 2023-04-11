import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.graph.WeightedMultigraph;
import java.util.ArrayList;
import java.util.Random;

public class Main {


  private static double delay;

  public static void main(String[] args) {

    Graph<Integer, DefaultWeightedEdge> tmpGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    ArrayList<DefaultWeightedEdge> edgesToRemove = new ArrayList<>();

    double dMax = 0.01;
    double destroyProbability = 0.8;
    final int relabilityTestAmount = 1000;
    int[][] matrixN = {{0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
            {1, 0, 2, 3, 4, 5, 6, 7, 8, 9},
            {2, 2, 0, 3, 4, 5, 6, 7, 8, 9},
            {3, 3, 3, 0, 4, 5, 6, 7, 8, 9},
            {4, 4, 4, 4, 0, 5, 6, 7, 8, 9},
            {5, 5, 5, 5, 5, 0, 6, 7, 8, 9},
            {6, 6, 6, 6, 6, 6, 0, 7, 8, 9},
            {7, 7, 7, 7, 7, 7, 7, 0, 8, 9},
            {8, 8, 8, 8, 8, 8, 8, 8, 0, 9},
            {9, 9, 9, 9, 9, 9, 9, 9, 9, 0}};


//    int[][] matrixC = {{0, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000,},
//            {1500000, 0, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000,},
//            {1500000, 1500000, 0, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000,},
//            {1500000, 1500000, 1500000, 0, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000,},
//            {1500000, 1500000, 1500000, 1500000, 0, 1500000, 1500000, 1500000, 1500000, 1500000,},
//            {1500000, 1500000, 1500000, 1500000, 1500000, 0, 1500000, 1500000, 1500000, 1500000,},
//            {1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 0, 1500000, 1500000, 1500000,},
//            {1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 0, 1500000, 1500000,},
//            {1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 0, 1500000,},
//            {1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 1500000, 0,}};


//    int[][] matrixC = {{0, 150000, 150000, 150000, 150000, 150000, 150000, 150000, 150000, 150000,},
//            {150000, 0, 150000, 150000, 150000, 150000, 150000, 150000, 150000, 150000,},
//            {150000, 150000, 0, 150000, 150000, 150000, 150000, 150000, 150000, 150000,},
//            {150000, 150000, 150000, 0, 150000, 150000, 150000, 150000, 150000, 150000,},
//            {150000, 150000, 150000, 150000, 0, 150000, 150000, 150000, 150000, 150000,},
//            {150000, 150000, 150000, 150000, 150000, 0, 150000, 150000, 150000, 150000,},
//            {150000, 150000, 150000, 150000, 150000, 150000, 0, 150000, 150000, 150000,},
//            {150000, 150000, 150000, 150000, 150000, 150000, 150000, 0, 150000, 150000,},
//            {150000, 150000, 150000, 150000, 150000, 150000, 150000, 150000, 0, 150000,},
//            {150000, 150000, 150000, 150000, 150000, 150000, 150000, 150000, 150000, 0,}};

//    int[][] matrixC = {{0, 75000, 75000, 75000, 75000, 75000, 75000, 75000, 75000, 75000,},
//            {75000, 0, 75000, 75000, 75000, 75000, 75000, 75000, 75000, 75000,},
//            {75000, 75000, 0, 75000, 75000, 75000, 75000, 75000, 75000, 75000,},
//            {75000, 75000, 75000, 0, 75000, 75000, 75000, 75000, 75000, 75000,},
//            {75000, 75000, 75000, 75000, 0, 75000, 75000, 75000, 75000, 75000,},
//            {75000, 75000, 75000, 75000, 75000, 0, 75000, 75000, 75000, 75000,},
//            {75000, 75000, 75000, 75000, 75000, 75000, 0, 75000, 75000, 75000,},
//            {75000, 75000, 75000, 75000, 75000, 75000, 75000, 0, 75000, 75000,},
//            {75000, 75000, 75000, 75000, 75000, 75000, 75000, 75000, 0, 75000,},
//            {75000, 75000, 75000, 75000, 75000, 75000, 75000, 75000, 75000, 0,}};



    int[][] matrixC = {{0, 30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000,},
            {30000, 0, 30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000,},
            {30000, 30000, 0, 30000, 30000, 30000, 30000, 30000, 30000, 30000,},
            {30000, 30000, 30000, 0, 30000, 30000, 30000, 30000, 30000, 30000,},
            {30000, 30000, 30000, 30000, 0, 30000, 30000, 30000, 30000, 30000,},
            {30000, 30000, 30000, 30000, 30000, 0, 30000, 30000, 30000, 30000,},
            {30000, 30000, 30000, 30000, 30000, 30000, 0, 30000, 30000, 30000,},
            {30000, 30000, 30000, 30000, 30000, 30000, 30000, 0, 30000, 30000,},
            {30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000, 0, 30000,},
            {30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000, 0,}};

    int[][] matrixA;

    Utils utils = new Utils(matrixN, matrixC, dMax, destroyProbability);
    SimpleWeightedGraph tempGraph;

    Random r = new Random();
    int passed = 0;

    // RELIABILITY CALCULATOR
    for (int i = 0; i < relabilityTestAmount; i++) {

      tempGraph = utils.createGraph();

        tempGraph.edgeSet().forEach(edge ->{
          if (r.nextDouble() > destroyProbability) {
            edgesToRemove.add((DefaultWeightedEdge)edge);
          }
        });

        edgesToRemove.forEach(tempGraph::removeEdge);
        edgesToRemove.clear();


        if(utils.isCohesive(tempGraph)){
          if(utils.graphReliability(tempGraph)){
            passed++;
          }
        }
      }
    double reliability = (double)passed/relabilityTestAmount;
    System.out.println("Network reliability = " + reliability + "(with " + relabilityTestAmount + " tries)");


    //Avarage DelayCalculator

//    passed = 0;
//
//
//    for (int i = 0; i < relabilityTestAmount; i++) {
//      tempGraph = utils.createGraph();
//      matrixA = utils.createMatrixA(matrixN, tempGraph);
//
//      tempGraph.edgeSet().forEach(edge -> {
//        if (r.nextDouble() > destroyProbability) {
//          edgesToRemove.add((DefaultWeightedEdge) edge);
//        }
//      });
//
//      edgesToRemove.forEach(tempGraph::removeEdge);
//      edgesToRemove.clear();
//
//      if (utils.isCohesive(tempGraph)) {
//        passed++;
//        delay += utils.calcDelay(matrixN, matrixC, matrixA, tempGraph);
//      }
//    }
//
//      double result = (delay / passed);
//      System.out.println("Average delay " + result + " (with " + relabilityTestAmount + " tries)");
  }
}


