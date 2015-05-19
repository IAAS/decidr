# SVN #

## Tools for SVN ##
SmartSVN: http://www.syntevo.com/smartsvn/download.html

This is a platform independet tool for using SVN. I recommend to use this tool because it is very easy to use and has most of the functions we need.

For the documentation there are several links for the necessary topics:

### Link for checking out a project ###
http://www.syntevo.com/smartsvn/documentation.html?page=commands.check-out

### Link for updating a project ###
http://www.syntevo.com/smartsvn/documentation.html?page=commands.update

### Link for adding/marking a file ###
http://www.syntevo.com/smartsvn/documentation.html?page=commands.add

### Link for commiting the changes on your local system ###
http://www.syntevo.com/smartsvn/documentation.html?page=commands.commit

## Using SVN in the console ##

The first time you have to checkout, to get the structure from SVN on your system:
**svn checkout https://decidr.googlecode.com/svn/trunk/ project\_folder --username username\_for\_google\_account**

If you want to upload a new file you have to mark it first:
**svn add file\_to\_add**

To get the latest version you can update your project's folder:
**svn update project\_folder**

To upload all marked files in your project's folder you have to commit your changes:
**svn commit project\_folder**


Deleting a file in the repository:
**svn delete file\_to\_delete**


The first thing you have to do is to checkout. Create a folder where you want to save the project and use the command above (my operating system is Linux, for other distributions or operating systems there are other tools, but ask me if you have questions). When you work on a new file and you want to add it, use svn add and then commit the added file. DON'T FORGET TO COMMIT IF YOU ADD A NEW FILE!!!! Also you should update your project's folder once a day. I suggest that deleting a file must not be used by a regular user. It should be discussed in the group and with the project leader. WRITE COMMENTS IF YOU COMMIT NEW FILES! THE COMMENTS SHOULD BE WELL DECLARED AND SELF-EXPLANATORY!!!!!

## For the comments the following standard will be used ##
First you have to put your abbreviation in your comment. After a colon you write your comment. If you changed files, please write which file you have changed. If you added a new file, write the filename of the newly created file. Also for deleting a file.<br>
<b>Example: AT: Added test.java. Changed test2.java, added a new function testprint().</b><br>
If you have any questions ask me!