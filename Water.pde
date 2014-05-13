class Water 
{
  float x, y, z;
  float waterSize = 5;
  float waterSpeed=5;
  boolean up = true;
  boolean left;
  boolean zchange;
  float yspeed;
  float xspeed;
  boolean remove;
  float rand;

 
  Water(float x, float y, float z)
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

    translate(x+25, y, z);
    

    fill(0,0,random(255));
    //fill(0);

    waterSize = map(vol, 0, 1, 5, 25);
    waterSpeed= map(vol, 0, 1, 2, 16);

    //take off the stroke weight
    strokeWeight(0);
    sphere(waterSize);
    
    
    //change the lavas position
    //if its going up then go up
    if (up)
    {
      z+= yspeed + waterSpeed;
      //x+=2;
      //make sure its not too high
      if (z>=400)
      {
        up=false;
      }
    }
    //go down
    else
    {
      z-=yspeed + waterSpeed;
      //z-=2;
      
      //check if its going left or right
      if(left)
      {
        y-= xspeed + waterSpeed;
        if (random(0,4)>2){x+=2;}
        else{x-=2;}
    
      }
      else if(!left)
      {
        y+=xspeed + waterSpeed;
        if (random(0,4)>2){x+=2;}
        else{x-=2;}
      }
      if(zchange)
      {
        x-= xspeed + waterSpeed;
        if (random(0,4)>2){y+=2;}
        else{y-=2;}
    
      }
      else if(!zchange)
      {
        x+=xspeed + waterSpeed;
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



