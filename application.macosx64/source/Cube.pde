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
  
  void display()
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
