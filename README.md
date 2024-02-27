# Spring Stork guitar hero

This is a demo project for Quarkus developers wanting to use Smallrye Stork for service discovery and instance selection.
This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

The following picture represents our system: it's a very simple application with an UI which is calling a dispatcher resource API. 

![](https://github.com/aureamunoz/stork-guitar-hero-demo/blob/main/Stork%20guitar%20hero%20architecture.png)

Dispatcher resource is calling a Guitar hero service running remotely. The dispatcher service use the Rest Client for that and Rest Client as shown in the picture is going to delegate to Stork the service discovery and instance selection.
The Guitar hero service provides 3 instances: Slash, Jimi Hendrix and Eddie Van Halen.

Slash instance returns the slash version of duke.
The hendrix instance returns the Jimmy hendrix one and is slightly slower than the Slash service.
Finally, the Eddie service is the fastest but with a 20% chance of failing.

We will use Stork in the Rest client when the Dispatcher service need to invoke the remote service: Stork will locate this service. 
This step retrieves 3 service instances, then we need to pick one, that's where the Stork load balancing capability comes into play for selecting one according to different strategies.

Slash instance returns the slash version of duke.
The hendrix instance returns the Jimmy hendrix one and is slightly slower than the Slash service.
Finally, the Eddie service is the fatests but with a 20% chance of failing.

We will use Stork when the Dispatcher service need to invoke the remote service: Stork will locate this service. This steps retrieves 3 service instances, then we need to pick one, thats where the Stork load balancing capability comes into play for selecting one according to different strategies.


## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/stork-guitar-hero-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- RESTEasy Reactive ([guide](https://quarkus.io/guides/resteasy-reactive)): Reactive implementation of JAX-RS with additional features. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.

## Provided Code

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)


## Consul help

https://developer.hashicorp.com/consul/tutorials/day-0/docker-container-agents#docker-container-agents

Set up Consul in Docker
docker pull hashicorp/consul
docker images -f 'reference=hashicorp/consul'
docker run -d -p 8500:8500 -p 8600:8600/udp --name=badger hashicorp/consul agent -server -ui -node=server-1 -bootstrap-expect=1 -client=0.0.0.0
docker exec badger consul members
docker run --name=fox hashicorp/consul agent -node=client-1 -retry-join=172.17.0.3
docker exec fox /bin/sh -c "echo '{\"service\": {\"name\": \"slash\", \"tags\": [\"guns-n-roses\"], \"port\": 9000}}' >> /consul/config/slash.json"
docker exec fox /bin/sh -c "echo '{\"service\": {\"name\": \"hendrix\", \"tags\": [\"jimi\"], \"port\": 9001}}' >> /consul/config/hendrix.json"
docker exec fox /bin/sh -c "echo '{\"service\": {\"name\": \"eddie\", \"tags\": [\"van-halen\"], \"port\": 9002}}' >> /consul/config/eddie.json"\n
docker exec fox consul reload
curl -X PUT -d '{"ID": "slash", "Name": "guitar-hero-service", "Address": "localhost", "Port": 9000, "Tags": ["guns-n-roses","slash"]}' http://127.0.0.1:8500/v1/agent/service/register
curl -X PUT -d '{"ID": "hendrix", "Name": "guitar-hero-service", "Address": "localhost", "Port": 9001, "Tags": ["legend","Woodstock"] }' http://127.0.0.1:8500/v1/agent/service/register
curl -X PUT -d '{"ID": "eddie", "Name": "guitar-hero-service", "Address": "localhost", "Port": 9002, "Tags": ["van-halen","Frankenstrat"]}' http://127.0.0.1:8500/v1/agent/service/register\n


Register services:
docker exec fox /bin/sh -c "echo '{\"service\": {\"name\": \"slash\", \"tags\": [\"guns-n-roses\"], \"port\": 9000}}' >> /consul/config/slash.json"
docker exec fox /bin/sh -c "echo '{\"service\": {\"name\": \"hendrix\", \"tags\": [\"jimi\"], \"port\": 9001}}' >> /consul/config/hendrix.json"
docker exec fox /bin/sh -c "echo '{\"service\": {\"name\": \"eddie\", \"tags\": [\"van-halen\"], \"port\": 9002}}' >> /consul/config/eddie.json"
docker exec fox consul reload

curl -X PUT -d '{"ID": "slash", "Name": "guitar-hero-service", "Address": "localhost", "Port": 9000, "Tags": ["guns-n-roses","slash"]}' http://127.0.0.1:8500/v1/agent/service/register
curl -X PUT -d '{"ID": "hendrix", "Name": "guitar-hero-service", "Address": "localhost", "Port": 9001, "Tags": ["legend","Woodstock"] }' http://127.0.0.1:8500/v1/agent/service/register
curl -X PUT -d '{"ID": "eddie", "Name": "guitar-hero-service", "Address": "localhost", "Port": 9002, "Tags": ["van-halen","Frankenstrat"]}' http://127.0.0.1:8500/v1/agent/service/register

Para borrar
curl -X PUT http://127.0.0.1:8500/v1/agent/service/deregister/slash
