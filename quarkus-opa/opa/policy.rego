package authz.quarkus

import future.keywords.in

default allow := false

# All users can greet themselves
allow {
    input.action == "greet"
    input.subject.name == input.resource
}

# Admin users can greet other users
allow {
    input.action == "greet"
    "admin" in input.subject.roles
}
