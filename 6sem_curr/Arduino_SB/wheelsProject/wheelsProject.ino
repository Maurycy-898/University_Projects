#include "Wheels.h"
#include "PinChangeInterrupt.h"
#include "LiquidCrystal_I2C.h"
#include <Servo.h>
#include <IRremote.hpp>

// ---------------------------- REMOTE CONTROLLER VARS --------------------------
int input_pin = 2; // + do VCC, - do GND, pozostały do pinu nr 2 (!!!)
// IRrecv irrecv(input_pin);
decode_results signals;

// ---------------------------- I2c_LCD VARS ------------------------------------
// piny A4(SDA), A5(SCL) + GND, VCC
byte LCDAddress = 0x27; // this lcd address
LiquidCrystal_I2C lcd(LCDAddress, 16, 2);
// LCD variables
const int distTellPos = 11;
const int speedLeftPos;
const int speedRightPos; 
const int animationPos;
long time_stamp0;

// ---------------------------- SONAR VARS --------------------------------------
// piny dla sonaru (HC-SR04)
#define TRIG A2
#define ECHO A3

// pin kontroli serwo (musi być PWM)
// pomaranczowy - digital - 6 (jak w define)
// czerwony - VCC
// brązowy - GND
#define SERVO 6 
Servo serwo;

// ---------------------------- AUTKO VARS --------------------------------------
// prawy sensor: VCC - brązowy, GND - czerwony, DO - pomarańczowy (A0/A1); 
// lewy sensor: VCC - szary, GND - fioletowy, DO - niebieski (A0/A1). 
#define INTINPUT0 A0
#define INTINPUT1 A1

// This wheel model with its pin states
Wheels w;
volatile int pin_state0, pin_state1;

// store distance vars
int safe_distance = 28; // safe to approach with ~ 120 speed
int front_distance;
int right_distance;
int left_distance;

// ---------------------------- PROGRAM --------------------------------------
// program setup
void setup() {
  /* 
    Tył - tam gdzie płytka.
    Niebieski - prawe tył - 8, 
    Zielony - prawe przód - 7,
    Szary - lewe przód - 12,
    Fioletowy - lewe tył - 11,
    Żółty - napięcie prawe - 5,
    Biały - napięcie lewe - 4
  */
  w.attach(7, 8, 5, 12, 11, 4);
  Serial.begin(9600);

  pinMode(INTINPUT0, INPUT);
  pinMode(INTINPUT1, INPUT);

  pinMode(TRIG, OUTPUT);    // TRIG startuje sonar
  pinMode(ECHO, INPUT);     // ECHO odbiera powracający impuls
  
  serwo.attach(SERVO);  // dodaj servo
  serwo.write(90);  // ustaw przodem

  // lcd.init();  // ustaw LCD
  // lcd.backlight(); 
  // setLCD();

  pin_state0 = 0; 
  pin_state1 = 0;

  PCICR  = 0x02;  // włącz pin change interrupt dla 1 grupy (A0..A5)
  PCMSK1 = 0x03;  // włącz przerwanie dla A0, A1

  // IrReceiver.begin(input_pin, ENABLE_LED_FEEDBACK); // connect remote controller
  w.setSpeed(150);
}

int safeDist = 25;
int base_speed = 100;

