class Lava 
{
  float x, y, z;
  float lavaSize = 5;
  float lavaSpeed=5;
  boolean up = true;
  boolean left;
  boolean zchange;
  float yspeed;
  float xspeed;
  boolean remove;
  float rand;


 
  Lava(float x, float y, float z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    
    
    
    yspeed = random(6,10);
    xspeed = random(2,6);
    
    rand = random(0,20);
    
    //choose a random direction
    if (rand<8)
    {
      left = true;
    }
    else if (rand<10)
    {
      left = false;
    }
    else if (rand<15)
    {
      zchange = true;
    }
    else if (rand<20)
    {
      zchange=false;
    }
    
    
  }

  void move()
  {
    float vol = volume;
    
    pushMatrix();

    translate(x, y, z+300);

    fill(random(255),0,0);
    //fill(0);

    lavaSize = map(vol, 0, 1, 5, 25);
    lavaSpeed= map(vol, 0, 1, 2, 26);

    //take off the stroke weight
    strokeWeight(0);
    sphere(lavaSize);
    
    
    //change the lavas position
    //if its going up then go up
    if (up)
    {
      z+= yspeed + lavaSpeed;
      //x+=2;
      //make sure its not too high
      if (z>=200)
      {
        up=false;
      }
    }
    //go down
    else
    {
      z-=yspeed + lavaSpeed;
      //z-=2;
      
      //check if its going left or right
      if(left)
      {
        y-= xspeed + lavaSpeed;
        if (random(0,4)>2){x+=2;}
        else{x-=2;}
    
      }
      else if(!left)
      {
        y+=xspeed + lavaSpeed;
        if (random(0,4)>2){x+=2;}
        else{x-=2;}
      }
       if(zchange)
      {
        x-= xspeed + lavaSpeed;
        if (random(0,4)>2){y+=2;}
        else{y-=2;}
    
      }
      else if(!zchange)
      {
        x+=xspeed + lavaSpeed;
        if (random(0,4)>2){y+=2;}
        else{y-=2;}
      }
      
        
      //check if its off the screen
      //if so reset it
      if(y>300 || y<-100)
      {
        remove = true;
      }
    }
    /*
    //check z change
    if(zchange)
    {
      z+=2;
    }
    else
    {
      z-=2;
    }
    */

    
    perspective();

    popMatrix();
  }
}




