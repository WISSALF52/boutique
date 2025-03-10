pipeline {
    agent any  // Spécifie un agent générique

    stages {
        stage('Checkout') {
            steps {
                // Récupère le code depuis GitHub
                git 'https://github.com/WISSALF52/boutique.git'
            }
        }

        stage('Compile') {
            steps {
                // Compile le projet avec Maven
                script {
                    bat 'mvn clean compile'
                }
            }
        }

      
        stage('Test') {
            steps {
                // Lance les tests unitaires avec Maven
                script {
                    bat 'mvn test'
                }
            }
        }

         stage('SonarQube Analysis') {
           
            steps {
                script {
                    withSonarQubeEnv('sonarqube') {
                        bat "\"C:\\Program Files\\apache-maven-3.9.9\\bin\\mvn\" clean verify sonar:sonar -Dsonar.projectKey=wissal3 -Dsonar.projectName=wissal3 "
                    }
                }
            }
        }
        stage('Package') {
            steps {
                // Crée le fichier .jar avec Maven
                script {
                    bat 'mvn package'
                }
            }
        }

        stage('Deploy') {
            steps {
                // Déploie l'application (exemple de message ici, à personnaliser selon ton processus de déploiement)
                echo 'Déploiement de l\'application'
            }
        }
    }

    post {
        success {
            echo 'Construction et déploiement réussis !'
        }
        failure {
            echo 'Échec de la pipeline !'
        }
    }
}
