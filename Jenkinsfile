pipeline {
  agent any
  stages {
    stage('Build & Deploy') {
      steps {
         sh 'mvn clean deploy -P deployToMrIvanPlays'
      }
    }

    stage('Update javadocs (1.x.y)') {
      when {
        branch 'master'
      }
      steps {
        sh 'cd api'
        sh 'jd-maven treasury/api'
        sh 'cd -'
        sh 'cd platform/bukkit/api'
        sh 'jd-maven treasury/api-bukkit'
      }
    }

    stage('Update javadocs (2.0.0)') {
      when {
        branch 'dev/2.0.0'
      }
      steps {
        sh 'cd api'
        sh 'jd-maven treasury/api-v2'
      }
    }

    stage('Archive artifacts (1.x.y)') {
      when {
        branch 'master'
      }
      steps {
        archiveArtifacts artifacts: 'platform/*/plugin/target/treasury-*-*-*-*.jar, platform/*/target/treasury-*-*-*-*.jar', excludes: 'platform/*/plugin/target/treasury-*-*-*-*-sources.jar, platform/*/target/treasury-*-*-*-*-sources.jar'
      }
    }

    stage('Archive artifacts (2.0.0)') {
      when {
        branch 'dev/2.0.0'
      }
      steps {
        archiveArtifacts artifacts: 'platform/*/target/treasury-*-*-*.jar', excludes: 'platform/*/target/treasury-*-*-*-*-sources.jar'
      }
    }
  }
}
