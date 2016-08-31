#include <Servo.h>

const unsigned int FWD = 7;
const unsigned int BWD = 8;
const unsigned int SRV = 9;
const unsigned int EN = 6;

const unsigned int BACKWARDS = 0;
const unsigned int FORWARD = 1;
    
Servo myservo;

void setup()
{
  pinMode(FWD, OUTPUT);
  pinMode(BWD, OUTPUT);
  
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
    char cReading = Serial.read();

    Serial.println("cReading");
    Serial.println(cReading);
    
    switch(cReading)
    {
      case 'A':
      {
        Serial.read();
        int iAngle = Serial.parseInt();
        iAngle = constrain(iAngle, 0, 255);
        Serial.println("iAngle");
        Serial.println(iAngle);
        myservo.write(iAngle);
        break;
      }
      case 'G':
      {
        Serial.read();
        int iGear = Serial.parseInt();
        iGear = constrain(iGear, 0, 255);
        digitalWrite( FWD, LOW );
        digitalWrite( BWD, LOW );
        Serial.println("iGear");
        Serial.println(iGear);
        digitalWrite( FWD, iGear );
        digitalWrite( BWD, !(bool)iGear );
        break;
      }
      case 'T':
      {
        Serial.read();
        int iThrottle = Serial.parseInt();
        iThrottle = constrain(iThrottle, 0, 255);
        Serial.println("iThrottle");
        Serial.println(iThrottle);
        analogWrite( EN, iThrottle );
        break;
      }
      default:
      {
        //ignore the reading...
        break;
      }
    }    
  }
  
  digitalWrite( FWD, LOW );
  digitalWrite( BWD, LOW );
  analogWrite( EN, LOW );
  myservo.write(90);
}
