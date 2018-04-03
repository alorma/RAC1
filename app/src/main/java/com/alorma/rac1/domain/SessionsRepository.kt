package com.alorma.rac1.domain

import com.alorma.rac1.data.net.Rac1Api
import com.alorma.rac1.data.net.SessionsDto
import io.reactivex.Single
import javax.inject.Inject

class SessionsRepository @Inject constructor(
        private val rac1Api: Rac1Api) {

    fun getSessions(program: String, section: String): Single<SessionsDto>
            = rac1Api.getSessions(program, section)
}