=================================================================================
=====================                                     =======================
=====================  WSU Khepera Suite - Introduction   =======================
=====================           by Duane Bolick           =======================
=====================                                     =======================
=================================================================================

                           =======================
                           === GETTING STARTED ===
                           =======================


This archive, which you've just unarchived to your own hard drive, contains
4 executable programs.  We're going to tell you what you need to do first, how to
use these programs, and also what the rest of this stuff is.  

The programs you have here are:

	1 - The Khepera Simulator (ksim.jar)
	2 - The Khepera Remote Client (kremote.jar)
	3 - A Windows compiler script (winc.bat)
	4 - A Windows console opener (console.bat)

The Khepera Simulator is the program you'll use to test the controllers
you write in a simulated environment, with a simulated robot.

The Khepera Remote Client is the program you'll use when you want to run your
controllers on the real Khepera.  

The Windows compiler script is a program that makes compiling your controllers
a little easier.

The Windows console opener opens a console window, which you'll use for
compiling your controllers and running the Remote Client program.


                                
                               ================
                               === CONTENTS ===
                               ================


This file contains the following sections, which you need to go through in order
to get started using the WSU Khepera Suite:

1 ...................................................... Opening a console window
2 ............................................................... Installing Java
3 ........................................................ Compiling a controller
4 ......................................................... Running the Simulator
5 ............................................................ Running the Remote
6 ................................................... Remote Client Configuration



=================================================================================
=====================                                     =======================
=====================     1 - Opening a console window    =======================
=====================                                     =======================
=================================================================================


The directory that this file is in is considered to be the 'home' directory for
the WSU Khepera Suite.  It's the directory in which you'll be doing some stuff,
like compiling your controllers, and running the Remote Client program.  Since
you'll need to do things in a console window from within this directory, we gave
you a console window opener program that'll open up a console window where you're
already in the Khepera Suite home directory.

To use this program, double-click on the icon for the console.bat file.  This
will open a console window.  Notice that the directory path that's displayed at
the prompt shows this directory (the last directory in the path will be this one,
WSUKheperaSuite). 

Go ahead and close the console window now.  We'll be using it later in these
instructions.





=================================================================================
=====================                                     =======================
=====================           2 - Installing Java       =======================
=====================                                     =======================
=================================================================================

You need the Java 2 Standard Edition Software Developers' Kit, version 1.4.2.  

(Huh?)

There are 2 flavors of Java you can have installed on your computer - the JRE
and the SDK.  JRE stands for 'Java Runtime Environment,' and SDK stands for
'Software Developers' Kit.' You need the JRE to RUN Java programs, and you need
the SDK to WRITE and COMPILE them.

