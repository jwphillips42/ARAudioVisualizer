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

  void display()
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
    if (vol > 0.02)
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
    if (vol > 0.03)
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
    if (vol > 0.03)
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
    if (vol > 0.02)
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


