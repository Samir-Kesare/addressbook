job('example-job') {
    description("Auto-generated job for selected environment and branch")

    parameters {
        choiceParam('ENVIRONMENT', ['Development', 'Staging', 'Production'], 'Select environment')
        stringParam('BRANCH_NAME', 'master', 'Branch to build')
        stringParam('REPO_URL', 'https://github.com/Samir-Kesare/addressbook.git', 'Git repository URL')
        stringParam('BUILD_COMMAND', './gradlew build', 'Build command')
        stringParam('TEST_COMMAND', './gradlew test', 'Test command')
        stringParam('DEPLOY_COMMAND', '', 'Deployment command (optional)')
        stringParam('CREDENTIALS_ID', '', 'Jenkins credentials ID (optional)')
        stringParam('EMAIL', '', 'Notification email (optional)')
    }

    scm {
        git {
            remote {
                url('$REPO_URL')
                if ('$CREDENTIALS_ID') {
                    credentials('$CREDENTIALS_ID')
                }
            }
            branch('$BRANCH_NAME')
        }
    }

    steps {
        shell("echo 'Running in \$ENVIRONMENT environment'")
        shell('$BUILD_COMMAND')
        shell('$TEST_COMMAND')
        shell('if [ ! -z "$DEPLOY_COMMAND" ]; then $DEPLOY_COMMAND; fi')
    }

    publishers {
        mailer('$EMAIL', false, false)
    }
}
