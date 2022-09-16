package authz.spring

default access = {
    "allowed": false,
    "msg": "insufficient access",
}

# HR staff can read salary information
access = result {
  # WHEN
  input.action == "read"
  input.resource.type == "salary"
  # input.subject.roles[_] == "ROLE_HR"
  is_hr_staff

  # THEN
  result = allow("hr:access")
}

# An employee can read his own salary
access = result {
  # WHEN
  input.action == "read"
  input.resource.type == "salary"
    #input.resource.owner == input.subject.name
  is_owner_access

  # THEN
  result = allow("owner:access")
}


# An employee can read saraly info of subordinates
access = result {
  # WHEN
  input.action == "read"
  input.resource.type == "salary"
  # input.resource.owner == users_access[input.subject.name][_]
  is_subordinate_access

  # THEN
  result = allow("subordinate:access")
}

# An employee can access his own documents
access = result {
  # WHEN
  input.action == "read"
  input.resource.type == "document"
  #input.resource.owner == input.subject.name
  # is_owner_access
  is_owner_or_subordinate_access

  # THEN
  result = allow("owner-or-subordinate:access")
}

####

allow(hint) = result {
    result = {
        "allowed": true,
        "hint": hint,
    }
}

# helper to check if current user is hr staff
is_hr_staff {
    input.subject.roles[_] == "ROLE_HR"
}

is_owner_access {
    input.resource.owner == input.subject.name
}

is_subordinate_access {
    input.resource.owner != input.subject.name
    input.resource.owner == users_access[input.subject.name][_]
}

is_owner_or_subordinate_access {
    input.resource.owner == users_access[input.subject.name][_]
}


# helper to resolve user graph
users_graph[data.authz.users[username].name] = edges {
  edges := data.authz.users[username].subordinates
}

# Check what a user can access
users_access[username] = access {
  # is user contained in user set?
  users_graph[username]
  # compute set of reachable nodes
  access := graph.reachable(users_graph, {username})
}
