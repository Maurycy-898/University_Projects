#ifndef GAME_HPP

# pragma once

#include <vector>
#include <iostream>

using std::vector, std::cout, std::endl;

// ========================= CONST VALUES =======================
const int WINNING_SCORE =  1000000; 
const int LOSING_SCORE  = -1000000;
const int MAX_SCORE = 1000000;

const int PLAYER_X_LOST_EVAL = -1;
const int PLAYER_O_LOST_EVAL = -2;
const int PLAYER_X_WON_EVAL = -11;
const int PLAYER_O_WON_EVAL = -12;

const int EVAL_1 = 10;
const int EVAL_2 = 40;
const int EVAL_3 = 100;

const int null_move = -1;
const int no_player =  0;
const int player_X  =  1;
const int player_O  =  2;

const int G_SIZE = 5;
const int TUPLE_SIZE = 4;
const int LOSING_TUPLE_SIZE = 3;
// All tuples of four in  game board
const int tuples[28][4][2] = { 
  { {0,0}, {0,1}, {0,2}, {0,3} },
  { {1,0}, {1,1}, {1,2}, {1,3} },
  { {2,0}, {2,1}, {2,2}, {2,3} },
  { {3,0}, {3,1}, {3,2}, {3,3} },
  { {4,0}, {4,1}, {4,2}, {4,3} },
  { {0,1}, {0,2}, {0,3}, {0,4} },
  { {1,1}, {1,2}, {1,3}, {1,4} },
  { {2,1}, {2,2}, {2,3}, {2,4} },
  { {3,1}, {3,2}, {3,3}, {3,4} },
  { {4,1}, {4,2}, {4,3}, {4,4} },
  { {0,0}, {1,0}, {2,0}, {3,0} },
  { {0,1}, {1,1}, {2,1}, {3,1} },
  { {0,2}, {1,2}, {2,2}, {3,2} },
  { {0,3}, {1,3}, {2,3}, {3,3} },
  { {0,4}, {1,4}, {2,4}, {3,4} },
  { {1,0}, {2,0}, {3,0}, {4,0} },
  { {1,1}, {2,1}, {3,1}, {4,1} },
  { {1,2}, {2,2}, {3,2}, {4,2} },
  { {1,3}, {2,3}, {3,3}, {4,3} },
  { {1,4}, {2,4}, {3,4}, {4,4} },
  { {0,1}, {1,2}, {2,3}, {3,4} },
  { {0,0}, {1,1}, {2,2}, {3,3} },
  { {1,1}, {2,2}, {3,3}, {4,4} },
  { {1,0}, {2,1}, {3,2}, {4,3} },
  { {0,3}, {1,2}, {2,1}, {3,0} },
  { {0,4}, {1,3}, {2,2}, {3,1} },
  { {1,3}, {2,2}, {3,1}, {4,0} },
  { {1,4}, {2,3}, {3,2}, {4,1} }
};
// losing triples that are not included in tuples
const int other_losing_tuples[4][3][2] {
  { {2,0}, {3,1}, {4,2} },
  { {2,4}, {3,3}, {4,2} },
  { {2,4}, {1,3}, {0,2} },
  { {2,0}, {1,1}, {0,2} },
}; 

