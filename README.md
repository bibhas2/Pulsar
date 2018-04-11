### Overview
Pulsar is a job schduler for Java EE applications. It is kept simple and easy to adopt in
an application. A task can be developed using one of:

- A plain Java class.
- An EJB, usually a statless session bean. Use this approach if you want transaction, 
security and other services offered by the EJB container.

## Building
Pulsar uses Maven as the build system. Run these commands to build Pulsar.

```
git clone https://github.com/bibhas2/Pulsar.git
cd Pulsar/pulsar
mvn clean install
```

The JAR file will be created in the target folder.

## Using Pulsar
If your project uses Maven, then simply add the dependency:

``` xml
<dependency>
	<groupId>com.mobiarch</groupId>
	<artifactId>pulsar</artifactId>
	<version>1.0.0-SNAPSHOT</version>
</dependency>
```

If you are not using Maven, then add the Pulsar JAR file to your WEB-INF/lib.

Register the scheduler Servlet to load at startup in web.xml.

``` xml
<servlet>
	<servlet-name>SchedulerServlet</servlet-name>
	<servlet-class>com.mobiarch.pulsar.SchedulerServlet</servlet-class>
	<load-on-startup>1</load-on-startup>
</servlet>
```
## Creating a Task
A POJO task class needs to implement the ``com.mobiarch.pulsar.Task`` interface.

``` java
public class TestTask implements Task {
	private static final long serialVersionUID = 8122298388820623514L;
	Logger logger = Logger.getLogger(getClass().getName());
	
	public void execute() throws RemoteException, Exception {
		logger.info("Scheduled task running.");
	}

}
```

The execute method gets called periodically on schedule.

## Defining the Task Schedule
Create a file called **WEB-INF/schedule.xml**. Register the tasks there. Specify the interval in minutes using the **frequency** attribute.

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<schedule>
	<!--Execute a Java class every 10 minutes -->
	<task type="Java" id="com.mobiarch.pulsar.test.TestTask"
		frequency="10" />
</schedule>
```

You can run a task at a specific time of day. For example, the following will run the task every day at 3PM.

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<schedule>
	<!--Execute a Java class once a day at 3PM -->
	<task type="Java" id="com.mobiarch.pulsar.test.TestTask"
	        start="15:00"
		frequency="1440" />
</schedule>
```

## Creating an EJB Task
Create a stateless session bean that implements the ``com.mobiarch.pulsar.Task`` interface.

``` java
@Stateless
public class SimpleEJB implements Task {
	private static final long serialVersionUID = -6110222237944363619L;
	Logger logger = Logger.getLogger(getClass().getName());

	public void execute() throws RemoteException, Exception {
		logger.info("***Scheduled EJB task running.");
	}
}
```

Register the EJB in **schedule.xml** using its JNDI name as the ID. There is no standard
at this point for the JNDI name and it varies wildely based on the container. For
example, in TomEE, the JNDI name is ``java:global/<project name>/<EJB name>``. Assuming
our EJB was created in a web project called MyWeb, the schedule.xml file will look like
this:

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<schedule>
	<!-- Execute a stateless session bean every 1 minute -->
	<task type="EJB" id="java:global/MyWeb/SimpleEJB"
		frequency="1" />
</schedule>
```
