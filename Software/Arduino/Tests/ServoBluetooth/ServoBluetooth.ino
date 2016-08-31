#include <Servo.h>

Servo myservo;  // create servo object to control a servo
                // a maximum of eight servo objects can be created

int pos = 0;    // variable to store the servo position
int motor = 0;

void setup()
{  
  pinMode(13, OUTPUT);
  Serial.begin(9600);  // initialize serial: 
  myservo.attach(9);  // attaches the servo on pin 9 to the servo object
 
  Serial.print("Arduino control Servo Motor Connected OK");
  Serial.print('\n');
}

void loop()
{ 
  digitalWrite(13,HIGH);
  // if there's any serial available, read it:
  while (Serial.available() > 0) {
    
    // look for the next valid integer in the incoming serial stream:
    motor = Serial.parseInt();
   
    // do it again:
    pos = Serial.parseInt();
  
    // look for the newline. That's the end of your  sentence:
    if (Serial.read() == '\n') {
              
       myservo.write(pos);              // tell servo to go to position in variable 'pos'
       delay(15);                       // waits 15ms for the servo to reach the position
     
      // print the three numbers in one string as hexadecimal:
      Serial.print("Data Response : ");
      Serial.print(motor, DEC);
      Serial.print(pos, DEC);
      
    }
  }
}
