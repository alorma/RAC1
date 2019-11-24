package com.alorma.rac.domain.model

data class Program(
    val id: String,
    val title: String,
    val subtitle: String?,
    val description: String?,
    val schedule: String?,
    val socialNetworks: SocialNetworks?,
    val images: Images?,
    val email: String?,
    val url: String?,
    val sections: List<Section> = emptyList(),
    val now: Boolean = false
)