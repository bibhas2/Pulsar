###Overview
Pulsar is a job schduler for Java EE applications. It is kept simple and easy to adopt in
an application.

##Building
Pulsar uses Maven as the build system. Run these commands to build Pulsar.

```
git clone https://github.com/bibhas2/Pulsar.git
cd Pulsar/pulsar
mvn clean install
```

The JAR file will be created in the target folder.

##Using Pulsar
If your project uses Maven, then simply add the dependency:

```
<dependency>
	<groupId>com.mobiarch</groupId>
	<artifactId>pulsar</artifactId>
	<version>1.0.0-SNAPSHOT</version>
</dependency>
```

If you are not using Maven, then add the Pulsar JAR file to your WEB-INF/lib.

##Creating a Task
A POJO task class needs to implement the ``com.mobiarch.pulsar.Task`` interface.

```
public class TestTask implements Task {
	private static final long serialVersionUID = 8122298388820623514L;
	Logger logger = Logger.getLogger(getClass().getName());
	
	public void execute() throws RemoteException, Exception {
		logger.info("Scheduled task running.");
	}

}
```

The execute method gets called periodically on schedule.

##Defining the Task Schedule
Create a file called WEB-INF/schedule.xml. Register the tasks there.

```
<?xml version="1.0" encoding="UTF-8"?>
<schedule>
	<!--Execute a Java class every 10 minutes -->
	<task type="Java" id="com.mobiarch.pulsar.test.TestTask"
		frequency="10" />
</schedule>
```
