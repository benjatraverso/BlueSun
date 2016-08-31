#include <Servo.h> 

Servo myservo;

void setup() 
{ 
  myservo.attach(9);
  myservo.write(90);  // set servo to mid-point
  delay(5000);
} 

void loop()
{
  myservo.write(0);
  delay(5000);
  myservo.write(180);
  delay(5000);
} 
