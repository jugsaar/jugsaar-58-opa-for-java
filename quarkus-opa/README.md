Quarkus Open Policy Agent Example
---

cd quarkus-opa

# Start OPA Container
docker run -d -p 8181:8181 --rm --name opa-server openpolicyagent/opa:0.50.1 run --server --set "decision_logs.console=true"

# Push policies
curl -X PUT --data-binary @opa/policy.rego  localhost:8181/v1/policies/authz/quarkus

# Show logs
docker logs -f opa-server 2>&1 | jq  -C '.'

http://localhost:8090/hello

tester:tester can greet only itself
admin:admin can greet everyone


curl -v --user tester:tester http://localhost:8090/hello
curl -v --user tester:tester http://localhost:8090/hello/javaland

curl -v --user admin:admin http://localhost:8090/hello
curl -v --user admin:admin http://localhost:8090/hello/javaland

# Delete opa-server container
docker rm -f opa-server