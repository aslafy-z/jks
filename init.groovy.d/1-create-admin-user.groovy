// Thanks https://gist.github.com/hayderimran7/50cb1244cc1e856873a4
import jenkins.model.*
import hudson.security.*

println "--> creating admin user"

def instance = Jenkins.getInstance()

//def adminUserName = System.getenv("ADMIN_USERNAME")
def adminPassword = System.getenv("ADMIN_PASSWORD")

//assert adminUserName != null : "No ADMIN_USERNAME env var provided, but required"
assert adminPassword != null : "No ADMIN_PASSWORD env var provided, but required"

def hudsonRealm = new HudsonPrivateSecurityRealm(false)
// FIXME : just during debugging/initial dev, remove the password part of that log
//println "Creating the '$adminUserName' admin user with provided password (using env var 'ADMIN_PASSWORD')"

if(adminPassword.equals(System.getenv("DEFAULT_ADMIN_PASSWORD"))) {
  println("WARNING: You didn't change the default image password, there may be a security risk")
  println("WARNING: Pass the value using 'docker run -e ADMIN_PASSWORD=theOneYouWant ...'")
}

hudsonRealm.createAccount("admin", adminPassword)
instance.setSecurityRealm(hudsonRealm)

def strategy = new GlobalMatrixAuthorizationStrategy()
strategy.add(Jenkins.ADMINISTER, 'authenticated')
//strategy.add(Jenkins.READ, "anonymous")
instance.setAuthorizationStrategy(strategy)

instance.save()