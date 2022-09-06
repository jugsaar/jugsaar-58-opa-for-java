Spring Security Open Policy Agent Example
---

This example is a revised version of the example code from the [original blog post](https://sultanov.dev/blog/externalized-authorization-using-opa-and-spring-security/)
by sultanov.


Controller -> Facade -> Service -> Repository
Access Control in Facade Layer

docker run -p 8181:8181 openpolicyagent/opa:0.43.0-envoy-2 run --server


curl -X PUT --data-binary @opa/policy.rego  localhost:8181/v1/policies/authz

curl -X PUT -H "Content-Type: application/json" -d '{"version": 1}' localhost:8181/v1/data/revision
curl -X PUT -H "Content-Type: application/json" -d @opa/users.json localhost:8181/v1/data/users


