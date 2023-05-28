#include "game.hpp"

// ================== GAME IMPLEMENTATION =================
Game::Game(int player) {
  this->player_symbol = player;
  this->opponent_symbol = get_opponent(player);

  this->x_factor = (player_symbol == player_X) ? 1 : -1;
  this->o_factor = (player_symbol == player_O) ? 1 : -1;
  
  this->reset_board();
}

void Game::reset_board() {
  for (int i = 0; i < G_SIZE; ++i) 
    for(int j = 0; j < G_SIZE; ++j) 
      board[i][j] = 0;
}

void Game::update_board(int move_id) {
  do_move(Move(move_id), opponent_symbol);
}

int Game::find_best_move(int depth) {
  if (depth == 0) return null_move;
  Eval eval = alfa_beta(depth, player_symbol, _alfa, _beta);
  int move_id = eval.move_id;
  cout << eval.score << endl;
  do_move(Move(move_id), player_symbol);
  return move_id;
}

Eval Game::alfa_beta(int depth, int player, int alfa, int beta) {
  if (depth == 0) {
    if (player == player_symbol) return Eval(null_move, evaluate_board());
    if (player == opponent_symbol) return Eval(null_move, -evaluate_board());
  }

  if (winCheck(player)) return Eval(null_move, WINNING_SCORE);
  if (loseCheck(player)) return Eval(null_move, LOSING_SCORE);

  int curr_score = 0;
  int best_score = LOSING_SCORE;
  Move out_move;

  vector<Move> moves = possible_moves();
  if (moves.size() == 0) {
    return Eval(null_move, evaluate_board());
  }

  for (Move move : moves) {
    do_move(move, player);
    curr_score = -alfa_beta(depth-1, get_opponent(player), -beta, -alfa).score;

    if (curr_score >= beta) {
      undo_move(move);
      return Eval(move.to_id(), beta);
    }
    if (curr_score > best_score) {
      out_move = move;
      best_score = curr_score;

      if (curr_score > alfa)
        alfa = curr_score;
    }
    undo_move(move);
  }
  if (best_score == LOSING_SCORE) {
    out_move = moves.front();
  }
  return Eval(out_move.to_id(), best_score);
}

int Game::evaluate_board() {
  int eval = 0;
  int curr_eval  = 0;

  bool x_player_lost = false;
  bool o_player_lost = false;
  
  int curr_tuple[TUPLE_SIZE] = { 0 };
  for (auto tuple : tuples) {
    for (int i = 0; i < TUPLE_SIZE; ++i) {
      int row = tuple[i][0];
      int col = tuple[i][1];
      curr_tuple[i] = board[row][col];
    }
    
    curr_eval = evaluate_tuple(curr_tuple);
    switch (curr_eval) {
      case PLAYER_X_WON_EVAL:
        if (player_symbol == player_X) 
          return WINNING_SCORE;
        if (player_symbol == player_O) 
          return LOSING_SCORE;
      
      case PLAYER_O_WON_EVAL:
        if (player_symbol == player_O) 
          return WINNING_SCORE;
        if (player_symbol == player_X) 
          return LOSING_SCORE;

      case PLAYER_X_LOST_EVAL:
        x_player_lost = true; break;
      
      case PLAYER_O_LOST_EVAL: 
        o_player_lost = true; break;

      default: 
        eval += curr_eval; break;
    }
  } 
  
  int lost_check = game_lost_check(); // aditional check
  if (lost_check == PLAYER_X_LOST_EVAL) x_player_lost = true;
  if (lost_check == PLAYER_O_LOST_EVAL) o_player_lost = true;
  
  if (x_player_lost) {
    if (player_symbol == player_O) 
      return WINNING_SCORE;
    if (player_symbol == player_X) 
      return LOSING_SCORE;
  }

  if (o_player_lost) {
    if (player_symbol == player_X) 
      return WINNING_SCORE;
    if (player_symbol == player_O) 
      return LOSING_SCORE;
  }

  return eval;
}

