=====================
GTAcampuS Android Application
=====================

**GTAcampuS** is an Android application (supports GINGERBREAD or later versions) 
intended for students and is developed basically for Aakash Tablets. `Aakash
<http://aakashlabs.org>`_ is a low cost computing device/tablet for
students, the project is initiated and funded by MHRD, Govt. of
India. Aakash already runs Android 4.2 with many educational apps
developed at IIT Bombay.  GTAcampuS provides an easy to use interface and is specially developed for making the day to day life of a student more easy. I have tried to
make the user's experience simple and elegant.

This initial version of the application is mounted with an attendance management system, group messaging and lot of other features. This application will make alerts before the class hours, on making alert application will provide options like ‘SNOOZE’, ‘ON THE WAY’ & ‘BUNK’. If the student dismiss the alert with the 'ON THE WAY' option then the application will turn off the alert volumes of the device and will switch the device to silent mode automatically which will be restored back to normal state after the class time. If ‘BUNK’ is selected then bunk-o-meter database of the application will be updated and the no: of classes bunked will be incremented for that respective course. If the no: of bunked classes crosses a limit then the application will give notifications. The application will provide a timetable view showing the class hours user have in the week. User can check the statistics and details of the classes they bunked at any time. This application also contains a calculator, a converter, feature for setting extra-alerts, saving notes and also option for back-up and restoring the database. You can also send group messages using this application. Messages can be seen by all those who are configured to the particular server, this will be much useful for class representatives or teacher for passing an information to all students in a class.

You can watch a DEMO VIDEO of the application `here <http://www.youtube.com/watch?v=FXxfec_vvOc>`_


Main Features
-------------
	
	- Attendance Management
	- Alerts for classes & Custom alerts
	- Bunk-O-Meter
	- Timetable
	- Notes
	- Convertor
	- Calculator
	- Group Messaging
	- Password Protection, Backup & Restore Features


How to install
---------------
Simple and recommended install procedure
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

- Open the browser on Aakash and visit this `link <https://drive.google.com/folderview?id=0B434AcDa-8UvYm5kV2tPNjBFTGs&usp=sharing>`_.
- Download and install the GTAcampus.apk file.
- The APK file of the application is also available at `Amazon appstore <http://www.amazon.com/Godly-T-Alias-GTAcampuS/dp/B00E7AQLM0>`_.
- Download the `Web-service files <https://drive.google.com/folderview?id=0B434AcDa-8UvYm5kV2tPNjBFTGs&usp=sharing>`_ and host it in a server (if you want to use group-messaging feature).
- After installing the application, click on the |icon| icon in the android application menu.
.. |icon| image::  res/drawable-mdpi/ic_launcher.png
   :align: middle
   :height: 1
   :width: 1
- On first use the application is to be initialized with the class timings, registered courses, username, password and also server address where the web-service files are hosted



Usage 
------

This `branch` contains an Android(2.2, API-8) code.

Users can clone this repo by typing :

   git clone https://github.com/godlytalias/Android.git 

Importing **GTAcampuS** as an `eclipse <http://www.eclipse.org/>`_ project
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

- For setting up Android SDK and AVD in eclipse, please visit this `link  <http://developer.android.com/sdk/index.html>`_
- With a single download, the ADT Bundle includes everything you need to 
  begin developing apps:
	#. Eclipse + ADT plugin
    	#. Android SDK Tools
    	#. Android Platform-tools
    	#. The latest Android platform
    	#. The latest Android system image for the emulator

  
 
After cloning the required branch, start eclipse

- go-to ``File`` menu -> ``Import``.
- from the ``Import`` dialog box, select ``Android``.
- from ``Android`` section, select ``Existing Android Code Into
  Workspace`` and click ``Next`` button.
- you will be taken to ``Import Projects`` dialog box, click ``Browse``
  button and select the cloned repository.


Documentation
-------------

For User manual and Technical Documentation, please visit `here <https://github.com/godlytalias/Android/tree/master/workspace/GTAcampuS/docs>`_ or read my `blog <http://godlytalias.blogspot.com/2013/09/gtacampus-android-application-source.html>`_


Help, bugs, feedback
--------------------
	#. Users can mail their queries, feedback and suggestions at godlytalias@yahoo.co.in 
	#. Developers/Contributor can raise issues at `issues <https://github.com/godlytalias/Android/issues>`_ or in my `blog <http://godlytalias.blogspot.com/2013/09/gtacampus-android-application-source.html>`_
	#. Pull requests are most welcome. 


License
-------

GNU GPL Version 3, 29 June 2007.

Please refer this `link <http://www.gnu.org/licenses/gpl-3.0.txt>`_
for detailed description.

All rights belong to `Godly T.Alias <http://godlytalias.blogspot.com>`_.

Copyright © 2013
