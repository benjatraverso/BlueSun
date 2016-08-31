#include <Servo.h>

const unsigned int FWD = 7;
const unsigned int BWD = 8;
const unsigned int SRV = 9;
const unsigned int EN = 10;

const unsigned int BACKWARDS = 0;
const unsigned int FORWARD = 1;

Servo myservo;  // create servo object to control a servo
                // a maximum of eight servo objects can be created

int gear = -1;
int angle = 0;    // variable to store the servo position
int power = 0;

void setup()
{
  pinMode(FWD, OUTPUT);
  pinMode(BWD, OUTPUT);
  pinMode(EN, OUTPUT);
  
  Serial.begin(9600);  // initialize serial: 
  myservo.attach(SRV);  // attaches the servo on pin 9 to the servo object
 
  Serial.print("Arduino control Servo Motor Connected OK");
  Serial.print('\n');
}

void loop()
{
  // if there's any serial available, read it:
  while (Serial.available() > 0)
  {
    // look for the next valid integer in the incoming serial stream:
    gear = Serial.parseInt();
    power = Serial.parseInt();
    gear = 255 - constrain(gear, 0, 255);
    power = 255 - constrain(power, 0, 255);
    if( gear > 0 )
    {
      moveForward( power );
    }
    else if( gear < 0 )
    {
      moveBackwards( power );
    }
    
    angle = Serial.parseInt();
    angle = 255 - constrain(angle, 0, 255);
    myservo.write(angle);
  }
}

void moveForward( int speed )
{
  digitalWrite( FWD, HIGH );
  digitalWrite( BWD, LOW );
  analogWrite( EN, speed );
}

void moveBackwards( int speed )
{
  digitalWrite( BWD, HIGH );
  digitalWrite( FWD, LOW );
  analogWrite( EN, speed );
}
