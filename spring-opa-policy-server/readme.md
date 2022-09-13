Spring Boot based Policy Server
---

Simple Spring Boot Server which can serve dynamically generated OPA policy bundles.

# Build
```
mvn clean package
```

# Run
```
java target/*.jar 
```

# Bundle URL
The policy bundle is available via http://localhost:8182/service/v1/policies/current/bundle.tar.gz