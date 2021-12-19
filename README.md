# User Auth

This is an Authentication and Authorization back-end service to demonstrate how it works with SPA applications as part of my book [Understanding React](https://leanpub.com/understandingreact). This book is completely free for my students (if you want to read it, just write to me).

# Install and Start

- Install maven on your computer
- git clone https://github.com/enriquemolinari/userauth.git userauth
- mvn install
- mvn exec:java -Dsecret=bfhAp4qdm92bD0FIOZLanC66KgCS8cYVxq/KlSVdjhI= 
  - This will start Javalin/Jetty with the services running on Port: 1234. And set the shared secret. You can create any cryptographically secure secret, as long as it is the same used by the [Task List](https://github.com/enriquemolinari/tasklist) microservice. The secret is used to create and validate paseto tokens).

# Users

- guser/guser123
- juser/juser123