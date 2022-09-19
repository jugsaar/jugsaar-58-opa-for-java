Simple Example for OPA Policies
---

The example can be executed via the [Open Policy Agent playground](https://play.openpolicyagent.org).

# Policy

The rego policy contains the logic to determine if a user has the required permissions to access a resource or to perform 
an operation. 

This example policy centers around the `allow` rule. If all conditions within the `allow` rule evaluate to `true` then access is granted, otherwise denied.

Initial rego policy with hardcoded name check:
```rego
package app.demo

default allow := false

allow {
	input.method = "GET"
	input.path = "/admin"
	input.subject.name = "admin"
}
```

Revised rego policy with role check
```rego
package app.demo

import future.keywords.in

default allow := false

allow {
	input.method = "GET"
	input.path = "/admin"
	"admin" in input.subject.roles 
}
```

Revised rego policy with externalized role check
```rego
package app.demo

import future.keywords.in

default allow := false

allow {
	input.method = "GET"
	input.path = "/admin"
	is_admin
}

# externalized condition to allow reuse
is_admin {
  "admin" in input.subject.roles 
}
```

# Some example queries

We can query the policy above with the following example `input` documents.

Can user `tom` access admin area?
 > Select `allow` block and click evaluate.
```json
{
  "method": "GET",
  "path": "/admin",
  "subject": {
    "name": "tom"
  }
}
```

Can user `admin` access admin area?
> Select `allow` block and click evaluate.
```json
{
  "method": "GET",
  "path": "/admin",
  "subject": {
    "name": "admin"
  }
}
```

Can admin user `bob` access admin area?
> Select `allow` block and click evaluate.
```json
{
  "method": "GET",
  "path": "/admin",
  "subject": {
    "name": "bob",
    "roles": ["user","admin"]
  }
}
```
