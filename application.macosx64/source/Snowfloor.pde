//sparkling, glistening snow effect for Snowman

class Snowfloor
{
  float x, y, z;
  color c;
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

  void move(float vol)
  {
    pushMatrix();

    translate(x, y, z);

    fill(this.c, this.a);

    //scale is based on volume
    snowSize = map(vol, 0, 1, 3, 10);
    
    //speed is based on volume
    speed = map(vol, 0, 1, 0.5, 1.5);

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

