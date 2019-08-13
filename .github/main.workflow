workflow "Sleep" {
  on = "push"
  resolves = "Sleep5"
}

action "Sleep" {
  uses = "maddox/actions/sleep@master"
  args = "15"
}

action "Sleep2" {
  needs = ["Sleep"]
  uses = "maddox/actions/sleep@master"
  args = "15"
}

action "Sleep3" {
  needs = ["Sleep"]
  uses = "maddox/actions/sleep@master"
  args = "15"
}

action "Sleep4" {
  needs = ["Sleep3", "Sleep2"]
  uses = "maddox/actions/sleep@master"
  args = "15"
}

action "Sleep5" {
  needs = ["Sleep4"]
  uses = "maddox/actions/sleep@master"
  args = "15"
}
