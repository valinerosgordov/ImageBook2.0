package ru.imagebook.server.auth

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import ru.imagebook.server.service.auth.AuthConfig
import ru.imagebook.server.service2.security.UserDetailsImpl
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author minogin@gmail.com
 */

const val API_VERSION = "1.0"

const val SSO_TOKEN_ATTR = "SSO_TOKEN"

@Component
class LoginSuccessHandler(val authConfig: AuthConfig) : SimpleUrlAuthenticationSuccessHandler() {
    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        /*try {
            val details = SecurityContextHolder.getContext().authentication.principal as UserDetailsImpl
            val account = details.account
            val user = account.user
            val sharedUser = SharedUser(user.id, account.isActive, user.userName)

            val token = RestTemplate().postForObject<String>("${authConfig.authServerUrl}/api/$API_VERSION/startSession", sharedUser)
            request.session.setAttribute(SSO_TOKEN_ATTR, token)

            println("########### RECEIVED SSO TOKEN: " + token)
        } catch (e: Exception) {
            e.printStackTrace()
        }*/

        super.onAuthenticationSuccess(request, response, authentication)
    }
}
