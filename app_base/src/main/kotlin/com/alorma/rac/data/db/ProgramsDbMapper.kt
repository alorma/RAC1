package com.alorma.rac.data.db

import com.alorma.rac.domain.model.Images
import com.alorma.rac.domain.model.Program
import com.alorma.rac.domain.model.Section
import com.alorma.rac.domain.model.SocialNetworks

class ProgramsDbMapper {

    fun map(programs: List<ProgramEntity>): List<Program> = programs.map {
        map(it)
    }

    fun map(it: ProgramEntity): Program {
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
            now = it.isNow
        )
    }

    fun map(it: ProgramWithSectionsEntity): Program {
        return Program(
            id = it.programEntity.id,
            title = it.programEntity.title,
            subtitle = it.programEntity.subtitle,
            description = it.programEntity.description,
            schedule = it.programEntity.schedule,
            socialNetworks = it.programEntity.socialNetworks?.let { mapSocialNetworks(it) },
            images = it.programEntity.images?.let { mapImages(it) },
            email = it.programEntity.email,
            url = it.programEntity.url,
            now = it.programEntity.isNow,
            sections = mapSections(it.sections)
        )
    }

    private fun mapSections(sections: List<SectionEntity>): List<Section> = sections.map {
        Section(
            id = it.id,
            title = it.title,
            type = it.type
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
            mapToEntity(program)
        }

    fun mapToEntity(program: Program): ProgramEntity = ProgramEntity(
        id = program.id,
        title = program.title,
        subtitle = program.subtitle,
        description = program.description,
        schedule = program.schedule,
        socialNetworks = program.socialNetworks?.let { mapSocialNetworksEntity(it) },
        images = program.images?.let { mapImagesEntity(it) },
        email = program.email,
        url = program.url,
        isNow = program.now
    )

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

    fun mapToSectionEntity(section: Section, program: Program): SectionEntity {
        return SectionEntity(
            id = section.id,
            programId = program.id,
            title = section.title,
            type = section.type
        )
    }
}