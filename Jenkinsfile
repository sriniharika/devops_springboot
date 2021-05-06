node{
	def mvnHome
	stage('Prepare'){
		git url:'https://github.com/sriniharika/devops_springboot.git',branch:'develop'
		mvnHome = tool 'mvn'
	}
	stage('Build'){
		bat "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
	}
	
	stage('Unit Test'){
		junit '**/target/surefire-reports/TEST-*.xml'
		archive 'target/*.jar'
	}
	stage('Integration Test'){
		bat "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean verify"
	}
	
	stage('Sonar'){
		bat "'${mvnHome}/bin/mvn' sonar:sonar"
	}
	
	stage('Deploy'){
		bat 'curl -u admin:admin -T target/**.war "http://localhost:7080/manager/text/deploy?path=/ibmdevops&update=true"'
	}
	
	stage('Smoke'){
		bat "curl --retry-delay 10 --retry 5 http://localhost:7080/ibmdevops/api/v1/products"
	}
}