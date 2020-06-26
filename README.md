# Udacity Cloud Developer Capstone project
This is the final project of the course Cloud Developer from Udacity.

It's built with a distributed microservice architecture separated into a number of services:

* api - a Spring Boot backend api exposing rest and websocket endpoints.
* client - the frontend client featuring an Angular Single Page Application.
* RabbitMQ - message broker used to send p2p chat messages, distributing user online statuses, and pinging other users.
* PostgreSQL - RDMS for persisting user information and chat messages.
* reverseproxy - an NGINX web server which acts like a reverse proxy, routing requests to the relevant service.

It is published on [https://bjorkcapstone.codespirit.se/](https://bjorkcapstone.codespirit.se/) and is hosted in AWS EKS (Elastic Kubernetes Service.

The Docker images are published at [https://hub.docker.com/u/patrikb](https://hub.docker.com/u/patrikb).

For user signup, authentication and authorization Auth0 is used. Auth0 libraries are used both in the client and the api.

The application has a number of features, such as:

* Signup
* Logging in/out
* See own profile information
* List other users.
* See which users are online.
* Request to chat with a user.
* The other user will get a notice that another user wants to chat.
* The users can then chat with each other.


## Prerequisites
* Java 11+
* Maven
* NPM

## Building
From the root of the project execute:

```
./build-all.sh
```

## Running with docker-compose
From the root of the project execute:

```
./run-docker-compose.sh
```

Then browse to `http://localhost:4200`

## Deploy to Kubernetes
Execute:

```
kubectl apply -f k8-yaml/
```

## CI/CD - Github workflows
As code is pushed into the repository one or more workflows are triggered. See `.github/workflows`. The workflows build and test the services, builds Docker images which are pushed to Dockerhub, and last deployed to Kubernetes.

## Logging
E.g. the application logs are logged to AWS CloudWatch via FluentD. See [https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/Container-Insights-setup-logs.html](https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/Container-Insights-setup-logs.html) for the setup instructions that have been followed.