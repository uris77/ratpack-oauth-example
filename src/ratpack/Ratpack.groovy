import com.uris.ratpack.examples.oauth.AuthPathAuthorizer
import org.pac4j.oauth.client.Google2Client
import org.pac4j.openid.client.GoogleOpenIdClient
import org.pac4j.openid.profile.google.GoogleOpenIdProfile
import ratpack.pac4j.Pac4jModule
import ratpack.pac4j.internal.Pac4jCallbackHandler
import ratpack.session.SessionModule
import ratpack.session.store.MapSessionsModule

import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.ratpack

ratpack {
    bindings {
        add new SessionModule()
        add new MapSessionsModule(10, 5)
        bind Pac4jCallbackHandler
        GoogleOpenIdClient openIdClient = new GoogleOpenIdClient()
        openIdClient.callbackUrl = "http://ratpack-oauth.stumblingoncode.com/pac4j-callback"
        Google2Client google2Client = new Google2Client()
        //google2Client.callbackUrl = "http://ratpack-oauth.stumblingoncode.com/pac4j-callback"
        //add new Pac4jModule(openIdClient, new AuthPathAuthorizer())
        add new Pac4jModule(google2Client, new AuthPathAuthorizer())
    }

    handlers {
        get {
          render groovyTemplate("index.html", title: "My Ratpack App")
        }

        get("welcome") { render "WELCOME" }

        prefix("admin") {
            get("secured"){
                def userProfile = request.maybeGet(Google2Client)
                println "userProfile: ${userProfile}"
                render groovyTemplate([userName: userProfile?.displayName], "secured.html")
            }
        }

        assets "public"
    }
}
