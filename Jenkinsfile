node{
	def mvnHome
	stage('Prepare'){
		git url:'git@github.com:sriniharika/devops_springboot.git',branch:'develop'
		mvnHome = tool 'mvn'
	}
	stage('Build'){
		sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
	}
	
	stage('Unit Test'){
		junit '**/target/surefire-reports/TEST-*.xml'
		archive 'target/*.jar'
	}
	stage('Integration Test'){
		sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean verify"
	}
	
	stage('Sonar'){
		sh "'${mvnHome}/bin/mvn' sonar:sonar"
	}
	
	stage('Deploy'){
		sh 'curl -u admin:admin -T target/**.war "http://localhost:7080/manager/text/deploy?path=/ibmdevops&update=true"'
	}
	
	stage('Smoke'){
		sh "curl --retry-delay 10 --retry 5 http://localhost:7080/ibmdevops/api/v1/products"
	}
}