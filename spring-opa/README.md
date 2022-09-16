Spring Boot Open Policy Agent Example
---

This Spring Boot example shows how to use Open Policy Agent to perform authorization for accessing API endpoints.

This example is a revised version of the example code from the [original blog post](https://sultanov.dev/blog/externalized-authorization-using-opa-and-spring-security/) by sultanov.

The the request processing happens along the following chain:
`Controller -> Facade -> Service -> Repository` with access control being applied at the `Facade Layer`.
The reason for applying the authorization at the facade layer is that requests can come in through various 
means like REST, GRPC, etc. and we want to ensure that the same authorization logic is always applied.

# Examples

## OPA server with 'pushed' policies and data

This example demonstrates the use of an OPA server instance that is provisioned by pushing policy rules and 
data to the server.

First we start the OPA server container:
```
docker run -p 8181:8181 --rm --name opa-server openpolicyagent/opa:0.44.0-rootless run --server
```

Then we initialize the OPA server with our custom policy and some data:
- `policy.rego` this file contains some access policies that we want to enforce
- `users.json` this file contains some user / subordinate mappings that we want to honor in the example 
```

curl -X PUT --data-binary @opa/policy.rego  localhost:8181/v1/policies/authz/spring
curl -X PUT -H "Content-Type: application/json" -d '{"version": 1}' localhost:8181/v1/data/revision
curl -X PUT -H "Content-Type: application/json" -d @opa/users.json localhost:8181/v1/data/users
```

## OPA server with 'pulled' policies and data

This example demonstrates how to periodically fetch policies from a policy server, by downloading a OPA policy bundle 
with data from the URL http://localhost:8182/service/v1/policies/current/bundle.tar.gz configured in `config.yaml`.

Note that this example requires the `spring-opa-policy-server` to be running.

First we start the OPA server container:
```
docker run --net=host --rm -v $PWD/opa/config.yaml:/config.yaml openpolicyagent/opa:0.44.0-rootless run --server -c /config.yaml
```