// Graph implementation
#include "graph.hpp"

// =============== GRAPH CLASS =============================
Graph::Graph(uint64_t V, uint64_t E, graph_T T) {
  this->V = V, this->E = E, this->type = T; this->W = 0;
  this->adj = new edgeVect_t[V + 1];
}

Graph::~Graph() { 
  delete[] this->adj; 
}

void Graph::addEdge(uint64_t u, uint64_t v, uint64_t w) {
  if (this->type == DIRECTED) {
    adj[u].push_back({w, v});
  } else { // UNDIRECTED
    adj[u].push_back({w, v});
    adj[v].push_back({w, u});
  }
  if (w > this->W) this->W = w;
}

// ------ SHORTEST PATH METHODS ------
// DIJKSTRA (STL priority queue - binary heap)
iVect_t Graph::dijkstraPQ(uint64_t s) {
  minHeap_t minPQ{ }; // priority queue
  iVect_t dists(V + 1, INF_DIST); // distances
  
  dists[s] = 0;
  minPQ.push({0, s});
  
  while (!minPQ.empty()) {
    uint64_t v = minPQ.top().second;
    uint64_t vw = minPQ.top().first;
    minPQ.pop();
    if (vw > dists[v]) continue;   
    
    for (auto vtx : adj[v]) {
      uint64_t u = vtx.second; // adjacent
      uint64_t nW = vtx.first + vw; // weight
        
      if (dists[u] > nW) {
        dists[u] = nW;
        minPQ.push({nW, u});
      }
    }
  } 
  // return list of distances
  return dists;
}

uint64_t Graph::dijkstraPQ(uint64_t s, uint64_t d) {
  minHeap_t minPQ{ }; // priority queue
  iVect_t dists(V + 1, INF_DIST); // distances
  
  dists[s] = 0;
  minPQ.push({0, s});
  
  while (!minPQ.empty()) {
    uint64_t v = minPQ.top().second;
    uint64_t vw = minPQ.top().first;

    if (v == d) return dists[v];
    minPQ.pop();
    if (vw > dists[v]) continue;   
    
    for (auto vtx : adj[v]) {
      uint64_t u = vtx.second; // adjacent
      uint64_t nW = vtx.first + vw; // weight
        
      if (dists[u] > nW) {
        dists[u] = nW;
        minPQ.push({nW, u});
      }
    }
  }
  // when no path found to destination 
  return NO_PATH;
}

// DIAL ALGORITHM
iVect_t Graph::dialAlgorithm(uint64_t s) {
  uint64_t bCycleSize = W   + 1ULL;
  iVect_t dists(V + 1, INF_DIST);
  bucket_t* buckets = new bucket_t[bCycleSize];
  auto bucketmap = new bucket_t::iterator[V + 1];

  dists[s] = 0ULL;
  buckets[0].push_back(s);
  bucketmap[s] = (--buckets[0].end());

  uint64_t emptyCnt = 0ULL;
  uint64_t bCycleIdx = 0ULL;
  while (true) {
    emptyCnt = 0ULL;
    while (emptyCnt < bCycleSize && buckets[bCycleIdx].empty()) {
      bCycleIdx = (bCycleIdx+1ULL) % (bCycleSize);
      emptyCnt += 1ULL;
    } if (emptyCnt >= bCycleSize) break;

    uint64_t v = buckets[bCycleIdx].front();
    buckets[bCycleIdx].pop_front();

    for (auto adjEdge : adj[v]) {
      uint64_t w = adjEdge.first;  // weight 
      uint64_t u = adjEdge.second; // adjacent
      
      uint64_t altDist = dists[v] + w;
      uint64_t currentDist = dists[u]; 

      if (altDist < currentDist) {
        if (currentDist != INF_DIST) {
          buckets[(currentDist % bCycleSize)].erase(bucketmap[u]);
        }
        dists[u] = altDist;
        auto newBucket = altDist % bCycleSize;
        buckets[newBucket].push_back(u);
        bucketmap[u] = (--buckets[newBucket].end());
      }
    }
  }
  delete[] buckets;
  delete[] bucketmap;
  return dists;
}

