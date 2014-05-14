//spaceship orbiting the giant Star

//special thanks to flashkit board user jbum for helpful math
//information on how to make an object move in a circle!

class Spaceship
{
  float x, y, z;
  color c;
  float a;
  float stuffSize = 5;
  float vol;

  float orbitAngle;
  int timer = 0;

  float orbitRadius = 100;
  float speed = 6;
  float orbitSpeed = (0.001*2*PI)/speed;
  
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

  void move(float vol)
  {
    pushMatrix();

    translate(x, y, z);

    fill(this.c, this.a);

    shape(rocketGEO);

    ////moves around in a circle
    speed = map(vol, 0, 1, 10, .5);
    orbitAngle = timer*orbitSpeed;
    this.x = sin(orbitAngle)*orbitRadius;
    this.y = cos(orbitAngle)*orbitRadius;
    this.z = 250 + (sin(orbitAngle)*orbitRadius);
    timer = timer + 25;

    perspective();

    popMatrix();
  }
}


