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
                // Run in background on port 9673 and bind to all interfaces (0.0.0.0)
                bat 'nohup java -jar target/AdminApi-0.0.1-SNAPSHOT.jar --server.port=9673 --server.address=0.0.0.0 > app.log 2>&1 &'
            }
        }
    }
}
