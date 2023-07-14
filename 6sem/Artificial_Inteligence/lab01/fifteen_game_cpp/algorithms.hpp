#ifndef ALGORITHMS_HPP
#define ALGORITHMS_HPP

#include "utils.hpp"

int manhatan_distance(int x, int y);

int manhatan_heuristic(int game_state[GAME_FIELDS]);
int linear_conflict_heuristic(int game_state[GAME_FIELDS]);

Result A_star(int initial_state[GAME_FIELDS], int (*heuristic) (int*));

bool is_game_completed(int game_state[GAME_FIELDS]);

#endif
