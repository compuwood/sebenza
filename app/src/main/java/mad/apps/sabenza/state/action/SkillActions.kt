package mad.apps.sabenza.state.action

import mad.apps.sabenza.data.model.employee.EmployeeSkill
import mad.apps.sabenza.data.rpc.calls.employee.Skill
import mad.apps.sabenza.framework.redux.Action

class NewSkillsAvailableAction(val skills : List<Skill>) : Action<List<Skill>>("skills_updated", skills)

class EmployeeSkillsUpdatedAction(val skills : List<EmployeeSkill>) : Action<List<EmployeeSkill>>("employee_skills_updated", skills)

class RefreshSkillsAction : Action<Any>("refresh_skills", Any());

class LinkSkillToEmployeeAction(val skill : Skill, val yearsExperience : Int) : Action<Pair<Skill, Int>>("link_skill_to_employee", Pair(skill, yearsExperience))

class LinkSkillsToEmployeeAction(val skills : List<Pair<Skill, Int>>) : Action<List<Pair<Skill, Int>>>("link_skills_to_employee", skills)

class UnlinkSkillToEmployeeAction(val skill: Skill) : Action<Skill>("unlink_skill", skill)

class RefreshEmployeeSkillAction : Action<Any>("refresh_employee_skills", Any())