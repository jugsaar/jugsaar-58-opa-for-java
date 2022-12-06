package authz.spring

import future.keywords.in

policy_version := 2

default access = {
    "allowed": false,
    "msg": "insufficient access",
}

# HR staff can read salary information
access = result {
  # WHEN
  input.action == "read"
  input.resource.type == "salary"
  # "ROLE_HR" in input.subject.roles
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
  is_owner_access

  # THEN
  result = allow("owner-or-subordinate:access")
}

####

allow(hint) = result {
    result = {
        "allowed": true,
        "hint": hint,
        "policy_version": policy_version,
        "data_version": data.revision.version,
    }
}

# helper to check if current user is hr staff
is_hr_staff {
    "ROLE_HR" in input.subject.roles
}

is_owner_access {
    input.resource.owner == input.subject.name
}

is_subordinate_access {
    input.resource.owner != input.subject.name
    input.resource.owner in users_access[input.subject.name]
}

is_owner_or_subordinate_access {
    input.resource.owner in users_access[input.subject.name]
}


# helper to resolve user graph
users_graph[data.users[username].name] = edges {
  edges := data.users[username].subordinates
}

# Check what a user can access
users_access[username] = access {
  # is user contained in user set?
  users_graph[username]
  # compute set of reachable nodes
  access := graph.reachable(users_graph, {username})
}
