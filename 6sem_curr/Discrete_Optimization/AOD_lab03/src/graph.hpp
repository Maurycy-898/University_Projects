#ifndef __GRAPH_HPP__
#pragma once

#include <algorithm>
#include <queue>
#include <list>
#include <vector>
#include <unordered_set>
#include <string>
#include <cmath>
#include <climits>
#include <iostream>
#include "radix_heap.hpp"


// CONST VALUES
const uint64_t NO_PATH = INT_MAX;
const uint64_t INF_DIST = INT_MAX;
enum graph_T { DIRECTED, UNDIRECTED };

// HELPFUL DEFINED TYPES
typedef std::pair<uint64_t, uint64_t> edge_t;
typedef std::list<uint64_t> bucket_t;

typedef std::list<edge_t> edgeList_t;
typedef std::vector<edge_t> edgeVect_t;

typedef std::list<uint64_t> iList_t;
typedef std::vector<uint64_t> iVect_t;

typedef radix_heap::pair_radix_heap<uint64_t, uint64_t> radixHeap_t;
typedef std::priority_queue <edge_t, edgeVect_t,
        std::greater<edge_t>> minHeap_t; // min binaary heap


// GRAPH IMPL
class Graph {
  private:
    graph_T type;
    edgeVect_t *adj;

  public:
    uint64_t V, E, W; // vertices, edges

    // creating new graph:
    void addEdge(uint64_t u, uint64_t v, uint64_t w);
    Graph(uint64_t V, uint64_t E = 0, graph_T T = DIRECTED);
    ~Graph(); 

    // shortest path algorithms:
    iVect_t dijkstraPQ(uint64_t s);
    iVect_t dialAlgorithm(uint64_t s);
    iVect_t radixHeapAlg(uint64_t s);
    iVect_t radixHeapAlg2(uint64_t s);
    
    // only to given destination
    uint64_t dijkstraPQ(uint64_t s, uint64_t d);
    uint64_t dialAlgorithm(uint64_t s, uint64_t d);
    uint64_t radixHeapAlg(uint64_t s, uint64_t d);
    uint64_t radixHeapAlg2(uint64_t s, uint64_t d);
};


// RADIX HEAAP IMPL
typedef std::pair<uint64_t, uint64_t> range_t;
typedef std::pair<uint64_t, uint64_t> iPair_t;
typedef std::list<iPair_t> iPairList_t;

class RadixBucket {
  public:
    static constexpr range_t NO_RANGE = {-1ULL, -1ULL};
    static constexpr iPair_t NO_PAIR  = {-1ULL, -1ULL};

    range_t range;
    iPairList_t vtxs;
    
    RadixBucket();
    RadixBucket(range_t range);
    RadixBucket(uint64_t from, uint64_t to);

    void addVtx(uint64_t vtx, uint64_t weight);
    void addVtx(iPair_t vtx);

    void erase(iPair_t vtx);
    uint64_t min();
    iPair_t first();
    
    void pop_first();
    void clear();
    bool empty();
    bool inRange(uint64_t w);
    
    // void printBucket() {
    //   std::cout << "rng:[" << range.first << "," << range.second << "], elems:{";
    //   for (auto e : vtxs) {
    //     std::cout << e.second << "(" << e.first << "),";
    //   }
    //   std::cout << "}" << std::endl;
    // }
};


// represents vector of buckets
typedef std::vector<RadixBucket> bucketVect_t;

class RadixHeap {
  uint64_t heapSize;
  uint64_t bucketsSize;
  iVect_t bucketMap;
  bucketVect_t buckets;
  
  public:
    RadixHeap(uint64_t V, uint64_t W);

    void push(iPair_t vtx);
    void erase(iPair_t vtx);
    iPair_t top();

    void pop();
    bool empty();

  private:
    const uint64_t CHECKED = -1;
    const uint64_t ERR_IDX = -2;
    
    void reArrange();
    void updateRanges(uint64_t minDist, uint64_t updateRange) ;
    void initBuckets();
    uint64_t findBucket(iPair_t vtx);
};

#endif /* __GRAPH_HPP__ */
