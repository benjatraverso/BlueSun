#include <Servo.h>

const unsigned int FWD = 7;
const unsigned int BWD = 8;
const unsigned int SRV = 6;
const unsigned int EN = 9;

const int MIN_THROTTLE = 10;

Servo myservo;

bool bFirstRead; // This indicates that data was received at least once.
int iLastTransmission; // Checkpoint to indicate when we last received data.

void setup()
{
  pinMode(FWD, OUTPUT);
  pinMode(BWD, OUTPUT);
  
  Serial.begin(9600);  // Initialize serial: 
  myservo.attach(SRV);

  digitalWrite( FWD, LOW );
  digitalWrite( BWD, LOW );
  analogWrite( EN, LOW );
  myservo.write(90);
  
  bFirstRead = false;
  iLastTransmission = 0;
}

void loop()
{  
  if( Serial.available() > 0 )
  {
    bFirstRead = true;
    char cReading = Serial.read();
    
    switch(cReading)
    {
      case 'A':
      {
        int iAngle = Serial.parseInt();
        iAngle = constrain(iAngle, 32, 160); // Steering servo supported range.
        myservo.write(iAngle);
        break;
      }
      case 'G':
      {
        bool bGear = ( bool )Serial.parseInt();
        if( bGear )
        {
          digitalWrite( BWD, LOW );
          digitalWrite( FWD, HIGH );
        }
        else
        {
          digitalWrite( FWD, LOW );
          digitalWrite( BWD, HIGH );
        }
        break;
      }
      case 'T':
      {
        int iThrottle = Serial.parseInt();
        if( iThrottle > 0 )
        {
          iThrottle = iThrottle * ( 255 - MIN_THROTTLE ) / 254 + MIN_THROTTLE;
        }
        analogWrite( EN, iThrottle );
        break;
      }
      default:
      {
        // Not a valid command. Ignore.
        break;
      }
    }
    
    // Data received. Reset checkpoint.
    iLastTransmission = millis();
  }
  
  int passedTime = millis() - iLastTransmission;
  if( bFirstRead && passedTime > 2000 )
  {
    // More than 2 seconds have passed since the last transmission.
    // Assume connection was lost.
    digitalWrite( FWD, LOW );
    digitalWrite( BWD, LOW );
    analogWrite( EN, LOW );
    myservo.write(90);
    bFirstRead = false;
  }
}
