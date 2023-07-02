properties([pipelineTriggers([githubPush()])])

pipeline {
  agent any

  options {
    buildDiscarder(logRotator(numToKeepStr: '15'))
  }

  tools {
    jdk 'java 17'
    maven 'Default'
  }

  stages {
    stage('Check SCM Skip') {
      steps {
        scmSkip(deleteBuild:true, skipPattern:'.*\\[ci skip\\].*')
      }
    }

    stage('Build & Deploy') {
      steps {
         sh 'mvn clean deploy -P deployToMrIvanPlays'
      }
    }

    stage('Update javadocs') {
      when {
        branch 'dev/v2'
      }
      steps {
        sh '''
        cd api
        jd-maven treasury/api
        '''
      }
    }

    stage('Archive artifacts') {
      when {
        branch 'dev/v2'
      }
      steps {
        archiveArtifacts artifacts: 'platform/*/target/treasury-*-*-*.jar', excludes: 'platform/*/target/treasury-*-*-*-*-sources.jar', fingerprint: true
      }
    }
  }
}
