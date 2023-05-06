const canvasElm = document.getElementById('game-canvas');
const colsInput = document.getElementById('columns');
const rowsInput = document.getElementById('rows');

let gameCanvas = null; 
let puzzleGame = null;

const imgSRC = [
	{path : 'images/img1.jpg'},
	{path : 'images/img2.jpg'},
	{path : 'images/img3.jpg'},
	{path : 'images/img4.jpg'},
	{path : 'images/img5.jpg'}
];

function init() {
	gallerySetup();
	listenerSetup();
	
	let gameImg = document.getElementById(localStorage.getItem('gameImg'));
	if (gameImg !== null) {
		let rows = Number(localStorage.getItem('rows'));
		let cols = Number(localStorage.getItem('cols'));
		puzzleGame = new PuzzleGame(cols, rows, false);
		gameCanvas = new GameCanvas(canvasElm, puzzleGame, gameImg);
		canvasElm.scrollIntoView();
	}
}

function gallerySetup() {
	const gameImgID = "gallery-img"
	const galleryContainer = document.querySelector('#image-gallery');
	let idIdx = 0;
	imgSRC.forEach(img => {
		galleryContainer.innerHTML += "<img src=\""+img.path+"\""+"id=\""+gameImgID+""+idIdx+"\"/>";
		idIdx++;
	});
}

function listenerSetup() {
	const images = document.querySelectorAll('#image-gallery > img');
	images.forEach((img) => {
		img.addEventListener('click', () => {
			startNewGame(img);
			localStorage.setItem('gameImg', img.id);
		})
	});
}

function startNewGame(image) {
	let cols = Number(colsInput.value);
	let rows = Number(rowsInput.value);

	localStorage.setItem('cols', cols);
	localStorage.setItem('rows', rows);

	if (gameCanvas !== null && puzzleGame !== null) {
		gameCanvas.finish();
		puzzleGame.finish();
	}

	delete gameCanvas;
	delete puzzleGame;

	puzzleGame = new PuzzleGame(cols, rows, true);
	puzzleGame.shuffle(cols*rows*10);

	gameCanvas = new GameCanvas(canvasElm, puzzleGame, image);
	canvasElm.scrollIntoView();
}
