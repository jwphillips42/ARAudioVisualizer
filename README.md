ARVisualizer
============

The ARVisualizer is an Augmented Reality program written in Processing that displays 3D
on top of fiducial markers that it picks up via the camera. The user can affect the 
object that's displayed using QR codes.

Markers
------- 

All of the markers used with the ARVisualizer can be found within the data folder.

Fiducial Marker - AV.png
QR Codes - Within the QR Patterns folder, there are two subdirectories, one for colors,
one for shapes. The shape QR codes can be used to change the shape that's being projected.
The color QR codes work with the Cube and Sphere shapes, letting the user play with their
colors.

Using The Visualizer
--------------------

There's a Mac-runnable version of the visualizer inside the application.macosx64 directory.
This includes all of the necessary libraries and imports, so you don't need to rework your
Processing sketchbook.

To see a shape, just hold the AR fiducial marker up to the screen.
To change the shape, hold up the QR code for the shape you want to see, then bring the
fiducial marker back.
To change the color for the sphere or cube, hold up the QR code for the color you want.

All of the shapes respond to audio input from the computer's microphone. We recommend
starting with one of the geometric shapes and playing a song with a clear, steady beat. 
Another One Bites The Dust by Queen was one of our favorites during testing.