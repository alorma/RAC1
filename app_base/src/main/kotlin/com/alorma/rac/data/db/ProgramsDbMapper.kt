package com.alorma.rac.data.db

import com.alorma.rac.domain.model.Images
import com.alorma.rac.domain.model.Program
import com.alorma.rac.domain.model.SocialNetworks

class ProgramsDbMapper {

    fun map(programs: List<ProgramEntity>): List<Program> = programs.map {
        map(it)
    }

    private fun map(it: ProgramEntity): Program {
        return Program(
            id = it.id,
            title = it.title,
            subtitle = it.subtitle,
            description = it.description,
            schedule = it.schedule,
            socialNetworks = it.socialNetworks?.let { mapSocialNetworks(it) },
            images = it.images?.let { mapImages(it) },
            email = it.email,
            url = it.url
        )
    }


    private fun mapSocialNetworks(it: SocialNetworksEntity): SocialNetworks = SocialNetworks(
        twitter = it.twitter,
        facebook = it.facebook,
        instagram = it.instagram
    )

    private fun mapImages(it: ImagesEntity): Images = Images(
        app = it.app,
        itunes = it.itunes,
        share = it.share,
        program = it.program,
        person = it.person,
        personSmall = it.personSmall,
        podcast = it.podcast
    )

    fun mapToEntity(programs: List<Program>): List<ProgramEntity> =
        programs.map { program: Program ->
            ProgramEntity(
                id = program.id,
                title = program.title,
                subtitle = program.subtitle,
                description = program.description,
                schedule = program.schedule,
                socialNetworks = program.socialNetworks?.let { mapSocialNetworksEntity(it) },
                images = program.images?.let { mapImagesEntity(it) },
                email = program.email,
                url = program.url
            )
        }

    private fun mapSocialNetworksEntity(socialNetworks: SocialNetworks): SocialNetworksEntity =
        SocialNetworksEntity(
            twitter = socialNetworks.twitter,
            facebook = socialNetworks.facebook,
            instagram = socialNetworks.instagram
        )

    private fun mapImagesEntity(images: Images): ImagesEntity = ImagesEntity(
        app = images.app,
        itunes = images.itunes,
        share = images.share,
        program = images.program,
        person = images.person,
        personSmall = images.personSmall,
        podcast = images.podcast
    )
}