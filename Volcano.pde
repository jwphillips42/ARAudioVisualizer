class Volcano extends Model
{
  float x, y, z;
  float len, wid, hei;
  boolean incr = true;
  float volcsize= 80;
  int angle=5;
 
  float lavaAmount=1;
  
  PShape volcImg;
  
  ArrayList<Lava> theStuff;

  
  //create the lava 
  //Lava[] myLava;
  
  Volcano()
  {
    x = 0;
    y = 150;
    z = 250;
    
    len = 80;
    wid = 80;
    hei = 160;
    
    theStuff = new ArrayList<Lava>();

    //look in our objec file
    volcImg = loadShape("volcanoGEO.obj");
    
    //scale the volcano
    volcImg.scale(15,15,15);
    
    //set the volume 
    //System.out.println(vol);
    
    
    
  }
  
  void display()
  {
    
      float vol = volume;
    
    
    //change the angle
    angle += 5;
    
    
    //there are more particles with more volume
    lavaAmount = map(vol, 0, 1, 1, 5);

    // create lava if there is sound
    if (vol > 0.03)
    {
      for (int j = 0; j < lavaAmount; j++)
      {
        //disperse the lava
      // select a random position
       float xPos = random(x, x+5);
       float yPos = random(y-5, y+5);
       float zPos = random(z-250, z-250);   
      
        Lava newStuff = new Lava(xPos, yPos, zPos);
        theStuff.add(newStuff);
      }
    }

    // display all lava
    for (int i = 0; i < theStuff.size(); i++)
    {
      Lava newStuff = theStuff.get(i);

      // move the lava
      newStuff.move();

      // remove disappeared lava stuff
      if (newStuff.remove == true) {
        theStuff.remove(newStuff);
        i--;
      }
    }
    
    pushMatrix();
    
    
    
    
    /*
    //draw the lava
    for (int i = 0; i<myLava.length;i++)
    {
      myLava[i].move();
    }*/
    
    fill(139,90,43);
    
    translate(x, y, z);
    //rotateY(radians(angle));
    rotateX(radians(270));
    rotateZ(radians(180));

    
    //box(len, wid, hei);
    shape(volcImg);
    
    perspective();
    
    popMatrix();
    
    
    
  }
  
  
  
}



