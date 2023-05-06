
class PuzzleGame {
  constructor(columns, rows, startNew) {
    this.rows = rows;
    this.columns = columns;

    this.grid = JSON.parse(localStorage.getItem('puzzle-grid'));
    this.gridOriginal = localStorage.getItem('original-grid');

    if (this.grid === null || this.gridOriginal === null || startNew) {
      this.grid = [];
      this.createGrid();
      this.gridOriginal = JSON.stringify(this.grid);
      localStorage.setItem('original-grid', this.gridOriginal);
    }
  }

  createGrid() {
    for (let row = 0; row < this.rows; row++) {
      for (let col = 0; col < this.columns; col++) {
        if (col === 0 && row === 0) {
            this.grid.push(null);
        } else {
      		this.grid.push(new Location(col, row));
        }
      }
    }
  }

  getPiece(pieceLoc) {
    return this.grid[this.toLinearIdx(pieceLoc)];
  }

  setPiece(pieceLoc, value) {
    this.grid[this.toLinearIdx(pieceLoc)] = value;
  }

  getNeighbours(pieceLoc) {
    return [
      this.getPiece(new Location(pieceLoc.col + 1, pieceLoc.row)),
      this.getPiece(new Location(pieceLoc.col - 1, pieceLoc.row)),
      this.getPiece(new Location(pieceLoc.col, pieceLoc.row + 1)),
      this.getPiece(new Location(pieceLoc.col, pieceLoc.row - 1))
    ];
  }

	emptyFieldLocation() {
		const linearIdx = this.emptyFieldLinearIdx();
		const rowIdx = Math.floor(linearIdx / this.columns);
		const colIdx = linearIdx % this.columns;
		return new Location(colIdx, rowIdx);
	}

	emptyFieldLinearIdx() {
		return this.grid.indexOf(null);
	}

	toLinearIdx(pieceLoc) {
		return this.columns * pieceLoc.row + pieceLoc.col;
	}

	shuffle(steps) {
		for (let i = 0; i < steps; i++) {
			let idx = Math.floor(Math.random()*3.999)
			this.move(Direction[Object.keys(Direction)[idx]])
		} localStorage.setItem('puzzle-grid', JSON.stringify(this.grid));
	}

	move(direction) {
		const emptyLoc = this.emptyFieldLocation();
		switch (direction) {
			case Direction.UP:
				if (emptyLoc.row > 0) {
					const newLoc = new Location(emptyLoc.col, emptyLoc.row - 1);
					this.setPiece(emptyLoc, this.getPiece(newLoc));
					this.setPiece(newLoc, null);
				} break;

			case Direction.DOWN:
				if (emptyLoc.row < this.rows - 1) {
					const newLoc = new Location(emptyLoc.col, emptyLoc.row + 1);
					this.setPiece(emptyLoc, this.getPiece(newLoc));
					this.setPiece(newLoc, null);
				} break;

			case Direction.LEFT:
				if (emptyLoc.col > 0) {
					const newLoc = new Location(emptyLoc.col - 1, emptyLoc.row);
					this.setPiece(emptyLoc, this.getPiece(newLoc));
					this.setPiece(newLoc, null);
				} break;

			case Direction.RIGHT:
				if (emptyLoc.col < this.columns - 1) {
					const newLoc = new Location(emptyLoc.col + 1, emptyLoc.row);
					this.setPiece(emptyLoc, this.getPiece(newLoc));
					this.setPiece(newLoc, null);
				} break;
		}
	}

	makeMove(direction) {
		this.move(direction);
		if (this.isDone()) {
			alert('Congratulations, Puzzle Solved!');
		} localStorage.setItem('puzzle-grid', JSON.stringify(this.grid));
	}

	isDone() {
		return (JSON.stringify(this.grid) === this.gridOriginal); 
	}

	finish() {
		Object.keys(this).forEach(key => delete this[key]);
	}
}

// ======== UTILS ================================================================================
class Location {
	constructor(col, row) {
		this.col = col;
		this.row = row;
	}
}

const Direction = {
	UP: 'up', DOWN: 'down', LEFT: 'left', RIGHT: 'right'
}
