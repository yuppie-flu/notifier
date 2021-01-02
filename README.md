### Notifier service

Prototype of a simple spring boot service which allows subscribing to favorite Reddit subreddits
and receiving a summary with top posts by email.

### Requirements

* JDK 11
* Docker, docker-compose

### Running locally

1. `docker-compose up` - pulls postgres image and starts it
1. `./mvnw clean spring-boot:run` - starts the spring boot app locally

### Development

Kotlin code is formatted using [ktlint](https://github.com/pinterest/ktlint).
Run `./mvnw antrun:run@ktlint-format` to autoformat the code.

### TODO list

[TODO list](docs/todo-list.md)