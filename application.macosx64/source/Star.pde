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

  void display()
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
    if (vol > 0.03)
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
    if (vol > 0.01)
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