int Game::evaluate_tuple(int* tuple) {
  int x_cnt = 0;
  int o_cnt = 0;

  for (int i = 0; i < TUPLE_SIZE; ++i) {
    if (tuple[i] == player_X) x_cnt++;
    else if (tuple[i] == player_O) o_cnt++;
  }

  if (x_cnt == 4) return PLAYER_X_WON_EVAL;
  if (o_cnt == 4) return PLAYER_O_WON_EVAL;

  if (x_cnt == 3 && tuple[1] == player_X && tuple[2] == player_X)
    return PLAYER_X_LOST_EVAL;
  if (o_cnt == 3 && tuple[1] == player_O && tuple[2] == player_O)
    return PLAYER_O_LOST_EVAL;
  
  if (o_cnt == 0) {
    switch (x_cnt) {
      case 1: return x_factor*EVAL_1;
      case 2: return x_factor*EVAL_2;
      case 3: return x_factor*EVAL_3;
      default: break;
    }
  }
  if (x_cnt == 0) {
    switch (o_cnt) {
      case 1: return o_factor*EVAL_1;
      case 2: return o_factor*EVAL_2;
      case 3: return o_factor*EVAL_3;
      default: break;
    }
  }
  
  return 0;
}

int Game::game_lost_check() {
  int symbol_cnt = 0;
  int curr_symbol = no_player;

  int curr_tuple[LOSING_TUPLE_SIZE] = { 0 };
  for (auto tuple : other_losing_tuples) {
    for (int i = 0; i < LOSING_TUPLE_SIZE; ++i) {
      int row = tuple[i][0];
      int col = tuple[i][1];
      curr_tuple[i] = board[row][col];
    }

    symbol_cnt = 1;
    curr_symbol = curr_tuple[0];
    if (curr_symbol == no_player) continue;
    
    for (int j = 1; j < LOSING_TUPLE_SIZE; ++j) {
      if (curr_tuple[j] == curr_symbol) 
        symbol_cnt++;
      else break;
    }

    if (symbol_cnt >= 3) {
      if (curr_symbol == player_X) return PLAYER_X_LOST_EVAL;
      if (curr_symbol == player_O) return PLAYER_O_LOST_EVAL;
    }
  }

  return 0;
}

vector<Move> Game::possible_moves() {
  vector<Move> moves = vector<Move>();
  for (int i = 0; i < G_SIZE; ++i) 
    for(int j = 0; j < G_SIZE; ++j) 
      if (board[i][j] == no_player) 
        moves.push_back(Move(i, j));

  return moves;
}

int Game::get_opponent(int player_symbol) {
  if (player_symbol == player_X) return player_O;
  if (player_symbol == player_O) return player_X;
  return no_player; // should not happen
}

void Game::do_move(Move move, int player) {
  board[move.row][move.col] = player;
}

void Game::undo_move(Move move) {
  board[move.row][move.col] = no_player;
}

bool Game::winCheck(int player) {
  for(int i = 0; i < 28; ++i)
    if( (board[win_[i][0][0]][win_[i][0][1]]==player) && (board[win_[i][1][0]][win_[i][1][1]]==player) && (board[win_[i][2][0]][win_[i][2][1]]==player) && (board[win_[i][3][0]][win_[i][3][1]]==player) )
      return true;
  return false;
}

bool Game::loseCheck(int player) {
  for(int i = 0; i < 48; ++i)
    if( (board[lose_[i][0][0]][lose_[i][0][1]]==player) && (board[lose_[i][1][0]][lose_[i][1][1]]==player) && (board[lose_[i][2][0]][lose_[i][2][1]]==player) )
      return true;
  return false;
}

// ================== MOVE IMPLEMENTATION =================
Move::Move() {
  this->row = -1;
  this->col = -1;
}

Move::Move(Move* move) {
  this->row = move->row;
  this->col = move->col;
}

Move::Move(int row, int col) {
  this->row = row;
  this->col = col;
}

Move::Move(int move_id) {
  int row = move_id / 10 - 1;
  int col = move_id % 10 - 1;
  this->row = row;
  this->col = col;
}

int Move::to_id() {
  return (this->row+1)*10 + (this->col + 1);
}


// ================== EVAL IMPLEMENTATION =================
Eval::Eval(int move_id, int score) {
  this->move_id  = move_id;
  this->score = score;
}