Most computers have the JRE installed.  Java programs are pretty common on the
web (they're called 'Applets' sometimes), and so most computers either come with
the JRE pre-installed, or your web browser might have led you through the
installation process if you didn't have it, and tried to go to a web page that
used a Java Applet.  Either way, we still need to install the SDK.


                        =================================
                        == HOW TO INSTALL THE JAVA SDK ==
                        =================================

There are 3 steps to doing this part:

1 - DOWNLOAD the installation program
2 - INSTALL the Java SDK
3 - MODIFY your PATH variable to include the bin directory

Don't worry if you don't know what some (or all) of this means.  We're going to
walk you through this step by step, explaining the above 3 steps right now.


1 - DOWNLOADING the installation program

The SDK is what you'll need to get.  Just so you know, the SDK includes the JRE
with it, so if you don't already have the JRE, you're about to get it.  Go to
the Java website at

	http://java.sun.com/j2se/1.4.2/download.html

and download the Java 2 Standard Edition SDK, version 1.4.2. If you see some
numbers after the 1.4.2, such as 1.4.2_06 or something like that, it's OK.  The
important part is the first digits (namely, 1.4.2) of the version number.

If the above link doesn't work, you should go to

	http://java.sun.com

and navigate to the download page from there.  Either way, the important stuff
to know is that you need the following (this is the abbreviated form that you'll
probably encounter on the Java website):

	J2SE v1.4.2 SDK

(which stands for Java 2, Standard Edition, version 1.4.2 Software
Developers' Kit).  You can choose the web install, or the full
download - it doesn't matter.  



2 - INSTALLING the Java 2 SE SDK, version 1.4.2

Once you've downloaded the installer, go ahead and start it (double-click on it).
Now, take note of where the files go when it's being installed.  If you don't
specify otherwise (which you shouldn't do, unless you really know what you're
doing) they'll probably end up in

	C:\j2sdk1.4.2_06

Note - those last few numbers MAY BE DIFFERENT, or may not be there at all.  



3 - MODIFYING your PATH variable

Click on the START menu, and click CONTROL PANEL.  From the CONTROL PANEL, go to
SYSTEM, and click the ADVANCED tab at the top.  At the bottom of the ADVANCED pane, 
click the button labelled Environment Variables.

You should see 2 panes - one on top, and one on the bottom.  The bottom one is
labelled System Variables.  Scroll through that one until you locate one called Path.
Click on that line in the pane (the one labelled Path) so that it's highlighted in
blue, and then click the Edit button near the System Variables pane.  A little window
should pop up, called Edit System Variable.  There'll be 2 text areas - one labelled
Variable Name, and one labelled Variable Value.  Click in the area labelled Variable
Value, so that its contents are highlighted blue, and scroll to the far right (you
can do this by either pressing the 'End' key on your keyboard, or using the
Right Arrow key to move over.)

Once you're at the rightmost end of the text in the Variable Value box, type a semicolon.
Then, type the full path of the directory that contains the Java SDK and follow it
with \bin - you'll end up typing something like:

	;C:\j2sdk1.4.2_06\bin

Yes, that semicolon is SUPPOSED TO BE THERE.  To recap, you should've typed a SEMICOLON
followed by the PATH TO THE SDK followed by \bin

Once you've done this, click OK on all the windows you just opened.

And we're done!



4 - TESTING to see if you've done everything right

Double-click on the icon for console.bat to open a console window.  Once the
console window opens, type

	java -version

at the prompt.  If you've done everything correctly, you should see a few lines
of text, the first of which should be:

	java version "1.4.2_06"

(or something like that - it might not have the _06 at the end...)  If you
haven't done everything right, you'll see a message that tells you the file
wasn't found, or some error like that.  If you see the error, go through the
above steps to ensure you've done everything correctly.




=================================================================================
=====================                                     =======================
=====================      3 - Compiling a Controller     =======================
=====================                                     =======================
=================================================================================


The source code files for your robot controllers should be kept in the source
directory.  The Java class files of your robot controllers need to be kept in the
controllers directory so the simulator and remote client programs can find them.

To compile a controller source code file into a Java class file, use the Windows
compiler script - it's called winc.bat.  When you use this program, it'll compile
your source code file that's in the source directory, and automatically move the
resulting class file to the controllers directory.

For demonstration purposes, there's an uncompiled source code file called

	basic_reactive.Java

located in the source directory.  We're going to go through the steps to compile
it now.

1) Open a console window (by double-clicking on the console.bat icon)
2) Run winc, giving it the name of the controller (without the .Java extension)
   Here's what you need to type:

            winc basic_reactive

3) That's it!  If Java is properly installed, you'll see a line or two, executing
   the compile command, and then you'll be back at the prompt.




=================================================================================
=====================                                     =======================
=====================       4 - Running the Simulator     =======================
=====================                                     =======================
=================================================================================

To start the Khepera Simulator, you can type the following at the prompt of a
console window (one you opened using console.bat):

              
            java -jar ksim.jar


Or, if you're using Windows, you should be able to double-click the ksim.jar icon.
Either way, wait for a moment, and the simulator program will open.




=================================================================================
=====================                                     =======================
=====================       5 - Running the Remote        =======================
=====================                                     =======================
=================================================================================

