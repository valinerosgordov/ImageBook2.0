package ru.imagebook.server.exchange

import org.springframework.http.HttpHeaders
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

const val BEARER_METHOD = "Bearer"
const val TOKEN_PARAM = "token"

const val EXCHANGE_API_USER = "EXCHANGE_API_USER"

@Component
class ExchangeAuthenticationFilter(
        val exchangeConfig: ExchangeConfig
) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val token = request.getHeader(HttpHeaders.AUTHORIZATION)?.removePrefix(BEARER_METHOD)?.trim()
                ?: request.getParameter(TOKEN_PARAM)
        if (token != null) {
            if (token == exchangeConfig.token) {
                SecurityContextHolder.getContext().authentication = PreAuthenticatedAuthenticationToken(
                        EXCHANGE_API_USER,
                        "",
                        mutableListOf(SimpleGrantedAuthority(EXCHANGE_API_USER))
                )
            }
        }

        chain.doFilter(request, response)
    }
}