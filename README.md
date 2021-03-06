# Task Master



## LAB 26

* Create home page Activity.
  
* Create Task Activity.
  
* Create All tasks Activity.

* home
  
    * add task button take user to add activity screen
      
    * all tasks button take user to view all tasks activity screen.
      
    * contain 3 tasks button navigate user to details on task.
    * add setting page button
    
* add activity screen
  
    * add task button make submit new event
    
* add tasks details page
  
    * show the task title and description.
    
* add new setting page
  
    * user can add his name and update it's value.
      
    * name will appear in home page

![](screenshots/lab26_home.png)


![](screenshots/Screenshot_1628447419.png)

## LAB 27

- **Task Details**: add a page with dynamic title and lorem description

![](screenshots/Screenshot_1628538384.png)

## LAB 28

* add a recycler view with 3 tasks in the homepage 

![](screenshots/lab28_home.png)

## LAB 29

* A database was added so the user Can Save tasks from add task screen

![](./screenshots/lab29_home.png)

## Lab 32

In this lab I changed the add task and main activity to create new tasks and save it to dynamoDB and also to retrieve the data from it

## Lab 33

In this lab the add task activity now add the new task to the amplify api with the team,
also in the settings activity the user now can choose his team and the tasks will appear in the homepage based on his team

## Lab34

In this lab I made an apk for this application so it can be published for google store

![](./screenshots/apk.JPG)

## Lab36

In this lab I added a signup and signin pages using amplify authentication 

![](./screenshots/lab36.png)

## Lab37 

In this lab I implement the AWS S3 storage in this project where the user can upload a file with a new task and this file will be associated with the task.
So if there is an uploaded file that is associated with any task it will be shown in the Task Detail page as an image if it is image or there will be a link to download the file if it not an image

This Image shows how the Task Detail looks with the uploaded file as an image:

![](./screenshots/lab37.png)

## Lab38

In this Lab I added the Firebase notification system where you can send a notification and after pressing on it the application will run:

This is an image for the notification message :

![](./screenshots/lab38.png)

## Lab41

In this Lab I added an intent filter with action of send to the add task activity where the user can share a photo from any application with this application , also I added a handler to recive the image

![](./screenshots/sendImage.png)

![](./screenshots/addTaskIntentFilter.JPG)

## Lab42 

In this lab I added an algorithm to add the current location with each added task to s3 storage. And then show this location for a specific task in the task detail page

This Image shows the location as longitude and latitude:

![](./screenshots/lab42.png)

## Lab39 

In this lab I added amplify analytics to collect data about the application

![](./screenshots/lab39.JPG)
