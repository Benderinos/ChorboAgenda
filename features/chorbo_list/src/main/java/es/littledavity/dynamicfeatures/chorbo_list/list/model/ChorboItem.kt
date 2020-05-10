package es.littledavity.dynamicfeatures.chorbo_list.list.model

import es.littledavity.dynamicfeatures.chorbo_list.list.ChorboListFragment

/**
 * Model view to display on the screen [ChorboListFragment].
 */
data class ChorboItem(
    val id: Long,
    val name: String,
    val image: String,
    val countryCode: String,
    val countryName: String,
    val flag: String,
    val whatsapp: String,
    val instagram: String
)