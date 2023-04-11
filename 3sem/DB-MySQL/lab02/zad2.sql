USE `db-aparaty`;

CREATE TABLE Matryca(
 ID int PRIMARY KEY AUTO_INCREMENT NOT NULL,
 przekatna decimal(4,2) NOT NULL,
 rozdzielczosc decimal(3,1) NOT NULL,
 typ varchar(10) NOT NULL,
 CHECK(przekatna > 0 AND rozdzielczosc > 0) 
 ) AUTO_INCREMENT = 100;

CREATE TABLE Obiektyw(
ID int PRIMARY KEY AUTO_INCREMENT NOT NULL, 
model varchar(30) NOT NULL, 
minPrzeslona float NOT NULL, 
maxPrzeslona float NOT NULL,
CHECK(minPrzeslona > 0 AND minPrzeslona < maxPrzeslona)
);

CREATE TABLE Producent (
 ID int PRIMARY KEY AUTO_INCREMENT NOT NULL,
 nazwa varchar(50) NULL,
 kraj varchar(20) NULL
 );
 
 CREATE TABLE  Aparat(
model varchar(30) PRIMARY KEY NOT NULL,
producent int NOT NULL,
matryca int NOT NULL,
obiektyw int NOT NULL,
typ enum('kompaktowy', 'lustrzanka','profesjonalny', 'inny'),
FOREIGN KEY (producent) REFERENCES Producent(ID) ON DELETE CASCADE,
FOREIGN KEY (matryca) REFERENCES Matryca(ID) ON DELETE CASCADE,
FOREIGN KEY (obiektyw) REFERENCES Obiektyw(ID) ON DELETE CASCADE
);

DROP TABLE Aparat;
DROP TABLE Producent;
DROP TABLE Matryca;
DROP TABLE Obiektyw;

