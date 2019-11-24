package com.alorma.rac.data.api

import com.alorma.rac.domain.model.Images
import com.alorma.rac.domain.model.Program
import com.alorma.rac.domain.model.Section
import com.alorma.rac.domain.model.SocialNetworks

class ProgramsApiMapper {

    fun map(programs: List<ProgramApiModel>): List<Program> = programs.map {
        map(it)
    }

    fun map(it: ProgramApiModel): Program {
        return Program(
            id = it.id,
            title = it.title,
            subtitle = it.subtitle,
            description = it.description,
            schedule = it.schedule,
            socialNetworks = it.socialNetworks?.let { mapSocialNetworks(it) },
            images = it.images?.let { mapImages(it) },
            email = it.email,
            url = it.url,
            sections = mapSections(it.sections)
        )
    }

    private fun mapSocialNetworks(it: SocialNetworksApiModel): SocialNetworks = SocialNetworks(
        twitter = it.twitter,
        facebook = it.facebook,
        instagram = it.instagram
    )

    private fun mapImages(it: ImagesApiModel): Images = Images(
        app = it.app,
        itunes = it.itunes,
        share = it.share,
        program = it.program,
        person = it.person,
        personSmall = it.personSmall,
        podcast = it.podcast
    )

    private fun mapSections(it: List<SectionApiModel>) = it.map {
        Section(
            id = it.id,
            title = it.title,
            type = it.type
        )
    }
}