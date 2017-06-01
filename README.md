# Kandidatarb
version 3.0
## Background for this repository
Kandidatarb contains code for the work in the course Independent Project in Sociotechnical Systems Engineering - IT System 10 hp at Uppsala University spring 2017.

```
Contributors:
Elsa Bergman
Angelica Elvin
Anna Eriksson
Arvid Gräns
Frida Kornsäter
Frans Larsson
 
Under supervisor of: 
Dave Clarke, Course Director
Anton Axelsson, Assistant
Davide Vega D'aurelio, Assistant
```

## How to orient yourself in the repository
In app->src->main->java/com/example/android/campusapp you will find .java files that handle the functionality of the app. The java files are named after what kind of user accesses them. student_etc are aimed at student users. org_etc are aimed at organisation user. Some files are not called student_ or org_, these files can be listViewAdapters which handles the lists visisble on different pages, or additional classes that only work together with other files. Both of these type of files are named after the file they are connected to. In some cases, the files are not named student_ or org_ but they are not ListViewAdapters nor files that have to connect to other files. Examples of this are the files add_event, EditEvent, favorites and todays_events. However, these file names are self explanatory, as a student user is the only type of user that can favorite an event and organisation users are the only users that can create events and edit events. 

In app->src->main->res you will find files that handles the layout. Specifically in the layout folder you find .xml files that are responsible for the main design of pages in the app. The xml files are named after what kind of user accesses them. student_etc are aimed at student users. org_etc are aimed at organisation user. The .xml files are also named in a similar way as their .java counterpart in order to make it easier to understand what .xml file belongs to what .java file. In the folder named values you can find some styling xml-files.

To be able to use all the functionalities of the app you need to run a database. To start the app you run the login.java file and continue from there. It is important the the correct ip address to the computer who runs the database is filled in on every page that connects to the database. The variable that needs to be changed in every file is either called serverURL/serverUrl or URL/url. 


