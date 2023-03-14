Quarkus Open Policy Agent Example
---

docker run -d -p 8181:8181 --rm --name opa-server openpolicyagent/opa:0.50.0 run --server --set "decision_logs.console=true"

docker logs -f opa-server 2>&1 | jq  -C '.'

curl -X PUT --data-binary @opa/policy.rego  localhost:8181/v1/policies/authz/quarkus 

http://localhost:8090/hello

tester:tester can greet only itself
admin:admin can greet everyone
