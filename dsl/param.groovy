def env = ENVIRONMENT
def jobName = JOB_NAME
def repoUrl = REPO_URL
def branch = BRANCH_NAME
def buildCmd = BUILD_COMMAND
def testCmd = TEST_COMMAND
def deployCmd = DEPLOY_COMMAND
def creds = CREDENTIALS_ID
def email = EMAIL

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
