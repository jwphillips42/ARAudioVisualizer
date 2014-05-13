class Whale extends Model
{
  float x, y, z;
  float len, wid, hei;
  boolean incr = true;
  float whalesize= 80;
  int angle;
  float waterAmount=1;
  float yangle=0;
  float yanglec = 2;
  float yanglev = 0;

  
  PShape whaleImg;
  
  ArrayList<Water> theStuff;
  
  
  
  Whale()
  {
    x = 0;
    y = 150;
    z = 250;
    
    
    
    theStuff = new ArrayList<Water>();

    //look in our objec file
    whaleImg = loadShape("whaleGEO.obj");
    
    //scale the volcano
    whaleImg.scale(15,15,15);
    
   
    
  }
  
  void display()
  {
    float vol = volume;

        
    
    
    //map yanglec to the volume
    yanglev = map(vol, 0, 1, 1,4 );
    
    
    //change the y angle
    if(yangle>20)
    {
      yanglec-=yanglev;
    }
    else if(yangle<-20)
    {
      yanglec+=yanglev;
    }
    //change the angle
    angle += 5;
    yangle += yanglec;
    
     //there are more particles with more volume
    waterAmount = map(vol, 0, 1, 5, 5);
    
     // create lava if there is sound
    if (vol > 0.03)
    {
      for (int j = 0; j < waterAmount; j++)
      {
         
      
        Water newStuff = new Water(x, y, z);
        theStuff.add(newStuff);
      }
    }

    // display all water
    for (int i = 0; i < theStuff.size(); i++)
    {
      Water newStuff = theStuff.get(i);

      // move the lava
      newStuff.move();

      // remove disappeared lava stuff
      if (newStuff.remove == true) {
        theStuff.remove(newStuff);
        i--;
      }
    }
   
    
    
    pushMatrix();
    
   
    
    fill(150);
    
    translate(x, y, z);
    //rotateY(radians(0));
    rotateX(radians(270));
    rotateZ(radians(180+yangle));

    
    //box(len, wid, hei);
    shape(whaleImg);
    
    perspective();
    
    popMatrix();
    
    
    
  }
  
  
  
}



