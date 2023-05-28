#ifndef __GRAPH_UTILS_HPP__
#pragma once

#include <fstream>
#include <bits/stdc++.h>
#include "graph.hpp"

using std::time_t, std::time;

enum AlgType { 
  DIJKSTRA, DIJKSTRA_SINGLE, 
  DIALS, DIALS_SINGLE,
  RADIX, RADIX_SINGLE
};

Graph* graphFromFile(std::string filename, graph_T T = DIRECTED);
time_t executionTime(std::string filename, Graph* graph, AlgType algtType, int source, int target=0);



#endif /* __GRAPH_UTILS_HPP__ */
