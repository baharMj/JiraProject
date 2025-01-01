import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.security.roles.ProjectRoleManager
import com.atlassian.jira.project.Project
import com.atlassian.jira.security.roles.ProjectRoleActors

// Ensure that categoryWithoutSuperior gets the correct value
def categoryWithoutSuperior = issue.get("customfield_11204")?.first()?.id
def reporter = issue.get("reporter") as ApplicationUser

// Get the group names for the reporter
def groups = reporter ? ComponentAccessor.groupManager.getGroupNamesForUser(reporter) : []

// Initialize reporterGroup
def reporterGroup = null

// Determine the reporter's group
if (groups) {
    reporterGroup = groups.find { it?.contains("-dep") } ?: groups.find { it?.contains("-branch") }
}

// Get role manager and project
def roleManager = ComponentAccessor.getComponent(ProjectRoleManager)
def project = ComponentAccessor.projectManager.getProjectObjByKey("CASE")

def isSuperior = roleManager.isUserInProjectRole(reporter, roleManager.getProjectRole("Superior"), project)
def isManager = roleManager.isUserInProjectRole(reporter, roleManager.getProjectRole("Manager"), project)
def isDeputy = roleManager.isUserInProjectRole(reporter, roleManager.getProjectRole("Deputy"), project)

// Check conditions and return or find superior
if (isSuperior || isManager || isDeputy || categoryWithoutSuperior in [1388, 1394, 1391]) {
    return null
} else {
    def superiorMembers = roleManager.getProjectRoleActors(roleManager.getProjectRole("Superior"), project).users
    def superior = superiorMembers.find {
        def userGroups = ComponentAccessor.groupManager.getGroupNamesForUser(it)
        userGroups.any { group -> group != null && reporterGroup != null && group.contains(reporterGroup) }
    }
}
