
#include "algorithms.hpp"

Result perform_A_star(int initial_state[GAME_FIELDS], int (*heuristic) (int*)) {
    priority_queue<GameState> open_set;
    state_map close_set;
    
    return Result();
}

int manhatan_distance(int x, int y);

int manhatan_heuristic(int game_state[GAME_FIELDS]);

int linear_conflict_heuristic(int game_state[GAME_FIELDS]);

