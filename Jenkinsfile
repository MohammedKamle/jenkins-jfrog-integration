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
