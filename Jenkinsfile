pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Deploy') {
            steps {
                bat '''
                    @echo off
                    if exist app.log del app.log
                    start /B java -jar target/AdminApi-0.0.1-SNAPSHOT.jar --server.port=9673 --server.address=0.0.0.0 > app.log 2>&1
                    timeout /t 5
                '''
            }
        }
    }
}
