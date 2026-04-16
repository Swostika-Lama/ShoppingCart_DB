pipeline {
    agent any
    tools {
        maven 'MAVEN_HOME'
    }

    environment {
        JAVA_HOME = tool 'JDK21'
        PATH = "${env.JAVA_HOME}/bin:/opt/homebrew/bin:/usr/local/bin:${env.PATH}"

        SONARQUBE_SERVER = 'SonarQubeServer'
        SONAR_TOKEN = credentials('sonar-token-id')

        DOCKERHUB_CREDENTIALS_ID = 'docker-jenkins'
        DOCKERHUB_REPO = 'swostikalama/shoppingcartgui'
        DOCKER_IMAGE_TAG = 'latest'
    }

    stages {

        stage('Check Docker') {
            steps {
                sh 'docker --version'
            }
        }

        stage('Checkout') {
            steps {
                git branch: 'main',
                url: 'git@github.com:Swostika-Lama/ShoppingCart_DB.git'
            }
        }

        stage('Build Job:') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Report') {
            steps {
                sh 'mvn jacoco:report'
            }
        }

        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Publish Coverage Report') {
            steps {
                jacoco()
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    sh 'mvn clean verify sonar:sonar -Dsonar.login=$SONAR_TOKEN'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh """
                docker build -t ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG} .
                """
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: "${DOCKERHUB_CREDENTIALS_ID}",
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    sh '''
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}
                    '''
                }
            }
        }
    }
}