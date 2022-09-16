Quarkus Open Policy Agent Example
---

docker run -d -p 8181:8181 --rm --name opa-server openpolicyagent/opa:0.44.0-rootless run --server

curl -X PUT --data-binary @opa/policy.rego  localhost:8181/v1/policies/authz/quarkus 
