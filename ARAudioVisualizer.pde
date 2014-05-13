 
// Libraries
import processing.video.*;
import jp.nyatla.nyar4psg.*;
import ddf.minim.*;
import qrcodeprocessing.*;

//Sound Objects
Minim minim;
AudioInput in;

//Video Objects
Capture video;
PImage mirroredImage;

//AR Objects
MultiMarker augmentedRealityMarkers;
color displayColor;

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

void setup() 
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

void draw()
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
float getVolume()
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

void checkQR()
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
void decoderEvent(Decoder decoder) 
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
