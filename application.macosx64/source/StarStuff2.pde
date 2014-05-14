//star particles orbiting around Star in a ring

//special thanks to flashkit board user jbum for helpful math
//information on how to make an object move in a circle!

class StarStuff2
{
  float x, y, z;
  color c;
  float a;
  float stuffSize = 5;
  float vol;

  float orbitAngle;
  int timer = 0;

  float orbitRadius = 150;
  float speed = 3;
  float orbitSpeed = (0.001*2*PI)/speed;

  StarStuff2(float x, float y, float z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    this.c = color(random(100, 255), 0, 0);
    this.a = random(150,255);
  }

  void move(float vol)
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
    this.a = this.a - 0.75;

    perspective();

    popMatrix();
  }
}


