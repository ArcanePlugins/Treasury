properties([pipelineTriggers([githubPush()])])

pipeline {
  agent any

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
         sh '''
         export JAVA_HOME=/usr/lib/jvm/java17-openjdk-amd64
         export PATH=$JAVA_HOME/bin:$PATH
         mvn clean deploy -P deployToMrIvanPlays
         '''
      }
    }

    stage('Update javadocs (1.x.y)') {
      when {
        branch 'master'
      }
      steps {
        sh '''
        cd api
        jd-maven treasury/api
        cd -
        cd platform/bukkit/api
        jd-maven treasury/api-bukkit
        '''
      }
    }

    stage('Update javadocs (2.0.0)') {
      when {
        branch 'dev/2.0.0'
      }
      steps {
        sh '''
        cd api
        jd-maven treasury/api-v2
        '''
      }
    }

    stage('Archive artifacts (1.x.y)') {
      when {
        branch 'master'
      }
      steps {
        archiveArtifacts artifacts: 'platform/*/plugin/target/treasury-*-*-*-*.jar, platform/*/target/treasury-*-*-*-*.jar', excludes: 'platform/*/plugin/target/treasury-*-*-*-*-sources.jar, platform/*/target/treasury-*-*-*-*-sources.jar', fingerprint: true
      }
    }

    stage('Archive artifacts (2.0.0)') {
      when {
        branch 'dev/2.0.0'
      }
      steps {
        archiveArtifacts artifacts: 'platform/*/target/treasury-*-*-*.jar', excludes: 'platform/*/target/treasury-*-*-*-*-sources.jar', fingerprint: true
      }
    }
  }
}
