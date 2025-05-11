pipeline {
    agent any
    tools {
        maven 'MVN'
        jdk 'JDK'
    }
    environment { 
        GIT_URL = 'https://github.com/Samir-Kesare/addressbook.git'
    }
    parameters {
        choice(name: 'BUILD_TYPE', choices: ['Development', 'Staging', 'Production'], description: 'Select the build environment')
    }
    options {
        timeout(time: 10, unit: 'MINUTES') 
    }
    stages {
        stage('Code Checkout') {
            steps {
                git url: "${env.GIT_URL}"
            }
        }
        stage('Parallel Stage') {
            parallel {
                stage('Compile') {
                    steps {
                        sh 'mvn compile'  
                    }
                }
                stage('Code Review') {
                    steps {
                        sh "mvn findbugs:findbugs"
                    }
                }
                stage('Unit Test') {
                    steps {
                        sh "mvn test"
                    }
                }
            }
        }
        stage('Approval') {
            steps {
                input message: 'Proceed to Build and Package?'
            }
        }
        stage('Package') {
            steps {
                echo "Building for environment: ${params.BUILD_TYPE}"
                sh 'mvn package'
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
        always {
            echo 'Pipeline finished. Cleaning up...'
            cleanWs()
        }
    }
}
