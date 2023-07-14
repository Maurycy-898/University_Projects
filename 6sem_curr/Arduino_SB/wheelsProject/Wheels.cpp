#include <Arduino.h>

#include "Wheels.h"

// #define SET_MOVEMENT(side,f,b) ( digitalWrite( side[0], f ) ); digitalWrite( side[1], b ) )
// #define SET_MOVEMENT(s,f,b) delay(10);
#define SET_MOVEMENT(side,f,b) digitalWrite( side[0], f);\
                               digitalWrite( side[1], b);
#define LOW 0
#define HIGH 1

const int BASE_SPEED = 150;


Wheels::Wheels() { this->setSpeed(BASE_SPEED); }

void Wheels::attachRight(int pF, int pB, int pS) {
  pinMode(pF, OUTPUT);
  pinMode(pB, OUTPUT);
  pinMode(pS, OUTPUT);
  this->pinsRight[0] = pF;
  this->pinsRight[1] = pB;
  this->pinsRight[2] = pS;
}


void Wheels::attachLeft(int pF, int pB, int pS) {
  pinMode(pF, OUTPUT);
  pinMode(pB, OUTPUT);
  pinMode(pS, OUTPUT);
  this->pinsLeft[0] = pF;
  this->pinsLeft[1] = pB;
  this->pinsLeft[2] = pS;
}

void Wheels::setSpeedRight(uint8_t s) {
  analogWrite(this->pinsRight[2], s);
}

void Wheels::setSpeedLeft(uint8_t s) {
  analogWrite(this->pinsLeft[2], s);
}

void Wheels::setSpeed(uint8_t s) {
  setSpeedLeft(s);
  setSpeedRight(s);
}

void Wheels::attach(int pRF, int pRB, int pRS, int pLF, int pLB, int pLS) {
  this->attachRight(pRF, pRB, pRS);
  this->attachLeft(pLF, pLB, pLS);
}

void Wheels::forwardLeft() {
  SET_MOVEMENT(pinsLeft, HIGH, LOW);
}

void Wheels::forwardRight() {
  SET_MOVEMENT(pinsRight, HIGH, LOW);
}

void Wheels::backLeft() {
  SET_MOVEMENT(pinsLeft, LOW, HIGH);
}

void Wheels::backRight() {
  SET_MOVEMENT(pinsRight, LOW, HIGH);
}

void Wheels::forward() {
  this->forwardLeft();
  this->forwardRight();
}

void Wheels::back() {
  this->backLeft();
  this->backRight();
}

void Wheels::stopLeft() {
  SET_MOVEMENT(pinsLeft, LOW, LOW);
}

void Wheels::stopRight() {
  SET_MOVEMENT(pinsRight, LOW, LOW);
}

void Wheels::stop() {
  this->stopLeft();
  this->stopRight();
}

//--------------------------------- LAB TASK FUNCTIONS --------------------------------------------------------

  // int a;  
  // int b = (PINC & (1 << PC0));
  // while (this->changes < 2*cm) {
  //   a = (PINC & (1 << PC0));
  //   if (a != b) { this->changes++; }
  // }

void Wheels::goForward(int cm) {
  this->setSpeed(BASE_SPEED);
  this->forward();

  this->changes = 0;
  while ((this->changes / 2) < 2*cm);

  this->setSpeed(BASE_SPEED);
  this->stop();
}

void Wheels::goBack(int cm) {
  this->setSpeed(BASE_SPEED);
  this->back();

  this->changes = 0;
  while ((this->changes / 2) < 2*cm);
  
  this->setSpeed(BASE_SPEED);
  this->stop();
}

void Wheels::turnLeft(int degree) {
  double x = 3.0;
  this->setSpeed(200);
  this->backLeft();
  this->forwardRight();

  this->changes = 0;  
  while (((double) this->changes / 2.0) < ((double) degree / x));

  this->setSpeed(BASE_SPEED);  
  this->stop();
}

void Wheels::turnRight(int degree) {
  double x = 3.0;
  this->setSpeed(200);
  this->forwardLeft();
  this->backRight();

  this->changes = 0;  
  while (((double) this->changes / 2.0) < ((double) degree / x));

  this->setSpeed(BASE_SPEED);
  this->stop();
}

int Wheels::leftDirection() {
  if (digitalRead(pinsLeft[0]) == HIGH) {
    return 1;
  } else if (digitalRead(pinsLeft[1]) == HIGH) {
    return -1;
  }
  return 0;
}

int Wheels::rightDirection() {
  if (digitalRead(pinsRight[0]) == HIGH) {
    return 1;
  } else if (digitalRead(pinsRight[1]) == HIGH) {
    return -1;
  }
  return 0;
}

