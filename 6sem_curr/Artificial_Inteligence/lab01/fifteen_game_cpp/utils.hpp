#ifndef UTILS_HPP
#define UTILS_HPP

#include <iostream>
#include <queue> 
#include <unordered_map>
#include <set>

using std::string, std::unordered_map, std::priority_queue, std::cout, std::endl;

// keep track of game states
typedef unordered_map<GameState, string> state_map;

// How many rows and columns are in the game
const int GAME_SIZE = 4;

// How many fields are in the game
const int GAME_FIELDS = 16;

// To represent this game empty field
const int EMPTY_FIELD = 0;

// Represents result of the solver algorithm
class Result {
    public:
        long visited_states;
        long execution_time;
};

// Represent game state
class GameState {
    public:
        string state_id;
        int g_score;
        int h_score;
        int f_score;
};

#endif
