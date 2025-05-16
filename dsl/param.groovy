def env = binding.variables.get('ENVIRONMENT')
def jobName = binding.variables.get('JOB_NAME')
def repoUrl = binding.variables.get('REPO_URL')
def branch = binding.variables.get('BRANCH_NAME')
def buildCmd = binding.variables.get('BUILD_COMMAND')
def testCmd = binding.variables.get('TEST_COMMAND')
def deployCmd = binding.variables.get('DEPLOY_COMMAND')
def creds = binding.variables.get('CREDENTIALS_ID')
def email = binding.variables.get('EMAIL')

job(jobName) {
    description("Auto-generated job for ${jobName} in ${env} environment")

    parameters {
        choiceParam('ENVIRONMENT', ['Development', 'Staging', 'Production'], 'Select environment')
        stringParam('BRANCH_NAME', branch, 'Branch to build')
    }

    scm {
        git {
            remote {
                url(repoUrl)
                if (creds) {
                    credentials(creds)
                }
            }
            branch(branch)
        }
    }

    steps {
        shell("echo 'Running in \$ENVIRONMENT environment'")
        shell(buildCmd)
        shell(testCmd)
        if (deployCmd?.trim()) {
            shell(deployCmd)
        }
    }

    publishers {
        if (email?.trim()) {
            mailer(email, false, false)
        }
    }
}
