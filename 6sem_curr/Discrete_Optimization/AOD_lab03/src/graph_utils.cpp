// Graph Utils implementation
#include "graph_utils.hpp"

iList_t ssFromFile(string filename) {
  iList_t sources{};
  int sources_no;
  bool paramsGiven = false;

  std::fstream file(filename);
  if (file.is_open()) {
    string dump;
    char lineType = 'c';
    while (file >> lineType) {
      switch (lineType) {
        case 'p': { // read params
          if (paramsGiven) { std::getline(file, dump); break; }
          file >> dump >> dump >> dump; // skip 'aux sp ss'
          file >> sources_no; // read number of sources
          std::getline(file, dump); // dump rest of the line
          paramsGiven = true;
          break;
        }
        case 's': { // read edges
          uint64_t s;
          file >> s;
          sources.push_back(s);
          std::getline(file, dump);// dump rest of the line
          break;
        }
        default: // skip line
          std::getline(file, dump);
          break;
        }
    }
    file.close();
  }
  return sources;
}


iPairList_t p2pFromFile(string filename) {
  iPairList_t srcToDests{};
  int sources_no;
  bool paramsGiven = false;

  std::fstream file(filename);
  if (file.is_open()) {
    string dump;
    char lineType = 'c';
    while (file >> lineType) {
      switch (lineType) {
        case 'p': { // read params
          if (paramsGiven) { std::getline(file, dump); break; }
          file >> dump >> dump >> dump; // skip 'aux sp p2p'
          file >> sources_no; // read number of sources
          std::getline(file, dump); // dump rest of the line
          paramsGiven = true;
          break;
        }
        case 'q': { // read edges
          uint64_t s, d;
          file >> s >> d;
          srcToDests.push_back({s, d});
          std::getline(file, dump);// dump rest of the line
          break;
        }
        default: // skip line
          std::getline(file, dump);
          break;
        }
    }
    file.close();
  }
  return srcToDests;
}


Graph* grFromFile(string filename, graph_T T) {
  Graph* graph = NULL;
  int nodes, edges;
  bool paramsGiven = false;

  std::fstream file(filename);
  if (file.is_open()) {
    string dump;
    char lineType = 'c';
    while (file >> lineType) {
      switch (lineType) {
        case 'p': { // read params
          if (paramsGiven) { std::getline(file, dump); break; }
          file >> dump; // ski problem type (is always shortest path)
          file >> nodes; // read nodes
          file >> edges; // read edges
          graph = new Graph(nodes, edges, T); // init graph
          std::getline(file, dump); // dump rest of the line
          paramsGiven = true; break;
        }
        case 'a': { // read edges
          int u, v, w; // from, to, weight
          file >> u >> v >> w; // read data
          graph->addEdge(u, v, w); // add edge
          std::getline(file, dump);// dump rest of the line
          break;
        }
        default: // skip line
          std::getline(file, dump);
          break;
        }
    }
    file.close();
  }

  return graph;
}


int64_t execTime(Graph* graph, AlgType algtType, uint64_t source, uint64_t target) {
  switch (algtType) {
    case DIJKSTRA: {
      auto start = high_resolution_clock::now();
      graph->dijkstraPQ(source);
      auto stop = high_resolution_clock::now();
      auto duration = duration_cast<microseconds>(stop - start).count();
      return duration;
    }
    case DIJKSTRA_SINGLE: {
      auto start = high_resolution_clock::now();
      graph->dijkstraPQ(source, target);
      auto stop = high_resolution_clock::now();
      auto duration = duration_cast<microseconds>(stop - start).count();
      return duration;
    }
    case DIALS: {
      auto start = high_resolution_clock::now();
      graph->dialAlgorithm(source);
      auto stop = high_resolution_clock::now();
      auto duration = duration_cast<microseconds>(stop - start).count();
      return duration;
    }
    case DIALS_SINGLE: {
      auto start = high_resolution_clock::now();
      graph->dialAlgorithm(source, target);
      auto stop = high_resolution_clock::now();
      auto duration = duration_cast<microseconds>(stop - start).count();
      return duration;
    }
    case RADIX: {
      auto start = high_resolution_clock::now();
      graph->radixHeapAlg(source);
      auto stop = high_resolution_clock::now();
      auto duration = duration_cast<microseconds>(stop - start).count();
      return duration;
    }
    case RADIX_SINGLE: {
      auto start = high_resolution_clock::now();
      graph->radixHeapAlg(source, target);
      auto stop = high_resolution_clock::now();
      auto duration = duration_cast<microseconds>(stop - start).count();
      return duration;
    }
    case RADIX2: {
      auto start = high_resolution_clock::now();
      graph->radixHeapAlg2(source);
      auto stop = high_resolution_clock::now();
      auto duration = duration_cast<microseconds>(stop - start).count();
      return duration;
    }
    case RADIX2_SINGLE: {
      auto start = high_resolution_clock::now();
      graph->radixHeapAlg2(source, target);
      auto stop = high_resolution_clock::now();
      auto duration = duration_cast<microseconds>(stop - start).count();
      return duration;
    }
  }
  std::cerr << "INVALID INPUT OR STH" << std::endl;
  return 0LL;
}


