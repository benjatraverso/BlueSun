
char NAME[10]  = "AUTITO";    
char BPS         = '4';           // 1=1200 , 2=2400, 3=4800, 4=9600, 5=19200, 6=38400, 7=57600, 8=115200
char PASS[10]    = "0000";        
 
void setup()
{
    Serial.begin(9600);
    pinMode(13,OUTPUT);
    digitalWrite(13,HIGH);
    delay(10000); // after led's connection, the module should be connected in this 10seconds to proceed with configuration
    digitalWrite(13,LOW);// time expired, if not present then: disconnect, reset and retry
    
    Serial.print("AT"); 
    delay(1000);
 
    Serial.print("AT+NAME"); 
    Serial.print(NAME);
    delay(1000);
 
    Serial.print("AT+BAUD"); 
    Serial.print(BPS);
    delay(1000);
 
    Serial.print("AT+PIN");
    Serial.print(PASS);
}
 
void loop()
{
    digitalWrite(13, !digitalRead(13));
    delay(500);
}
