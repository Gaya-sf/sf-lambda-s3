#!/usr/bin/env groovy

node {
	def workspace = pwd()
	
	stage('checkout') {
        checkout scm
        sh "echo checkout"
    }

    docker.image('openjdk:17.0.1-slim').inside('-u root -e MAVEN_OPTS="-Duser.home=./sf-lambda-s3/"') {
        stage('check java') {
            sh "java -version"
        }

        stage('artifact clean and package') {
        
        	sh "chmod +x sf-lambda-s3/mvnw"
	        	sh "mvn_wraper_prefix=/sf-lambda-s3 ./sf-lambda-s3/mvnw -f sf-lambda-s3/pom.xml clean package -DskipTests"
        }

    }
   
}