uint64_t Graph::dialAlgorithm(uint64_t s, uint64_t d) {
  uint64_t bCycleSize = W   + 1ULL;
  iVect_t dists(V + 1, INF_DIST);
  bucket_t* buckets = new bucket_t[bCycleSize];
  auto bucketmap = new bucket_t::iterator[V + 1];

  dists[s] = 0ULL;
  buckets[0].push_back(s);
  bucketmap[s] = (--buckets[0].end());

  uint64_t emptyCnt = 0ULL;
  uint64_t bCycleIdx = 0ULL;
  while (true) {
    emptyCnt = 0ULL;
    while (emptyCnt < bCycleSize && buckets[bCycleIdx].empty()) {
      bCycleIdx = (bCycleIdx+1ULL) % (bCycleSize);
      emptyCnt += 1ULL;
    } if (emptyCnt >= bCycleSize) break;

    uint64_t v = buckets[bCycleIdx].front();
    if (v == d) {
      delete[] buckets;
      delete[] bucketmap;
      return dists[v];
    }
    buckets[bCycleIdx].pop_front();

    for (auto adjEdge : adj[v]) {
      uint64_t w = adjEdge.first;  // weight 
      uint64_t u = adjEdge.second; // adjacent
      
      uint64_t altDist = dists[v] + w;
      uint64_t currentDist = dists[u]; 

      if (altDist < currentDist) {
        if (currentDist != INF_DIST) {
          buckets[(currentDist % bCycleSize)].erase(bucketmap[u]);
        }
        dists[u] = altDist;
        auto newBucket = altDist % bCycleSize;
        buckets[newBucket].push_back(u);
        bucketmap[u] = (--buckets[newBucket].end());
      }
    }
  }
  delete[] buckets;
  delete[] bucketmap;
  return NO_PATH;
}

// RADIX HEAP
iVect_t Graph::radixHeapAlg(uint64_t s) {
  uint64_t heapSize = ceil(log2(W*V)) + 1ULL;
  RadixHeap minPQ(V, heapSize); // priority queue
  iVect_t dists(V+1, INF_DIST); // distances
  
  dists[s] = 0ULL;
  minPQ.push({0ULL, s});
  
  while (!minPQ.empty()) {
    auto x = minPQ.top();
    uint64_t v = x.second;
    uint64_t vw = x.first;
    minPQ.pop();
    if (vw > dists[v]) { continue; }

    for (auto vtx : adj[v]) {     
      uint64_t u = vtx.second; // adjacent
      uint64_t nW = vtx.first + vw; // weight
        
      if (dists[u] > nW) {
        dists[u] = nW;
        minPQ.push({nW, u});
      }
    }
  } 
  // return list of distances
  return dists;
}

uint64_t Graph::radixHeapAlg(uint64_t s, uint64_t d) {
  uint64_t heapSize = ceil(log2(W*V)) + 1ULL;
  RadixHeap minPQ(V, heapSize); // priority queue
  iVect_t dists(V+1, INF_DIST); // distances
  
  dists[s] = 0ULL;
  minPQ.push({0ULL, s});
  
  while (!minPQ.empty()) {
    auto x = minPQ.top();
    uint64_t v = x.second;
    uint64_t vw = x.first;
    
    if (v == d) { return dists[v]; }
    minPQ.pop();
    if (vw > dists[v]) { continue; }

    for (auto vtx : adj[v]) {     
      uint64_t u = vtx.second; // adjacent
      uint64_t nW = vtx.first + vw; // weight
        
      if (dists[u] > nW) {
        dists[u] = nW;
        minPQ.push({nW, u});
      }
    }
  } 
  // when no path found to destination 
  return NO_PATH;
}

// RADIX HEAP (WITH MIT HEAP)
iVect_t Graph::radixHeapAlg2(uint64_t s) {
  radixHeap_t minPQ; // priority queue
  iVect_t dists(V+1, INF_DIST); // distances
  
  dists[s] = 0ULL;
  minPQ.emplace(0ULL, s);
  
  while (!minPQ.empty()) {
    uint64_t v = minPQ.top_value();
    uint64_t vw = minPQ.top_key();
    minPQ.pop();
    if (vw > dists[v]) { continue; }

    for (auto vtx : adj[v]) {     
      uint64_t u = vtx.second; // adjacent
      uint64_t nW = vtx.first + vw; // weight
        
      if (dists[u] > nW) {
        dists[u] = nW;
        minPQ.emplace(nW, u);
      }
    }
  } 
  // return list of distances
  return dists;
}

uint64_t Graph::radixHeapAlg2(uint64_t s, uint64_t d) {
  radixHeap_t minPQ; // priority queue
  iVect_t dists(V+1, INF_DIST); // distances
  
  dists[s] = 0ULL;
  minPQ.emplace(0ULL, s);
  
  while (!minPQ.empty()) {
    uint64_t v = minPQ.top_value();
    uint64_t vw = minPQ.top_key();
    
    if (v == d) { return dists[v]; }
    minPQ.pop();
    if (vw > dists[v]) { continue; }

    for (auto vtx : adj[v]) {     
      uint64_t u = vtx.second; // adjacent
      uint64_t nW = vtx.first + vw; // weight
        
      if (dists[u] > nW) {
        dists[u] = nW;
        minPQ.emplace(nW, u);
      }
    }
  } 
  // when no path found to destination 
  return NO_PATH;
}



// =============== RADIX HEAP IMPLEMENTATION =============================
// ------ RADIX BUCKET ------
RadixBucket::RadixBucket() { /* no range */ }

RadixBucket::RadixBucket(range_t range) {
  this->range = range;
}

RadixBucket::RadixBucket(uint64_t from, uint64_t to) {
  this->range = {from, to};
}

void RadixBucket::addVtx(uint64_t vtx, uint64_t weight) {
  this->vtxs.push_back({weight, vtx});
}

