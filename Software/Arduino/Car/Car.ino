#include <Servo.h>

const unsigned int FWD = 7;
const unsigned int BWD = 8;
const unsigned int SRV = 6;
const unsigned int EN = 9;

const unsigned int BACKWARDS = 0;
const unsigned int FORWARD = 1;
    
Servo myservo;

void setup()
{
  pinMode(FWD, OUTPUT);
  pinMode(BWD, OUTPUT);
  
  Serial.begin(9600);  // initialize serial: 
  myservo.attach(SRV);

  digitalWrite( FWD, LOW );
  digitalWrite( BWD, LOW );
  analogWrite( EN, LOW );
  myservo.write(90);
}

void loop()
{  
  if( Serial.available() > 0 )
  {
    char cReading = Serial.read();
    
    switch(cReading)
    {
      case 'A':
      {
        int iAngle = Serial.parseInt();
        iAngle = constrain(iAngle, 32, 160);//for this stearing we cant go further than these values
        myservo.write(iAngle);
        break;
      }
      case 'G':
      {
        bool bGear = ( bool )Serial.parseInt();
        digitalWrite( FWD, LOW );
        digitalWrite( BWD, LOW );
        digitalWrite( FWD, bGear );
        digitalWrite( BWD, !bGear );
        break;
      }
      case 'T':
      {
        int iThrottle = Serial.parseInt();
        iThrottle = constrain( iThrottle, 10, 255 );
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

  // if been two seconds without receiving data, either valid or not, set everything to low
  /*
  if( passedTime > 2000 )
  {
    digitalWrite( FWD, LOW );
    digitalWrite( BWD, LOW );
    analogWrite( EN, LOW );
    myservo.write(90);
  }
  */
}
