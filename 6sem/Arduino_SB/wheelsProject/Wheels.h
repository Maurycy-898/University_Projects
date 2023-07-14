#include <Arduino.h>

#ifndef Wheels_h
#define Wheels_h
extern volatile int changes, cnt1, cnt2;

class Wheels {
  public:
    Wheels();
    void attachRight(int pinForward, int pinBack, int pinSpeed);
    void attachLeft(int pinForward, int pinBack, int pinSpeed);
    void attach(int pinRightForward, int pinRightBack, int pinRightSpeed,
                int pinLeftForward, int pinLeftBack, int pinLeftSpeed);
    void forward();
    void forwardLeft();
    void forwardRight();
    void back();
    void backLeft();
    void backRight();
    void stop();
    void stopLeft();
    void stopRight();
    void setSpeed(uint8_t);
    void setSpeedRight(uint8_t);
    void setSpeedLeft(uint8_t);
    // LAB TASK FUNCTIONS
    void goForward(int cm);
    void goBack(int cm);
    void turnRight(int degree); 
    void turnLeft(int degree); 
    int leftDirection();
    int rightDirection();
    // wheel changes
    volatile int changes;
    volatile int cntLeft;
    volatile int cntRight;

  private: 
    int pinsRight[3];
    int pinsLeft[3];
};

#endif
