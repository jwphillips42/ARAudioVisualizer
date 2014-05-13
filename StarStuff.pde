//smaller particles emitting from Star

class StarStuff
{
  float x, y, z;
  color c;
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

  void move(float vol)
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
    this.a= a - 2.5;

    perspective();

    popMatrix();
  }
}


