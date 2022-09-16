package authz.quarkus

default allow = false

allow {
    input.action == "greet"
    input.subject.name == input.resource
}

allow {
    input.action == "greet"
    input.subject.roles[_] == "admin"
}
