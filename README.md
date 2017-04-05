## Initial Setup
````
$ git clone https://github.com/ykpivot/sample-scdf.git
$ cd sample-scdf
````

You now need to build the source application.

````
$ cd spring-data-flow-source
$ mvn install
````
This will also install the jar to the local maven cache, which will be used, when running SCDF server locally.

## Run locally

The sink app stores data in a mySql database, so you need to install it on your computer. Please find the schema information from the source code.

First, run the server

````
$ cd spring-data-flow-server
$ mvn spring-boot:run

````

Secondly, run the shell

````
$ cd spring-data-flow-shell
$ mvn spring-boot:run
````
Now you can register apps that are cached locally by maven

````
dataflow:> app register --name time-source --type source --uri maven://com.ping.cloud:spring-data-flow-source:jar:0.0.1-SNAPSHOT
dataflow:> app register --name time-processor --type processor --uri maven://com.ping.cloud:spring-data-flow-processor:jar:0.0.1-SNAPSHOT
dataflow:> app register --name time-sink --type sink --uri maven://com.ping.cloud:spring-data-flow-sink:jar:0.0.1-SNAPSHOT
````

You can see all the apps that are registered now

````
dataflow:> app list
````

You now need to create a stream and deploy it

````
dataflow:> stream create --name time-to-string --definition "time-source | time-processor | time-sink"
dataflow:> stream listdataflow:> stream deploy --name time-to-string
````

In order to verify if the stream worked properly, log into your databse instance and see if records are created.

## Run on CF

Follow the instruction on the following page:
[http://docs.spring.io/spring-cloud-dataflow-server-cloudfoundry/docs/current-SNAPSHOT/reference/htmlsingle/](http://docs.spring.io/spring-cloud-dataflow-server-cloudfoundry/docs/current-SNAPSHOT/reference/htmlsingle/)

In order to register an SCDF application to the SCDF server, you need to deploy `.jars` to a publicly-accessible artifact repository such as Maven Central or host them on a website. I hosted on an static file app in PWS to accomplish it. The app is located in the `my-scdf` folder.

Once the following tasks are completed,

1. SCDF server is deployed to CF
2. Required services are provisioned (RabbitMQ and MySQL) on CF
3. Source, processor, and sink app are publicly available

you can now use local shell to access the server:

````
$ cd spring-data-flow-shell
$ mvn spring-boot:run server [your-dataserver-url]
````

Once in the shell, you can then register apps, create and deploy a stream.

````
dataflow:>app register --name time-processor --type processor --uri http://jars.cfapps.io/jar/spring-data-flow-processor-0.0.1-SNAPSHOT.jar
Successfully registered application 'processor:time-processor'
dataflow:>app register --name time-source --type source --uri http://jars.cfapps.io/jar/spring-data-flow-source-0.0.1-SNAPSHOT.jar
Successfully registered application 'source:time-source'
dataflow:>app register --name time-sink --type sink --uri http://jars.cfapps.io/jar/spring-data-flow-sink-0.0.1-SNAPSHOT.jar
dataflow:>app list
dataflow:>stream create --name time-to-string --definition "time-source | time-processor | time-sink"
dataflow:>stream deploy --name time-to-string --properties "deployer.time-sink.cloudfoundry.services=my_mysql"
````

## Working with MySQL on CF
You can find more information on how to setup/access a mySql instance [here](https://docs.run.pivotal.io/devguide/deploy-apps/ssh-services.html).


You can get your database credential by:

````
$ cf create-service-key my_mysql EXTERNAL-ACCESS-KEY
$ cf service-key my_mysql EXTERNAL-ACCESS-KEY
==>
{
 "hostname": "us-cdbr-iron-east-03.cleardb.net",
 "jdbcUrl": "jdbc:mysql://us-cdbr-iron-east-03.cleardb.net/ad_0682abfc0f7109d?user=b7f238a1792e1a\u0026password=6a6989a4",
 "name": "ad_0682abfc0f7109d",
 "password": "6a6989a4",
 "port": "3306",
 "uri": "mysql://b7f238a1792e1a:6a6989a4@us-cdbr-iron-east-03.cleardb.net:3306/ad_0682abfc0f7109d?reconnect=true",
 "username": "b7f238a1792e1a"
}
````

Then you can access your database as follows:

````
$ cf ssh -L 63306:us-cdbr-iron-east-03.cleardb.net:3306 atl-demo-dataflow-server // 63306 is an available local port
$ mysql -u b7f238a1792e1a -p -h us-cdbr-iron-east-03.cleardb.net
````

