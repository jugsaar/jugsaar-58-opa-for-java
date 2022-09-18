package authz

import data.user_roles
import data.app_role_perms
import data.user_groups

default allow = false

allow {
        user = input.user
        app = input.app
        
        roles = user_roles[user][app]
        role = roles[_]
        perm = app_role_perms[app][role]

        perm[_] == input.operation
}

whatcan[perm] {
        user = input.user
        app = input.app
        
        roles = user_roles[user][app]
        role = roles[_]
        perm = app_role_perms[app][role]
}

whatroles[role] {
        user = input.user
        app = input.app
        roles = user_roles[user][app]
        role = roles
}

whatperms[perm] {
        user = input.user
        app = input.app
        roles = user_roles[user][app]
        role = roles[_]
        perm = app_role_perms[app][role]
}

mygroups[group] {
    user = input.user
    group = user_groups[user]
}