void testAlgorithms(string filename, string resFilename) {
  string grFile  = filename + ".gr";
  string ssFile  = filename + ".ss";
  string p2pFile = filename + ".p2p";

  Graph* graph;
  int64_t time;
  int sampleCount;
  
  auto ss  = ssFromFile(ssFile);
  auto p2p = p2pFromFile(p2pFile);
  graph = grFromFile(grFile);

  // store results
  string ssResFile  = resFilename + "_ss_res.csv";
  string p2pResFile = resFilename + "_p2p_res.csv";

  // ----------- TEST SS -----------------------
  std::ofstream FileResSS(ssResFile);
  FileResSS << "VERTEXES"   << ";";
  FileResSS << "EDGES"      << ";";
  FileResSS << "MAX_WEIGHT" << ";";
  FileResSS << std::endl;

  FileResSS << graph->V << ";";
  FileResSS << graph->E << ";";
  FileResSS << graph->W << ";";
  FileResSS << std::endl;
  FileResSS << std::endl;

  FileResSS << "SOURCE"   << ";";
  FileResSS << "DIJKSTRA" << ";";
  FileResSS << "DIALS"    << ";";
  FileResSS << "RADIX"    << ";";
  FileResSS << "RADIX_2"  << ";";
  FileResSS << std::endl;

  sampleCount = 0;
  std::cout << filename << " : SS" << std::endl;
  for (auto s : ss) {
    FileResSS << s << ";";
    time = execTime(graph, DIJKSTRA, s);
    FileResSS << time << ";";
    time = execTime(graph, DIALS, s);
    FileResSS << time << ";";
    time = execTime(graph, RADIX, s);
    FileResSS << time << ";";
    time = execTime(graph, RADIX2, s);
    FileResSS << time << ";";
    std::cout << sampleCount << ", ";
    // finish line:
    FileResSS << std::endl;
    if (sampleCount > 50) {
      break;
    } sampleCount++;
  }
  std::cout << std::endl;
  FileResSS.close();

  // ----------- TEST P2P -----------------------
  std::ofstream FileResP2P(p2pResFile);
  FileResP2P << "VERTEXES"   << ";";
  FileResP2P << "EDGES"      << ";";
  FileResP2P << "MAX_WEIGHT" << ";";
  FileResP2P << std::endl;

  FileResP2P << graph->V << ";";
  FileResP2P << graph->E << ";";
  FileResP2P << graph->W << ";";
  FileResP2P << std::endl;
  FileResP2P << std::endl;

  FileResP2P << "SOURCE"   << ";";
  FileResP2P << "TARGET"   << ";";
  FileResP2P << "DIJKSTRA" << ";";
  FileResP2P << "DIALS"    << ";";
  FileResP2P << "RADIX"    << ";";
  FileResP2P << "RADIX_2"  << ";";
  FileResP2P << std::endl; // finish descriptions

  sampleCount = 0;
  std::cout << filename << " : P2P" << std::endl;
  for (auto p : p2p) {
    auto sr = p.first;
    auto tg = p.second;

    // source & target
    FileResP2P << sr << ";";
    FileResP2P << tg << ";";

    // execution times 
    time = execTime(graph, DIJKSTRA_SINGLE, sr, tg);
    FileResP2P << time << ";";
    time = execTime(graph, DIALS_SINGLE, sr, tg);
    FileResP2P << time << ";";
    time = execTime(graph, RADIX_SINGLE, sr, tg);
    FileResP2P << time << ";";
    time = execTime(graph, RADIX2_SINGLE, sr, tg);
    FileResP2P << time << ";";

    // finish line:
    FileResP2P << std::endl;

    std::cout << sampleCount << ", " ;
    if (sampleCount > 50) {
      break;
    } sampleCount++;
  }
  std::cout << std::endl;
  FileResP2P.close();

  // Finish testing
  delete graph;
}
