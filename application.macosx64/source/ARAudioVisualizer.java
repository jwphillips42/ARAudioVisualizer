import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.video.*; 
import jp.nyatla.nyar4psg.*; 
import ddf.minim.*; 
import qrcodeprocessing.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ARAudioVisualizer extends PApplet {

 
// Libraries





//Sound Objects
Minim minim;
AudioInput in;

//Video Objects
Capture video;
PImage mirroredImage;

//AR Objects
MultiMarker augmentedRealityMarkers;
int displayColor;

//QR Objects
Decoder decoder;
int frame;

//Useful Data
float volume;
String colorCode = "";
String modelCode = "";
String modelType = "Cube";

//Things to Display
Model theModel;

public void setup() 
{
  size(640, 480, P3D);
  smooth();
  
  //AUDIO
  minim = new Minim(this);
  in = minim.getLineIn(Minim.MONO, 512);

  //VIDEO  
  video = new Capture(this, 640, 480);
  video.start();

  //DISCO
  // AR
  augmentedRealityMarkers = new MultiMarker(this, width, height, "camera_para.dat", NyAR4PsgConfig.CONFIG_PSG);
  displayColor = color(0, 0, 255);

  // QR  
  decoder = new Decoder(this);
  frame = 0;

  // Tell the list what pattern to track (the String) and the width of the pattern (the 80)
  augmentedRealityMarkers.addARMarker(loadImage("AV.png"), 16, 25, 80);
  
  //GENERIC MODEL TO DISPLAY
  theModel = new Cube();
}

public void draw()
{
  if (video.available())
  {
    video.read();
    
    //This helps with the shapes disappearing behind the image
    //Might be able to get rid of it when we don't have to use forward-facing cameras
    hint(DISABLE_DEPTH_TEST);
    image(video, 0, 0);
    hint(ENABLE_DEPTH_TEST);

    //Get Audio Info
    volume = getVolume();

    //Look for QR Code once every 30 frames. Helps code run more smoothly
    if(frame < 30) frame++;
    else
    {
      frame = 0;
      checkQR();
    }

    //Augmented Reality Checks
    try {
      augmentedRealityMarkers.detect(video);
    }
    catch (Exception e) {
      println("Issue with AR detection ... resuming regular operation ..");
    }

    if (augmentedRealityMarkers.isExistMarker(0))
    {
      augmentedRealityMarkers.setARPerspective();

      pushMatrix();
      setMatrix(augmentedRealityMarkers.getMarkerMatrix(0));
      
      scale(-1, -1);
      perspective();

      fill(displayColor);
      
      //OUR CODE GOES HERE
      theModel.display();

      popMatrix();
    }
  }
}

//Gets audio input from the microphone, returns the average volume
//At the moment it was called (Between 0 and 1)
public float getVolume()
{
  float sum = 0;
  float samples = 0;
 
  // look at all of our sample inputs from the microphone
  for (int i = 0; i < in.bufferSize() - 1; i++)
  {
    // grab the value we see in the left channel - this is a number between 0.0 and 1.0
    // add it to our sum
    sum += abs(in.left.get(i));
    samples++;
  }

  // return the average volume across all samples
  if (samples > 0)
  {
    return sum / samples;
  }
  else
  {
    return 0;
  }
}

public void checkQR()
{
  if (!decoder.decoding())
    {
      // copy video frame to the PImage savedFrame:
      PImage savedFrame = createImage(video.width, video.height, RGB);
      savedFrame.copy(video, 0, 0, video.width, video.height, 0, 0, video.width, video.height);
      savedFrame.updatePixels();
      
      try
      {
        decoder.decodeImage(savedFrame);
      }
      catch (Exception e)
      {
        println("Error initializing decoder, resetting");
      }
    }

    //OUR CHANGES GO HERE
    if(colorCode.equals("Red")) displayColor = color(255, 0, 0);
    else if(colorCode.equals("Green")) displayColor = color(0, 255, 0);
    else if(colorCode.equals("Blue")) displayColor = color(0, 0, 255);
    else if(colorCode.equals("Purple")) displayColor = color(52,21,99);
    else if(colorCode.equals("Pink")) displayColor = color(223,47,149);
    else if(colorCode.equals("Orange")) displayColor = color(231,111,40);
    else if(colorCode.equals("Yellow")) displayColor = color(253,249,39);
    else if(colorCode.equals("Teal")) displayColor = color(99,179,176);
    
    if(!modelCode.equals(modelType))
    {
      if(modelCode.equals("Sphere")) theModel = new Ball();
      else if(modelCode.equals("Cube")) theModel = new Cube();
      else if(modelCode.equals("Space")) theModel = new Star();
      else if(modelCode.equals("Volcano")) theModel = new Volcano();
      else if(modelCode.equals("Snowman")) theModel = new Snowman();
      else if(modelCode.equals("Whale")) theModel = new Whale();
      
      modelType = modelCode;
    }
}

// Called when the Decoder finishes
public void decoderEvent(Decoder decoder) 
{
  try
  {
    String temp = decoder.getDecodedString();
    println("*" + temp + "*");
    if (!temp.equals("NO QRcode image found"))
    {
      if(temp.equals("Red") || temp.equals("Green") || temp.equals("Blue") || temp.equals("Yellow") 
          || temp.equals("Orange") || temp.equals("Teal") || temp.equals("Purple"))
      {
        colorCode = temp;
      }
      if(temp.equals("Sphere") || temp.equals("Cube") || temp.equals("Space") || temp.equals("Snowman") || temp.equals("Volcano") || temp.equals("Whale"))
         {
            //OUR CODE CAN GO HERE TOO!
            modelCode = temp;
         }
      
    }
  }
  catch (Exception e)
  {
    println("Error decoding String, resetting.");
  }
}
class Ball extends Model
{
  float x, y, z;
  float rad;
  int angle;
  
  Ball()
  {
    x = 0;
    y = 0;
    z = 250;
    
    rad = 30;
  }
  
  public void display()
  { 
    //VolumeResponse
    rad = map(volume, 0, 1, 40, 200);
    
    angle += 5;
    
    pushMatrix();
    
    translate(x, y, z);
    
    //Rotation
    rotateZ(radians(angle));
    rotateX(radians(angle));
    rotateY(radians(angle));
    
    strokeWeight(2);
    sphere(rad);
    
    popMatrix();
  }
}
class Cube extends Model
{
  float x, y, z;
  float len, wid, hei;
  int angle;
  
  Cube()
  {
    x = 0;
    y = 0;
    z = 250;
    
    len = 40;
    wid = 40;
    hei = 40;
  }
  
  public void display()
  {
    len = map(volume, 0, 1, 20, 200);
    wid = len;
    hei = wid;
    
    angle += 5;
    
    pushMatrix();
    
    translate(x, y, z);
    
    //Rotation
    rotateZ(radians(angle));
    rotateX(radians(angle));
    rotateY(radians(angle));
    
    strokeWeight(2);
    box(len, wid, hei);
    
    popMatrix();
  }
}
class Lava 
{
  float x, y, z;
  float lavaSize = 5;
  float lavaSpeed=5;
  boolean up = true;
  boolean left;
  boolean zchange;
  float yspeed;
  float xspeed;
  boolean remove;
  float rand;


 
  Lava(float x, float y, float z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    
    
    
    yspeed = random(6,10);
    xspeed = random(2,6);
    
    rand = random(0,20);
    
    //choose a random direction
    if (rand<8)
    {
      left = true;
    }
    else if (rand<10)
    {
      left = false;
    }
    else if (rand<15)
    {
      zchange = true;
    }
    else if (rand<20)
    {
      zchange=false;
    }
    
    
  }

  public void move()
  {
    float vol = volume;
    
    pushMatrix();

    translate(x, y, z+300);

    fill(random(255),0,0);
    //fill(0);

    lavaSize = map(vol, 0, 1, 5, 25);
    lavaSpeed= map(vol, 0, 1, 2, 26);

    //take off the stroke weight
    strokeWeight(0);
    sphere(lavaSize);
    
    
    //change the lavas position
    //if its going up then go up
    if (up)
    {
      z+= yspeed + lavaSpeed;
      //x+=2;
      //make sure its not too high
      if (z>=200)
      {
        up=false;
      }
    }
    //go down
    else
    {
      z-=yspeed + lavaSpeed;
      //z-=2;
      
      //check if its going left or right
      if(left)
      {
        y-= xspeed + lavaSpeed;
        if (random(0,4)>2){x+=2;}
        else{x-=2;}
    
      }
      else if(!left)
      {
        y+=xspeed + lavaSpeed;
        if (random(0,4)>2){x+=2;}
        else{x-=2;}
      }
       if(zchange)
      {
        x-= xspeed + lavaSpeed;
        if (random(0,4)>2){y+=2;}
        else{y-=2;}
    
      }
      else if(!zchange)
      {
        x+=xspeed + lavaSpeed;
        if (random(0,4)>2){y+=2;}
        else{y-=2;}
      }
      
        
      //check if its off the screen
      //if so reset it
      if(y>300 || y<-100)
      {
        remove = true;
      }
    }
    /*
    //check z change
    if(zchange)
    {
      z+=2;
    }
    else
    {
      z-=2;
    }
    */

    
    perspective();

    popMatrix();
  }
}




class Model
{
  Model()
  {
    
  }
  
  public void display()
  {
    
  }
}
//swirling snow around Snowman

//special thanks to flashkit board user jbum for helpful math
//information on how to make an object move in a circle!

class Snow
{
  float x, y, z;
  int c;
  float a;
  float snowSize = 5;
  float vol;

  float orbitAngle;
  int timer = 0;

  float orbitRadius = 150;
  float speed = 3;
  float orbitSpeed = (0.001f*2*PI)/speed;

  Snow(float x, float y, float z)
  {
    this.x = x;
    this.y = y;
    this.z = z-100;
    this.c = color(200, random(200,255), random(200, 255));
    this.a = random(150,255);
  }

  public void move(float vol)
  {
    pushMatrix();

    translate(x, y, z);

    fill(this.c, this.a);

    //size is based on volume
    snowSize = map(vol, 0, 1, 5, 50);

    sphere(snowSize);

    //moves around in a circle
    speed = map(vol, 0, 1, 10, 0.5f);
    orbitAngle = timer*orbitSpeed;
    this.x = sin(orbitAngle)*orbitRadius;
    this.y = cos(orbitAngle)*orbitRadius;
    this.z = this.z + 1;
    timer = timer + 25;

    //becomes more transparent
    this.a = this.a - 0.25f;

    perspective();

    popMatrix();
  }
}

//wider swirling snow around Snowman

//special thanks to flashkit board user jbum for helpful math
//information on how to make an object move in a circle!

class Snow2
{
  float x, y, z;
  int c;
  float a;
  float snowSize = 5;
  float vol;

  float orbitAngle;
  int timer = 0;

  float orbitRadius = 250;
  float speed = 3;
  float orbitSpeed = (0.001f*2*PI)/speed;

  Snow2(float x, float y, float z)
  {
    this.x = x;
    this.y = y;
    this.z = z-100;
    this.c = color(150, 150, random(200, 255));
    this.a = random(150,255);
  }

  public void move(float vol)
  {
    pushMatrix();

    translate(x, y, z);

    fill(this.c, this.a);

    //size is based on volume
    snowSize = map(vol, 0, 1, 5, 50);

    sphere(snowSize);

    //moves around in a circle
    speed = map(vol, 0, 1, 10, 1);
    orbitAngle = timer*orbitSpeed;
    this.x = sin(orbitAngle)*orbitRadius;
    this.y = cos(orbitAngle)*orbitRadius;
    this.z = this.z + 1;
    timer = timer + 25;

    //becomes more transparent
    this.a = this.a - 0.5f;

    perspective();

    popMatrix();
  }
}

//falling snow for Snowman

class Snowfall
{
  float x, y, z;
  int c;
  float a;
  float snowSize = 5;
  float vol;
  float speed;

  Snowfall(float x, float y, float z)
  {
    this.x = random(x-250, x+250);
    this.y = random(z-250, z+250);
    this.z = 500;
    this.c = color(255);
    this.a = 255;
  }

  public void move(float vol)
  {
    pushMatrix();

    translate(x, y, z);

    fill(this.c, this.a);

    //scale is based on volume
    snowSize = map(vol, 0, 1, 3, 8);
    
    //speed is based on volume
    speed = map(vol, 0, 1, 0.2f, 100);

    sphere(snowSize);

    this.z -= speed;

    //becomes more transparent
    this.a= a - 0.25f;

    perspective();

    popMatrix();
  }
}

//sparkling, glistening snow effect for Snowman

class Snowfloor
{
  float x, y, z;
  int c;
  float a;
  float snowSize = 5;
  float vol;
  float speed;

  Snowfloor(float x, float y, float z)
  {
    this.x = x;
    this.y = y;
    this.z = -250;
    this.c = color(255);
    this.a = 255;
  }

  public void move(float vol)
  {
    pushMatrix();

    translate(x, y, z);

    fill(this.c, this.a);

    //scale is based on volume
    snowSize = map(vol, 0, 1, 3, 10);
    
    //speed is based on volume
    speed = map(vol, 0, 1, 0.5f, 1.5f);

    sphere(snowSize);

    this.x = random(x-150, x+150);
    this.y = random(y-150, y+150);
    this.z -= speed;

    //becomes more transparent quickly to give it the glistening effect
    this.a= a - 1;

    perspective();

    popMatrix();
  }
}

//snowman in winter setting

class Snowman extends Model
{
  float x, y, z;
  float rad;
  boolean incr = false;
  int angle;
  float snowAmount = 0;
  float snowfallAmount = 0;

  ArrayList<Snow> theSnow;
  ArrayList<Snow2> theSnow2;
  ArrayList<Snowfall> theSnowfall;
  ArrayList<Snowfloor> theSnowfloor;

  //3D Model Objects
  PShape snowmanGEO;

  Snowman()
  {
    x = 0;
    y = 0;
    z = 250;

    rad = 30;

    theSnow = new ArrayList<Snow>();
    theSnow2 = new ArrayList<Snow2>();
    theSnowfall = new ArrayList<Snowfall>();
    theSnowfloor = new ArrayList<Snowfloor>();

    //load in model
    snowmanGEO = loadShape("snowmanGEO.obj");
    snowmanGEO.scale(25, 25, 25);
    snowmanGEO.rotateX(radians(90));
    snowmanGEO.rotateX(radians(180));
  }

  public void display()
  {

    float vol = volume;
    pushMatrix();

    fill(255, 200, 50);

    translate(x, y, z);

    noStroke();

    shape(snowmanGEO);

    perspective();

    popMatrix();

    //SNOW

    //create more swirling snow if the audio is louder
    snowAmount = map(vol, 0, 1, 0, 5);

    //if there is sound
    if (vol > 0.02f)
    {
      //create snow
      for (int j = 0; j < snowAmount; j++)
      {
        Snow newSnow = new Snow(x, y, z);
        theSnow.add(newSnow);
      }
    }

    // display the snow
    for (int i = 0; i < theSnow.size(); i++)
    {
      Snow newSnow = theSnow.get(i);

      // move the snow
      newSnow.move(vol);

      // remove disappeared snow
      if (newSnow.a < 0) {
        theSnow.remove(newSnow);
        i--;
      }
    }
    
    //SNOW2
    
    //refers to snowAmount
    //creates more swirling snow if the audio is louder

    //if there is sound
    if (vol > 0.03f)
    {
      //create snow
      for (int j = 0; j < snowAmount; j++)
      {
        Snow2 newSnow2 = new Snow2(x, y, z);
        theSnow2.add(newSnow2);
      }
    }

    // display the snow
    for (int i = 0; i < theSnow2.size(); i++)
    {
      Snow2 newSnow2 = theSnow2.get(i);

      // move the snow
      newSnow2.move(vol);

      // remove disappeared snow
      if (newSnow2.a < 0) {
        theSnow2.remove(newSnow2);
        i--;
      }
    }

    //SNOWFALL

    //creates more falling snow if the audio is louder
    snowfallAmount = map(vol, 0, 1, 5, 25);

    //if there is sound
    if (vol > 0.03f)
    {
      //create snow
      for (int j = 0; j < snowfallAmount; j++)
      {
        Snowfall newSnowfall = new Snowfall(x, y, z);
        theSnowfall.add(newSnowfall);
      }
    }

    // display the snow
    for (int i = 0; i < theSnowfall.size(); i++)
    {
      Snowfall newSnowfall = theSnowfall.get(i);

      // move the snow
      newSnowfall.move(vol);

      // remove disappeared snow
      if (newSnowfall.a < 0) {
        theSnowfall.remove(newSnowfall);
        i--;
      }
    }
    
    //SNOWFLOOR

    //creates more glistening snow on the ground if the audio is louder via snowfallAmount

    //if there is sound
    if (vol > 0.02f)
    {
      //create snow
      for (int j = 0; j < snowfallAmount; j++)
      {
        Snowfloor newSnowfloor = new Snowfloor(x, y, z);
        theSnowfloor.add(newSnowfloor);
      }
    }

    // display the snow
    for (int i = 0; i < theSnowfloor.size(); i++)
    {
      Snowfloor newSnowfloor = theSnowfloor.get(i);

      // move the snow
      newSnowfloor.move(vol);

      // remove disappeared snow
      if (newSnowfloor.a < 0) {
        theSnowfloor.remove(newSnowfloor);
        i--;
      }
    }
  }
}


//spaceship orbiting the giant Star

//special thanks to flashkit board user jbum for helpful math
//information on how to make an object move in a circle!

class Spaceship
{
  float x, y, z;
  int c;
  float a;
  float stuffSize = 5;
  float vol;

  float orbitAngle;
  int timer = 0;

  float orbitRadius = 100;
  float speed = 6;
  float orbitSpeed = (0.001f*2*PI)/speed;
  
  //3D Model Objects
  PShape rocketGEO;
  
  //int rotateAngle;

  Spaceship(float x, float y, float z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    this.c = color(0, 255, 0);
    this.a = 255;
    
    //load in model
    rocketGEO = loadShape("rocketShipGEO.obj");
    rocketGEO.scale(25,25,25);

  }

  public void move(float vol)
  {
    pushMatrix();

    translate(x, y, z);

    fill(this.c, this.a);

    shape(rocketGEO);

    ////moves around in a circle
    speed = map(vol, 0, 1, 10, .5f);
    orbitAngle = timer*orbitSpeed;
    this.x = sin(orbitAngle)*orbitRadius;
    this.y = cos(orbitAngle)*orbitRadius;
    this.z = 250 + (sin(orbitAngle)*orbitRadius);
    timer = timer + 25;

    perspective();

    popMatrix();
  }
}


//star system

class Star extends Model
{
  float x, y, z;
  float rad;
  boolean incr = false;
  int angle;
  float sphereSize = 50;
  float starAmount = 0;

  ArrayList<StarStuff> theStuff;
  ArrayList<StarStuff2> theRing;
  Spaceship spaceship;

  Star()
  {
    x = 0;
    y = 0;
    z = 250;

    rad = 30;

    theStuff = new ArrayList<StarStuff>();
    theRing = new ArrayList<StarStuff2>();
    spaceship = new Spaceship(x,y,z);
  }

  public void display()
  {

    float vol = volume;
    pushMatrix();

    fill(255, 200, 50);

    translate(x, y, z);

    //size is based on volume
    sphereSize = map(vol, 0, 1, 50, 500);

    noStroke();

    sphere(sphereSize);

    perspective();

    popMatrix();

    //STAR STUFF

    //create more star stuff if the audio is louder
    starAmount = map(vol, 0, 1, 5, 80);

    //if there is sound
    if (vol > 0.03f)
    {
      //create the little StarStuff objects
      for (int j = 0; j < starAmount; j++)
      {
        StarStuff newStuff = new StarStuff(x,y,z);
        theStuff.add(newStuff);
      }
    }

    // display all little stars
    for (int i = 0; i < theStuff.size(); i++)
    {
      StarStuff newStuff = theStuff.get(i);

      // move the little stars
      newStuff.move(vol);

      // remove disappeared star stuff
      if (newStuff.a < 0) {
        theStuff.remove(newStuff);
        i--;
      }
    }

    //STAR STUFF 2

    // create star stuff if there is sound
    if (vol > 0.01f)
    {
      StarStuff2 ringStuff = new StarStuff2(x, y, z);
      theRing.add(ringStuff);
    }

    // display all little stars
    for (int i = 0; i < theRing.size(); i++)
    {
      // pull out this particle from the array list
      StarStuff2 ringStuff = theRing.get(i);

      // move the little stars
      ringStuff.move(vol);

      // remove disappeared star stuff
      if (ringStuff.a < 0) {
        theRing.remove(ringStuff);
        i--;
      }
    }

    //SPACESHIP

    spaceship.move(vol);
  }
}

//smaller particles emitting from Star

class StarStuff
{
  float x, y, z;
  int c;
  float a;
  float stuffSize = 5;
  float vol;
  float xdir;
  float ydir;
  float zdir;
  float speed;

  StarStuff(float x, float y, float z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    this.c = color(0, 0, random(100, 255));
    this.a = 255;

    //random direction to travel
    xdir = random(-3, 3);
    ydir = random(-3, 3);
    zdir = random(-3, 3);
  }

  public void move(float vol)
  {
    pushMatrix();

    translate(x, y, z);

    fill(this.c, this.a);

    //scale is based on volume
    stuffSize = map(vol, 0, 1, 5, 15);
    
    //speed is based on volume
    speed = map(vol, 0, 1, 1, 3);

    sphere(stuffSize);

    this.x += xdir * speed;
    this.y += ydir * speed;
    this.z += zdir * speed;

    //becomes more transparent
    this.a= a - 2.5f;

    perspective();

    popMatrix();
  }
}


//star particles orbiting around Star in a ring

//special thanks to flashkit board user jbum for helpful math
//information on how to make an object move in a circle!

class StarStuff2
{
  float x, y, z;
  int c;
  float a;
  float stuffSize = 5;
  float vol;

  float orbitAngle;
  int timer = 0;

  float orbitRadius = 150;
  float speed = 3;
  float orbitSpeed = (0.001f*2*PI)/speed;

  StarStuff2(float x, float y, float z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    this.c = color(random(100, 255), 0, 0);
    this.a = random(150,255);
  }

  public void move(float vol)
  {
    pushMatrix();

    translate(x, y, z);

    fill(this.c, this.a);

    //size is based on volume
    stuffSize = map(vol, 0, 1, 5, 10);

    sphere(stuffSize);

    //moves around in a circle
    speed = map(vol, 0, 1, 10, 1);
    orbitAngle = timer*orbitSpeed;
    this.x = sin(orbitAngle)*orbitRadius;
    this.y = cos(orbitAngle)*orbitRadius;
    timer = timer + 25;

    //becomes more transparent
    this.a = this.a - 0.75f;

    perspective();

    popMatrix();
  }
}


class Volcano extends Model
{
  float x, y, z;
  float len, wid, hei;
  boolean incr = true;
  float volcsize= 80;
  int angle=5;
 
  float lavaAmount=1;
  
  PShape volcImg;
  
  ArrayList<Lava> theStuff;

  
  //create the lava 
  //Lava[] myLava;
  
  Volcano()
  {
    x = 0;
    y = 150;
    z = 250;
    
    len = 80;
    wid = 80;
    hei = 160;
    
    theStuff = new ArrayList<Lava>();

    //look in our objec file
    volcImg = loadShape("volcanoGEO.obj");
    
    //scale the volcano
    volcImg.scale(15,15,15);
    
    //set the volume 
    //System.out.println(vol);
    
    
    
  }
  
  public void display()
  {
    
      float vol = volume;
    
    
    //change the angle
    angle += 5;
    
    
    //there are more particles with more volume
    lavaAmount = map(vol, 0, 1, 1, 5);

    // create lava if there is sound
    if (vol > 0.03f)
    {
      for (int j = 0; j < lavaAmount; j++)
      {
        //disperse the lava
      // select a random position
       float xPos = random(x, x+5);
       float yPos = random(y-5, y+5);
       float zPos = random(z-250, z-250);   
      
        Lava newStuff = new Lava(xPos, yPos, zPos);
        theStuff.add(newStuff);
      }
    }

    // display all lava
    for (int i = 0; i < theStuff.size(); i++)
    {
      Lava newStuff = theStuff.get(i);

      // move the lava
      newStuff.move();

      // remove disappeared lava stuff
      if (newStuff.remove == true) {
        theStuff.remove(newStuff);
        i--;
      }
    }
    
    pushMatrix();
    
    
    
    
    /*
    //draw the lava
    for (int i = 0; i<myLava.length;i++)
    {
      myLava[i].move();
    }*/
    
    fill(139,90,43);
    
    translate(x, y, z);
    //rotateY(radians(angle));
    rotateX(radians(270));
    rotateZ(radians(180));

    
    //box(len, wid, hei);
    shape(volcImg);
    
    perspective();
    
    popMatrix();
    
    
    
  }
  
  
  
}



class Water 
{
  float x, y, z;
  float waterSize = 5;
  float waterSpeed=5;
  boolean up = true;
  boolean left;
  boolean zchange;
  float yspeed;
  float xspeed;
  boolean remove;
  float rand;

 
  Water(float x, float y, float z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    
 
    yspeed = random(6,10);
    xspeed = random(2,6);
    
    rand = random(0,20);
    
    //choose a random direction
    if (rand<8)
    {
      left = true;
    }
    else if (rand<10)
    {
      left = false;
    }
    else if (rand<15)
    {
      zchange = true;
    }
    else if (rand<20)
    {
      zchange=false;
    }
    
  }

  public void move()
  {
    float vol = volume;
    
    pushMatrix();

    translate(x+25, y, z);
    

    fill(0,0,random(255));
    //fill(0);

    waterSize = map(vol, 0, 1, 5, 25);
    waterSpeed= map(vol, 0, 1, 2, 16);

    //take off the stroke weight
    strokeWeight(0);
    sphere(waterSize);
    
    
    //change the lavas position
    //if its going up then go up
    if (up)
    {
      z+= yspeed + waterSpeed;
      //x+=2;
      //make sure its not too high
      if (z>=400)
      {
        up=false;
      }
    }
    //go down
    else
    {
      z-=yspeed + waterSpeed;
      //z-=2;
      
      //check if its going left or right
      if(left)
      {
        y-= xspeed + waterSpeed;
        if (random(0,4)>2){x+=2;}
        else{x-=2;}
    
      }
      else if(!left)
      {
        y+=xspeed + waterSpeed;
        if (random(0,4)>2){x+=2;}
        else{x-=2;}
      }
      if(zchange)
      {
        x-= xspeed + waterSpeed;
        if (random(0,4)>2){y+=2;}
        else{y-=2;}
    
      }
      else if(!zchange)
      {
        x+=xspeed + waterSpeed;
        if (random(0,4)>2){y+=2;}
        else{y-=2;}
      }
      
        
      //check if its off the screen
      //if so reset it
      if(y>300 || y<-100)
      {
        remove = true;
      }
    }
    
    /*
    //check z change
    if(zchange)
    {
      z+=2;
    }
    else
    {
      z-=2;
    }
    */

    
    perspective();

    popMatrix();
  }
}



class Whale extends Model
{
  float x, y, z;
  float len, wid, hei;
  boolean incr = true;
  float whalesize= 80;
  int angle;
  float waterAmount=1;
  float yangle=0;
  float yanglec = 2;
  float yanglev = 0;

  
  PShape whaleImg;
  
  ArrayList<Water> theStuff;
  
  
  
  Whale()
  {
    x = 0;
    y = 150;
    z = 250;
    
    
    
    theStuff = new ArrayList<Water>();

    //look in our objec file
    whaleImg = loadShape("whaleGEO.obj");
    
    //scale the volcano
    whaleImg.scale(15,15,15);
    
   
    
  }
  
  public void display()
  {
    float vol = volume;

        
    
    
    //map yanglec to the volume
    yanglev = map(vol, 0, 1, 1,4 );
    
    
    //change the y angle
    if(yangle>20)
    {
      yanglec-=yanglev;
    }
    else if(yangle<-20)
    {
      yanglec+=yanglev;
    }
    //change the angle
    angle += 5;
    yangle += yanglec;
    
     //there are more particles with more volume
    waterAmount = map(vol, 0, 1, 5, 5);
    
     // create lava if there is sound
    if (vol > 0.03f)
    {
      for (int j = 0; j < waterAmount; j++)
      {
         
      
        Water newStuff = new Water(x, y, z);
        theStuff.add(newStuff);
      }
    }

    // display all water
    for (int i = 0; i < theStuff.size(); i++)
    {
      Water newStuff = theStuff.get(i);

      // move the lava
      newStuff.move();

      // remove disappeared lava stuff
      if (newStuff.remove == true) {
        theStuff.remove(newStuff);
        i--;
      }
    }
   
    
    
    pushMatrix();
    
   
    
    fill(150);
    
    translate(x, y, z);
    //rotateY(radians(0));
    rotateX(radians(270));
    rotateZ(radians(180+yangle));

    
    //box(len, wid, hei);
    shape(whaleImg);
    
    perspective();
    
    popMatrix();
    
    
    
  }
  
  
  
}



  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ARAudioVisualizer" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