// main loop executing
bool init_vars_itr = true;
void loop() {
  /* ========= LOOP VARS INIT ========== */
    // if (init_vars_itr) {
    //   w.setSpeed(150);
    //   w.forward();
    //   time_stamp0 = millis();
    //   init_vars_itr = false;    
    // }
  // -----
  /* ========= TEST here ========== */
  // -----
  /* Sonar test */
    Serial.print(": widzę coś w odległości ");
    Serial.println(tellDistance());
    delay(1000);
  // -----  
  /* ========= TASKS here ========= */
  // -----
  /* Square 50cm trip: */
    // w.goForward(50);
    // delay(1000);
    // w.turnRight(90);
    // delay(1000);
    // w.goForward(50);
    // delay(1000);
    // w.turnRight(90);
    // delay(1000);
    // w.goForward(50);
    // delay(1000);
    // w.turnRight(90);
    // delay(1000);
    // w.goForward(50);
    // delay(1000);
    // w.turnRight(90);
    // delay(1000); 
  // -----
  /* Sonar safe trip */
    // delay(10); // delay to get more accurate distance  
    // front_distance = tellDistance();

    // if (front_distance <= safe_distance) {
    //   w.stop();
    //   serwo.write(180);
    //   delay(250);
    //   right_distance = tellDistance();  
      
    //   serwo.write(0);
    //   delay(250);
    //   left_distance = tellDistance();   

    //   serwo.write(90);
    //   delay(250);
    //   if (right_distance > safe_distance) {
    //     w.turnRight(90);      
    //   }
    //   else if (left_distance > safe_distance) {
    //     w.turnLeft(90);
    //   }
    //   else {
    //     w.turnLeft(180);
    //   }
    // }
    // else {
    //   w.forward();
    // }
  // -----
  /* LCD display task */
    // display sonar dist:
    // Serial.println(": widzę coś w odległości ");
    // Serial.println(tellDistanceAndPrintLCD());
    // delay(1000);

    // // display speed
    // if (millis() - time_stamp0 > 500) {
    //   Serial.println(w.cntLeft);
    //   Serial.println(w.cntRight);
    //   double leftSpeed = 502.4 * (double)w.cntLeft / (millis() - time_stamp0) * w.leftDirection();
    //   double rightSpeed = 502.4 * (double)w.cntRight / (millis() - time_stamp0) * w.rightDirection();

    //   lcd.setCursor(0, 1);
    //   lcd.print(leftSpeed);

    //   lcd.setCursor(9, 1);
    //   lcd.print(rightSpeed);

    //   w.cntLeft = 0;
    //   w.cntRight = 0;      
    //   time_stamp0 = millis();
    // }
  // -----
  /* Remote controller task */
    // if (IrReceiver.decode()) {
    //   uint16_t cmd = IrReceiver.decodedIRData.command;
    //   Serial.println(cmd);
      
    //   if (cmd == 24)      { w.goForward(5); } // front arrow
    //   else if (cmd == 90) { w.turnRight(10); }
    //   else if (cmd == 82) { w.goBack(5); }
    //   else if (cmd ==  8) { w.turnLeft(10); }
    //   else if (cmd == 28) { w.stop(); }

    //   IrReceiver.resume();
    // }
  // ----
  /* Spring behaviour */
    delay(10);
    int dist = tellDistance();
    if (dist <= safeDist) {
      int speed = base_speed + (50-dist/2);
      w.setSpeed(speed);
      w.back();
    }
    else if (dist > safeDist) {
      int speed = base_speed - (50-dist/2);
      w.setSpeed(speed);
      w.forward();
    }
}

void doTrip() {
  // TODO
}

int tellDistance() {
  unsigned long tot;      // czas powrotu (time-of-travel)
  unsigned int distance;
  
  /* uruchamia sonar (puls 10 ms na `TRIGGER')
    oczekuje na powrotny sygnał i aktualizuje */
  digitalWrite(TRIG, HIGH);
  delay(10);
  digitalWrite(TRIG, LOW);
  tot = pulseIn(ECHO, HIGH);

  /* prędkość dźwięku = 340m/s => 1 cm w 29 mikrosekund
    droga tam i z powrotem, zatem: */
  distance = tot / 58;
  return distance;
}

int tellDistanceAndPrintLCD() {
  unsigned long tot; // czas powrotu (time-of-travel)
  unsigned int distance;

  digitalWrite(TRIG, HIGH);
  delay(10);
  digitalWrite(TRIG, LOW);
  tot = pulseIn(ECHO, HIGH);

  distance = tot / 58;
  clearDistLCD();
  lcd.setCursor(distTellPos, 0);
  lcd.print(distance);
  Serial.println(distance);

  return distance;  
}

void setLCD() {
  lcd.setCursor(1, 0);
  lcd.print('d');
  lcd.setCursor(2, 0);
  lcd.print('i');
  lcd.setCursor(3, 0);
  lcd.print('s');
  lcd.setCursor(4, 0);
  lcd.print('t');
  lcd.setCursor(5, 0);
  lcd.print('a');
  lcd.setCursor(6, 0);
  lcd.print('n');
  lcd.setCursor(7, 0);
  lcd.print('c');
  lcd.setCursor(8, 0);
  lcd.print('e');
  lcd.setCursor(9, 0);
  lcd.print(':');
}

void clearDistLCD() {
  for (int i = distTellPos; i < 16; ++i) {
    lcd.setCursor(i, 0);
    lcd.print(' ');
  }
}

// to measure device speed
ISR(PCINT1_vect) {
  if ( ((PINC >> PC0) & 1) != pin_state0 ) { 
    pin_state0 ^= 1;
    w.cntLeft++;
    w.changes++;
  }
  
  if ( ((PINC >> PC1) & 1) != pin_state1 ) { 
    pin_state1 ^= 1;
    w.cntRight++;
    w.changes++;
  }
}
