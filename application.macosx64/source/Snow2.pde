//wider swirling snow around Snowman

//special thanks to flashkit board user jbum for helpful math
//information on how to make an object move in a circle!

class Snow2
{
  float x, y, z;
  color c;
  float a;
  float snowSize = 5;
  float vol;

  float orbitAngle;
  int timer = 0;

  float orbitRadius = 250;
  float speed = 3;
  float orbitSpeed = (0.001*2*PI)/speed;

  Snow2(float x, float y, float z)
  {
    this.x = x;
    this.y = y;
    this.z = z-100;
    this.c = color(150, 150, random(200, 255));
    this.a = random(150,255);
  }

  void move(float vol)
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
    this.a = this.a - 0.5;

    perspective();

    popMatrix();
  }
}

