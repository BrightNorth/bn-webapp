# {{name}}

FIXME


Clojure (1.5.1) application, using Compojure and a stack of useful libs.

## Prerequisites

* JDK 1.7
* MySQL 
* Leiningen

Create the DB as follows:

```
mysql -uroot -p <enter> <enter>
create database {{name}};
grant all on {{name}}.* to '{{name}}'@'localhost' identified by '{{name}}';
flush privileges;
```

## Running

To start a web server for the application, run:

```
lein ring server
```
or open a REPL

```
lein repl
```
and init the application manually:

```
(use '{{name}}.core)
(def svr (start-server))
```

(this last gives you a ref to the Jetty server that you can use to (.stop svr) and (.start svr)

## Testing

For unit testing we are using midje (https://github.com/marick/Midje). To execute unit tests run:
```
lein midje
```
## Configuration

Configuration options are now loaded from a configuration file. For dev this is set to pickup `application.conf` in the root dorectory.

If you run it with jar you need to provide path to configuration file using `DConfig option`:
```
java -Dconfig=application.conf -jar target/{{name}}-0.1.0-SNAPSHOT-standalone.jar
```