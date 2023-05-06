
class GameCanvas {
	constructor(canvas, game, imageElm) {
		this.image = imageElm;
		this.puzzleGame = game;
		this.canvas = canvas;
		this.ctx = canvas.getContext('2d');
		this.mousePosition = null;

		this.listeners = [
			this.onWindowResize.bind(this),
			this.onMouseMove.bind(this),
			this.onMouseOut.bind(this),
			this.onMouseDown.bind(this)
		]

		this.updateCanvasSize();
		this.setupEventListeners();
		this.repaint();
	}
	
	onMouseDown(event) {
		const x = event.offsetX;
		const y = event.offsetY;

		const col = Math.floor(x / this.width * this.puzzleGame.columns);
		const row = Math.floor(y / this.height * this.puzzleGame.rows);

		const p = this.puzzleGame;
		if (p.getPiece(new Location(col - 1, row)) === null) {
			p.makeMove(Direction.RIGHT);
		} else if (p.getPiece(new Location(col + 1, row)) === null) {
			p.makeMove(Direction.LEFT);
		} else if (p.getPiece(new Location(col, row - 1)) === null) {
			p.makeMove(Direction.DOWN);
		} else if (p.getPiece(new Location(col, row + 1)) === null) {
			p.makeMove(Direction.UP);
		}

		this.repaint();
	}

	onMouseMove(event) {
		this.mousePosition = {
			x: event.offsetX, 
			y: event.offsetY
		};
		this.repaint();
	}

	onMouseOut() {
		this.mousePosition = null;
		this.repaint();
	}

	onWindowResize() {
		this.updateCanvasSize();
		this.repaint();
	}

	updateCanvasSize() {
		let newWidth = document.body.clientWidth;
		let newHeight = document.body.clientHeight;

		if (newWidth > 700) { newWidth = 750; }
		if (newHeight > 400) { newHeight = 450; }

		this.canvas.width = newWidth - 50;
		this.canvas.height = newHeight - 50;
		
		this.width = newWidth - 50;
		this.height = newHeight - 50;
	}

	repaint() {
		const rows = this.puzzleGame.rows;
		const cols = this.puzzleGame.columns;

		this.ctx.clearRect(0, 0, this.width, this.height);
		for (let row = 0; row < rows; row++) {
			for (let col = 0; col < cols; col++) {
				const piece = this.puzzleGame.getPiece(new Location(col, row));
				const pieceX = col * this.width / cols;
				const pieceY = row * this.height / rows;
				const pieceWidth = this.width / cols;
				const pieceHeight = this.height / rows

				if (piece !== null) {
					this.ctx.drawImage(this.image,
							piece.col*this.image.naturalWidth / cols,
							piece.row*this.image.naturalHeight / rows,
							this.image.naturalWidth / cols,
							this.image.naturalHeight / rows,
							pieceX, pieceY, pieceWidth, pieceHeight
					);
				} else {
					this.ctx.fillStyle = "#8be9fd";
					this.ctx.fillRect(pieceX, pieceY, pieceWidth, pieceHeight);  
				}
				
				if (
					this.mousePosition !== null && this.puzzleGame.getNeighbours(new Location(col, row)).includes(null)
					&& this.mousePosition.x >= col * pieceWidth && this.mousePosition.x <= (col + 1) * pieceWidth
					&& this.mousePosition.y >= row * pieceHeight && this.mousePosition.y <= (row + 1) * pieceHeight
				) {
					this.ctx.fillStyle = "#ffffff50";
					this.ctx.fillRect(pieceX, pieceY, pieceWidth, pieceHeight);
				}
				this.ctx.strokeStyle = "#f8f8f2"
				this.ctx.strokeRect(pieceX, pieceY, pieceWidth, pieceHeight);
			}
		}
	}

	setupEventListeners() {
		window.addEventListener('resize', this.listeners[0], true);
		this.canvas.addEventListener('mousemove', this.listeners[1], true);
		this.canvas.addEventListener('mouseout',  this.listeners[2], true);
		this.canvas.addEventListener('mousedown', this.listeners[3], true);
	}

	removeEventListeners() {
		window.removeEventListener('resize', this.listeners[0], true);
		this.canvas.removeEventListener('mousemove', this.listeners[1], true);
		this.canvas.removeEventListener('mouseout',  this.listeners[2], true);
		this.canvas.removeEventListener('mousedown', this.listeners[3], true);
	}

	finish() {
		this.removeEventListeners();
		Object.keys(this).forEach(key => delete this[key]);
	}
}