To start the Remote Client program, which you'll use to run your controllers on
the real Khepera in our lab (from the comfort of your home, or office or computer
lab), open a console window, and type the following:


           java -jar kremote.jar


Do that now.  You'll see the following lines displayed in your console window:


	-Configuration baboo55 loaded-
	WSU Khepera Remote Interface
	Press CTRL-C to exit

	Webcam URL: http://robocam.cs.wright.edu        

	Please type the number for the Controller you wish to run
	0 basic_reactive


Go ahead and type

	0

(that's a zero) and press enter.  You'll see:


	Loading basic_reactive...
	Server Unavailable


Normally, you'd see an indication that your controller was running, or a timer 
counting down the time until it was your turn to run.  
	



=================================================================================
=====================                                     =======================
=====================   6 - Remote Client Configuration   =======================
=====================                                     =======================
=================================================================================

The configuration of the remote client (kremote.jar) is controlled by the file
named client.conf, which is located in the same directory as kremote.jar.  (and
everything else in the Khepera Suite.

This file is very important to the proper operation of the remote, and you should
make sure that it's contents are correct before attempting to run the remote.

The file client.conf is a text file, and you can load it in a plain text editor,
like Notepad, to edit it.  The first character on each line of text is very
important - it determines exactly what the function of that line is.  There are 3
different characters you can start a line with - the pound sign (#), an exclamation
point (!), or the 'at' sign (@).  Here's what each means:


Lines starting with # are comment lines.  They are ignored by the remote client, and
exist only to tell people who are reading the contents of client.conf something.
The file begins with a bunch of these comment lines, which explain (basically) how
the file works.

Lines starting with ! will be echoed (that means 'printed out') in the console window
when you run kremote.jar.  There's only one of these lines, at the end of the file
which is:


	!- Configuration baboo55 loaded -


When you start the remote, look for this line.  It'll be displayed in the console.
You can put other things to be echoed in client.conf.  It doesn't matter what it is
(recipe for chicken soup, a dirty limerick, etc.) Just make sure that EACH LINE
begins with an exclamation point.

Lines starting with @ are the lines that actually tell the program stuff it needs
to know to run.  These lines should contain NO SPACES and be of the form


	@variableName=value


In other words, "something equals something else."  As far as the "somethings" that
you can specify, there are only a few.  Here they are:

	@CONTROLLER_TIMEOUT
	@LISTENER_TIMEOUT
	@COMMANDER_TIMEOUT
	@UPDATER_TIMEOUT

	@PATH
	@IP
	@PORT
	@WEBCAM_URL


The first four items should have these EXACT values:



	@CONTROLLER_TIMEOUT=100
	@LISTENER_TIMEOUT=5
	@COMMANDER_TIMEOUT=100
	@UPDATER_TIMEOUT=100


Unless someone more knowledgeable has told you to change these (i.e., a course instructor,
the author of this program), you should make sure that these 4 lines match the above 4 EXACTLY.

We'll go through the next 4 items one-by-one.



	@PATH=./controllers/

The PATH variable sets where the remote will look for your compiled class files of your robot
controllers.  You shouldn't change this, because the other programs (like the simulator and 
the Windows compiler script) use this same directory.



	@IP=130.108.23.31

This line should be the IP address of the robot you're trying to connect to.  If you are using
this program in conjunction with a class, you should verify with your instructor, or the 
administrator of the live robot that you have the correct address here.



	@PORT=2600

This line is the port number on which the robot resides.  As with the IP address, you should
verify with whoever is administering the robot class/lab/whatever that this number is correct.



	@WEBCAM_URL=http://ehrg10.cs.wright.edu:8080


This line is just to remind you of where you can look at the robot if you're running it
remotely.  It really only means anything if you're taking the mobile autonomous robotics class
at Wright State University.  Otherwise, ignore this.

Unless otherwise directed by your instructor/lab admin/etc. you should make sure that 
client.conf contains the same values for the TIMEOUT variables as we showed you here.  You should
ensure that you have the correct IP and PORT values for your specific situation (ask your
instructor/admin/whoever), and you should leave PATH alone.