Group 4
Andrew Litzerman
Rishabh Dutta
Arun Joel John
Tim-Lukas Katko


-----README------

MySQL Workbench is required. To begin, start SQL Workkbench. 
URL must = "dbc:mysql://localhost:3306/cs6650" 
User must = "root"
Password must = "admin"

In SQL, run the "sql schema.txt" file located in the SQL project folder. This will create the needed databases and tables.

In the Java project, one JAR file is required: mysql-connector-java-8.0.16.jar. This JAR must be in the Build Path of the project.

To begin, the application requires starting six servers in the below order.
1: Start RMIRegistry
2: Run as Java Application RoundRobinServer.java 
3: Run as Java Application Server1.java
4: Run as Java Application Server2.java
5: Run as Java Application Server3.java
6: Run as Java Application Server4.java
7: Run as Java Application Server5.java

From there, Run as Java Application Client.java and input commands in the Client.java console.
The menu will show 5 different options, input 1-5 in the console to select.
The options are:
1: Create Account
2: Get Balance
3: Withdraw Funds
4: Deposit Funds
5: Show Menu

Creating an account requires entering a first name, last name, and username of your choosing.
Follow the console instructions for each command.
