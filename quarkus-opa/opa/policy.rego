package authz.quarkus

default allow = false

allow {
    input.action = "greet"
    input.subject.name = input.resource
}

allow {
    input.action = "greet"
    role = input.subject.roles[_]
    role = "admin"
}