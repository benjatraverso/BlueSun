const unsigned int FWD = 9;
const unsigned int BWD = 8;
const unsigned int EN = 10;

const unsigned int BACKWARDS = 0;
const unsigned int FORWARD = 1;

void setup()
{
  myservo.attach(servo);  // attaches the servo on pin A0 to the servo object
  pinMode(FWD, OUTPUT);
  pinMode(BWD, OUTPUT);
  pinMode(EN, OUTPUT);
}

void loop()
{
  for (int iSpeed = 0; iSpeed <= 180; iSpeed += 1)
  {
    move(FORWARD, iSpeed);
    delay(15);
  }
  
  for (int iSpeed = 180; iSpeed >= 0; iSpeed -= 1)
  {
    move(BACKWARDS, iSpeed);
    delay(15);
  }
}

void move( bool direction, int speed )
{
  if( direction )
  {
    moveForward(speed);
  }
  else
  {
    moveBackwards(speed);
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
