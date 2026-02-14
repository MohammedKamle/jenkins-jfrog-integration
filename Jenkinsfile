pipeline {
    agent any

    tools {
        jfrog 'jfrog-cli'
        maven 'Maven-3'
    }

    environment {
        BUILD_NAME = "${JOB_NAME}"
        BUILD_NUMBER = "${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verify JFrog Connection') {
            steps {
                echo '--- Verifying JFrog CLI configuration ---'
                jf 'c show'
                jf 'rt ping'
            }
        }

        stage('Configure Maven Repos') {
            steps {
                echo '--- Configuring Maven resolution and deployment repositories ---'
                jf 'mvnc --repo-resolve-releases demo-libs-release --repo-resolve-snapshots demo-libs-snapshot --repo-deploy-releases demo-libs-release-local --repo-deploy-snapshots demo-libs-snapshot-local'
            }
        }

        stage('Frogbot Scan') {
            steps {
                echo '--- Running Frogbot Security Scan ---'
                withCredentials([
                    string(credentialsId: 'jfrog-url', variable: 'JF_URL'),
                    string(credentialsId: 'jfrog-access-token', variable: 'JF_ACCESS_TOKEN'),
                    string(credentialsId: 'github-token', variable: 'JF_GIT_TOKEN')
                ]) {
                    withEnv([
                        "JF_GIT_PROVIDER=github",
                        "JF_GIT_OWNER=MohammedKamle",
                        "JF_GIT_REPO=jenkins-jfrog-integration"
                    ]) {
                        sh '''
                            curl -fLg "https://releases.jfrog.io/artifactory/frogbot/v2/[RELEASE]/getFrogbot.sh" | sh
                            ./frogbot scan-repository
                        '''
                    }
                }
            }
        }

        stage('Build & Deploy') {
            steps {
                echo '--- Building project and deploying artifacts to JFrog ---'
                jf 'mvn clean install -f pom.xml --build-name=${BUILD_NAME} --build-number=${BUILD_NUMBER}'
            }
        }

        stage('Publish Build Info') {
            steps {
                echo '--- Publishing build info to JFrog Artifactory ---'
                jf 'rt bp ${BUILD_NAME} ${BUILD_NUMBER}'
            }
        }

        stage('Xray Build Scan') {
            steps {
                echo '--- Scanning build with JFrog Xray ---'
                jf 'bs ${BUILD_NAME} ${BUILD_NUMBER}'
            }
        }
    }

    post {
        success {
            echo "Build ${BUILD_NAME}#${BUILD_NUMBER} completed successfully and published to JFrog!"
        }
        failure {
            echo "Build ${BUILD_NAME}#${BUILD_NUMBER} failed."
        }
    }
}
