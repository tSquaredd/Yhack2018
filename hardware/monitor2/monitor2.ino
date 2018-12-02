const int sensorIn = A0;
int mVperAmp = 100; // use 100 for 20A Module and 66 for 30A Module

int count = 0;
float Voltage = 0;
float VRMS = 0;
float AmpsRMS = 0;

struct data_t{
  float watt;
};
float amps = 0.0;

#define VOLTAGE 115
#define PACKET_SIZE sizeof(data_t)

data_t data;

char buff[PACKET_SIZE];
String msg;

void setup() {
  // put your setup code here, to run once:
  //Serial.begin(9600);
  Serial.begin(9600);
  pinMode(7, OUTPUT);
  digitalWrite(7, HIGH);
  while(!Serial.available()){};
  msg = Serial.readString();
  //Serial.println(msg);
  delay(5000);
  //Serial1.begin(9600);
  //Serial.println("Starting");
}

void loop() {
  if(Serial.available()){
    msg = Serial.readString();
    msg.trim();
    //Serial.println(msg);
    if(msg == "true"){
      //Serial.println("light on");
      digitalWrite(7, HIGH);
    }else if(msg == "false"){
      //Serial.println("light off");
      digitalWrite(7, LOW);
    }
  }
  
  if(count < 2){
    Voltage = getVPP();
    VRMS = (Voltage/2.0) *0.707; 
    AmpsRMS = (VRMS * 1000)/mVperAmp;
    //Serial.print(AmpsRMS);
    //Serial.println(" Amps RMS");
    amps = AmpsRMS;
    if(amps <= 1.0){
      amps = 0.0;
    }
    //Serial.println(amps);
    data.watt = data.watt + (amps * VOLTAGE);
    //Serial.println(data.watt);
    count++;
  } else{
    //Serial.println(data.watt);
    count = 0;
    // send data
    memcpy(buff, &data, PACKET_SIZE);
    //Serial.println("sending packet...");
    Serial.write(buff, PACKET_SIZE);
    data.watt = 0.0;
    // wait for response
    while(!Serial.available()){};
    msg = Serial.readString();
    delay(1000);
    while(true){
      if(msg == "true"){
        digitalWrite(7, HIGH);
      }else if(msg == "false"){
        digitalWrite(7, LOW);
      }else if(msg == "bad"){
        //Serial.println("resending packet...");
        Serial.write(buff, PACKET_SIZE);
        while(!Serial.available()){};
        msg = Serial.readString();
      } else if(msg == "sent"){
        //Serial.println("send successful");
        break;
      }
      delay(1000);
    }
  }
}

float getVPP()
{
  float result;
  
  int readValue;             //value read from the sensor
  int maxValue = 0;          // store max value here
  int minValue = 1024;          // store min value here
  
   uint32_t start_time = millis();
   while((millis()-start_time) < 1000) //sample for 1 Sec
   {
       readValue = analogRead(sensorIn);
       // see if you have a new maxValue
       if (readValue > maxValue) 
       {
           /*record the maximum sensor value*/
           maxValue = readValue;
       }
       if (readValue < minValue) 
       {
           /*record the maximum sensor value*/
           minValue = readValue;
       }
   }
   
   // Subtract min from max
   result = (((maxValue - minValue) * 5.0)/1024.0);
   result -= 0.01569;
   
   return result;
 }
