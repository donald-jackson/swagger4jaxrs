package swagger4jaxrs

import grails.plugins.Plugin
import groovy.util.logging.Log4j
import io.swagger.jaxrs.config.BeanConfig
import org.grails.plugins.jaxrs.core.ScanningResourceRegistrar

@Log4j
class Swagger4jaxrsGrailsPlugin extends Plugin {
    /**
     * Grails version the plugin is intended for.
     */
    def grailsVersion = "3.0 > *"

    /**
     * Plugin description.
     */
    def description = 'Adds Swagger support to document REST APIs of projects that use the JAX-RS (JSR 311) plugin'

    /**
     * Documentation URL.
     */
    def documentation = "https://github.com/nerdErg/swagger4jaxrs"

    /**
     * License.
     */
    def license = "APACHE"

    /**
     * Organizations.
     */
    def organization = [name: "nerdErg Pty. Ltd.", url: "http://www.nerderg.com/"]

    /**
     * Developers.
     */
    def developers = [
        [name: "Angel Ruiz", email: "aruizca@gmail.com"],
        [name: "Aaron Brown", email: "brown.aaron.lloyd@gmail.com"],
    ]

    /**
     * Issues URL.
     */
    def issueManagement = [system: "GitHub", url: "https://github.com/nerdErg/swagger4jaxrs/issues"]

    /**
     * SCM URL.
     */
    def scm = [url: "https://github.com/nerdErg/swagger4jaxrs"]

    /**
     * Loading order.
     */
    def loadAfter = [
        "jaxrs-core",
    ]

    /**
     * Configure plugin beans.
     *
     * @return
     */
    Closure doWithSpring() {
        { ->
            log.info "Configuring Swagger..."

            Map config = getSwaggerConfiguration()
            swaggerConfig(BeanConfig) { bean ->
                bean.autowire = true
                resourcePackage = config.resourcePackage
                basePath = config.basePath
                version = config.version ?: '1'
                title = config.title ?: 'Unspecified'
                description = config.description ?: ''
                contact = config.contact ?: ''
                license = config.license ?: ''
                licenseUrl = config.licenseUrl ?: ''
                scan = config.scan ?: true
            }

            swaggerResourceRegistrar(ScanningResourceRegistrar, 'io.swagger.jaxrs.listing')
        }
    }

    @Override
    void onConfigChange(Map<String, Object> event) {
        Map config = getSwaggerConfiguration()

        event.ctx.getBean('swaggerConfig').with {
            resourcePackage = config.resourcePackage
            basePath = config.basePath
            version = config.version ?: "1"
            title = config.title ?: 'Unspecified'
            description = config.description ?: ""
            contact = config.contact ?: ""
            license = config.license ?: ""
            licenseUrl = config.licenseUrl ?: ""
            scan = config.scan ?: true
        }
    }

    /**
     * Retrieves, validates, and returns the swagger configuration.
     *
     * @return Validated swagger configuration.
     */
    Map getSwaggerConfiguration() {
        Map config = getGrailsApplication().config.'swagger4jaxrs' as Map

        if (config.isEmpty()) {
            throw new IllegalStateException("The swagger4jaxrs config is missing.")
        }

        if (config.resourcePackage.isEmpty()) {
            throw new IllegalStateException("The swagger4jaxrs config requires a resourcePackage path.")
        }

        return config
    }
}
