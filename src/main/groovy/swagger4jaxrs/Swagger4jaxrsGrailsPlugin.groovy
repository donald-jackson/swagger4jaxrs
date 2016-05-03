import grails.core.GrailsApplication
import grails.plugins.Plugin
import grails.util.Environment

import io.swagger.jaxrs.config.BeanConfig

class Swagger4jaxrsGrailsPlugin extends Plugin {
    def version = "3.0.1"
    def grailsVersion = "3.0 > *"
    def pluginExcludes = [
            "web-app/WEB-INF/**"
    ]
    def title = "Swagger for JAX-RS Plugin"
    def description = 'Adds Swagger support to document REST APIs of projects that use the JAX-RS (JSR 311) plugin'

    def documentation = "https://github.com/nerdErg/swagger4jaxrs"
    def license = "APACHE"
    def organization = [ name: "nerdErg Pty. Ltd.", url: "http://www.nerderg.com/" ]
    def developers = [
            [ name: "Angel Ruiz", email: "aruizca@gmail.com" ],
            [ name: "Aaron Brown", email: "brown.aaron.lloyd@gmail.com"],
    ]
    def issueManagement = [ system: "GitHub", url: "https://github.com/nerdErg/swagger4jaxrs/issues" ]
    def scm = [ url: "https://github.com/nerdErg/swagger4jaxrs" ]

    def loadAfter = [
            "jaxrs-core",
            "jaxrs-jersey1"
    ]

    Closure doWithSpring() {{ ->
        println "Configuring Swagger \n"
        mergeConfig(grailsApplication)

        ConfigObject local = grailsApplication.config.'swagger4jaxrs'

        validateLocalConfig(local)

        swaggerConfig(BeanConfig) { bean ->
            bean.autowire = true
            resourcePackage = local.resourcePackage
            basePath = local.basePath
            version = local.version ?: '1'
            title = local.title ?: 'Unspecified'
            description = local.description ?: ''
            contact = local.contact ?: ''
            license = local.license ?: ''
            licenseUrl = local.licenseUrl ?: ''
            scan = local.scan ?: true
        }
    }}

    def onConfigChange(event) {
        mergeConfig(grailsApplication)

        ConfigObject local = grailsApplication.config.'swagger4jaxrs'

        validateLocalConfig(local)

        event.ctx.getBean('swaggerConfig').with {
            resourcePackage = local.resourcePackage
            basePath = local.basePath
            version = local.version ?: "1"
            title = local.title ?: 'Unspecified'
            description = local.description ?: ""
            contact = local.contact ?: ""
            license = local.license ?: ""
            licenseUrl = local.licenseUrl ?: ""
            scan = local.scan ?: true
        }
    }

    /**
     * Merges plugin config with host app config, but allowing customization
     * @param app
     */
    private void mergeConfig(GrailsApplication app) {
//        ConfigObject currentConfig = app.config.org.grails.jaxrs
//        ConfigSlurper slurper = new ConfigSlurper(Environment.current.name)
//        ConfigObject secondaryConfig = slurper.parse(app.classLoader.loadClass("SwaggerConfig"))
//
//        ConfigObject config = new ConfigObject()
//        config.putAll(secondaryConfig.org.grails.jaxrs.merge(currentConfig))
//
//        app.config.org.grails.jaxrs = config
    }

    private void validateLocalConfig(ConfigObject local) {
        if (local.isEmpty()) {
            throw new IllegalStateException("The swagger4jaxrs config is missing.")
        }

        if (local.resourcePackage.isEmpty()) {
            throw new IllegalStateException("The swagger4jaxrs config requires a resourcePackage path.")
        }
    }
}