// victory check 
const int win_[28][4][2] = { 
  { {0,0}, {0,1}, {0,2}, {0,3} },
  { {1,0}, {1,1}, {1,2}, {1,3} },
  { {2,0}, {2,1}, {2,2}, {2,3} },
  { {3,0}, {3,1}, {3,2}, {3,3} },
  { {4,0}, {4,1}, {4,2}, {4,3} },
  { {0,1}, {0,2}, {0,3}, {0,4} },
  { {1,1}, {1,2}, {1,3}, {1,4} },
  { {2,1}, {2,2}, {2,3}, {2,4} },
  { {3,1}, {3,2}, {3,3}, {3,4} },
  { {4,1}, {4,2}, {4,3}, {4,4} },
  { {0,0}, {1,0}, {2,0}, {3,0} },
  { {0,1}, {1,1}, {2,1}, {3,1} },
  { {0,2}, {1,2}, {2,2}, {3,2} },
  { {0,3}, {1,3}, {2,3}, {3,3} },
  { {0,4}, {1,4}, {2,4}, {3,4} },
  { {1,0}, {2,0}, {3,0}, {4,0} },
  { {1,1}, {2,1}, {3,1}, {4,1} },
  { {1,2}, {2,2}, {3,2}, {4,2} },
  { {1,3}, {2,3}, {3,3}, {4,3} },
  { {1,4}, {2,4}, {3,4}, {4,4} },
  { {0,1}, {1,2}, {2,3}, {3,4} },
  { {0,0}, {1,1}, {2,2}, {3,3} },
  { {1,1}, {2,2}, {3,3}, {4,4} },
  { {1,0}, {2,1}, {3,2}, {4,3} },
  { {0,3}, {1,2}, {2,1}, {3,0} },
  { {0,4}, {1,3}, {2,2}, {3,1} },
  { {1,3}, {2,2}, {3,1}, {4,0} },
  { {1,4}, {2,3}, {3,2}, {4,1} }
};
// failure check
const int lose_[48][3][2] = {
  { {0,0}, {0,1}, {0,2} }, { {0,1}, {0,2}, {0,3} }, { {0,2}, {0,3}, {0,4} }, 
  { {1,0}, {1,1}, {1,2} }, { {1,1}, {1,2}, {1,3} }, { {1,2}, {1,3}, {1,4} }, 
  { {2,0}, {2,1}, {2,2} }, { {2,1}, {2,2}, {2,3} }, { {2,2}, {2,3}, {2,4} }, 
  { {3,0}, {3,1}, {3,2} }, { {3,1}, {3,2}, {3,3} }, { {3,2}, {3,3}, {3,4} }, 
  { {4,0}, {4,1}, {4,2} }, { {4,1}, {4,2}, {4,3} }, { {4,2}, {4,3}, {4,4} }, 
  { {0,0}, {1,0}, {2,0} }, { {1,0}, {2,0}, {3,0} }, { {2,0}, {3,0}, {4,0} }, 
  { {0,1}, {1,1}, {2,1} }, { {1,1}, {2,1}, {3,1} }, { {2,1}, {3,1}, {4,1} }, 
  { {0,2}, {1,2}, {2,2} }, { {1,2}, {2,2}, {3,2} }, { {2,2}, {3,2}, {4,2} }, 
  { {0,3}, {1,3}, {2,3} }, { {1,3}, {2,3}, {3,3} }, { {2,3}, {3,3}, {4,3} }, 
  { {0,4}, {1,4}, {2,4} }, { {1,4}, {2,4}, {3,4} }, { {2,4}, {3,4}, {4,4} }, 
  { {0,2}, {1,3}, {2,4} }, { {0,1}, {1,2}, {2,3} }, { {1,2}, {2,3}, {3,4} }, 
  { {0,0}, {1,1}, {2,2} }, { {1,1}, {2,2}, {3,3} }, { {2,2}, {3,3}, {4,4} }, 
  { {1,0}, {2,1}, {3,2} }, { {2,1}, {3,2}, {4,3} }, { {2,0}, {3,1}, {4,2} }, 
  { {0,2}, {1,1}, {2,0} }, { {0,3}, {1,2}, {2,1} }, { {1,2}, {2,1}, {3,0} }, 
  { {0,4}, {1,3}, {2,2} }, { {1,3}, {2,2}, {3,1} }, { {2,2}, {3,1}, {4,0} }, 
  { {1,4}, {2,3}, {3,2} }, { {2,3}, {3,2}, {4,1} }, { {2,4}, {3,3}, {4,2} }
};

// ========================= CLASSES =========================
class Move {
  public:
    int row; 
    int col;

    Move();
    Move(Move* move);
    Move(int row, int col);
    Move(int move_id);

    int to_id();
};

class Eval {
  public:
    int move_id;
    int score;
    Eval(int move_id, int score);
};

class Game {
  public:
    Game(int player);

    void reset_board();
    void update_board(int move_id);

    int find_best_move(int depth);
    Eval alfa_beta(int depth, int player, int alfa, int beta);
    
  private:
    int player_symbol;
    int opponent_symbol;
    int board[G_SIZE][G_SIZE]; 

    int x_factor;
    int o_factor;
    
    const int _alfa = LOSING_SCORE;
    const int _beta = WINNING_SCORE;
    
    int evaluate_board();
    int evaluate_tuple(int* tuple);
    int game_lost_check();

    vector<Move> possible_moves();
    int get_opponent(int player);
    
    void do_move(Move move, int player);
    void undo_move(Move move);
    
    bool winCheck(int player);
    bool loseCheck(int player);
};

#endif // GAME_HPP