void RadixBucket::addVtx(iPair_t vtx) {
  this->vtxs.push_back(vtx);
}

void RadixBucket::erase(iPair_t vtx) {
  for (auto it = this->vtxs.begin(); it != this->vtxs.end(); ++it) 
    if (it->second == vtx.second) it = this->vtxs.erase(it);
}

uint64_t RadixBucket::min() {
  uint64_t min = INF_DIST;
  for (auto vtx : this->vtxs) {
    if (vtx.first < min) min = vtx.first;
  }
  return min;
}

iPair_t RadixBucket::first() {
  return this->vtxs.front();
}

void RadixBucket::pop_first() {
  if (!this->empty())
    this->vtxs.pop_front();
}

bool RadixBucket::empty() {
  return this->vtxs.empty();
}

void RadixBucket::clear() {
  this->vtxs.clear();
}

bool RadixBucket::inRange(uint64_t w) {
  if (this->range.first <= w) {
    if (this->range.second >= w) 
      return true;
  } return false;
}

// ----------------------------RADIX HEAP ----------------------------
RadixHeap::RadixHeap(uint64_t V, uint64_t size) {
  this->bucketsSize = size;
  this->buckets.resize(size);
  this->bucketMap.resize(V + 1);
  this->initBuckets();
}

void RadixHeap::push(iPair_t vtx) {
  uint64_t bucketIdx = this->findBucket(vtx);
  this->buckets[bucketIdx].addVtx(vtx);
  this->bucketMap[vtx.second] = bucketIdx;
  this->heapSize++;
}

void RadixHeap::erase(iPair_t vtx) {
  uint64_t bucketIdx = this->bucketMap[vtx.second];
  this->buckets[bucketIdx].erase(vtx);
  this->bucketMap[vtx.second] = CHECKED;
  if (this->heapSize > 0) { this->heapSize--; }
}

iPair_t RadixHeap::top() {
  if (this->buckets.front().empty()) {
    this->reArrange();
  }
  return this->buckets.front().first();
}

void RadixHeap::pop() {
  iPair_t v = this->top();
  this->bucketMap[v.second] = CHECKED;
  // this->erase(v);
  this->buckets.front().pop_first();
  if (this->heapSize > 0) { this->heapSize--; }
}

bool RadixHeap::empty() {
  if (this->buckets.front().empty()) {
    this->reArrange();
  }
  return this->buckets.front().empty();
}

void RadixHeap::reArrange() {
  if (!this->buckets.front().empty()) return;

  int firstNotEmptyIdx = 0;
  while (this->buckets[firstNotEmptyIdx].empty() && (uint64_t)firstNotEmptyIdx < this->bucketsSize) { 
    firstNotEmptyIdx++;
  }  if ((uint64_t)firstNotEmptyIdx == this->bucketsSize) return;
  
  this->updateRanges(this->buckets[firstNotEmptyIdx].min(), firstNotEmptyIdx);
  this->buckets[firstNotEmptyIdx].range = RadixBucket::NO_RANGE;
  while (!this->buckets[firstNotEmptyIdx].empty()) {
    iPair_t vtx = this->buckets[firstNotEmptyIdx].first();
    uint64_t bucketIdx = this->findBucket(vtx, firstNotEmptyIdx-1);
    this->buckets[bucketIdx].addVtx(vtx);
    this->bucketMap[vtx.second] = bucketIdx;
    this->buckets[firstNotEmptyIdx].pop_first();
  }
}

void RadixHeap::updateRanges(uint64_t minDist, uint64_t updateRange) {
  this->buckets[0] = RadixBucket(minDist, minDist);
  uint64_t fromOffset = 1, toOffset = 1;
  uint64_t from = minDist+fromOffset, to = minDist+toOffset;
  
  for (uint64_t i = 1; i < updateRange; ++i) {
    this->buckets[i] = RadixBucket(from, to);
    fromOffset = toOffset+1; toOffset = 2*toOffset + 1;
    from = minDist+fromOffset; to = minDist+toOffset;
  }
}

void RadixHeap::initBuckets() {
  this->buckets[0] = RadixBucket(0, 0);
  uint64_t from = 1, to = 1;
  for (uint64_t i = 1; i < this->bucketsSize; ++i) {
    this->buckets[i] = RadixBucket(from, to);
    from = to + 1; to = 2*to + 1;
  }
}

uint64_t RadixHeap::findBucket(iPair_t vtx) {
  uint64_t weight = vtx.first;
  for (int i = (int) this->bucketsSize-1; i >= 0; --i) {
    if (this->buckets[i].inRange(weight)) { 
      return i;
    }
  } throw "No bucket containing vertex found";
}

uint64_t RadixHeap::findBucket(iPair_t vtx, int from) {
  uint64_t weight = vtx.first;
  for (int i = from; i >= 0; --i) {
    if (this->buckets[i].inRange(weight)) { 
      return i;
    }
  } throw "No bucket containing vertex found";
}
