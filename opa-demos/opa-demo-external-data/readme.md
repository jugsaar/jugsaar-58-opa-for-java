Open Policy Agent Demo
---

# Start OPA Server
docker run -p 8181:8181 --rm --name opa-server -d openpolicyagent/opa:0.50.0 run --server --set "decision_logs.console=true"

# Populate Policies
curl -X PUT --data-binary @policy.rego  localhost:8181/v1/policies/authz

# Populate Data
curl -X PUT -H "Content-Type: application/json" -d @data_user_roles.json localhost:8181/v1/data/user_roles
curl -X PUT -H "Content-Type: application/json" -d @data_app_role_perms.json localhost:8181/v1/data/app_role_perms
curl -X PUT -H "Content-Type: application/json" -d @data_user_groups.json localhost:8181/v1/data/user_groups

# Wich roles has alice in "hr" app?
curl -X POST -v -H "Content-Type: application/json" \
  -d '{"input":{"user":"alice", "app":"hr"}}' \
  http://localhost:8181/v1/data/authz/whatroles

# Which permissions has alice in "hr" app?
curl -X POST -v -H "Content-Type: application/json" \
  -d '{"input":{"user":"alice", "app":"hr"}}' \
  http://localhost:8181/v1/data/authz/whatperms

# Can alice "view:salary" in "hr" app?
curl -X POST -v -H "Content-Type: application/json" \
  -d '{"input":{"user":"alice", "app":"hr", "operation":"view:salary"}}' \
  http://localhost:8181/v1/data/authz/allow

# Can alice "update:salary" in "hr" app?
curl -X POST -v -H "Content-Type: application/json" \
  -d '{"input":{"user":"alice", "app":"hr", "operation":"update:salary"}}' \
  http://localhost:8181/v1/data/authz/allow


# Which groups is alice in?
curl -X POST -v -H "Content-Type: application/json" \
  -d '{"input":{"user":"alice"}}' \
  http://localhost:8181/v1/data/authz/mygroups

# Which groups is bob in?
curl -X POST -v -H "Content-Type: application/json" \
  -d '{"input":{"user":"bob"}}' \
  http://localhost:8181/v1/data/authz/mygroups


# What roles does the unknown user "tom" have in app "app1"? 
curl -X POST -v -H "Content-Type: application/json" \
  -d '{"input":{"user":"tom", "app":"app1"}}' \
  http://localhost:8181/v1/data/authz/whatroles

# Add new user tom

curl -v -X PATCH -H "Content-Type: application/json-patch+json" \
  -d @patch-addNewUser.json \
  http://localhost:8181/v1/data/roles

# What roles does "tom" have in app "app1"?
curl -X POST -v -H "Content-Type: application/json" \
  -d '{"input":{"user":"tom2", "app":"app1"}}' \
  http://localhost:8181/v1/data/authz/whatroles
