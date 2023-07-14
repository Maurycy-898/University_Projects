#include "src/graph_utils.hpp"

void simpleTestAlgorithms() {
  Graph* g = grFromFile("data/samples/sample1.txt", UNDIRECTED);
  // Graph* g = grFromFile("data/samples/USA-road-t.NY.gr");

  uint64_t start_node{ 1 };
  std::cout << "Graph decoded, start node: " << start_node << std::endl;

  // iVect_t res = g->dialAlgorithm(start_node);
  // std::cout << "Alg done:\n" << std::endl;
  // for (int dist : res) {
  //   std::cout << dist << ", " << std::endl;
  // } std::cout << std::endl;
  
  // res = g->dialAlgorithm(0);
  // for (int dist : res) {
  //   std::cout << dist << ", ";
  // } std::cout << std::endl;

  iVect_t res = g->radixHeapAlg2(0);
  for (int dist : res) {
    std::cout << dist << ", ";
  } std::cout << std::endl;

  // for (int i = 1; i <= g->V; i++) {
  //   std::cout << g->dijkstraPQ(0, i) << ", ";
  // } std::cout << std::endl;

  // for (uint64_t i = 1ULL; i <= g->V; ++i) {
  //   std::cout << g->dialAlgorithm(start_node, i) << std::endl;
  // } std::cout << std::endl;

  // for (int i = 1; i <= g->V; i++) {
  //   std::cout << g->radixHeapAlg(0, i) << ", ";
  // } std::cout << std::endl;
  
  delete g;
  
}


void simpleTestUtils() {
  // auto r = ssFromFile("./data/ch9-1.1/inputs/Long-C/Long-C.0.0.ss");
  // for (auto x : r) {
  //   std::cout << x << std::endl;
  // }

  auto r = p2pFromFile("./data/ch9-1.1/inputs/Long-C/Long-C.0.0.p2p");
  for (auto x : r) {
    std::cout << x.first << ", " << x.second << std::endl;
  }
}


void mainTest() {
  string tstFile;
  string resFile;

  // int sampleNO = 6;
  for (int sampleNO = 4; sampleNO < 6; ++sampleNO) {
    // tstFile = "./data/ch9-1.1/inputs/Long-C/Long-C." + std::to_string(sampleNO) + ".0";
    // resFile = "./res2/longC/lc_" + std::to_string(sampleNO);
    // testAlgorithms(tstFile, resFile);

    // tstFile = "./data/ch9-1.1/inputs/Long-n/Long-n.1" + std::to_string(sampleNO) + ".0";
    // resFile = "./res2/longN/ln_" + std::to_string(sampleNO);
    // testAlgorithms(tstFile, resFile);

    // tstFile = "./data/ch9-1.1/inputs/Random4-C/Random4-C." + std::to_string(sampleNO) + ".0";
    // resFile = "./res2/rand4C/r4c_" + std::to_string(sampleNO);
    // testAlgorithms(tstFile, resFile);

    // tstFile = "./data/ch9-1.1/inputs/Random4-n/Random4-n.1" + std::to_string(sampleNO) + ".0";
    // resFile = "./res2/rand4N/r4n_" + std::to_string(sampleNO);
    // testAlgorithms(tstFile, resFile);

    // tstFile = "./data/ch9-1.1/inputs/Square-C/Square-C." + std::to_string(sampleNO) + ".0";
    // resFile = "./res2/squareC/sc_" + std::to_string(sampleNO);
    // testAlgorithms(tstFile, resFile);

    tstFile = "./data/ch9-1.1/inputs/Square-n/Square-n.1" + std::to_string(sampleNO) + ".0";
    resFile = "./res2/squareN/sn_" + std::to_string(sampleNO);
    testAlgorithms(tstFile, resFile);
  }
}

int main(int argc, char **argv) {
  mainTest();
  // simpleTestAlgorithms();
  // simpleTestUtils();
  return 0;
}
