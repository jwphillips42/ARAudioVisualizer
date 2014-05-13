class Ball extends Model
{
  float x, y, z;
  float rad;
  int angle;
  
  Ball()
  {
    x = 0;
    y = 0;
    z = 250;
    
    rad = 30;
  }
  
  void display()
  { 
    //VolumeResponse
    rad = map(volume, 0, 1, 40, 200);
    
    angle += 5;
    
    pushMatrix();
    
    translate(x, y, z);
    
    //Rotation
    rotateZ(radians(angle));
    rotateX(radians(angle));
    rotateY(radians(angle));
    
    strokeWeight(2);
    sphere(rad);
    
    popMatrix();
  }
}
