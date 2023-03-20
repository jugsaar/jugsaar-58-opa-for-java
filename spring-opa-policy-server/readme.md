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

# Demo

## Run OPA to dynamically fetch bundles

Run as repl
```
opa run \
  --set "bundles.cli1.polling.min_delay_seconds=10" \
  --set "bundles.cli1.polling.max_delay_seconds=20" \
  http://localhost:8182/service/v1/policies/current/bundle.tar.gz
```

Show current users
```
data.authz.spring.users
```

Add new user
```
  {"name": "tom", "subordinates": []}
```

Run as server
```
opa run \
  --server \
  --set "bundles.cli1.polling.min_delay_seconds=10" \
  --set "bundles.cli1.polling.max_delay_seconds=20" \
  http://localhost:8182/service/v1/policies/current/bundle.tar.gz
```