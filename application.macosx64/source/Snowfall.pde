//falling snow for Snowman

class Snowfall
{
  float x, y, z;
  color c;
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

  void move(float vol)
  {
    pushMatrix();

    translate(x, y, z);

    fill(this.c, this.a);

    //scale is based on volume
    snowSize = map(vol, 0, 1, 3, 8);
    
    //speed is based on volume
    speed = map(vol, 0, 1, 0.2, 100);

    sphere(snowSize);

    this.z -= speed;

    //becomes more transparent
    this.a= a - 0.25;

    perspective();

    popMatrix();
  }
}

