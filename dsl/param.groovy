def environments = ['Development', 'Staging', 'Production']

def jobTemplateWithEnv = { jobName, repoUrl, branch, buildCommand, testCommand, deployCommand, notificationEmail, env ->

    job(jobName) {
        description("Job for ${jobName} - Environment: ${env}")

        parameters {
            choiceParam('ENVIRONMENT', environments, 'Select the environment')
            stringParam('BRANCH_NAME', branch, 'Branch to build')
        }

        steps {
            shell("echo 'Environment: \$ENVIRONMENT'")
            shell(buildCommand)
            shell(testCommand)
            
            if (deployCommand) {
                shell(deployCommand)
            }
        }

        publishers {
            mailer(notificationEmail, false, false)
        }
    }
}

     
