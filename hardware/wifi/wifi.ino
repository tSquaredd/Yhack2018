#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <FirebaseArduino.h>

// wifi credentials
#define FIREBASE_HOST "yhack2018-77c5f.firebaseio.com"
#define FIREBASE_AUTH "bgg1eM5OdhXM1HbPSKygseiTSyXwKBhSUT4kiljf"
#define ssid "Verizon VS995 C19F"
#define pass "Y!3cb[2Z"

struct data_t{
  float watt;
  int r;
};

// packet size
#define PACKET_SIZE sizeof(data_t)

void setup() {
  delay(5000);
  Serial.begin(9600);
  WiFi.begin(ssid, pass);
  while(WiFi.status() != WL_CONNECTED){
    delay(500);
  }
  Serial.println("wifi connected");
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  
  FirebaseObject init = Firebase.get("/devices/outlet-one/status");
  
  Firebase.stream("/devices/outlet-one/status/isOn");
}
const int capacity = JSON_OBJECT_SIZE(2);

StaticJsonBuffer<capacity> jsonBuffer;

JsonObject& root = jsonBuffer.createObject();

char read_buff[PACKET_SIZE];

data_t in;
int ran = 0;
void loop(){
  //Serial.println("...");
  if(Firebase.available()){
    FirebaseObject event = Firebase.readEvent();
    String eventType = event.getString("type");
    eventType.toLowerCase();
    if(eventType == "put"){
      String data = event.getString("data");
      if(data == "true"){
        Serial.println("true");
      }else{
        Serial.println("false");
      }
    }
  }
  if(Serial.available()){
    Serial.readBytes(read_buff, PACKET_SIZE);
    memcpy(&in, read_buff, PACKET_SIZE);
    root["watt"] = in.watt;
    root["r"] = ran;
    Firebase.set("/arduino/outlet-one", root);
    if(Firebase.success()){
      Serial.print("sent");
    }else{
      Serial.print("bad");
    }
  }
  if(ran == 0){
    ran = 1;
  }else{
    ran = 0;
  }
}
